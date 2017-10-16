// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.google.blocks.ftcrobotcontroller.util.HardwareItem;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import junit.framework.Assert;

/**
 * A class that provides JavaScript access to a {@link AnalogInput}.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
class AnalogInputAccess extends HardwareAccess<AnalogInput> {
  private final AnalogInput analogInput;

  AnalogInputAccess(BlocksOpMode blocksOpMode, HardwareItem hardwareItem, HardwareMap hardwareMap,
      Class<? extends HardwareDevice> deviceType) {
    super(blocksOpMode, hardwareItem, hardwareMap, AnalogInput.class);
    Assert.assertTrue(deviceType == AnalogInput.class);
    this.analogInput = hardwareDevice;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = AnalogInput.class, methodName = "getVoltage")
  public double getVoltage() {
    checkIfStopRequested();
    if (analogInput != null) {
      return analogInput.getVoltage();
    }
    return 0.0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = AnalogInput.class, methodName = "getMaxVoltage")
  public double getMaxVoltage() {
    checkIfStopRequested();
    if (analogInput != null) {
      return analogInput.getMaxVoltage();
    }
    return 0.0;
  }
}
