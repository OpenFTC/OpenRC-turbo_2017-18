// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller.runtime;

import android.app.Activity;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.google.blocks.ftcrobotcontroller.util.HardwareItem;
import com.google.blocks.ftcrobotcontroller.util.HardwareItemMap;
import com.google.blocks.ftcrobotcontroller.util.HardwareType;
import com.google.blocks.ftcrobotcontroller.util.HardwareUtil;
import com.google.blocks.ftcrobotcontroller.util.Identifier;
import com.google.blocks.ftcrobotcontroller.util.ProjectsUtil;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.internal.opmode.InstanceOpModeManager;
import org.firstinspires.ftc.robotcore.internal.opmode.InstanceOpModeRegistrar;
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeManagerImpl;
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeMeta;
import org.firstinspires.ftc.robotcore.internal.opmode.RegisteredOpModes;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A subclass of {@link LinearOpMode} that loads JavaScript from a file and executes it.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
public class BlocksOpMode extends LinearOpMode {
  private static final String REFERENCE_ERROR_PREFIX = "ReferenceError: ";
  private static final String LOG_PREFIX = "BlocksOpMode - ";

  private static final AtomicReference<String> fatalErrorMessageHolder = new AtomicReference<String>();

  private static Activity activity;
  private static WebView webView;
  private static final AtomicReference<String> nameOfOpModeLoadedIntoWebView = new AtomicReference<String>();
  private static final Map<String, Access> javascriptInterfaces = new ConcurrentHashMap<String, Access>();

  private final String project;
  private final String logPrefix;
  private final AtomicLong interruptedTime = new AtomicLong();

  /**
   * Instantiates a BlocksOpMode that loads JavaScript from a file and executes it when the op mode
   * is run.
   *
   * @param project the name of the project.
   */
  // Visible for testing
  BlocksOpMode(String project) {
    super();
    this.project = project;
    logPrefix = LOG_PREFIX + "\"" + project + "\" - ";
  }

  private String getLogPrefix() {
    Thread thread = Thread.currentThread();
    return logPrefix + thread.getThreadGroup().getName() + "/" + thread.getName() + " - ";
  }

  void checkIfStopRequested() {
    if (interruptedTime.get() != 0L &&
        isStopRequested() &&
        System.currentTimeMillis() - interruptedTime.get() >= msStuckDetectStop) {
      RobotLog.i(getLogPrefix() + "checkIfStopRequested - about to stop opmode by throwing RuntimeException");
      throw new RuntimeException("Stopping opmode " + project + " by force.");
    }
  }

  void waitForStartForBlocks() {
    // Because this method is executed on the Java Bridge thread, it is not interrupted when stop
    // is called. To fix this, we repeatedly wait 100ms and check isStarted.
    RobotLog.i(getLogPrefix() + "waitForStartForBlocks - start");
    try {
      while (!isStartedForBlocks()) {
        synchronized (this) {
          try {
            this.wait(100);
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
          }
        }
      }
    } finally {
      RobotLog.i(getLogPrefix() + "waitForStartForBlocks - end");
    }
  }

  void sleepForBlocks(long millis) {
    // Because this method is executed on the Java Bridge thread, it is not interrupted when stop
    // is called. To fix this, we repeatedly sleep 100ms and check isInterrupted.
    RobotLog.i(getLogPrefix() + "sleepForBlocks - start");
    try {
      long endTime = System.currentTimeMillis() + millis;
      while (!isInterrupted()) {
        long chunk = Math.min(100L, endTime - System.currentTimeMillis());
        if (chunk <= 0) {
          break;
        }
        sleep(chunk);
      }
    } finally {
      RobotLog.i(getLogPrefix() + "sleepForBlocks - end");
    }
  }

  private boolean isInterrupted() {
    return interruptedTime.get() != 0L;
  }

  boolean isStartedForBlocks() {
    return super.isStarted() || isInterrupted();
  }

  boolean isStopRequestedForBlocks() {
    return super.isStopRequested() || isInterrupted();
  }

