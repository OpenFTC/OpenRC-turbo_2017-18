// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.google.blocks.ftcrobotcontroller.util.HardwareItem;
import com.qualcomm.hardware.lynx.LynxI2cColorRangeSensor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.Locale;
import junit.framework.Assert;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

/**
 * A class that provides JavaScript access to a {@link LynxI2cColorRangeSensor}.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
class LynxI2cColorRangeSensorAccess extends HardwareAccess<LynxI2cColorRangeSensor> {
  private final LynxI2cColorRangeSensor lynxI2cColorRangeSensor;

  LynxI2cColorRangeSensorAccess(BlocksOpMode blocksOpMode, HardwareItem hardwareItem, HardwareMap hardwareMap, Class<? extends HardwareDevice> deviceType) {
    super(blocksOpMode, hardwareItem, hardwareMap, LynxI2cColorRangeSensor.class);
    Assert.assertTrue(deviceType == LynxI2cColorRangeSensor.class);
    this.lynxI2cColorRangeSensor = hardwareDevice;
  }

  // Properties

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {LynxI2cColorRangeSensor.class}, methodName = "red")
  public int getRed() {
    checkIfStopRequested();
    if (lynxI2cColorRangeSensor != null) {
      return lynxI2cColorRangeSensor.red();
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {LynxI2cColorRangeSensor.class}, methodName = "green")
  public int getGreen() {
    checkIfStopRequested();
    if (lynxI2cColorRangeSensor != null) {
      return lynxI2cColorRangeSensor.green();
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {LynxI2cColorRangeSensor.class}, methodName = "blue")
  public int getBlue() {
    checkIfStopRequested();
    if (lynxI2cColorRangeSensor != null) {
      return lynxI2cColorRangeSensor.blue();
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {LynxI2cColorRangeSensor.class}, methodName = "alpha")
  public int getAlpha() {
    checkIfStopRequested();
    if (lynxI2cColorRangeSensor != null) {
      return lynxI2cColorRangeSensor.alpha();
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {LynxI2cColorRangeSensor.class}, methodName = "argb")
  public int getArgb() {
    checkIfStopRequested();
    if (lynxI2cColorRangeSensor != null) {
      return lynxI2cColorRangeSensor.argb();
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {LynxI2cColorRangeSensor.class}, methodName = "setI2cAddress")
  public void setI2cAddress7Bit(int i2cAddr7Bit) {
    checkIfStopRequested();
    try {
      if (lynxI2cColorRangeSensor != null) {
        lynxI2cColorRangeSensor.setI2cAddress(I2cAddr.create7bit(i2cAddr7Bit));
      }
    } catch (Exception e) {
      RobotLog.e("LynxI2cColorRangeSensor.setI2cAddress7Bit - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {LynxI2cColorRangeSensor.class}, methodName = "getI2cAddress")
  public int getI2cAddress7Bit() {
    checkIfStopRequested();
    if (lynxI2cColorRangeSensor != null) {
      I2cAddr i2cAddr = lynxI2cColorRangeSensor.getI2cAddress();
      if (i2cAddr != null) {
        return i2cAddr.get7Bit();
      }
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {LynxI2cColorRangeSensor.class}, methodName = "setI2cAddress")
  public void setI2cAddress8Bit(int i2cAddr8Bit) {
    checkIfStopRequested();
    try {
      if (lynxI2cColorRangeSensor != null) {
        lynxI2cColorRangeSensor.setI2cAddress(I2cAddr.create8bit(i2cAddr8Bit));
      }
    } catch (Exception e) {
      RobotLog.e("LynxI2cColorRangeSensor.setI2cAddress8Bit - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {LynxI2cColorRangeSensor.class}, methodName = "getI2cAddress")
  public int getI2cAddress8Bit() {
    checkIfStopRequested();
    if (lynxI2cColorRangeSensor != null) {
      I2cAddr i2cAddr = lynxI2cColorRangeSensor.getI2cAddress();
      if (i2cAddr != null) {
        return i2cAddr.get8Bit();
      }
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {LynxI2cColorRangeSensor.class}, methodName = "getLightDetected")
  public double getLightDetected() {
    checkIfStopRequested();
    if (lynxI2cColorRangeSensor != null) {
      return lynxI2cColorRangeSensor.getLightDetected();
    }
    return 0.0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {LynxI2cColorRangeSensor.class}, methodName = "getRawLightDetected")
  public double getRawLightDetected() {
    checkIfStopRequested();
    if (lynxI2cColorRangeSensor != null) {
      return lynxI2cColorRangeSensor.getRawLightDetected();
    }
    return 0.0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {LynxI2cColorRangeSensor.class}, methodName = "getRawLightDetectedMax")
  public double getRawLightDetectedMax() {
    checkIfStopRequested();
    if (lynxI2cColorRangeSensor != null) {
      return lynxI2cColorRangeSensor.getRawLightDetectedMax();
    }
    return 0.0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {LynxI2cColorRangeSensor.class}, methodName = "getDistance")
  public double getDistance(String distanceUnitString) {
    checkIfStopRequested();
    try {
      if (lynxI2cColorRangeSensor != null) {
        DistanceUnit distanceUnit = DistanceUnit.valueOf(distanceUnitString.toUpperCase(Locale.ENGLISH));
        return lynxI2cColorRangeSensor.getDistance(distanceUnit);
      }
    } catch (Exception e) {
      RobotLog.e("LynxI2cColorRangeSensor.getDistance - caught " + e);
    }
    return 0.0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {LynxI2cColorRangeSensor.class}, methodName = "getNormalizedColors")
  public String getNormalizedColors() {
    checkIfStopRequested();
    if (lynxI2cColorRangeSensor != null) {
      NormalizedRGBA color = lynxI2cColorRangeSensor.getNormalizedColors();
      return "{ \"Red\":" + color.red +
          ", \"Green\":" + color.green +
          ", \"Blue\":" + color.blue +
          ", \"Alpha\":" + color.alpha +
          ", \"Color\":" + color.toColor() + " }";
    }
    return "{ \"Red\":0" +
        ", \"Green\":0" +
        ", \"Blue\":0" +
        ", \"Alpha\":0" +
        ", \"Color\":0 }";
  }
}
