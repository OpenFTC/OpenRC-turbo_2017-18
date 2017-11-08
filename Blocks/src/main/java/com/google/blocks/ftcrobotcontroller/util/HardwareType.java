// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller.util;

import com.qualcomm.hardware.adafruit.AdafruitBNO055IMU;
import com.qualcomm.hardware.bosch.BNO055IMUImpl;
import com.qualcomm.hardware.lynx.LynxEmbeddedIMU;
import com.qualcomm.hardware.lynx.LynxI2cColorRangeSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cCompassSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.hardware.motors.Matrix12vMotor;
import com.qualcomm.hardware.motors.MatrixLegacyMotor;
import com.qualcomm.hardware.motors.NeveRest20Gearmotor;
import com.qualcomm.hardware.motors.NeveRest3_7GearmotorV1;
import com.qualcomm.hardware.motors.NeveRest40Gearmotor;
import com.qualcomm.hardware.motors.NeveRest60Gearmotor;
import com.qualcomm.hardware.motors.RevRoboticsCoreHexMotor;
import com.qualcomm.hardware.motors.RevRoboticsHdHexMotor;
import com.qualcomm.hardware.motors.TetrixMotor;
import com.qualcomm.robotcore.hardware.AccelerationSensor;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.AnalogOutput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.CompassSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;
import com.qualcomm.robotcore.hardware.LED;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.hardware.configuration.BuiltInConfigurationType;
import com.qualcomm.robotcore.hardware.configuration.I2cSensor;
import com.qualcomm.robotcore.hardware.configuration.MotorType;
import com.qualcomm.robotcore.hardware.configuration.UnspecifiedMotor;
import com.qualcomm.robotcore.hardware.configuration.UserConfigurationTypeManager;

