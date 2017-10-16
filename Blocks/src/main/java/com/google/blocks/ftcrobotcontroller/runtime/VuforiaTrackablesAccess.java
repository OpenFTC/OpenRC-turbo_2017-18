// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.qualcomm.robotcore.util.RobotLog;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/**
 * A class that provides JavaScript access to {@link VuforiaTrackables}.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
class VuforiaTrackablesAccess extends Access {

  VuforiaTrackablesAccess(BlocksOpMode blocksOpMode, String identifier) {
    super(blocksOpMode, identifier);
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public int getSize(Object vuforiaTrackables) {
    checkIfStopRequested();
    try {
      if (vuforiaTrackables instanceof VuforiaTrackables) {
        return ((VuforiaTrackables) vuforiaTrackables).size();
      } else {
        RobotLog.e("VuforiaTrackables.getSize - vuforiaTrackables is not a VuforiaTrackables");
      }
    } catch (Exception e) {
      RobotLog.e("VuforiaTrackables.getSize - caught " + e);
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public String getName(Object vuforiaTrackables) {
    checkIfStopRequested();
    try {
      if (vuforiaTrackables instanceof VuforiaTrackables) {
        String name = ((VuforiaTrackables) vuforiaTrackables).getName();
        if (name != null) {
          return name;
        }
      } else {
        RobotLog.e("VuforiaTrackables.getName - vuforiaTrackables is not a VuforiaTrackables");
      }
    } catch (Exception e) {
      RobotLog.e("VuforiaTrackables.getName - caught " + e);
    }
    return "";
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public VuforiaLocalizer getLocalizer(Object vuforiaTrackables) {
    checkIfStopRequested();
    try {
      if (vuforiaTrackables instanceof VuforiaTrackables) {
        return ((VuforiaTrackables) vuforiaTrackables).getLocalizer();
      } else {
        RobotLog.e("VuforiaTrackables.getLocalizer - vuforiaTrackables is not a VuforiaTrackables");
      }
    } catch (Exception e) {
      RobotLog.e("VuforiaTrackables.getLocalizer - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public VuforiaTrackable get(Object vuforiaTrackables, int index) {
    checkIfStopRequested();
    try {
      if (vuforiaTrackables instanceof VuforiaTrackables) {
        return ((VuforiaTrackables) vuforiaTrackables).get(index);
      } else {
        RobotLog.e("VuforiaTrackables.get - vuforiaTrackables is not a VuforiaTrackables");
      }
    } catch (Exception e) {
      RobotLog.e("VuforiaTrackables.get - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void setName(Object vuforiaTrackables, String name) {
    checkIfStopRequested();
    try {
      if (vuforiaTrackables instanceof VuforiaTrackables) {
        ((VuforiaTrackables) vuforiaTrackables).setName(name);
      } else {
        RobotLog.e("VuforiaTrackables.setName - vuforiaTrackables is not a VuforiaTrackables");
      }
    } catch (Exception e) {
      RobotLog.e("VuforiaTrackables.setName - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void activate(Object vuforiaTrackables) {
    checkIfStopRequested();
    try {
      if (vuforiaTrackables instanceof VuforiaTrackables) {
        ((VuforiaTrackables) vuforiaTrackables).activate();
      } else {
        RobotLog.e("VuforiaTrackables.activate - vuforiaTrackables is not a VuforiaTrackables");
      }
    } catch (Exception e) {
      RobotLog.e("VuforiaTrackables.activate - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void deactivate(Object vuforiaTrackables) {
    checkIfStopRequested();
    try {
      if (vuforiaTrackables instanceof VuforiaTrackables) {
        ((VuforiaTrackables) vuforiaTrackables).deactivate();
      } else {
        RobotLog.e("VuforiaTrackables.deactivate - vuforiaTrackables is not a VuforiaTrackables");
      }
    } catch (Exception e) {
      RobotLog.e("VuforiaTrackables.deactivate - caught " + e);
    }
  }
}
