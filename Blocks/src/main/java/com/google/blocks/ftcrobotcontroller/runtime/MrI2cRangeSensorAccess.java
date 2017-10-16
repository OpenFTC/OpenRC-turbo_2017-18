// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.google.blocks.ftcrobotcontroller.util.HardwareItem;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.util.RobotLog;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import java.util.Locale;
import junit.framework.Assert;

/**
 * A class that provides JavaScript access to a {@link ModernRoboticsI2cRangeSensor}.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
class MrI2cRangeSensorAccess extends HardwareAccess<ModernRoboticsI2cRangeSensor> {
  private final ModernRoboticsI2cRangeSensor mrI2cRangeSensor;

  MrI2cRangeSensorAccess(BlocksOpMode blocksOpMode, HardwareItem hardwareItem,
      HardwareMap hardwareMap, Class<? extends HardwareDevice> deviceType) {
    super(blocksOpMode, hardwareItem, hardwareMap, ModernRoboticsI2cRangeSensor.class);
    Assert.assertTrue(deviceType == ModernRoboticsI2cRangeSensor.class);
    this.mrI2cRangeSensor = hardwareDevice;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {ModernRoboticsI2cRangeSensor.class}, methodName = "getLightDetected")
  public double getLightDetected() {
    checkIfStopRequested();
    if (mrI2cRangeSensor != null) {
      return mrI2cRangeSensor.getLightDetected();
    }
    return 0.0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {ModernRoboticsI2cRangeSensor.class}, methodName = "getRawLightDetected")
  public double getRawLightDetected() {
    checkIfStopRequested();
    if (mrI2cRangeSensor != null) {
      return mrI2cRangeSensor.getRawLightDetected();
    }
    return 0.0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {ModernRoboticsI2cRangeSensor.class}, methodName = "getRawLightDetectedMax")
  public double getRawLightDetectedMax() {
    checkIfStopRequested();
    if (mrI2cRangeSensor != null) {
      return mrI2cRangeSensor.getRawLightDetectedMax();
    }
    return 0.0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {ModernRoboticsI2cRangeSensor.class}, methodName = "rawUltrasonic")
  public double getRawUltrasonic() {
    checkIfStopRequested();
    if (mrI2cRangeSensor != null) {
      return mrI2cRangeSensor.rawUltrasonic();
    }
    return 0.0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {ModernRoboticsI2cRangeSensor.class}, methodName = "rawOptical")
  public double getRawOptical() {
    checkIfStopRequested();
    if (mrI2cRangeSensor != null) {
      return mrI2cRangeSensor.rawOptical();
    }
    return 0.0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {ModernRoboticsI2cRangeSensor.class}, methodName = "cmUltrasonic")
  public double getCmUltrasonic() {
    checkIfStopRequested();
    if (mrI2cRangeSensor != null) {
      return mrI2cRangeSensor.cmUltrasonic();
    }
    return 0.0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {ModernRoboticsI2cRangeSensor.class}, methodName = "cmOptical")
  public double getCmOptical() {
    checkIfStopRequested();
    if (mrI2cRangeSensor != null) {
      return mrI2cRangeSensor.cmOptical();
    }
    return 0.0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {ModernRoboticsI2cRangeSensor.class}, methodName = "getDistance")
  public double getDistance(String distanceUnitString) {
    checkIfStopRequested();
    try {
      if (mrI2cRangeSensor != null) {
        DistanceUnit distanceUnit = DistanceUnit.valueOf(distanceUnitString.toUpperCase(Locale.ENGLISH));
        return mrI2cRangeSensor.getDistance(distanceUnit);
      }
    } catch (Exception e) {
      RobotLog.e("MrI2cRangeSensor.getDistance - caught " + e);
    }
    return 0.0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {ModernRoboticsI2cRangeSensor.class}, methodName = "setI2cAddress")
  public void setI2cAddress7Bit(int i2cAddr7Bit) {
    checkIfStopRequested();
    if (mrI2cRangeSensor != null) {
      mrI2cRangeSensor.setI2cAddress(I2cAddr.create7bit(i2cAddr7Bit));
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {ModernRoboticsI2cRangeSensor.class}, methodName = "getI2cAddress")
  public int getI2cAddress7Bit() {
    checkIfStopRequested();
    if (mrI2cRangeSensor != null) {
      I2cAddr i2cAddr = mrI2cRangeSensor.getI2cAddress();
      if (i2cAddr != null) {
        return i2cAddr.get7Bit();
      }
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {ModernRoboticsI2cRangeSensor.class}, methodName = "setI2cAddress")
  public void setI2cAddress8Bit(int i2cAddr8Bit) {
    checkIfStopRequested();
    if (mrI2cRangeSensor != null) {
      mrI2cRangeSensor.setI2cAddress(I2cAddr.create8bit(i2cAddr8Bit));
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {ModernRoboticsI2cRangeSensor.class}, methodName = "getI2cAddress")
  public int getI2cAddress8Bit() {
    checkIfStopRequested();
    if (mrI2cRangeSensor != null) {
      I2cAddr i2cAddr = mrI2cRangeSensor.getI2cAddress();
      if (i2cAddr != null) {
        return i2cAddr.get8Bit();
      }
    }
    return 0;
  }
}