  @Override
  public void runOpMode() {
    RobotLog.i(getLogPrefix() + "runOpMode - start");
    cleanUpPreviousBlocksOpMode();
    try {
      fatalErrorMessageHolder.set(null);

      boolean interrupted = false;
      interruptedTime.set(0L);

      final AtomicBoolean scriptFinished = new AtomicBoolean();
      final Object scriptFinishedLock = new Object();

      final BlocksOpModeAccess blocksOpModeAccess =
          new BlocksOpModeAccess(Identifier.BLOCKS_OP_MODE.identifier, scriptFinishedLock, scriptFinished);

      javascriptInterfaces.put(
          Identifier.BLOCKS_OP_MODE.identifier, blocksOpModeAccess);

      // Start running the user's op mode blocks by calling loadScript on the UI thread.
      // Execution of the script is done by the WebView component, which uses the Java Bridge
      // thread to call into java.

      AppUtil appUtil = AppUtil.getInstance();

      synchronized (scriptFinishedLock) {
        appUtil.runOnUiThread(new Runnable() {
          @Override
          public void run() {
            try {
              RobotLog.i(getLogPrefix() + "run1 - before loadScript");
              loadScript();
              RobotLog.i(getLogPrefix() + "run1 - after loadScript");
            } catch (Exception e) {
              RobotLog.e(getLogPrefix() + "run1 - caught " + e);
              // The exception may not have a stacktrace, so we check before calling
              // RobotLog.logStackTrace.
              if (e.getStackTrace() != null) {
                RobotLog.logStackTrace(e);
              }
            }
          }
        });

        // This thread (the thread executing BlocksOpMode.runOpMode) waits for the script to finish
        // When the script finishes, it calls BlocksOpModeAccess.scriptFinished() (on the Java
        // Bridge thread), which will set scriptFinished to true and call
        // scriptFinishedLock.notifyAll(). At that point, the scriptFinished.wait() call below
        // finish, allowing this thread to continue running.

        // If the stop button is pressed, the scriptFinished.wait() call below will be interrrupted
        // and this thread will catch InterruptedException. The script will continue to run and
        // this thread will continue to wait until scriptFinished is set. However, all calls from
        // javascript into java call Access.stopRunawayTrain. Access.stopRunawayTrain will throw a
        // RuntimeException if the elapsed time since catching the InterruptedException exceeds 20
        // seconds. The RuntimeException will cause the script to stop immediately, set
        // scriptFinished to true and call scriptFinished.notifyAll().

        RobotLog.i(getLogPrefix() + "runOpMode - before while !scriptFinished loop");
        while (!scriptFinished.get()) {
          try {
            scriptFinishedLock.wait();
          } catch (InterruptedException e) {
            RobotLog.e(getLogPrefix() + "runOpMode - caught InterruptedException during scriptFinishedLock.wait");
            interrupted = true;
            interruptedTime.set(System.currentTimeMillis());
          }
        }
        RobotLog.i(getLogPrefix() + "runOpMode - after while !scriptFinished loop");
      }

      // Clean up the WebView component by calling clearScript on the UI thread.
      appUtil.runOnUiThread(new Runnable() {
        @Override
        public void run() {
          try {
            RobotLog.i(getLogPrefix() + "run2 - before clearScript");
            clearScript();
            RobotLog.i(getLogPrefix() + "run2 - after clearScript");
          } catch (Exception e) {
            RobotLog.e(getLogPrefix() + "run2 - caught " + e);
            // The exception may not have a stacktrace, so we check before calling
            // RobotLog.logStackTrace.
            if (e.getStackTrace() != null) {
              RobotLog.logStackTrace(e);
            }
          }
        }
      });

      // If an InterruptedException was caught, call Thread.currentThread().interrupt() to set
      // the interrupted status.

      if (interrupted) {
        Thread.currentThread().interrupt();
      }

      // If there was a fatal error in the WebView component, throw a RuntimeException.

      String fatalErrorMessage = fatalErrorMessageHolder.getAndSet(null);
      if (fatalErrorMessage != null) {
        throw new RuntimeException(
            "A fatal error occurred while running the Blocks op mode named " + project + ". " +
            fatalErrorMessage);
      }
    } finally {
      long interruptedTime = this.interruptedTime.get();
      if (interruptedTime != 0L) {
        RobotLog.i(getLogPrefix() + "runOpMode - end - " +
            (System.currentTimeMillis() - interruptedTime) + "ms after InterruptedException");
      } else {
        RobotLog.i(getLogPrefix() + "runOpMode - end - no InterruptedException");
      }
    }
  }

