// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.google.blocks.ftcrobotcontroller.util.HardwareItem;
import com.qualcomm.hardware.hitechnic.HiTechnicNxtLightSensor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.util.RobotLog;
import junit.framework.Assert;

/**
 * A class that provides JavaScript access to a {@link LightSensor}.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
class LightSensorAccess extends HardwareAccess<LightSensor> {
  private final LightSensor lightSensor;

  LightSensorAccess(BlocksOpMode blocksOpMode, HardwareItem hardwareItem, HardwareMap hardwareMap,
      Class<? extends HardwareDevice> deviceType) {
    super(blocksOpMode, hardwareItem, hardwareMap, LightSensor.class);
    Assert.assertTrue(deviceType == LightSensor.class);
    this.lightSensor = hardwareDevice;
  }

  // from com.qualcomm.robotcore.hardware.LightSensor

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtLightSensor.class}, methodName = "getLightDetected")
  public double getLightDetected() {
    checkIfStopRequested();
    if (lightSensor != null) {
      return lightSensor.getLightDetected();
    }
    return 0.0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtLightSensor.class}, methodName = "getRawLightDetected")
  public double getRawLightDetected() {
    checkIfStopRequested();
    if (lightSensor != null) {
      return lightSensor.getRawLightDetected();
    }
    return 0.0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtLightSensor.class}, methodName = "getRawLightDetectedMax")
  public double getRawLightDetectedMax() {
    checkIfStopRequested();
    if (lightSensor != null) {
      return lightSensor.getRawLightDetectedMax();
    }
    return 0.0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtLightSensor.class}, methodName = "enableLed")
  public void enableLed(boolean enable) {
    checkIfStopRequested();
    try {
      if (lightSensor != null) {
        lightSensor.enableLed(enable);
      }
    } catch (Exception e) {
      RobotLog.e("LightSensor.enableLed - caught " + e);
    }
  }
}
