// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.qualcomm.robotcore.util.RobotLog;
import org.firstinspires.ftc.robotcore.external.navigation.MagneticFlux;

/**
 * A class that provides JavaScript access to {@link MagneticFlux}.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
class MagneticFluxAccess extends Access {

  MagneticFluxAccess(BlocksOpMode blocksOpMode, String identifier) {
    super(blocksOpMode, identifier);
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public double getX(Object magneticFlux) {
    checkIfStopRequested();
    try {
      if (magneticFlux instanceof MagneticFlux) {
        return ((MagneticFlux) magneticFlux).x;
      } else {
        RobotLog.e("MagneticFlux.getX - magneticFlux is not a MagneticFlux");
      }
    } catch (Exception e) {
      RobotLog.e("MagneticFlux.getX - caught " + e);
    }
    return 0;
  }


  @SuppressWarnings("unused")
  @JavascriptInterface
  public double getY(Object magneticFlux) {
    checkIfStopRequested();
    try {
      if (magneticFlux instanceof MagneticFlux) {
        return ((MagneticFlux) magneticFlux).y;
      } else {
        RobotLog.e("MagneticFlux.getY - magneticFlux is not a MagneticFlux");
      }
    } catch (Exception e) {
      RobotLog.e("MagneticFlux.getY - caught " + e);
    }
    return 0;
  }


  @SuppressWarnings("unused")
  @JavascriptInterface
  public double getZ(Object magneticFlux) {
    checkIfStopRequested();
    try {
      if (magneticFlux instanceof MagneticFlux) {
        return ((MagneticFlux) magneticFlux).z;
      } else {
        RobotLog.e("MagneticFlux.getZ - magneticFlux is not a MagneticFlux");
      }
    } catch (Exception e) {
      RobotLog.e("MagneticFlux.getZ - caught " + e);
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public long getAcquisitionTime(Object magneticFlux) {
    checkIfStopRequested();
    try {
      if (magneticFlux instanceof MagneticFlux) {
        return ((MagneticFlux) magneticFlux).acquisitionTime;
      } else {
        RobotLog.e("MagneticFlux.getAcquisitionTime - magneticFlux is not a MagneticFlux");
      }
    } catch (Exception e) {
      RobotLog.e("MagneticFlux.getAcquisitionTime - caught " + e);
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public MagneticFlux create() {
    checkIfStopRequested();
    try {
      return new MagneticFlux();
    } catch (Exception e) {
      RobotLog.e("MagneticFlux.create - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public MagneticFlux create_withArgs(double x, double y, double z, long acquisitionTime) {
    checkIfStopRequested();
    try {
      return new MagneticFlux(x, y, z, acquisitionTime);
    } catch (Exception e) {
      RobotLog.e("MagneticFlux.create - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public String toText(Object magneticFlux) {
    checkIfStopRequested();
    try {
      if (magneticFlux instanceof MagneticFlux) {
        return ((MagneticFlux) magneticFlux).toString();
      } else {
        RobotLog.e("MagneticFlux.toText - magneticFlux is not a MagneticFlux");
      }
    } catch (Exception e) {
      RobotLog.e("MagneticFlux.toText - caught " + e);
    }
    return "";
  }
}