/**
 * An enum to represent a specific type of hardware.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
public enum HardwareType {
  ACCELERATION_SENSOR( // See acceleration_sensor.js
      "createAccelerationSensorDropdown", "accelerationSensor", "",
      ToolboxFolder.SENSORS, "AccelerationSensor", ToolboxIcon.ACCELERATION_SENSOR,
      AccelerationSensor.class,
      BuiltInConfigurationType.ACCELEROMETER.getXmlTag()),
  ANALOG_INPUT( // See analog_input.js
      "createAnalogInputDropdown", "analogInput", "AsAnalogInput",
      ToolboxFolder.OTHER, "AnalogInput", ToolboxIcon.ANALOG_INPUT,
      AnalogInput.class,
      BuiltInConfigurationType.ANALOG_INPUT.getXmlTag()),
  ANALOG_OUTPUT( // See analog_output.js
      "createAnalogOutputDropdown", "analogOutput", "AsAnalogOutput",
      ToolboxFolder.OTHER, "AnalogOutput", ToolboxIcon.ANALOG_OUTPUT,
      AnalogOutput.class,
      BuiltInConfigurationType.ANALOG_OUTPUT.getXmlTag()),
  BNO055IMU( // see bno055imu.js
      "createBNO055IMUDropdown", "bno055imu", "AsBNO055IMU",
      ToolboxFolder.SENSORS, "IMU-BNO055", null, // No toolbox icon yet.
      BNO055IMUImpl.class,
      UserConfigurationTypeManager.getXmlTag(AdafruitBNO055IMU.class.getAnnotation(I2cSensor.class)),
      UserConfigurationTypeManager.getXmlTag(LynxEmbeddedIMU.class.getAnnotation(I2cSensor.class))),
  COLOR_SENSOR( // see color_sensor.js
      "createColorSensorDropdown", "colorSensor", "",
      ToolboxFolder.SENSORS, "ColorSensor", ToolboxIcon.COLOR_SENSOR,
      ColorSensor.class,
      BuiltInConfigurationType.COLOR_SENSOR.getXmlTag(),
      BuiltInConfigurationType.ADAFRUIT_COLOR_SENSOR.getXmlTag()),
  COMPASS_SENSOR( // see compass_sensor.js
      "createCompassSensorDropdown", "compassSensor", "",
      ToolboxFolder.SENSORS, "CompassSensor", ToolboxIcon.COMPASS_SENSOR,
      CompassSensor.class,
      BuiltInConfigurationType.COMPASS.getXmlTag()),
  CR_SERVO( // see cr_servo.js
      "createCRServoDropdown", "crServo", "",
      ToolboxFolder.ACTUATORS, "CRServo", ToolboxIcon.CR_SERVO,
      CRServo.class,
      BuiltInConfigurationType.CONTINUOUS_ROTATION_SERVO.getXmlTag()),
  DC_MOTOR( // see dc_motor.js
      "createDcMotorDropdown", "dcMotor", "",
      ToolboxFolder.ACTUATORS, "DcMotor", ToolboxIcon.DC_MOTOR,
      DcMotor.class,
      UserConfigurationTypeManager.getXmlTag(Matrix12vMotor.class.getAnnotation(MotorType.class)),
      UserConfigurationTypeManager.getXmlTag(MatrixLegacyMotor.class.getAnnotation(MotorType.class)),
      UserConfigurationTypeManager.getXmlTag(NeveRest20Gearmotor.class.getAnnotation(MotorType.class)),
      UserConfigurationTypeManager.getXmlTag(NeveRest3_7GearmotorV1.class.getAnnotation(MotorType.class)),
      UserConfigurationTypeManager.getXmlTag(NeveRest40Gearmotor.class.getAnnotation(MotorType.class)),
      UserConfigurationTypeManager.getXmlTag(NeveRest60Gearmotor.class.getAnnotation(MotorType.class)),
      UserConfigurationTypeManager.getXmlTag(RevRoboticsCoreHexMotor.class.getAnnotation(MotorType.class)),
      UserConfigurationTypeManager.getXmlTag(RevRoboticsHdHexMotor.class.getAnnotation(MotorType.class)),
      UserConfigurationTypeManager.getXmlTag(TetrixMotor.class.getAnnotation(MotorType.class)),
      UserConfigurationTypeManager.getXmlTag(UnspecifiedMotor.class.getAnnotation(MotorType.class))),
  DIGITAL_CHANNEL( // see digital_channel.js
      "createDigitalChannelDropdown", "digitalChannel", "AsDigitalChannel",
      ToolboxFolder.OTHER, "DigitalChannel", ToolboxIcon.DIGITAL_CHANNEL,
      DigitalChannel.class,
      BuiltInConfigurationType.DIGITAL_DEVICE.getXmlTag()),
  GYRO_SENSOR( // see gyro_sensor.js
      "createGyroSensorDropdown", "gyroSensor", "",
      ToolboxFolder.SENSORS, "GyroSensor", ToolboxIcon.GYRO_SENSOR,
      GyroSensor.class,
      BuiltInConfigurationType.GYRO.getXmlTag()),
  IR_SEEKER_SENSOR( // see ir_seeker_sensor.js
      "createIrSeekerSensorDropdown", "irSeekerSensor", "",
      ToolboxFolder.SENSORS, "IrSeekerSensor", ToolboxIcon.IR_SEEKER_SENSOR,
      IrSeekerSensor.class,
      BuiltInConfigurationType.IR_SEEKER.getXmlTag(),
      BuiltInConfigurationType.IR_SEEKER_V3.getXmlTag()),
  LED( // see led.js
      "createLedDropdown", "led", "",
      ToolboxFolder.OTHER, "LED", ToolboxIcon.LED,
      LED.class,
      BuiltInConfigurationType.LED.getXmlTag()),
  LIGHT_SENSOR( // see light_sensor.js
      "createLightSensorDropdown", "lightSensor", "",
      ToolboxFolder.SENSORS, "LightSensor", ToolboxIcon.LIGHT_SENSOR,
      LightSensor.class,
      BuiltInConfigurationType.LIGHT_SENSOR.getXmlTag()),
  LYNX_I2C_COLOR_RANGE_SENSOR( // see lynx_i2c_color_range_sensor.js
      "createLynxI2cColorRangeSensorDropdown", "lynxI2cColorRangeSensor", "asLynxI2cColorRangeSensor",
      ToolboxFolder.SENSORS, "REV Color/Range Sensor", ToolboxIcon.COLOR_SENSOR,
      LynxI2cColorRangeSensor.class,
      BuiltInConfigurationType.LYNX_COLOR_SENSOR.getXmlTag()),
  MR_I2C_COMPASS_SENSOR( // see mr_i2c_compass_sensor.js
      "createMrI2cCompassSensorDropdown", "mrI2cCompassSensor", "AsMrI2cCompassSensor",
      ToolboxFolder.SENSORS, "MrI2cCompassSensor", ToolboxIcon.COMPASS_SENSOR,
      ModernRoboticsI2cCompassSensor.class,
      UserConfigurationTypeManager.getXmlTag(ModernRoboticsI2cCompassSensor.class.getAnnotation(I2cSensor.class))),
  MR_I2C_RANGE_SENSOR( // see mr_i2c_range_sensor.js
      "createMrI2cRangeSensorDropdown", "mrI2cRangeSensor", "AsMrI2cRangeSensor",
      ToolboxFolder.SENSORS, "MrI2cRangeSensor", ToolboxIcon.OPTICAL_DISTANCE_SENSOR,
      ModernRoboticsI2cRangeSensor.class,
      UserConfigurationTypeManager.getXmlTag(ModernRoboticsI2cRangeSensor.class.getAnnotation(I2cSensor.class))),
  OPTICAL_DISTANCE_SENSOR( // see optical_distance_sensor.js
      "createOpticalDistanceSensorDropdown", "opticalDistanceSensor", "AsOpticalDistanceSensor",
      ToolboxFolder.SENSORS, "OpticalDistanceSensor", ToolboxIcon.OPTICAL_DISTANCE_SENSOR,
      OpticalDistanceSensor.class,
      BuiltInConfigurationType.OPTICAL_DISTANCE_SENSOR.getXmlTag()),
  SERVO( // see servo.js
      "createServoDropdown", "servo", "",
      ToolboxFolder.ACTUATORS, "Servo", ToolboxIcon.SERVO,
      Servo.class,
      BuiltInConfigurationType.SERVO.getXmlTag()),
  SERVO_CONTROLLER( // see servo_controller.js
      "createServoControllerDropdown", "servoController", "AsServoController",
      ToolboxFolder.ACTUATORS, "ServoController", ToolboxIcon.SERVO_CONTROLLER,
      ServoController.class,
      BuiltInConfigurationType.SERVO_CONTROLLER.getXmlTag(),
      BuiltInConfigurationType.MATRIX_CONTROLLER.getXmlTag()),
  TOUCH_SENSOR( // see touch_sensor.js
      "createTouchSensorDropdown", "touchSensor", "",
      ToolboxFolder.SENSORS, "TouchSensor", ToolboxIcon.TOUCH_SENSOR,
      TouchSensor.class,
      BuiltInConfigurationType.TOUCH_SENSOR.getXmlTag(),
      BuiltInConfigurationType.MR_ANALOG_TOUCH_SENSOR.getXmlTag()),
  ULTRASONIC_SENSOR( // see ultrasonic_sensor.js
      "createUltrasonicSensorDropdown", "ultrasonicSensor", "",
      ToolboxFolder.SENSORS, "UltrasonicSensor", ToolboxIcon.ULTRASONIC_SENSOR,
      UltrasonicSensor.class,
      BuiltInConfigurationType.ULTRASONIC_SENSOR.getXmlTag()),
  VOLTAGE_SENSOR( // see voltage_sensor.js
      "createVoltageSensorDropdown", "voltageSensor", "AsVoltageSensor",
      ToolboxFolder.SENSORS, "VoltageSensor", ToolboxIcon.VOLTAGE_SENSOR,
      VoltageSensor.class,
      BuiltInConfigurationType.MOTOR_CONTROLLER.getXmlTag(),
      BuiltInConfigurationType.LYNX_MODULE.getXmlTag());

  /**
   * The xmlTags corresponding to this HardwareType.
   */
  public final String[] xmlTags;

  /**
   * The name of the javascript function which creates a block dropdown showing the names of all
   * hardware items of this HardwareType. The javascript code is produced dynamically in
   * {@link HardwareUtil#fetchJavaScriptForHardware}. This must match the function name used in the
   * appropriate js file.
   */
  public final String createDropdownFunctionName;

  /**
   * The prefix of all block types associated with this HardwareType. The toolbox xml is produced
   * dynamically in {@link ToolboxUtil}. This must match the prefix used in the appropriate js file.
   */
  public final String blockTypePrefix;
  /**
   * The suffix of all identifiers for devices of this HardwareType.
   */
  public final String identifierSuffix;
  /**
   * The toolbox folder that will contain the toolbox category associated with this HardwareType.
   */
  public final ToolboxFolder toolboxFolder;
  /**
   * The name of the toolbox category associated with this HardwareType.
   */
  public final String toolboxCategoryName;
  /**
   * The toolbox icon enum associated with this HardwareType.
   */
  public final ToolboxIcon toolboxIcon;
  /**
   * The common type shared by all instances of this HardwareType.
   */
  public final Class<? extends HardwareDevice> deviceType;

  HardwareType(
      String createDropdownFunctionName, String blockTypePrefix, String identifierSuffix,
      ToolboxFolder toolboxFolder, String toolboxCategoryName, ToolboxIcon toolboxIcon,
      Class<? extends HardwareDevice> deviceType,
      String... xmlTags) {
    this.createDropdownFunctionName = createDropdownFunctionName;
    this.blockTypePrefix = blockTypePrefix;
    this.identifierSuffix = identifierSuffix;
    this.toolboxFolder = toolboxFolder;
    this.toolboxCategoryName = toolboxCategoryName;
    this.toolboxIcon = toolboxIcon;
    this.deviceType = deviceType;
    this.xmlTags = xmlTags;
  }
}
