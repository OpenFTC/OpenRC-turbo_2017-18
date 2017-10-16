// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.qualcomm.robotcore.util.RobotLog;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/**
 * A class that provides JavaScript access to {@link VuforiaTrackable}.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
class VuforiaTrackableAccess extends Access {

  VuforiaTrackableAccess(BlocksOpMode blocksOpMode, String identifier) {
    super(blocksOpMode, identifier);
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void setLocation(Object vuforiaTrackable, Object matrix) {
    checkIfStopRequested();
    try {
      if (vuforiaTrackable instanceof VuforiaTrackable) {
        if (matrix instanceof OpenGLMatrix) {
          ((VuforiaTrackable) vuforiaTrackable).setLocation((OpenGLMatrix) matrix);
        } else {
          RobotLog.e("VuforiaTrackable.setLocation - matrix is not an OpenGLMatrix");
        }
      } else {
        RobotLog.e("VuforiaTrackable.setLocation - vuforiaTrackable is not a VuforiaTrackable");
      }
    } catch (Exception e) {
      RobotLog.e("VuforiaTrackable.setLocation - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public OpenGLMatrix getLocation(Object vuforiaTrackable) {
    checkIfStopRequested();
    try {
      if (vuforiaTrackable instanceof VuforiaTrackable) {
        return ((VuforiaTrackable) vuforiaTrackable).getLocation();
      } else {
        RobotLog.e("VuforiaTrackable.getLocation - vuforiaTrackable is not a VuforiaTrackable");
      }
    } catch (Exception e) {
      RobotLog.e("VuforiaTrackable.getLocation - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void setUserData(Object vuforiaTrackable, Object userData) {
    checkIfStopRequested();
    try {
      if (vuforiaTrackable instanceof VuforiaTrackable) {
        ((VuforiaTrackable) vuforiaTrackable).setUserData(userData);
      } else {
        RobotLog.e("VuforiaTrackable.setUserData - vuforiaTrackable is not a VuforiaTrackable");
      }
    } catch (Exception e) {
      RobotLog.e("VuforiaTrackable.setUserData - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public Object getUserData(Object vuforiaTrackable) {
    checkIfStopRequested();
    try {
      if (vuforiaTrackable instanceof VuforiaTrackable) {
        return ((VuforiaTrackable) vuforiaTrackable).getUserData();
      } else {
        RobotLog.e("VuforiaTrackable.getUserData - vuforiaTrackable is not a VuforiaTrackable");
      }
    } catch (Exception e) {
      RobotLog.e("VuforiaTrackable.getUserData - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public VuforiaTrackables getTrackables(Object vuforiaTrackable) {
    checkIfStopRequested();
    try {
      if (vuforiaTrackable instanceof VuforiaTrackable) {
        return ((VuforiaTrackable) vuforiaTrackable).getTrackables();
      } else {
        RobotLog.e("VuforiaTrackable.getTrackables - vuforiaTrackable is not a VuforiaTrackable");
      }
    } catch (Exception e) {
      RobotLog.e("VuforiaTrackable.getTrackables - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void setName(Object vuforiaTrackable, String name) {
    checkIfStopRequested();
    try {
      if (vuforiaTrackable instanceof VuforiaTrackable) {
        ((VuforiaTrackable) vuforiaTrackable).setName(name);
      } else {
        RobotLog.e("VuforiaTrackable.setName - vuforiaTrackable is not a VuforiaTrackable");
      }
    } catch (Exception e) {
      RobotLog.e("VuforiaTrackable.setName - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public String getName(Object vuforiaTrackable) {
    checkIfStopRequested();
    try {
      if (vuforiaTrackable instanceof VuforiaTrackable) {
        String name = ((VuforiaTrackable) vuforiaTrackable).getName();
        if (name != null) {
          return name;
        }
      } else {
        RobotLog.e("VuforiaTrackable.getName - vuforiaTrackable is not a VuforiaTrackable");
      }
    } catch (Exception e) {
      RobotLog.e("VuforiaTrackable.getName - caught " + e);
    }
    return "";
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public VuforiaTrackable.Listener getListener(Object vuforiaTrackable) {
    checkIfStopRequested();
    try {
      if (vuforiaTrackable instanceof VuforiaTrackable) {
        return ((VuforiaTrackable) vuforiaTrackable).getListener();
      } else {
        RobotLog.e("VuforiaTrackable.getListener - vuforiaTrackable is not a VuforiaTrackable");
      }
    } catch (Exception e) {
      RobotLog.e("VuforiaTrackable.getListener - caught " + e);
    }
    return null;
  }
}
