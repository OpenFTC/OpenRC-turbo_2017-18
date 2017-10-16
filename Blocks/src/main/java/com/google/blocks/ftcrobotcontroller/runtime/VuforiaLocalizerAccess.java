// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.concurrent.atomic.AtomicReference;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.Parameters;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/**
 * A class that provides JavaScript access to {@link VuforiaLocalizer}.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
class VuforiaLocalizerAccess extends Access {

  VuforiaLocalizerAccess(BlocksOpMode blocksOpMode, String identifier) {
    super(blocksOpMode, identifier);
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public VuforiaLocalizer create(Object vuforiaLocalizerParameters) {
    checkIfStopRequested();
    try {
      if (vuforiaLocalizerParameters instanceof Parameters) {
        // Because the JavaBridge thread is a looper, but not the main looper, we need to create
        // another thread to call ClassFactory.createVuforiaLocalizer(parameters). Otherwise the
        // Vuforia.UpdateCallbackInterface.Vuforia_onUpdate method is called on the JavaBridge
        // thread and the camera monitor view won't update until after waitForStart is finished.
        final Parameters parameters = (Parameters) vuforiaLocalizerParameters;
        final AtomicReference<VuforiaLocalizer> vuforiaLocalizerHolder = new AtomicReference<VuforiaLocalizer>();
        Thread thread = new Thread(new Runnable() {
          @Override
          public void run() {
            vuforiaLocalizerHolder.set(ClassFactory.createVuforiaLocalizer(parameters));
          }
        });
        thread.start();
        thread.join();
        return vuforiaLocalizerHolder.get();
      } else {
        RobotLog.e("VuforiaLocalizer.create - " +
            "vuforiaLocalizerParameters is not a VuforiaLocalizer.Parameters");
      }
    } catch (Exception e) {
      RobotLog.e("VuforiaLocalizer.create - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public VuforiaTrackables loadTrackablesFromAsset(Object vuforiaLocalizer, String assetName) {
    checkIfStopRequested();
    try {
      if (vuforiaLocalizer instanceof VuforiaLocalizer) {
        return ((VuforiaLocalizer) vuforiaLocalizer).loadTrackablesFromAsset(assetName);
      } else {
        RobotLog.e("VuforiaLocalizer.loadTrackablesFromAsset - " +
            "vuforiaLocalizerParameters is not a VuforiaLocalizer.Parameters");
      }
    } catch (Exception e) {
      RobotLog.e("VuforiaLocalizer.loadTrackablesFromAsset - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public VuforiaTrackables loadTrackablesFromFile(Object vuforiaLocalizer, String absoluteFileName) {
    checkIfStopRequested();
    try {
      if (vuforiaLocalizer instanceof VuforiaLocalizer) {
        return ((VuforiaLocalizer) vuforiaLocalizer).loadTrackablesFromFile(absoluteFileName);
      } else {
        RobotLog.e("VuforiaLocalizer.loadTrackablesFromFile - " +
            "vuforiaLocalizerParameters is not a VuforiaLocalizer.Parameters");
      }
    } catch (Exception e) {
      RobotLog.e("VuforiaLocalizer.loadTrackablesFromFile - caught " + e);
    }
    return null;
  }
}
