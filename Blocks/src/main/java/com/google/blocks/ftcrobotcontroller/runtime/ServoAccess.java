// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.google.blocks.ftcrobotcontroller.util.HardwareItem;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.Servo.Direction;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.Locale;
import junit.framework.Assert;

/**
 * A class that provides JavaScript access to a {@link Servo}.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
class ServoAccess extends HardwareAccess<Servo> {
  private final Servo servo;

  ServoAccess(BlocksOpMode blocksOpMode, HardwareItem hardwareItem, HardwareMap hardwareMap,
      Class<? extends HardwareDevice> deviceType) {
    super(blocksOpMode, hardwareItem, hardwareMap, Servo.class);
    Assert.assertTrue(deviceType == Servo.class);
    this.servo = hardwareDevice;
  }

  // From com.qualcomm.robotcore.hardware.Servo

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void setDirection(String directionString) {
    checkIfStopRequested();
    try {
      if (servo != null) {
        Direction direction = Direction.valueOf(directionString.toUpperCase(Locale.ENGLISH));
        servo.setDirection(direction);
      }
    } catch (Exception e) {
      RobotLog.e("Servo.setDirection - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public String getDirection() {
    checkIfStopRequested();
    if (servo != null) {
      Direction direction = servo.getDirection();
      if (direction != null) {
        return direction.toString();
      }
    }
    return "";
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void setPosition(double position) {
    checkIfStopRequested();
    if (servo != null) {
      servo.setPosition(position);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public double getPosition() {
    checkIfStopRequested();
    if (servo != null) {
      return servo.getPosition();
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void scaleRange(double min, double max) {
    checkIfStopRequested();
    if (servo != null) {
      servo.scaleRange(min, max);
    }
  }
}
