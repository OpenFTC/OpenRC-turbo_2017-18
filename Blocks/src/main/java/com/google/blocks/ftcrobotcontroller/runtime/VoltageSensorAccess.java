// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.google.blocks.ftcrobotcontroller.util.HardwareItem;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import junit.framework.Assert;

/**
 * A class that provides JavaScript access to an {@link VoltageSensor}.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
class VoltageSensorAccess extends HardwareAccess<VoltageSensor> {
  private final VoltageSensor voltageSensor;

  VoltageSensorAccess(BlocksOpMode blocksOpMode, HardwareItem hardwareItem, HardwareMap hardwareMap,
      Class<? extends HardwareDevice> deviceType) {
    super(blocksOpMode, hardwareItem, hardwareMap, VoltageSensor.class);
    Assert.assertTrue(deviceType == VoltageSensor.class);
    this.voltageSensor = hardwareDevice;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public double getVoltage() {
    checkIfStopRequested();
    if (voltageSensor != null) {
      return voltageSensor.getVoltage();
    }
    return 0.0;
  }
}
