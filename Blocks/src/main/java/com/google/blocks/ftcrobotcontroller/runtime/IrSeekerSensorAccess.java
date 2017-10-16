// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.google.blocks.ftcrobotcontroller.util.HardwareItem;
import com.qualcomm.hardware.hitechnic.HiTechnicNxtIrSeekerSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cIrSeekerSensorV3;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;
import com.qualcomm.robotcore.hardware.IrSeekerSensor.Mode;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.Locale;
import junit.framework.Assert;

/**
 * A class that provides JavaScript access to a {@link IrSeekerSensor}.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
class IrSeekerSensorAccess extends HardwareAccess<IrSeekerSensor> {
  private final IrSeekerSensor irSeekerSensor;

  IrSeekerSensorAccess(BlocksOpMode blocksOpMode, HardwareItem hardwareItem, HardwareMap hardwareMap,
      Class<? extends HardwareDevice> deviceType) {
    super(blocksOpMode, hardwareItem, hardwareMap, IrSeekerSensor.class);
    Assert.assertTrue(deviceType == IrSeekerSensor.class);
    this.irSeekerSensor = hardwareDevice;
  }

  // Properties

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtIrSeekerSensor.class, ModernRoboticsI2cIrSeekerSensorV3.class}, methodName = "setSignalDetectedThreshold")
  public void setSignalDetectedThreshold(double threshold) {
    checkIfStopRequested();
    if (irSeekerSensor != null) {
      irSeekerSensor.setSignalDetectedThreshold(threshold);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtIrSeekerSensor.class, ModernRoboticsI2cIrSeekerSensorV3.class}, methodName = "getSignalDetectedThreshold")
  public double getSignalDetectedThreshold() {
    checkIfStopRequested();
    if (irSeekerSensor != null) {
      return irSeekerSensor.getSignalDetectedThreshold();
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtIrSeekerSensor.class, ModernRoboticsI2cIrSeekerSensorV3.class}, methodName = "setMode")
  public void setMode(String modeString) {
    checkIfStopRequested();
    try {
      if (irSeekerSensor != null) {
        Mode mode = Mode.valueOf(modeString.toUpperCase(Locale.ENGLISH));
        irSeekerSensor.setMode(mode);
      }
    } catch (Exception e) {
      RobotLog.e("IrSeekerSensor.setMode - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtIrSeekerSensor.class, ModernRoboticsI2cIrSeekerSensorV3.class}, methodName = "getMode")
  public String getMode() {
    checkIfStopRequested();
    if (irSeekerSensor != null) {
      Mode mode = irSeekerSensor.getMode();
      if (mode != null) {
        return mode.toString();
      }
    }
    return "";
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtIrSeekerSensor.class, ModernRoboticsI2cIrSeekerSensorV3.class}, methodName = "signalDetected")
  public boolean getIsSignalDetected() {
    checkIfStopRequested();
    if (irSeekerSensor != null) {
      return irSeekerSensor.signalDetected();
    }
    return false;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtIrSeekerSensor.class, ModernRoboticsI2cIrSeekerSensorV3.class}, methodName = "getAngle")
  public double getAngle() {
    checkIfStopRequested();
    if (irSeekerSensor != null) {
      return irSeekerSensor.getAngle();
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtIrSeekerSensor.class, ModernRoboticsI2cIrSeekerSensorV3.class}, methodName = "getStrength")
  public double getStrength() {
    checkIfStopRequested();
    if (irSeekerSensor != null) {
      return irSeekerSensor.getStrength();
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtIrSeekerSensor.class, ModernRoboticsI2cIrSeekerSensorV3.class}, methodName = "setI2cAddress")
  public void setI2cAddress7Bit(int i2cAddr7Bit) {
    checkIfStopRequested();
    try {
      if (irSeekerSensor != null) {
        irSeekerSensor.setI2cAddress(I2cAddr.create7bit(i2cAddr7Bit));
      }
    } catch (Exception e) {
      RobotLog.e("IrSeekerSensor.setI2cAddress7Bit - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtIrSeekerSensor.class, ModernRoboticsI2cIrSeekerSensorV3.class}, methodName = "getI2cAddress")
  public int getI2cAddress7Bit() {
    checkIfStopRequested();
    if (irSeekerSensor != null) {
      I2cAddr i2cAddr = irSeekerSensor.getI2cAddress();
      if (i2cAddr != null) {
        return i2cAddr.get7Bit();
      }
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtIrSeekerSensor.class, ModernRoboticsI2cIrSeekerSensorV3.class}, methodName = "setI2cAddress")
  public void setI2cAddress8Bit(int i2cAddr8Bit) {
    checkIfStopRequested();
    try {
      if (irSeekerSensor != null) {
        irSeekerSensor.setI2cAddress(I2cAddr.create8bit(i2cAddr8Bit));
      }
    } catch (Exception e) {
      RobotLog.e("IrSeekerSensor.setI2cAddress8Bit - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtIrSeekerSensor.class, ModernRoboticsI2cIrSeekerSensorV3.class}, methodName = "getI2cAddress")
  public int getI2cAddress8Bit() {
    checkIfStopRequested();
    if (irSeekerSensor != null) {
      I2cAddr i2cAddr = irSeekerSensor.getI2cAddress();
      if (i2cAddr != null) {
        return i2cAddr.get8Bit();
      }
    }
    return 0;
  }
}
