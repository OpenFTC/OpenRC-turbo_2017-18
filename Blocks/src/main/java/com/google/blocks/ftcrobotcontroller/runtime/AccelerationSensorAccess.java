// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.google.blocks.ftcrobotcontroller.util.HardwareItem;
import com.qualcomm.hardware.hitechnic.HiTechnicNxtAccelerationSensor;
import com.qualcomm.robotcore.hardware.AccelerationSensor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import junit.framework.Assert;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;

/**
 * A class that provides JavaScript access to a {@link AccelerationSensor}.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
class AccelerationSensorAccess extends HardwareAccess<AccelerationSensor> {
  private final AccelerationSensor accelerationSensor;

  AccelerationSensorAccess(BlocksOpMode blocksOpMode, HardwareItem hardwareItem, HardwareMap hardwareMap, Class<? extends HardwareDevice> deviceType) {
    super(blocksOpMode, hardwareItem, hardwareMap, AccelerationSensor.class);
    Assert.assertTrue(deviceType == AccelerationSensor.class);
    this.accelerationSensor = hardwareDevice;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(exclusiveToBlocks = true)
  public double getXAccel() {
    checkIfStopRequested();
    if (accelerationSensor != null) {
      Acceleration acceleration = accelerationSensor.getAcceleration();
      if (acceleration != null) {
        return acceleration.xAccel;
      }
    }
    return 0.0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(exclusiveToBlocks = true)
  public double getYAccel() {
    checkIfStopRequested();
    if (accelerationSensor != null) {
      Acceleration acceleration = accelerationSensor.getAcceleration();
      if (acceleration != null) {
        return acceleration.yAccel;
      }
    }
    return 0.0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(exclusiveToBlocks = true)
  public double getZAccel() {
    checkIfStopRequested();
    if (accelerationSensor != null) {
      Acceleration acceleration = accelerationSensor.getAcceleration();
      if (acceleration != null) {
        return acceleration.zAccel;
      }
    }
    return 0.0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtAccelerationSensor.class}, methodName = "getAcceleration")
  public Acceleration getAcceleration() {
    checkIfStopRequested();
    if (accelerationSensor != null) {
      return accelerationSensor.getAcceleration();
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtAccelerationSensor.class}, methodName = "toString")
  public String toText() {
    checkIfStopRequested();
    if (accelerationSensor != null) {
      return accelerationSensor.toString();
    }
    return "";
  }
}
