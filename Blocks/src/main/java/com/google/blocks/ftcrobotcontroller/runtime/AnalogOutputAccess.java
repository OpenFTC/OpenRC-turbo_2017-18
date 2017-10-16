// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.google.blocks.ftcrobotcontroller.util.HardwareItem;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.AnalogOutput;
import com.qualcomm.robotcore.util.RobotLog;
import junit.framework.Assert;

/**
 * A class that provides JavaScript access to a {@link AnalogOutput}.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
class AnalogOutputAccess extends HardwareAccess<AnalogOutput> {
  private final AnalogOutput analogOutput;

  AnalogOutputAccess(BlocksOpMode blocksOpMode, 
          HardwareItem hardwareItem, HardwareMap hardwareMap, Class<? extends HardwareDevice> deviceType) {
    super(blocksOpMode, hardwareItem, hardwareMap, AnalogOutput.class);
    Assert.assertTrue(deviceType == AnalogOutput.class);
    this.analogOutput = hardwareDevice;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = AnalogOutput.class, methodName = "setAnalogOutputVoltage")
  public void setAnalogOutputVoltage(int voltage) {
    checkIfStopRequested();
    try {
      if (analogOutput != null) {
        analogOutput.setAnalogOutputVoltage(voltage);
      }
    } catch (Exception e) {
      RobotLog.e("AnalogOutput.setAnalogOutputVoltage - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = AnalogOutput.class, methodName = "setAnalogOutputFrequency")
  public void setAnalogOutputFrequency(int frequency) {
    checkIfStopRequested();
    try {
      if (analogOutput != null) {
        analogOutput.setAnalogOutputFrequency(frequency);
      }
    } catch (Exception e) {
      RobotLog.e("AnalogOutput.setAnalogOutputFrequency - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = AnalogOutput.class, methodName = "setAnalogOutputMode")
  public void setAnalogOutputMode(int mode) {
    checkIfStopRequested();
    try {
      if (analogOutput != null) {
        analogOutput.setAnalogOutputMode((byte) mode);
      }
    } catch (Exception e) {
      RobotLog.e("AnalogOutput.setAnalogOutputMode - caught " + e);
    }
  }
}