  private void cleanUpPreviousBlocksOpMode() {
    String name = nameOfOpModeLoadedIntoWebView.get();
    if (name != null) {
      RobotLog.w(getLogPrefix() + "cleanUpPreviousBlocksOpMode - Warning: The Blocks runtime system is still loaded " +
          "with the Blocks op mode named " + name + ".");
      RobotLog.w(getLogPrefix() + "cleanUpPreviousBlocksOpMode - Trying to clean up now.");
      AppUtil.getInstance().synchronousRunOnUiThread(new Runnable() {
        @Override
        public void run() {
          try {
            RobotLog.w(getLogPrefix() + "cleanUpPreviousBlocksOpMode run - before clearScript");
            clearScript();
            RobotLog.w(getLogPrefix() + "cleanUpPreviousBlocksOpMode run - after clearScript");
          } catch (Exception e) {
            RobotLog.e(getLogPrefix() + "cleanUpPreviousBlocksOpMode run - caught " + e);
            // The exception may not have a stacktrace, so we check before calling
            // RobotLog.logStackTrace.
            if (e.getStackTrace() != null) {
              RobotLog.logStackTrace(e);
            }
          }
        }
      });
      if (nameOfOpModeLoadedIntoWebView.get() != null) {
        RobotLog.w(getLogPrefix() + "cleanUpPreviousBlocksOpMode - Clean up was successful.");
      } else {
        RobotLog.e(getLogPrefix() + "cleanUpPreviousBlocksOpMode - Error: Clean up failed.");
        throw new RuntimeException(
            "Unable to start running the Blocks op mode named " + project + ". The Blocks runtime " +
            "system is still loaded with the previous Blocks op mode named " + name + ". " +
            "Please restart the Robot Controller app.");
      }
    }
  }

  private void addJavascriptInterfaces(HardwareItemMap hardwareItemMap) {
    addJavascriptInterfacesForIdentifiers();
    addJavascriptInterfacesForHardware(hardwareItemMap);

    for (Map.Entry<String, Access> entry : javascriptInterfaces.entrySet()) {
      String identifier = entry.getKey();
      Access access = entry.getValue();
      webView.addJavascriptInterface(access, identifier);
    }
  }

