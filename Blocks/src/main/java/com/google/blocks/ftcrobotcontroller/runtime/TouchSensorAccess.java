// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.google.blocks.ftcrobotcontroller.util.HardwareItem;
import com.qualcomm.hardware.hitechnic.HiTechnicNxtTouchSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsTouchSensor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;
import junit.framework.Assert;

/**
 * A class that provides JavaScript access to a {@link TouchSensor}.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
class TouchSensorAccess extends HardwareAccess<TouchSensor> {
  private final TouchSensor touchSensor;

  TouchSensorAccess(BlocksOpMode blocksOpMode, HardwareItem hardwareItem, HardwareMap hardwareMap,
      Class<? extends HardwareDevice> deviceType) {
    super(blocksOpMode, hardwareItem, hardwareMap, TouchSensor.class);
    Assert.assertTrue(deviceType == TouchSensor.class);
    this.touchSensor = hardwareDevice;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtTouchSensor.class, ModernRoboticsTouchSensor.class}, methodName = "isPressed")
  public boolean getIsPressed() {
    checkIfStopRequested();
    if (touchSensor != null) {
      return touchSensor.isPressed();
    }
    return false;
  }
}
