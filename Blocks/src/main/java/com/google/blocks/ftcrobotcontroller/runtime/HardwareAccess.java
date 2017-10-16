// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller.runtime;

import com.google.blocks.ftcrobotcontroller.util.HardwareItem;
import com.google.blocks.ftcrobotcontroller.util.HardwareType;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Hardware;
import com.qualcomm.robotcore.util.RobotLog;

/**
 * An abstract class for classes that provides JavaScript access to a {@link HardwareDevice}.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
abstract class HardwareAccess<DEVICE_TYPE extends HardwareDevice> extends Access {
  protected final HardwareItem hardwareItem;
  protected final DEVICE_TYPE hardwareDevice;

  /**
   * Constructs a {@link HardwareAccess} for the given {@link HardwareItem}.
   */
  protected HardwareAccess(BlocksOpMode blocksOpMode, HardwareItem hardwareItem,
      HardwareMap hardwareMap, Class<DEVICE_TYPE> deviceType) {
    super(blocksOpMode, hardwareItem.identifier);
    this.hardwareItem = hardwareItem;

    DEVICE_TYPE hardwareDevice = null;
    try {
      hardwareDevice = hardwareMap.get(deviceType, hardwareItem.deviceName);
    } catch (Exception e) {
      RobotLog.e("HardwareAcccess - caught " + e);
    }
    this.hardwareDevice = hardwareDevice;
  }

  /**
   * Creates a new {@link HardwareAccess} for the given {@link HardwareItem}.
   */
  static HardwareAccess newHardwareAccess(BlocksOpMode blocksOpMode,
      HardwareType hardwareType, HardwareMap hardwareMap, HardwareItem hardwareItem) {

    // We pass hardwareType.deviceType into each constructor so that they can do a sanity check
    // that the deviceTypes in HardwareType match those expected in each sensor access class.
    switch (hardwareType) {
      case ACCELERATION_SENSOR:
        return new AccelerationSensorAccess(blocksOpMode, hardwareItem, hardwareMap, hardwareType.deviceType);
      case ANALOG_INPUT:
        return new AnalogInputAccess(blocksOpMode, hardwareItem, hardwareMap, hardwareType.deviceType);
      case ANALOG_OUTPUT:
        return new AnalogOutputAccess(blocksOpMode, hardwareItem, hardwareMap, hardwareType.deviceType);
      case BNO055IMU:
        return new BNO055IMUAccess(blocksOpMode, hardwareItem, hardwareMap, hardwareType.deviceType);
      case COLOR_SENSOR:
        return new ColorSensorAccess(blocksOpMode, hardwareItem, hardwareMap, hardwareType.deviceType);
      case COMPASS_SENSOR:
        return new CompassSensorAccess(blocksOpMode, hardwareItem, hardwareMap, hardwareType.deviceType);
      case CR_SERVO:
        return new CRServoAccess(blocksOpMode, hardwareItem, hardwareMap, hardwareType.deviceType);
      case DC_MOTOR:
        return new DcMotorAccess(blocksOpMode, hardwareItem, hardwareMap, hardwareType.deviceType);
      case DIGITAL_CHANNEL:
        return new DigitalChannelAccess(blocksOpMode, hardwareItem, hardwareMap, hardwareType.deviceType);
      case GYRO_SENSOR:
        return new GyroSensorAccess(blocksOpMode, hardwareItem, hardwareMap, hardwareType.deviceType);
      case IR_SEEKER_SENSOR:
        return new IrSeekerSensorAccess(blocksOpMode, hardwareItem, hardwareMap, hardwareType.deviceType);
      case LED:
        return new LedAccess(blocksOpMode, hardwareItem, hardwareMap, hardwareType.deviceType);
      case LIGHT_SENSOR:
        return new LightSensorAccess(blocksOpMode, hardwareItem, hardwareMap, hardwareType.deviceType);
      case LYNX_I2C_COLOR_RANGE_SENSOR:
        return new LynxI2cColorRangeSensorAccess(blocksOpMode, hardwareItem, hardwareMap, hardwareType.deviceType);
      case MR_I2C_COMPASS_SENSOR:
        return new MrI2cCompassSensorAccess(blocksOpMode, hardwareItem, hardwareMap, hardwareType.deviceType);
      case MR_I2C_RANGE_SENSOR:
        return new MrI2cRangeSensorAccess(blocksOpMode, hardwareItem, hardwareMap, hardwareType.deviceType);
      case OPTICAL_DISTANCE_SENSOR:
        return new OpticalDistanceSensorAccess(blocksOpMode, hardwareItem, hardwareMap, hardwareType.deviceType);
      case SERVO:
        return new ServoAccess(blocksOpMode, hardwareItem, hardwareMap, hardwareType.deviceType);
      case SERVO_CONTROLLER:
        return new ServoControllerAccess(blocksOpMode, hardwareItem, hardwareMap, hardwareType.deviceType);
      case TOUCH_SENSOR:
        return new TouchSensorAccess(blocksOpMode, hardwareItem, hardwareMap, hardwareType.deviceType);
      case ULTRASONIC_SENSOR:
        return new UltrasonicSensorAccess(blocksOpMode, hardwareItem, hardwareMap, hardwareType.deviceType);
      case VOLTAGE_SENSOR:
        return new VoltageSensorAccess(blocksOpMode, hardwareItem, hardwareMap, hardwareType.deviceType);
    }
    throw new IllegalArgumentException("Unknown hardware type " + hardwareType);
  }
}
