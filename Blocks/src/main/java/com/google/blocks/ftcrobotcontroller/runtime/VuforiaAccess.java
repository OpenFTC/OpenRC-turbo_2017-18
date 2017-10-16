// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller.runtime;

import android.content.Context;
import android.content.res.AssetManager;
import android.webkit.JavascriptInterface;

import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.Parameters;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.Parameters.CameraMonitorFeedback;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.vuforia.VuforiaTrackableContainer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * A class that provides JavaScript access to Vuforia.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
class VuforiaAccess extends Access {
  private static final float MM_PER_INCH  = 25.4f;
  private static final float MM_FTC_FIELD_WIDTH  = (12*12 - 2) * MM_PER_INCH;
  private static final String NAME_RELIC = "Relic";

  private final Context context;
  private volatile VuforiaLocalizer vuforiaLocalizer;
  private volatile VuforiaTrackables vuforiaTrackables;
  private final Map<String, VuforiaTrackableDefaultListener> listenerMap =
      new HashMap<String, VuforiaTrackableDefaultListener>();
  private final Map<String, OpenGLMatrix> locationMap =
      new HashMap<String, OpenGLMatrix>();
  private final Map<String, OpenGLMatrix> poseMap =
      new HashMap<String, OpenGLMatrix>();

  private static class VuforiaTrackingResults {
    boolean isVisible = false;
    RelicRecoveryVuMark relicRecoveryVuMark = RelicRecoveryVuMark.UNKNOWN;
    float x = 0;
    float y = 0;
    float z = 0;
    float xAngle = 0;
    float yAngle = 0;
    float zAngle = 0;

    void setVisible(boolean isVisible) {
      this.isVisible = isVisible;
    }

    void setRelicRecoveryVuMark(RelicRecoveryVuMark relicRecoveryVuMark) {
      this.relicRecoveryVuMark = relicRecoveryVuMark;
    }

    void setMatrix(OpenGLMatrix matrix) {
      VectorF translation = matrix.getTranslation();
      Orientation orientation = Orientation.getOrientation(
          matrix, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);
      x = translation.get(0);
      y = translation.get(1);
      z = translation.get(2);
      xAngle = orientation.firstAngle;
      yAngle = orientation.secondAngle;
      zAngle = orientation.thirdAngle;
    }

    @Override
    public String toString() {
      return "{ \"IsVisible\":" + isVisible +
          ", \"RelicRecoveryVuMark\":\"" + relicRecoveryVuMark.toString() + "\"" +
          ", \"X\":" + x +
          ", \"Y\":" + y +
          ", \"Z\":" + z +
          ", \"XAngle\":" + xAngle +
          ", \"YAngle\":" + yAngle +
          ", \"ZAngle\":" + zAngle + " }";
    }
  }

