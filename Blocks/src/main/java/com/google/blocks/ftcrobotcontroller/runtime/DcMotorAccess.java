// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.google.blocks.ftcrobotcontroller.util.HardwareItem;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.RobotLog;
import junit.framework.Assert;
import java.util.Locale;

/**
 * A class that provides JavaScript access to a {@link DcMotor}.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
class DcMotorAccess extends HardwareAccess<DcMotor> {
  private final DcMotor dcMotor;

  DcMotorAccess(BlocksOpMode blocksOpMode, HardwareItem hardwareItem, HardwareMap hardwareMap, Class<? extends HardwareDevice> deviceType) {
    super(blocksOpMode, hardwareItem, hardwareMap, DcMotor.class);
    Assert.assertTrue(deviceType == DcMotor.class);
    this.dcMotor = hardwareDevice;
  }

  // From com.qualcomm.robotcore.hardware.DcMotorSimple

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {DcMotor.class}, methodName = "setDirection")
  public void setDirection(String directionString) {
    checkIfStopRequested();
    try {
      if (dcMotor != null) {
        Direction direction = Direction.valueOf(directionString.toUpperCase(Locale.ENGLISH));
        dcMotor.setDirection(direction);
      }
    } catch (Exception e) {
      RobotLog.e("DcMotor.setDirection - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {DcMotor.class}, methodName = "getDirection")
  public String getDirection() {
    checkIfStopRequested();
    if (dcMotor != null) {
      Direction direction = dcMotor.getDirection();
      if (direction != null) {
        return direction.toString();
      }
    }
    return "";
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {DcMotor.class}, methodName = "setPower")
  public void setPower(double power) {
    checkIfStopRequested();
    if (dcMotor != null) {
      dcMotor.setPower(power);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {DcMotor.class}, methodName = "getPower")
  public double getPower() {
    checkIfStopRequested();
    if (dcMotor != null) {
      return dcMotor.getPower();
    }
    return 0;
  }

  // From com.qualcomm.robotcore.hardware.DcMotor

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Deprecated
  public void setMaxSpeed(double maxSpeed) {
    checkIfStopRequested();
    // This method does nothing. MaxSpeed is deprecated. 
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Deprecated
  public int getMaxSpeed() {
    checkIfStopRequested();
    // This method always returns 0. MaxSpeed is deprecated. 
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {DcMotor.class}, methodName = "setZeroPowerBehavior")
  public void setZeroPowerBehavior(String zeroPowerBehaviorString) {
    checkIfStopRequested();
    try {
      if (dcMotor != null) {
        ZeroPowerBehavior zeroPowerBehavior =
            ZeroPowerBehavior.valueOf(zeroPowerBehaviorString.toUpperCase(Locale.ENGLISH));
        dcMotor.setZeroPowerBehavior(zeroPowerBehavior);
      }
    } catch (Exception e) {
      RobotLog.e("DcMotor.setZeroPowerBehavior - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {DcMotor.class}, methodName = "getZeroPowerBehavior")
  public String getZeroPowerBehavior() {
    checkIfStopRequested();
    if (dcMotor != null) {
      ZeroPowerBehavior zeroPowerBehavior = dcMotor.getZeroPowerBehavior();
      if (zeroPowerBehavior != null) {
        return zeroPowerBehavior.toString();
      }
    }
    return "";
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {DcMotor.class}, methodName = "getPowerFloat")
  public boolean getPowerFloat() {
    checkIfStopRequested();
    if (dcMotor != null) {
      return dcMotor.getPowerFloat();
    }
    return false;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {DcMotor.class}, methodName = "setTargetPosition")
  public void setTargetPosition(double position) {
    checkIfStopRequested();
    if (dcMotor != null) {
      dcMotor.setTargetPosition((int) position);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {DcMotor.class}, methodName = "getTargetPosition")
  public int getTargetPosition() {
    checkIfStopRequested();
    if (dcMotor != null) {
      return dcMotor.getTargetPosition();
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {DcMotor.class}, methodName = "isBusy")
  public boolean isBusy() {
    checkIfStopRequested();
    if (dcMotor != null) {
      return dcMotor.isBusy();
    }
    return false;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {DcMotor.class}, methodName = "getCurrentPosition")
  public int getCurrentPosition() {
    checkIfStopRequested();
    if (dcMotor != null) {
      return dcMotor.getCurrentPosition();
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {DcMotor.class}, methodName = "setMode")
  public void setMode(String runModeString) {
    checkIfStopRequested();
    try {
      if (dcMotor != null) {
        RunMode runMode = RunMode.valueOf(runModeString.toUpperCase(Locale.ENGLISH));
        dcMotor.setMode(runMode);
      }
    } catch (Exception e) {
      RobotLog.e("DcMotor.setMode - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {DcMotor.class}, methodName = "getMode")
  public String getMode() {
    checkIfStopRequested();
    if (dcMotor != null) {
      RunMode runMode = dcMotor.getMode();
      if (runMode != null) {
        return runMode.toString();
      }
    }
    return "";
  }

  // Dual set property

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Deprecated
  public void setDualMaxSpeed(double maxSpeed1, Object other, double maxSpeed2) {
    checkIfStopRequested();
    // This method does nothing. MaxSpeed is deprecated. 
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {DcMotor.class}, methodName = "setMode")
  public void setDualMode(String runMode1, Object other, String runMode2) {
    checkIfStopRequested();
    if (other instanceof DcMotorAccess) {
      setMode(runMode1);
      ((DcMotorAccess) other).setMode(runMode2);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {DcMotor.class}, methodName = "setPower")
  public void setDualPower(double power1, Object other, double power2) {
    checkIfStopRequested();
    if (other instanceof DcMotorAccess) {
      setPower(power1);
      ((DcMotorAccess) other).setPower(power2);
    } else {
      RobotLog.e("DCMotor.setDualPower - parameter is not a DCMotor");
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {DcMotor.class}, methodName = "setTargetPosition")
  public void setDualTargetPosition(double position1, Object other, double position2) {
    checkIfStopRequested();
    if (other instanceof DcMotorAccess) {
      setTargetPosition(position1);
      ((DcMotorAccess) other).setTargetPosition(position2);
    } else {
      RobotLog.e("DCMotor.setDualTargetPosition - parameter is not a DCMotor");
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {DcMotor.class}, methodName = "setZeroPowerBehavior")
  public void setDualZeroPowerBehavior(String zeroPowerBehaviorString1,
      Object other, String zeroPowerBehaviorString2) {
    checkIfStopRequested();
    if (other instanceof DcMotorAccess) {
      setZeroPowerBehavior(zeroPowerBehaviorString1);
      ((DcMotorAccess) other).setZeroPowerBehavior(zeroPowerBehaviorString2);
    } else {
      RobotLog.e("DCMotor.setDualZeroPowerBehavior - parameter is not a DCMotor");
    }
  }
}
