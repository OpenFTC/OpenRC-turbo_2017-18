/* Copyright (c) 2014, 2015 Qualcomm Technologies Inc

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package com.qualcomm.robotcore.hardware.configuration;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class ReadXMLFileHandler extends ConfigurationUtility
  {

  public static final String TAG = "ReadXMLFileHandler";

  private static boolean DEBUG = false;
  List<ControllerConfiguration> deviceControllers;

  private XmlPullParser parser;

  public ReadXMLFileHandler() {
    deviceControllers = new ArrayList<ControllerConfiguration>();
  }

  public List<ControllerConfiguration> getDeviceControllers() {
    return deviceControllers;
  }

  public static XmlPullParser xmlPullParserFromReader(Reader reader) {
    XmlPullParserFactory factory;
    XmlPullParser parser = null;
    try {
      factory = XmlPullParserFactory.newInstance();
      factory.setNamespaceAware(true);
      parser = factory.newPullParser();
      parser.setInput(reader);
    } catch (XmlPullParserException e) {
      e.printStackTrace();
    }
    return parser;
  }

  public List<ControllerConfiguration> parse(Reader reader) throws RobotCoreException {
    parser = xmlPullParserFromReader(reader);
    return performParse();
  }

  public List<ControllerConfiguration> parse(XmlPullParser parser) throws RobotCoreException {
    this.parser = parser;
    return performParse();
  }

  private List<ControllerConfiguration> performParse() throws RobotCoreException {
    try {
      int eventType = parser.getEventType();
      while (eventType != XmlPullParser.END_DOCUMENT) {
        ConfigurationType configurationType = deform(parser.getName());
        if (eventType == XmlPullParser.START_TAG){
          if (configurationType == BuiltInConfigurationType.MOTOR_CONTROLLER) {
            deviceControllers.add(handleMotorController(true));
          }
          else if (configurationType == BuiltInConfigurationType.SERVO_CONTROLLER) {
            deviceControllers.add(handleServoController(true));
          }
          else if (configurationType == BuiltInConfigurationType.LEGACY_MODULE_CONTROLLER) {
            deviceControllers.add(handleLegacyModule());
          }
          else if (configurationType == BuiltInConfigurationType.DEVICE_INTERFACE_MODULE) {
            deviceControllers.add(handleDeviceInterfaceModule());
          }
          else if (configurationType == BuiltInConfigurationType.LYNX_USB_DEVICE) {
            deviceControllers.add(handleLynxUsbDevice());
          }
        }
        eventType = parser.next();
      } // hit end of document

    } catch (XmlPullParserException e) {
      RobotLog.w("XmlPullParserException");
      e.printStackTrace();
    } catch (IOException e) {
      RobotLog.w("IOException");
      e.printStackTrace();
    }

    checkForAbsentEmbeddedLynx();

    return deviceControllers;
  }

  /** If we are a device with an integrated / embedded lynx device, make *sure* that the serially
   * connected device and the primary module are in the map */
  protected void checkForAbsentEmbeddedLynx() {

    if (LynxConstants.isRevControlHub()) {
      for (ControllerConfiguration controllerConfiguration : deviceControllers) {
        if (LynxConstants.isEmbeddedSerialNumber(controllerConfiguration.getSerialNumber())) {
          // Ok, the 'usb' device is there, so we'll *assume* that the primary lynx module
          // is there too, as there's no reasonable way to remove same
          RobotLog.vv(TAG, "serially-connected USB device is already present");
          return;
        }
      }
      // A serially-connected module is absent. Make a new one.
      ControllerConfiguration controllerConfiguration = buildNewEmbeddedLynxUsbDevice();
      deviceControllers.add(controllerConfiguration);
    }
  }

  public List<ControllerConfiguration> parse(InputStream is) throws RobotCoreException {
    XmlPullParserFactory factory;
    parser = null;
    try {
      factory = XmlPullParserFactory.newInstance();
      factory.setNamespaceAware(true);
      parser = factory.newPullParser();
      parser.setInput(is, null);
    } catch (XmlPullParserException e) {
      e.printStackTrace();
    }
    return performParse();
  }

  private ControllerConfiguration handleDeviceInterfaceModule() throws IOException, XmlPullParserException, RobotCoreException {
    String name = parser.getAttributeValue(null, DeviceConfiguration.XMLATTR_NAME);
    String serialNumber = parser.getAttributeValue(null, ControllerConfiguration.XMLATTR_SERIAL_NUMBER);

    // add empty device configuration objects to the lists.
    List<DeviceConfiguration> pwmDevices          = buildEmptyDevices(0, ModernRoboticsConstants.NUMBER_OF_PWM_CHANNELS,   BuiltInConfigurationType.PULSE_WIDTH_DEVICE);
    List<DeviceConfiguration> i2cDevices          = buildEmptyDevices(0, ModernRoboticsConstants.NUMBER_OF_I2C_CHANNELS,   BuiltInConfigurationType.I2C_DEVICE);
    List<DeviceConfiguration> analogInputDevices  = buildEmptyDevices(0, ModernRoboticsConstants.NUMBER_OF_ANALOG_INPUTS,  BuiltInConfigurationType.ANALOG_INPUT);
    List<DeviceConfiguration> digitalDevices      = buildEmptyDevices(0, ModernRoboticsConstants.NUMBER_OF_DIGITAL_IOS,    BuiltInConfigurationType.DIGITAL_DEVICE);
    List<DeviceConfiguration> analogOutputDevices = buildEmptyDevices(0, ModernRoboticsConstants.NUMBER_OF_ANALOG_OUTPUTS, BuiltInConfigurationType.ANALOG_OUTPUT);

    int eventType = parser.next();
    ConfigurationType configurationType = deform(parser.getName());

    while (eventType != XmlPullParser.END_DOCUMENT) { // we shouldn't reach the end of the document here anyway...
      if (eventType == XmlPullParser.END_TAG) {
        if (configurationType == null) {
          // just an empty <DEVICE> </> closing tag
          continue;
        }
        if (DEBUG) RobotLog.e("[handleDeviceInterfaceModule] tagname: " + configurationType);
        if (configurationType == BuiltInConfigurationType.DEVICE_INTERFACE_MODULE) {
          // end of loop...
          noteExistingName(BuiltInConfigurationType.DEVICE_INTERFACE_MODULE, name);
          DeviceInterfaceModuleConfiguration deviceInterfaceModuleConfiguration = new DeviceInterfaceModuleConfiguration(name, new SerialNumber(serialNumber));
          deviceInterfaceModuleConfiguration.setPwmOutputs(pwmDevices);
          deviceInterfaceModuleConfiguration.setI2cDevices(i2cDevices);
          deviceInterfaceModuleConfiguration.setAnalogInputDevices(analogInputDevices);
          deviceInterfaceModuleConfiguration.setDigitalDevices(digitalDevices);
          deviceInterfaceModuleConfiguration.setAnalogOutputDevices(analogOutputDevices);
          deviceInterfaceModuleConfiguration.setEnabled(true);
          return deviceInterfaceModuleConfiguration;
        }
      }
      if (eventType == XmlPullParser.START_TAG) {
        if (configurationType == BuiltInConfigurationType.ANALOG_INPUT ||
            configurationType == BuiltInConfigurationType.MR_ANALOG_TOUCH_SENSOR ||
            configurationType == BuiltInConfigurationType.OPTICAL_DISTANCE_SENSOR) {
          DeviceConfiguration dev = handleDevice();
          analogInputDevices.set(dev.getPort(), dev);
        }
        if (configurationType == BuiltInConfigurationType.PULSE_WIDTH_DEVICE) {
          DeviceConfiguration dev = handleDevice();
          pwmDevices.set(dev.getPort(), dev);
        }
        if (configurationType.isDeviceFlavor(UserConfigurationType.Flavor.I2C)) {
          DeviceConfiguration dev = handleDevice();
          i2cDevices.set(dev.getPort(), dev);
        }
        if (configurationType == BuiltInConfigurationType.ANALOG_OUTPUT  ){
          DeviceConfiguration dev = handleDevice();
          analogOutputDevices.set(dev.getPort(), dev);
        }
        if (configurationType == BuiltInConfigurationType.DIGITAL_DEVICE  ||
            configurationType == BuiltInConfigurationType.TOUCH_SENSOR  ||
            configurationType == BuiltInConfigurationType.LED) {
          DeviceConfiguration dev = handleDevice();
          digitalDevices.set(dev.getPort(), dev);
        }
      }
      eventType = parser.next();
      configurationType = deform(parser.getName());
    }
    // we should never reach the end of the document while parsing this part. If we do, it's an error.
    RobotLog.logAndThrow("Reached the end of the XML file while parsing.");
    return null;
  }

  private ControllerConfiguration handleLegacyModule() throws IOException, XmlPullParserException, RobotCoreException {
    String name = parser.getAttributeValue(null, DeviceConfiguration.XMLATTR_NAME);
    String serialNumber = parser.getAttributeValue(null, ControllerConfiguration.XMLATTR_SERIAL_NUMBER);

    List<DeviceConfiguration> modules = buildEmptyDevices(0, ModernRoboticsConstants.NUMBER_OF_LEGACY_MODULE_PORTS, BuiltInConfigurationType.NOTHING);

    int eventType = parser.next();
    ConfigurationType configurationType = deform(parser.getName());

    while(eventType != XmlPullParser.END_DOCUMENT) { // we shouldn't reach the end of the document here anyway...
      if (eventType == XmlPullParser.END_TAG) {
        if (configurationType == null) {
          // just an empty <DEVICE> </> closing tag
          continue;
        }
        if (configurationType == BuiltInConfigurationType.LEGACY_MODULE_CONTROLLER) {
          // end of loop...
          noteExistingName(BuiltInConfigurationType.LEGACY_MODULE_CONTROLLER, name);
          LegacyModuleControllerConfiguration legacyModule = new LegacyModuleControllerConfiguration(name, modules, new SerialNumber(serialNumber));
          legacyModule.setEnabled(true);
          return legacyModule;
        }
      }
      if (eventType == XmlPullParser.START_TAG) {
        if (DEBUG) RobotLog.e("[handleLegacyModule] tagname: " + configurationType);
        if (configurationType == BuiltInConfigurationType.COMPASS ||
            configurationType == BuiltInConfigurationType.LIGHT_SENSOR ||
            configurationType == BuiltInConfigurationType.IR_SEEKER ||
            configurationType == BuiltInConfigurationType.ACCELEROMETER ||
            configurationType == BuiltInConfigurationType.GYRO ||
            configurationType == BuiltInConfigurationType.TOUCH_SENSOR ||
            configurationType == BuiltInConfigurationType.TOUCH_SENSOR_MULTIPLEXER ||
            configurationType == BuiltInConfigurationType.ULTRASONIC_SENSOR ||
            configurationType == BuiltInConfigurationType.COLOR_SENSOR ||
            configurationType == BuiltInConfigurationType.NOTHING) {
          DeviceConfiguration dev = handleDevice();
          modules.set(dev.getPort(), dev);
        }
        else if (configurationType == BuiltInConfigurationType.MOTOR_CONTROLLER) {
          ControllerConfiguration mc = handleMotorController(false);
          modules.set(mc.getPort(), mc);
        }
        else if (configurationType == BuiltInConfigurationType.SERVO_CONTROLLER) {
          ControllerConfiguration sc = handleServoController(false);
          modules.set(sc.getPort(), sc);
        }
        else if (configurationType == BuiltInConfigurationType.MATRIX_CONTROLLER) {
          ControllerConfiguration mc = handleMatrixController();
          modules.set(mc.getPort(), mc);
        }
      }
      eventType = parser.next();
      configurationType = deform(parser.getName());
    }

    return new LegacyModuleControllerConfiguration(name, modules, new SerialNumber(serialNumber));
  }

  private DeviceConfiguration handleDevice(DeviceConfiguration device) {
    ConfigurationType configurationType = deform(parser.getName());
    device.setConfigurationType(configurationType);
    device.deserializeAttributes(parser);
    // We only write *enabled* devices, so if we *read* one, it's enabled
    device.setEnabled(true);
    noteExistingName(configurationType, device.getName());
    return device;
  }

  private DeviceConfiguration handleDevice() {
    return handleDevice(new DeviceConfiguration());
    }

  private LynxI2cDeviceConfiguration handleLynxI2cDevice() {
    return (LynxI2cDeviceConfiguration)handleDevice(new LynxI2cDeviceConfiguration());
  }

  private LynxModuleConfiguration handleLynxModule(int parentModuleAddress)  throws IOException, XmlPullParserException, RobotCoreException {
    String name = parser.getAttributeValue(null, DeviceConfiguration.XMLATTR_NAME);
    int moduleAddress = Integer.parseInt(parser.getAttributeValue(null, DeviceConfiguration.XMLATTR_PORT));

    RobotLog.vv(TAG, "handleLynxModule() mod#=%d...", moduleAddress);
    LynxModuleConfiguration moduleConfiguration = buildEmptyLynxModule(name, moduleAddress, moduleAddress == parentModuleAddress, true);

    List<ServoConfiguration>  servos       = new ArrayList<ServoConfiguration>(moduleConfiguration.getServos());
    List<MotorConfiguration>  motors       = new ArrayList<MotorConfiguration>(moduleConfiguration.getMotors());
    List<DeviceConfiguration> pwmOutputs   = new ArrayList<DeviceConfiguration>(moduleConfiguration.getPwmOutputs());
    List<DeviceConfiguration> analogInputs = new ArrayList<DeviceConfiguration>(moduleConfiguration.getAnalogInputs());
    List<DeviceConfiguration> digitalIOs   = new ArrayList<DeviceConfiguration>(moduleConfiguration.getDigitalDevices());
    List<LynxI2cDeviceConfiguration> i2cDevices = new ArrayList<LynxI2cDeviceConfiguration>(moduleConfiguration.getI2cDevices());

    int eventType = parser.next();
    ConfigurationType configurationType = deform(parser.getName());

    while(eventType != XmlPullParser.END_DOCUMENT) { // we shouldn't reach the end of the document here anyway...
      if (eventType == XmlPullParser.END_TAG) {
        if (configurationType == null) {
          // just an empty closing tag
          continue;
        }
        if (configurationType == BuiltInConfigurationType.LYNX_MODULE) {
          // end of loop...
          moduleConfiguration.setMotors(motors);
          moduleConfiguration.setServos(servos);
          moduleConfiguration.setPwmOutputs(pwmOutputs);
          moduleConfiguration.setAnalogInputs(analogInputs);
          moduleConfiguration.setDigitalDevices(digitalIOs);
          moduleConfiguration.setI2cDevices(i2cDevices);
          //
          RobotLog.vv(TAG, "...handleLynxModule() mod#=%d", moduleAddress);
          return moduleConfiguration;
        }
      }
      if (eventType == XmlPullParser.START_TAG) {

          if (configurationType==BuiltInConfigurationType.CONTINUOUS_ROTATION_SERVO ||
              configurationType==BuiltInConfigurationType.SERVO) {
            ServoConfiguration servo = handleServo(configurationType);
            servos.set(servo.getPort()-LynxConstants.INITIAL_SERVO_PORT, servo);

          } else if (configurationType.isDeviceFlavor(UserConfigurationType.Flavor.MOTOR)) {
            MotorConfiguration motor = handleMotor();
            motors.set(motor.getPort()-LynxConstants.INITIAL_MOTOR_PORT, motor);

          } else if (configurationType==BuiltInConfigurationType.PULSE_WIDTH_DEVICE) {
            DeviceConfiguration pwm = handleDevice();
            pwmOutputs.set(pwm.getPort()-0, pwm);

          } else if (configurationType==BuiltInConfigurationType.ANALOG_INPUT ||
              configurationType == BuiltInConfigurationType.MR_ANALOG_TOUCH_SENSOR ||
              configurationType == BuiltInConfigurationType.OPTICAL_DISTANCE_SENSOR) {
            DeviceConfiguration analogInput = handleDevice();
            analogInputs.set(analogInput.getPort()-0, analogInput);

          } else if (configurationType==BuiltInConfigurationType.DIGITAL_DEVICE ||
              configurationType == BuiltInConfigurationType.TOUCH_SENSOR  ||
              configurationType == BuiltInConfigurationType.LED) {
            DeviceConfiguration digitalIO = handleDevice();
            digitalIOs.set(digitalIO.getPort()-0, digitalIO);

          } else if (configurationType.isDeviceFlavor(UserConfigurationType.Flavor.I2C)) {
            LynxI2cDeviceConfiguration i2cDevice = handleLynxI2cDevice();
            i2cDevices.add(i2cDevice);

          } else {
            // Nothing to do
          }
      }
      eventType = parser.next();
      configurationType = deform(parser.getName());
    }
    // we should never reach the end of the document while parsing this part. If we do, it's an error.
    RobotLog.logAndThrow("Reached the end of the XML file while parsing.");
    return null;
  }

  private MotorConfiguration handleMotor() {
    ConfigurationType configurationType = deform(parser.getName());
    int port = Integer.parseInt(parser.getAttributeValue(null, DeviceConfiguration.XMLATTR_PORT));
    String motorName = parser.getAttributeValue(null, DeviceConfiguration.XMLATTR_NAME);
    MotorConfiguration motor = new MotorConfiguration(port, motorName, true);
    motor.setConfigurationType(configurationType);
    noteExistingName(configurationType, motorName);
    return motor;
  }

  private ServoConfiguration handleServo(ConfigurationType type) {
    int port = Integer.parseInt(parser.getAttributeValue(null, DeviceConfiguration.XMLATTR_PORT));
    String servoName = parser.getAttributeValue(null, DeviceConfiguration.XMLATTR_NAME);
    ServoConfiguration servo = new ServoConfiguration(port, type, servoName, true);
    noteExistingName(type, servoName);
    return servo;
  }

  private ControllerConfiguration handleMatrixController() throws IOException, XmlPullParserException, RobotCoreException {
    String name = parser.getAttributeValue(null, DeviceConfiguration.XMLATTR_NAME);

    // USB devices have serial numbers instead of ports.
    String serialNumber = new SerialNumber().toString();
    int controllerPort = Integer.parseInt(parser.getAttributeValue(null, DeviceConfiguration.XMLATTR_PORT));

    List<ServoConfiguration> servos = buildEmptyServos(MatrixConstants.INITIAL_SERVO_PORT, MatrixConstants.NUMBER_OF_SERVOS, BuiltInConfigurationType.SERVO);
    List<MotorConfiguration> motors = buildEmptyMotors(MatrixConstants.INITIAL_MOTOR_PORT, MatrixConstants.NUMBER_OF_MOTORS, MotorConfigurationType.getUnspecifiedMotorType());

    int eventType = parser.next();
    ConfigurationType configurationType = deform(parser.getName());

    while(eventType != XmlPullParser.END_DOCUMENT) { // we shouldn't reach the end of the document here anyway...
      if (eventType == XmlPullParser.END_TAG) {
        if (configurationType == null) {
          // just an empty <SERVO> </> closing tag
          continue;
        }
        if (configurationType == BuiltInConfigurationType.MATRIX_CONTROLLER) {
          // end of loop...
          noteExistingName(BuiltInConfigurationType.MATRIX_CONTROLLER, name);
          MatrixControllerConfiguration newController = new MatrixControllerConfiguration(name, motors, servos, new SerialNumber(serialNumber));
          newController.setPort(controllerPort);
          newController.setEnabled(true);
          return newController;
        }
      }
      if (eventType == XmlPullParser.START_TAG) {
        if (configurationType == BuiltInConfigurationType.SERVO || configurationType == BuiltInConfigurationType.CONTINUOUS_ROTATION_SERVO) {
          ServoConfiguration servo = handleServo(configurationType);
          // ModernRobotics HW is indexed by 1, but internally this code indexes by 0.
          servos.set(servo.getPort()-1, servo);
        }
        else if (configurationType.isDeviceFlavor(UserConfigurationType.Flavor.MOTOR)) {
          MotorConfiguration motor = handleMotor();
          // ModernRobotics HW is indexed by 1, but internally this code indexes by 0.
          motors.set(motor.getPort()-1, motor);
        }
      }
      eventType = parser.next();
      configurationType = deform(parser.getName());
    }
    // we should never reach the end of the document while parsing this part. If we do, it's an error.
    RobotLog.logAndThrow("Reached the end of the XML file while parsing.");
    return null;
  }

  private ControllerConfiguration handleServoController(boolean isUsbDevice) throws IOException, XmlPullParserException {
    String name = parser.getAttributeValue(null, DeviceConfiguration.XMLATTR_NAME);

    // USB devices have serial numbers instead of ports.
    int controllerPort = -1;
    String serialNumber = new SerialNumber().toString();
    if (isUsbDevice) {
      serialNumber = parser.getAttributeValue(null, ControllerConfiguration.XMLATTR_SERIAL_NUMBER);
    } else {
      controllerPort = Integer.parseInt(parser.getAttributeValue(null, DeviceConfiguration.XMLATTR_PORT));
    }

    List<ServoConfiguration> servos = buildEmptyServos(ModernRoboticsConstants.INITIAL_SERVO_PORT, ModernRoboticsConstants.NUMBER_OF_SERVOS);

    int eventType = parser.next();
    ConfigurationType configurationType = deform(parser.getName());

    while(eventType != XmlPullParser.END_DOCUMENT) { // we shouldn't reach the end of the document here anyway...
      if (eventType == XmlPullParser.END_TAG) {
        if (configurationType == null) {
          // just an empty <SERVO> </> closing tag
          continue;
        }
        if (configurationType == BuiltInConfigurationType.SERVO_CONTROLLER) {
          // end of loop...
          noteExistingName(BuiltInConfigurationType.SERVO_CONTROLLER, name);
          ServoControllerConfiguration newController = new ServoControllerConfiguration(name, servos, new SerialNumber(serialNumber));
          newController.setPort(controllerPort);
          newController.setEnabled(true);
          return newController;
        }
      }
      if (eventType == XmlPullParser.START_TAG) {
        if (configurationType == BuiltInConfigurationType.SERVO || configurationType == BuiltInConfigurationType.CONTINUOUS_ROTATION_SERVO) {
          ServoConfiguration servo = handleServo(configurationType);
          // ModernRobotics HW is indexed by 1, but internally this code indexes by 0.
          servos.set(servo.getPort()-1, servo);
        }
      }
      eventType = parser.next();
      configurationType = deform(parser.getName());
    }
    ServoControllerConfiguration newController = new ServoControllerConfiguration(name, servos, new SerialNumber(serialNumber));
    newController.setPort(controllerPort);
    return newController;
  }

  private ControllerConfiguration handleLynxUsbDevice() throws IOException, XmlPullParserException, RobotCoreException  {
    String name = parser.getAttributeValue(null, DeviceConfiguration.XMLATTR_NAME);
    String serialNumber = parser.getAttributeValue(null, ControllerConfiguration.XMLATTR_SERIAL_NUMBER);
    String parentModuleAddrAttr = parser.getAttributeValue(null, LynxUsbDeviceConfiguration.XMLATTR_PARENT_MODULE_ADDRESS);
    int parentModuleAddress = Integer.parseInt(parentModuleAddrAttr==null ? Integer.toString(LynxUsbDeviceConfiguration.DEFAULT_PARENT_MODULE_ADDRESS) : parentModuleAddrAttr);
    List<LynxModuleConfiguration> modules = new LinkedList<LynxModuleConfiguration>();

    int eventType = parser.next();
    ConfigurationType configurationType = deform(parser.getName());

    while(eventType != XmlPullParser.END_DOCUMENT) { // we shouldn't reach the end of the document here anyway...
      if (eventType == XmlPullParser.END_TAG) {
        if (configurationType == null) {
          // just an empty closing tag
          continue;
        }
        if (configurationType == BuiltInConfigurationType.LYNX_USB_DEVICE) {
          // end of loop...
          noteExistingName(BuiltInConfigurationType.LYNX_USB_DEVICE, name);
          LynxUsbDeviceConfiguration newController = new LynxUsbDeviceConfiguration(name, modules, new SerialNumber(serialNumber));
          newController.setEnabled(true);
          return newController;
        }
      }
      if (eventType == XmlPullParser.START_TAG) {
        if (configurationType == BuiltInConfigurationType.LYNX_MODULE) {
          LynxModuleConfiguration moduleConfiguration = handleLynxModule(parentModuleAddress);
          modules.add(moduleConfiguration);
        }
      }
      eventType = parser.next();
      configurationType = deform(parser.getName());
    }

    LynxUsbDeviceConfiguration newController = new LynxUsbDeviceConfiguration(name, modules, new SerialNumber(serialNumber));
    return newController;
  }

  private ControllerConfiguration handleMotorController(boolean isUsbDevice) throws IOException, XmlPullParserException {
    String name = parser.getAttributeValue(null, DeviceConfiguration.XMLATTR_NAME);

    // USB devices have serial numbers instead of ports.
    int controllerPort = -1;
    String serialNumber = new SerialNumber().toString();
    if (isUsbDevice) {
      serialNumber = parser.getAttributeValue(null, ControllerConfiguration.XMLATTR_SERIAL_NUMBER);
    } else {
      controllerPort = Integer.parseInt(parser.getAttributeValue(null, DeviceConfiguration.XMLATTR_PORT));
    }

    List<MotorConfiguration> motors = buildEmptyMotors(ModernRoboticsConstants.INITIAL_MOTOR_PORT, ModernRoboticsConstants.NUMBER_OF_MOTORS, MotorConfigurationType.getUnspecifiedMotorType());

    int eventType = parser.next();
    ConfigurationType configurationType = deform(parser.getName());

    while(eventType != XmlPullParser.END_DOCUMENT) { // we shouldn't reach the end of the document here anyway...
      if (eventType == XmlPullParser.END_TAG) {
        if (configurationType == null) {
          // just an empty <MOTOR> </> closing tag
          continue;
        }
        if (configurationType == BuiltInConfigurationType.MOTOR_CONTROLLER) {
          // end of loop...
          noteExistingName(BuiltInConfigurationType.MOTOR_CONTROLLER, name);
          MotorControllerConfiguration newController = new MotorControllerConfiguration(name, motors, new SerialNumber(serialNumber));
          newController.setPort(controllerPort);
          newController.setEnabled(true);
          return newController;
        }
      }
      if (eventType == XmlPullParser.START_TAG) {
        if (configurationType.isDeviceFlavor(UserConfigurationType.Flavor.MOTOR)) {
          MotorConfiguration motor = handleMotor();
          // ModernRobotics HW is indexed by 1, but internally this code indexes by 0.
          motors.set(motor.getPort()- ModernRoboticsConstants.INITIAL_MOTOR_PORT, motor);
        }
      }
      eventType = parser.next();
      configurationType = deform(parser.getName());
    }

    MotorControllerConfiguration newController = new MotorControllerConfiguration(name, motors, new SerialNumber(serialNumber));
    newController.setPort(controllerPort);
    return newController;
  }

  private ConfigurationType deform(String xmlTag){
    ConfigurationType result = null;
    if (xmlTag != null) {
      result = UserConfigurationTypeManager.getInstance().configurationTypeFromTag(xmlTag);
    }
    return result;
  }

}