  // Visible for testing
  void addJavascriptInterfacesForIdentifiers() {
    javascriptInterfaces.put(
        Identifier.ACCELERATION.identifier,
        new AccelerationAccess(this, Identifier.ACCELERATION.identifier));
    javascriptInterfaces.put(
        Identifier.ANDROID_ACCELEROMETER.identifier,
        new AndroidAccelerometerAccess(this, Identifier.ANDROID_ACCELEROMETER.identifier, activity));
    javascriptInterfaces.put(
        Identifier.ANDROID_GYROSCOPE.identifier,
        new AndroidGyroscopeAccess(this, Identifier.ANDROID_GYROSCOPE.identifier, activity));
    javascriptInterfaces.put(
        Identifier.ANDROID_ORIENTATION.identifier,
        new AndroidOrientationAccess(this, Identifier.ANDROID_ORIENTATION.identifier, activity));
    javascriptInterfaces.put(
        Identifier.ANDROID_SOUND_POOL.identifier,
        new AndroidSoundPoolAccess(this, Identifier.ANDROID_SOUND_POOL.identifier));
    javascriptInterfaces.put(
        Identifier.ANDROID_TEXT_TO_SPEECH.identifier,
        new AndroidTextToSpeechAccess(this, Identifier.ANDROID_TEXT_TO_SPEECH.identifier, activity));
    javascriptInterfaces.put(
        Identifier.ANGULAR_VELOCITY.identifier,
        new AngularVelocityAccess(this, Identifier.ANGULAR_VELOCITY.identifier));
    javascriptInterfaces.put(
        Identifier.BNO055IMU_PARAMETERS.identifier,
        new BNO055IMUParametersAccess(this, Identifier.BNO055IMU_PARAMETERS.identifier));
    javascriptInterfaces.put(
        Identifier.COLOR.identifier,
        new ColorAccess(this, Identifier.COLOR.identifier));
    javascriptInterfaces.put(
        Identifier.DBG_LOG.identifier,
        new DbgLogAccess(this, Identifier.DBG_LOG.identifier));
    javascriptInterfaces.put(
        Identifier.ELAPSED_TIME.identifier,
        new ElapsedTimeAccess(this, Identifier.ELAPSED_TIME.identifier));
    javascriptInterfaces.put(
        Identifier.GAMEPAD_1.identifier,
        new GamepadAccess(this, Identifier.GAMEPAD_1.identifier, gamepad1));
    javascriptInterfaces.put(
        Identifier.GAMEPAD_2.identifier,
        new GamepadAccess(this, Identifier.GAMEPAD_2.identifier, gamepad2));
    javascriptInterfaces.put(
        Identifier.LINEAR_OP_MODE.identifier,
        new LinearOpModeAccess(this, Identifier.LINEAR_OP_MODE.identifier));
    javascriptInterfaces.put(
        Identifier.MAGNETIC_FLUX.identifier,
        new MagneticFluxAccess(this, Identifier.MAGNETIC_FLUX.identifier));
    javascriptInterfaces.put(
        Identifier.MATRIX_F.identifier,
        new MatrixFAccess(this, Identifier.MATRIX_F.identifier));
    javascriptInterfaces.put(
        Identifier.MISC.identifier,
        new MiscAccess(this, Identifier.MISC.identifier));
    javascriptInterfaces.put(
        Identifier.NAVIGATION.identifier,
        new NavigationAccess(this, Identifier.NAVIGATION.identifier));
    javascriptInterfaces.put(
        Identifier.OPEN_GL_MATRIX.identifier,
        new OpenGLMatrixAccess(this, Identifier.OPEN_GL_MATRIX.identifier));
    javascriptInterfaces.put(
        Identifier.ORIENTATION.identifier,
        new OrientationAccess(this, Identifier.ORIENTATION.identifier));
    javascriptInterfaces.put(
        Identifier.POSITION.identifier,
        new PositionAccess(this, Identifier.POSITION.identifier));
    javascriptInterfaces.put(
        Identifier.QUATERNION.identifier,
        new QuaternionAccess(this, Identifier.QUATERNION.identifier));
    javascriptInterfaces.put(
        Identifier.RANGE.identifier,
        new RangeAccess(this, Identifier.RANGE.identifier));
    javascriptInterfaces.put(
        Identifier.SYSTEM.identifier,
        new SystemAccess(this, Identifier.SYSTEM.identifier));
    javascriptInterfaces.put(
        Identifier.TELEMETRY.identifier,
        new TelemetryAccess(this, Identifier.TELEMETRY.identifier, telemetry));
    javascriptInterfaces.put(
        Identifier.TEMPERATURE.identifier,
        new TemperatureAccess(this, Identifier.TEMPERATURE.identifier));
    javascriptInterfaces.put(
        Identifier.VECTOR_F.identifier,
        new VectorFAccess(this, Identifier.VECTOR_F.identifier));
    javascriptInterfaces.put(
        Identifier.VELOCITY.identifier,
        new VelocityAccess(this, Identifier.VELOCITY.identifier));
    javascriptInterfaces.put(
        Identifier.VUFORIA.identifier,
        new VuforiaAccess(this, Identifier.VUFORIA.identifier, activity));
    javascriptInterfaces.put(
        Identifier.VUFORIA_LOCALIZER.identifier,
        new VuforiaLocalizerAccess(this, Identifier.VUFORIA_LOCALIZER.identifier));
    javascriptInterfaces.put(
        Identifier.VUFORIA_LOCALIZER_PARAMETERS.identifier,
        new VuforiaLocalizerParametersAccess(this, Identifier.VUFORIA_LOCALIZER_PARAMETERS.identifier, activity));
    javascriptInterfaces.put(
        Identifier.VUFORIA_TRACKABLE.identifier,
        new VuforiaTrackableAccess(this, Identifier.VUFORIA_TRACKABLE.identifier));
    javascriptInterfaces.put(
        Identifier.VUFORIA_TRACKABLE_DEFAULT_LISTENER.identifier,
        new VuforiaTrackableDefaultListenerAccess(this, Identifier.VUFORIA_TRACKABLE_DEFAULT_LISTENER.identifier));
    javascriptInterfaces.put(
        Identifier.VUFORIA_TRACKABLES.identifier,
        new VuforiaTrackablesAccess(this, Identifier.VUFORIA_TRACKABLES.identifier));
  }

  private void addJavascriptInterfacesForHardware(HardwareItemMap hardwareItemMap) {
    for (HardwareType hardwareType : HardwareType.values()) {
      if (hardwareItemMap.contains(hardwareType)) {
        for (HardwareItem hardwareItem : hardwareItemMap.getHardwareItems(hardwareType)) {
          if (javascriptInterfaces.containsKey(hardwareItem.identifier)) {
            RobotLog.w(getLogPrefix() + "There is already a JavascriptInterface for identifier \"" +
                hardwareItem.identifier + "\". Ignoring hardware type " + hardwareType + ".");
            continue;
          }
          javascriptInterfaces.put(
              hardwareItem.identifier,
              HardwareAccess.newHardwareAccess(this, hardwareType, hardwareMap, hardwareItem));
        }
      }
    }
  }

