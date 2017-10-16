// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.google.blocks.ftcrobotcontroller.util.HardwareItem;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsAnalogOpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.util.RobotLog;
import junit.framework.Assert;

/**
 * A class that provides JavaScript access to a {@link OpticalDistanceSensor}.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
class OpticalDistanceSensorAccess extends HardwareAccess<OpticalDistanceSensor> {
  private final OpticalDistanceSensor opticalDistanceSensor;

  OpticalDistanceSensorAccess(BlocksOpMode blocksOpMode, HardwareItem hardwareItem,
      HardwareMap hardwareMap, Class<? extends HardwareDevice> deviceType) {
    super(blocksOpMode, hardwareItem, hardwareMap, OpticalDistanceSensor.class);
    Assert.assertTrue(deviceType == OpticalDistanceSensor.class);
    this.opticalDistanceSensor = hardwareDevice;
  }

  // from com.qualcomm.robotcore.hardware.LightSensor

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {ModernRoboticsAnalogOpticalDistanceSensor.class}, methodName = "getLightDetected")
  public double getLightDetected() {
    checkIfStopRequested();
    if (opticalDistanceSensor != null) {
      return opticalDistanceSensor.getLightDetected();
    }
    return 0.0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {ModernRoboticsAnalogOpticalDistanceSensor.class}, methodName = "getRawLightDetected")
  public double getRawLightDetected() {
    checkIfStopRequested();
    if (opticalDistanceSensor != null) {
      return opticalDistanceSensor.getRawLightDetected();
    }
    return 0.0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {ModernRoboticsAnalogOpticalDistanceSensor.class}, methodName = "getRawLightDetectedMax")
  public double getRawLightDetectedMax() {
    checkIfStopRequested();
    if (opticalDistanceSensor != null) {
      return opticalDistanceSensor.getRawLightDetectedMax();
    }
    return 0.0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {ModernRoboticsAnalogOpticalDistanceSensor.class}, methodName = "enableLed")
  public void enableLed(boolean enable) {
    checkIfStopRequested();
    try {
      if (opticalDistanceSensor != null) {
        opticalDistanceSensor.enableLed(enable);
      }
    } catch (Exception e) {
      RobotLog.e("OpticalDistanceSensor.enableLed - caught " + e);
    }
  }
}
