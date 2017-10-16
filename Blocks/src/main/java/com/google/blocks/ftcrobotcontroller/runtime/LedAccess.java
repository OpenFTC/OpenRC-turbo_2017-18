// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.google.blocks.ftcrobotcontroller.util.HardwareItem;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.LED;
import com.qualcomm.robotcore.util.RobotLog;
import junit.framework.Assert;

/**
 * A class that provides JavaScript access to a {@link LED}.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
class LedAccess extends HardwareAccess<LED> {
  private final LED led;

  LedAccess(BlocksOpMode blocksOpMode, HardwareItem hardwareItem, HardwareMap hardwareMap,
      Class<? extends HardwareDevice> deviceType) {
    super(blocksOpMode, hardwareItem, hardwareMap, LED.class);
    Assert.assertTrue(deviceType == LED.class);
    this.led = hardwareDevice;
  }

  // from com.qualcomm.robotcore.hardware.LED

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {LED.class}, methodName = "enable")
  public void enableLed(boolean enable) {
    checkIfStopRequested();
    try {
      if (led != null) {
        led.enable(enable);
      }
    } catch (Exception e) {
      RobotLog.e("Led.enableLed - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {LED.class}, methodName = "isLightOn")
  public boolean isLightOn() {
    checkIfStopRequested();
    try {
      if (led != null) {
        return led.isLightOn();
      }
    } catch (Exception e) {
      RobotLog.e("Led.isLightOn - caught " + e);
    }
    return false;
  }
}