  private void removeJavascriptInterfaces() {
    Iterator<Map.Entry<String, Access>> it = javascriptInterfaces.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry<String, Access> entry = it.next();
      String identifier = entry.getKey();
      Access access = entry.getValue();
      webView.removeJavascriptInterface(identifier);
      access.close();
      it.remove();
    }
  }

  private class BlocksOpModeAccess extends Access {
    private final Object scriptFinishedLock;
    private final AtomicBoolean scriptFinished;

    private BlocksOpModeAccess(String identifier, Object scriptFinishedLock, AtomicBoolean scriptFinished) {
      super(BlocksOpMode.this, identifier);
      this.scriptFinishedLock = scriptFinishedLock;
      this.scriptFinished = scriptFinished;
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    public void scriptStarting() {
      RobotLog.i(getLogPrefix() + "scriptStarting");
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    public void scriptFinished() {
      RobotLog.i(getLogPrefix() + "scriptFinished");
      synchronized (scriptFinishedLock) {
        scriptFinished.set(true);
        scriptFinishedLock.notifyAll();
      }
    }
  }

  private void loadScript() throws IOException {
    nameOfOpModeLoadedIntoWebView.set(project);
    HardwareItemMap hardwareItemMap = HardwareItemMap.newHardwareItemMap(hardwareMap);

    addJavascriptInterfaces(hardwareItemMap);

    String jsFileContent = ProjectsUtil.fetchJsFileContent(project);
    String jsContent = HardwareUtil.upgradeJs(jsFileContent, hardwareItemMap);

    String html = "<html><body onload='callRunOpMode()'><script type='text/javascript'>\n"
        + "function callRunOpMode() {\n"
        + "  blocksOpMode.scriptStarting();\n"
        + "  try {\n"
        + "    runOpMode();\n" // This calls the runOpMode method in the generated javascript.
        + "  } catch (e) {\n"
        + "    console.log(e);\n"
        + "  }\n"
        + "  blocksOpMode.scriptFinished();\n"
        + "}\n"
        + "\n"
        + jsContent
        + "\n"
        + "</script></body></html>\n";
    webView.loadDataWithBaseURL(
        null /* baseUrl */, html, "text/html", "UTF-8", null /* historyUrl */);
  }

  private void clearScript() {
    removeJavascriptInterfaces();
    if (!javascriptInterfaces.isEmpty()) {
      RobotLog.w(getLogPrefix() + "clearScript - Warning: javascriptInterfaces is not empty.");
    }
    javascriptInterfaces.clear();

    webView.loadDataWithBaseURL(
        null /* baseUrl */, "", "text/html", "UTF-8", null /* historyUrl */);
    nameOfOpModeLoadedIntoWebView.set(null);
  }

  /**
   * Sets the {@link WebView} so that all BlocksOpModes can access it.
   */
  public static void setActivityAndWebView(Activity a, WebView wv) {
    if (activity == null && webView == null) {
      addOpModeRegistrar();
    }

    activity = a;
    webView = wv;
    webView.getSettings().setJavaScriptEnabled(true);

    webView.setWebChromeClient(new WebChromeClient() {
      @Override
      public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        RobotLog.i(LOG_PREFIX + "onConsoleMessage.message() " + consoleMessage.message());
        RobotLog.i(LOG_PREFIX + "onConsoleMessage.lineNumber() " + consoleMessage.lineNumber());
        // If a hardware device is used in blocks, but has been removed (or renamed) in the
        // configuration, there will be a console message like this:
        // "ReferenceError: left_drive is not defined".
        String message = consoleMessage.message();
        if (message.startsWith(REFERENCE_ERROR_PREFIX)) {
          RobotLog.e(LOG_PREFIX + "fatalErrorMessage: " + message);
          fatalErrorMessageHolder.compareAndSet(null, message);
        }
        return false; // continue with console logging.
      }
    });
  }

  private static void addOpModeRegistrar() {
    RegisteredOpModes.getInstance().addInstanceOpModeRegistrar(new InstanceOpModeRegistrar() {
      @Override public void register(InstanceOpModeManager manager) {
        try {
          // fetchEnabledProjectsWithJavaScript is thread-safe wrt concurrent saves from the browswer
          List<OpModeMeta> projects = ProjectsUtil.fetchEnabledProjectsWithJavaScript();
          for (OpModeMeta opModeMeta : projects) {
            manager.register(opModeMeta, new BlocksOpMode(opModeMeta.name));
          }
        } catch (Exception e) {
          RobotLog.logStackTrace(e);
        }
      }
    });
  }

  /**
   * @deprecated functionality now automatically called by the system
   */
  @Deprecated
  public static void registerAll(OpModeManager manager) {
    RobotLog.w(BlocksOpMode.class.getSimpleName(), "registerAll(OpModeManager) is deprecated and will be removed soon, as calling it is unnecessary in this and future API version");
  }
}
