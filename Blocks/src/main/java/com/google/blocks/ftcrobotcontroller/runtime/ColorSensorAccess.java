// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.google.blocks.ftcrobotcontroller.util.HardwareItem;
import com.qualcomm.hardware.adafruit.AdafruitI2cColorSensor;
import com.qualcomm.hardware.hitechnic.HiTechnicNxtColorSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.Light;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.SwitchableLight;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.Locale;
import junit.framework.Assert;

/**
 * A class that provides JavaScript access to a {@link ColorSensor}.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
class ColorSensorAccess extends HardwareAccess<ColorSensor> {
  private final ColorSensor colorSensor;

  ColorSensorAccess(BlocksOpMode blocksOpMode, HardwareItem hardwareItem, HardwareMap hardwareMap, Class<? extends HardwareDevice> deviceType) {
    super(blocksOpMode, hardwareItem, hardwareMap, ColorSensor.class);
    Assert.assertTrue(deviceType == ColorSensor.class);
    this.colorSensor = hardwareDevice;
  }

  // Properties

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {AdafruitI2cColorSensor.class, HiTechnicNxtColorSensor.class, ModernRoboticsI2cColorSensor.class},
      methodName = "red")
  public int getRed() {
    checkIfStopRequested();
    if (colorSensor != null) {
      return colorSensor.red();
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {AdafruitI2cColorSensor.class, HiTechnicNxtColorSensor.class, ModernRoboticsI2cColorSensor.class},
      methodName = "green")
  public int getGreen() {
    checkIfStopRequested();
    if (colorSensor != null) {
      return colorSensor.green();
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {AdafruitI2cColorSensor.class, HiTechnicNxtColorSensor.class, ModernRoboticsI2cColorSensor.class},
      methodName = "blue")
  public int getBlue() {
    checkIfStopRequested();
    if (colorSensor != null) {
      return colorSensor.blue();
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {AdafruitI2cColorSensor.class, HiTechnicNxtColorSensor.class, ModernRoboticsI2cColorSensor.class},
      methodName = "alpha")
  public int getAlpha() {
    checkIfStopRequested();
    if (colorSensor != null) {
      return colorSensor.alpha();
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {AdafruitI2cColorSensor.class, HiTechnicNxtColorSensor.class, ModernRoboticsI2cColorSensor.class},
      methodName = "argb")
  public int getArgb() {
    checkIfStopRequested();
    if (colorSensor != null) {
      return colorSensor.argb();
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {AdafruitI2cColorSensor.class, HiTechnicNxtColorSensor.class, ModernRoboticsI2cColorSensor.class},
      methodName = "enableLed")
  public void enableLed(boolean enable) {
    checkIfStopRequested();
    try {
      if (colorSensor != null) {
        colorSensor.enableLed(enable);
      }
    } catch (Exception e) {
      RobotLog.e("ColorSensor.enableLed - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtColorSensor.class, ModernRoboticsI2cColorSensor.class},
      methodName = "enableLight")
  public void enableLight(boolean enable) {
    checkIfStopRequested();
    try {
      if (colorSensor != null) {
        if (colorSensor instanceof SwitchableLight) {
          ((SwitchableLight) colorSensor).enableLight(enable);
        } else {
          RobotLog.e("ColorSensor.enableLight - color sensor is not a SwitchableLight");
        }
      }
    } catch (Exception e) {
      RobotLog.e("ColorSensor.enableLight - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {AdafruitI2cColorSensor.class, HiTechnicNxtColorSensor.class, ModernRoboticsI2cColorSensor.class},
      methodName = "isLightOn")
  public boolean isLightOn() {
    checkIfStopRequested();
    try {
      if (colorSensor != null) {
        if (colorSensor instanceof Light) {
          return ((Light) colorSensor).isLightOn();
        } else {
          RobotLog.e("ColorSensor.isLightOn - color sensor is not a Light");
        }
      }
    } catch (Exception e) {
      RobotLog.e("ColorSensor.isLightOn - caught " + e);
    }
    return false;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {AdafruitI2cColorSensor.class, HiTechnicNxtColorSensor.class, ModernRoboticsI2cColorSensor.class},
      methodName = "setI2cAddress")
  public void setI2cAddress7Bit(int i2cAddr7Bit) {
    checkIfStopRequested();
    try {
      if (colorSensor != null) {
        colorSensor.setI2cAddress(I2cAddr.create7bit(i2cAddr7Bit));
      }
    } catch (Exception e) {
      RobotLog.e("ColorSensor.setI2cAddress7Bit - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {AdafruitI2cColorSensor.class, HiTechnicNxtColorSensor.class, ModernRoboticsI2cColorSensor.class},
      methodName = "getI2cAddress")
  public int getI2cAddress7Bit() {
    checkIfStopRequested();
    if (colorSensor != null) {
      I2cAddr i2cAddr = colorSensor.getI2cAddress();
      if (i2cAddr != null) {
        return i2cAddr.get7Bit();
      }
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {AdafruitI2cColorSensor.class, HiTechnicNxtColorSensor.class, ModernRoboticsI2cColorSensor.class},
      methodName = "setI2cAddress")
  public void setI2cAddress8Bit(int i2cAddr8Bit) {
    checkIfStopRequested();
    try {
      if (colorSensor != null) {
        colorSensor.setI2cAddress(I2cAddr.create8bit(i2cAddr8Bit));
      }
    } catch (Exception e) {
      RobotLog.e("ColorSensor.setI2cAddress8Bit - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {AdafruitI2cColorSensor.class, HiTechnicNxtColorSensor.class, ModernRoboticsI2cColorSensor.class},
      methodName = "getI2cAddress")
  public int getI2cAddress8Bit() {
    checkIfStopRequested();
    if (colorSensor != null) {
      I2cAddr i2cAddr = colorSensor.getI2cAddress();
      if (i2cAddr != null) {
        return i2cAddr.get8Bit();
      }
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {AdafruitI2cColorSensor.class, HiTechnicNxtColorSensor.class, ModernRoboticsI2cColorSensor.class},
      methodName = "toString")
  public String toText() {
    checkIfStopRequested();
    if (colorSensor != null) {
      return colorSensor.toString();
    }
    return "";
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {AdafruitI2cColorSensor.class, HiTechnicNxtColorSensor.class, ModernRoboticsI2cColorSensor.class},
      methodName = "getNormalizedColors")
  public String getNormalizedColors() {
    checkIfStopRequested();
    if (colorSensor instanceof NormalizedColorSensor) {
      NormalizedRGBA color = ((NormalizedColorSensor) colorSensor).getNormalizedColors();
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
