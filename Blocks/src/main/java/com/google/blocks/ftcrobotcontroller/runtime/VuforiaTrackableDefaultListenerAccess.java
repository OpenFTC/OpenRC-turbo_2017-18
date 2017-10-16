// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.Locale;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;

/**
 * A class that provides JavaScript access to {@link VuforiaTrackableDefaultListener}.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
class VuforiaTrackableDefaultListenerAccess extends Access {

  VuforiaTrackableDefaultListenerAccess(BlocksOpMode blocksOpMode, String identifier) {
    super(blocksOpMode, identifier);
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void setPhoneInformation(
      Object vuforiaTrackableDefaultListener, Object phoneLocationOnRobot,
      String cameraDirectionString) {
    checkIfStopRequested();
    try {
      if (vuforiaTrackableDefaultListener instanceof VuforiaTrackableDefaultListener) {
        if (phoneLocationOnRobot instanceof OpenGLMatrix) {
          CameraDirection cameraDirection =
              CameraDirection.valueOf(cameraDirectionString.toUpperCase(Locale.ENGLISH));
          ((VuforiaTrackableDefaultListener) vuforiaTrackableDefaultListener).setPhoneInformation(
              (OpenGLMatrix) phoneLocationOnRobot, cameraDirection);
        } else {
          RobotLog.e("VuforiaTrackableDefaultListener.setPhoneInformation - " +
              "phoneLocationOnRobot is not an OpenGLMatrix");
        }
      } else {
        RobotLog.e("VuforiaTrackableDefaultListener.setPhoneInformation - " +
            "vuforiaTrackableDefaultListener is not a VuforiaTrackableDefaultListener");
      }
    } catch (Exception e) {
      RobotLog.e("VuforiaTrackableDefaultListener.setPhoneInformation - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public boolean isVisible(Object vuforiaTrackableDefaultListener) {
    checkIfStopRequested();
    try {
      if (vuforiaTrackableDefaultListener instanceof VuforiaTrackableDefaultListener) {
        return ((VuforiaTrackableDefaultListener) vuforiaTrackableDefaultListener).isVisible();
      } else {
        RobotLog.e("VuforiaTrackableDefaultListener.isVisible - " +
            "vuforiaTrackableDefaultListener is not a VuforiaTrackableDefaultListener");
      }
    } catch (Exception e) {
      RobotLog.e("VuforiaTrackableDefaultListener.isVisible - caught " + e);
    }
    return false;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public OpenGLMatrix getUpdatedRobotLocation(Object vuforiaTrackableDefaultListener) {
    checkIfStopRequested();
    try {
      if (vuforiaTrackableDefaultListener instanceof VuforiaTrackableDefaultListener) {
        return ((VuforiaTrackableDefaultListener) vuforiaTrackableDefaultListener).getUpdatedRobotLocation();
      } else {
        RobotLog.e("VuforiaTrackableDefaultListener.getUpdatedRobotLocation - " +
            "vuforiaTrackableDefaultListener is not a VuforiaTrackableDefaultListener");
      }
    } catch (Exception e) {
      RobotLog.e("VuforiaTrackableDefaultListener.getUpdatedRobotLocation - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public OpenGLMatrix getPose(Object vuforiaTrackableDefaultListener) {
    checkIfStopRequested();
    try {
      if (vuforiaTrackableDefaultListener instanceof VuforiaTrackableDefaultListener) {
        return ((VuforiaTrackableDefaultListener) vuforiaTrackableDefaultListener).getPose();
      } else {
        RobotLog.e("VuforiaTrackableDefaultListener.getPose - " +
            "vuforiaTrackableDefaultListener is not a VuforiaTrackableDefaultListener");
      }
    } catch (Exception e) {
      RobotLog.e("VuforiaTrackableDefaultListener.getPose - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public String getRelicRecoveryVuMark(Object vuforiaTrackableDefaultListener) {
    checkIfStopRequested();
    try {
      if (vuforiaTrackableDefaultListener instanceof VuforiaTrackableDefaultListener) {
        return RelicRecoveryVuMark.from((VuforiaTrackableDefaultListener) vuforiaTrackableDefaultListener).toString();
      } else {
        RobotLog.e("VuforiaTrackableDefaultListener.getRelicRecoveryVuMark - " +
            "vuforiaTrackableDefaultListener is not a VuforiaTrackableDefaultListener");
      }
    } catch (Exception e) {
      RobotLog.e("VuforiaTrackableDefaultListener.getRelicRecoveryVuMark - caught " + e);
    }
    return RelicRecoveryVuMark.UNKNOWN.toString();
  }
}
