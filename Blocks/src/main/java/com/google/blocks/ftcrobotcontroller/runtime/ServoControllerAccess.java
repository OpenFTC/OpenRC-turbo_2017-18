// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.google.blocks.ftcrobotcontroller.util.HardwareItem;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.hardware.ServoController.PwmStatus;
import java.util.Locale;
import junit.framework.Assert;

/**
 * A class that provides JavaScript access to a {@link ServoController}.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
class ServoControllerAccess extends HardwareAccess<ServoController> {
  private final ServoController servoController;

  ServoControllerAccess(BlocksOpMode blocksOpMode, HardwareItem hardwareItem,
      HardwareMap hardwareMap, Class<? extends HardwareDevice> deviceType) {
    super(blocksOpMode, hardwareItem, hardwareMap, ServoController.class);
    Assert.assertTrue(deviceType == ServoController.class);
    this.servoController = hardwareDevice;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public String getPwmStatus() {
    checkIfStopRequested();
    if (servoController != null) {
      PwmStatus pwmStatus = servoController.getPwmStatus();
      if (pwmStatus != null) {
        return pwmStatus.toString();
      }
    }
    return "";
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void pwmEnable() {
    checkIfStopRequested();
    if (servoController != null) {
      servoController.pwmEnable();
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void pwmDisable() {
    checkIfStopRequested();
    if (servoController != null) {
      servoController.pwmDisable();
    }
  }
}
