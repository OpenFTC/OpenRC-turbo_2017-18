// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.google.blocks.ftcrobotcontroller.util.HardwareItem;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.CRServoImpl;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.Locale;
import junit.framework.Assert;

/**
 * A class that provides JavaScript access to a {@link CRServo}.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
class CRServoAccess extends HardwareAccess<CRServo> {
  private final CRServo crServo;

  CRServoAccess(BlocksOpMode blocksOpMode, HardwareItem hardwareItem, HardwareMap hardwareMap, Class<? extends HardwareDevice> deviceType) {
    super(blocksOpMode, hardwareItem, hardwareMap, CRServo.class);
    Assert.assertTrue(deviceType == CRServo.class);
    this.crServo = hardwareDevice;
  }

  // From com.qualcomm.robotcore.hardware.CRServo

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {CRServoImpl.class}, methodName = "setDirection")
  public void setDirection(String directionString) {
    checkIfStopRequested();
    try {
      if (crServo != null) {
        Direction direction = Direction.valueOf(directionString.toUpperCase(Locale.ENGLISH));
        crServo.setDirection(direction);
      }
    } catch (Exception e) {
      RobotLog.e("CRServo.setDirection - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {CRServoImpl.class}, methodName = "getDirection")
  public String getDirection() {
    checkIfStopRequested();
    if (crServo != null) {
      Direction direction = crServo.getDirection();
      if (direction != null) {
        return direction.toString();
      }
    }
    return "";
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {CRServoImpl.class}, methodName = "setPower")
  public void setPower(double power) {
    checkIfStopRequested();
    if (crServo != null) {
      crServo.setPower(power);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {CRServoImpl.class}, methodName = "getPower")
  public double getPower() {
    checkIfStopRequested();
    if (crServo != null) {
      return crServo.getPower();
    }
    return 0;
  }
}