  VuforiaAccess(BlocksOpMode blocksOpMode, String identifier, Context context) {
    super(blocksOpMode, identifier);
    this.context = context;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void initialize(String vuforiaLicenseKey,
      String cameraDirectionString, boolean enableCameraMonitoring, String cameraMonitorFeedbackString,
      float dx, float dy, float dz, float xAngle, float yAngle, float zAngle) {
    checkIfStopRequested();
    try {
      Parameters parameters = createParameters(vuforiaLicenseKey,
          cameraDirectionString, enableCameraMonitoring, cameraMonitorFeedbackString);
      initialize(parameters, dx, dy, dz, xAngle, yAngle, zAngle,
          true /* useCompetitionFieldTargetLocations */);
    } catch (Exception e) {
      RobotLog.e("Vuforia.initialize - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void initializeExtended(String vuforiaLicenseKey,
      String cameraDirectionString, boolean useExtendedTracking,
      boolean enableCameraMonitoring, String cameraMonitorFeedbackString,
      float dx, float dy, float dz, float xAngle, float yAngle, float zAngle,
      boolean useCompetitionFieldTargetLocations) {
    checkIfStopRequested();
    try {
      Parameters parameters = createParameters(vuforiaLicenseKey,
          cameraDirectionString, enableCameraMonitoring, cameraMonitorFeedbackString);
      parameters.useExtendedTracking = useExtendedTracking;
      initialize(parameters, dx, dy, dz, xAngle, yAngle, zAngle,
          useCompetitionFieldTargetLocations);
    } catch (Exception e) {
      RobotLog.e("Vuforia.initializeExtended - caught " + e);
    }
  }

  private Parameters createParameters(String vuforiaLicenseKey, String cameraDirectionString,
      boolean enableCameraMonitoring, String cameraMonitorFeedbackString) {
    Parameters parameters = new Parameters();
    if (vuforiaLicenseKey.length() < 217) {
      try {
        AssetManager assetManager = AppUtil.getInstance().getRootActivity().getAssets();
        BufferedReader reader = new BufferedReader(new InputStreamReader(assetManager.open(vuforiaLicenseKey)));
        try {
          vuforiaLicenseKey = reader.readLine();
        } finally {
          reader.close();
        }
      } catch (IOException e) {
        RobotLog.e("Vuforia.createParameters - caught " + e);
      }
    }
    parameters.vuforiaLicenseKey = vuforiaLicenseKey;
    parameters.cameraDirection =
        CameraDirection.valueOf(cameraDirectionString.toUpperCase(Locale.ENGLISH));
    cameraMonitorFeedbackString = cameraMonitorFeedbackString.toUpperCase(Locale.ENGLISH);
    parameters.cameraMonitorFeedback = cameraMonitorFeedbackString.equals("DEFAULT")
        ? null : CameraMonitorFeedback.valueOf(cameraMonitorFeedbackString);
    if (enableCameraMonitoring) {
      parameters.cameraMonitorViewIdParent = context.getResources().getIdentifier(
          "cameraMonitorViewId", "id", context.getPackageName());
    }
    return parameters;
  }

  private void initialize(final Parameters parameters,
      float dx, float dy, float dz, float xAngle, float yAngle, float zAngle,
      boolean useCompetitionFieldTargetLocations)
      throws InterruptedException {
    OpenGLMatrix locationOnField;
    if (useCompetitionFieldTargetLocations) {
      // TODO(lizlooney): Figure out how to handle the locations of the VuMarks on the field.
      locationOnField = null;
    } else {
      // Create an image translation/rotation matrix to be used for all images.
      // Essentially put all the image centers at the 0:0:0 origin,
      // but rotate them so they along the -X axis.
      locationOnField =
          OpenGLMatrix.rotation(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES, 90, 0, -90);
    }

    OpenGLMatrix phoneLocationOnRobot = OpenGLMatrix.translation(dx, dy, dz)
        .multiplied(Orientation.getRotationMatrix(
            AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES, xAngle, yAngle, zAngle));

    // Because the JavaBridge thread is a looper, but not the main looper, we need to create
    // another thread to call ClassFactory.createVuforiaLocalizer(parameters). Otherwise the
    // Vuforia.UpdateCallbackInterface.Vuforia_onUpdate method is called on the JavaBridge
    // thread and the camera monitor view won't update until after waitForStart is finished.
    Thread thread = new Thread(new Runnable() {
      @Override
      public void run() {
        vuforiaLocalizer = ClassFactory.createVuforiaLocalizer(parameters);
      }
    });
    thread.start();
    thread.join();

    vuforiaTrackables = vuforiaLocalizer.loadTrackablesFromAsset("RelicVuMark");

    VuforiaTrackable relicTemplate = vuforiaTrackables.get(0);
    initTrackable(relicTemplate, NAME_RELIC, locationOnField, phoneLocationOnRobot, parameters.cameraDirection);
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void activate() {
    checkIfStopRequested();
    try {
      if (vuforiaTrackables != null) {
        vuforiaTrackables.activate();
      } else {
        RobotLog.e("Vuforia.activate - you forgot to call Vuforia.initialize!");
      }
    } catch (Exception e) {
      RobotLog.e("Vuforia.activate - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void deactivate() {
    checkIfStopRequested();
    try {
      if (vuforiaTrackables != null) {
        vuforiaTrackables.deactivate();
      } else {
        RobotLog.e("Vuforia.deactivate - you forgot to call Vuforia.initialize!");
      }
    } catch (Exception e) {
      RobotLog.e("Vuforia.deactivate - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public String track(String name) {
    checkIfStopRequested();
    VuforiaTrackingResults vuforiaTrackingResults = new VuforiaTrackingResults();
    try {
      VuforiaTrackableDefaultListener listener = getListener(name);
      if (vuforiaTrackables != null) {
        if (listener != null) {
          vuforiaTrackingResults.setVisible(listener.isVisible());
          vuforiaTrackingResults.setRelicRecoveryVuMark(RelicRecoveryVuMark.from(listener));

          OpenGLMatrix matrix = listener.getUpdatedRobotLocation();
          if (matrix != null) {
            locationMap.put(name, matrix);
          } else {
            matrix = locationMap.get(name);
          }
          if (matrix != null) {
            vuforiaTrackingResults.setMatrix(matrix);
          }
        } else {
          RobotLog.e("Vuforia.track - name must be \"Relic\".");
        }
      } else {
        RobotLog.e("Vuforia.track - you forgot to call Vuforia.initialize!");
      }
    } catch (Exception e) {
      RobotLog.e("Vuforia.track - caught " + e);
    }
    return vuforiaTrackingResults.toString();
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public String trackPose(String name) {
    checkIfStopRequested();
    VuforiaTrackingResults vuforiaTrackingResults = new VuforiaTrackingResults();
    try {
      VuforiaTrackableDefaultListener listener = getListener(name);
      if (vuforiaTrackables != null) {
        if (listener != null) {
          vuforiaTrackingResults.setVisible(listener.isVisible());
          vuforiaTrackingResults.setRelicRecoveryVuMark(RelicRecoveryVuMark.from(listener));

          OpenGLMatrix matrix = listener.getPose();
          if (matrix != null) {
            poseMap.put(name, matrix);
          } else {
            matrix = poseMap.get(name);
          }
          if (matrix != null) {
            vuforiaTrackingResults.setMatrix(matrix);
          }
        } else {
          RobotLog.e("Vuforia.trackPose - name must be \"Relic\".");
        }
      } else {
        RobotLog.e("Vuforia.trackPose - you forgot to call Vuforia.initialize!");
      }
    } catch (Exception e) {
      RobotLog.e("Vuforia.trackPose - caught " + e);
    }
    return vuforiaTrackingResults.toString();
  }

  private void initTrackable(
      VuforiaTrackable vuforiaTrackable, String name, OpenGLMatrix locationOnField,
      OpenGLMatrix phoneLocationOnRobot, CameraDirection cameraDirection) {
    vuforiaTrackable.setName(name);
    if (locationOnField != null) {
      vuforiaTrackable.setLocation(locationOnField);
    }

    VuforiaTrackableDefaultListener listener =
        (VuforiaTrackableDefaultListener) vuforiaTrackable.getListener();
    listener.setPhoneInformation(phoneLocationOnRobot, cameraDirection);
    listenerMap.put(name.toUpperCase(Locale.ENGLISH), listener);
  }

  private VuforiaTrackableDefaultListener getListener(String name) {
    return listenerMap.get(name.toUpperCase(Locale.ENGLISH));
  }
}
