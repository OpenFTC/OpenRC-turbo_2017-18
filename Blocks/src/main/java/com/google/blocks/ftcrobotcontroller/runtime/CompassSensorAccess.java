// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.google.blocks.ftcrobotcontroller.util.HardwareItem;
import com.qualcomm.hardware.hitechnic.HiTechnicNxtCompassSensor;
import com.qualcomm.robotcore.hardware.CompassSensor;
import com.qualcomm.robotcore.hardware.CompassSensor.CompassMode;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.Locale;
import junit.framework.Assert;

/**
 * A class that provides JavaScript access to a {@link CompassSensor}.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
class CompassSensorAccess extends HardwareAccess<CompassSensor> {
  private final CompassSensor compassSensor;

  CompassSensorAccess(BlocksOpMode blocksOpMode, HardwareItem hardwareItem, HardwareMap hardwareMap, Class<? extends HardwareDevice> deviceType) {
    super(blocksOpMode, hardwareItem, hardwareMap, CompassSensor.class);
    Assert.assertTrue(deviceType == CompassSensor.class);
    this.compassSensor = hardwareDevice;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtCompassSensor.class}, methodName = "getDirection")
  public double getDirection() {
    checkIfStopRequested();
    if (compassSensor != null) {
      return compassSensor.getDirection();
    }
    return 0.0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtCompassSensor.class}, methodName = "calibrationFailed")
  public boolean getCalibrationFailed() {
    checkIfStopRequested();
    if (compassSensor != null) {
      return compassSensor.calibrationFailed();
    }
    return false;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtCompassSensor.class}, methodName = "setMode")
  public void setMode(String compassModeString) {
    checkIfStopRequested();
    try {
      if (compassSensor != null) {
        CompassMode compassMode = CompassMode.valueOf(compassModeString.toUpperCase(Locale.ENGLISH));
        compassSensor.setMode(compassMode);
      }
    } catch (Exception e) {
      RobotLog.e("CompassSensor.setMode - caught " + e);
    }
  }
}
