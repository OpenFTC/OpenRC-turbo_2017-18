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

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Xml;

import com.qualcomm.robotcore.exception.DuplicateNameException;
import com.qualcomm.robotcore.exception.RobotCoreException;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;


public class WriteXMLFileHandler {

  private XmlSerializer serializer;
  private HashSet<String> names = new HashSet<String>();
  private List<String> duplicates = new ArrayList<String>();
  private String[] indentation = {"    ", "        ", "            "};
  private int indent = 0;

  public WriteXMLFileHandler(Context context) {
    serializer = Xml.newSerializer();
  }

  public String toXml(Collection<ControllerConfiguration> deviceControllerConfigurations) {
    return toXml(deviceControllerConfigurations, null, null);
  }

  public String toXml(Collection<ControllerConfiguration> deviceControllerConfigurations, @Nullable String attribute, @Nullable String attributeValue) {
    duplicates = new ArrayList<String>();
    names = new HashSet<String>();

    StringWriter writer = new StringWriter();
    try {
      serializer.setOutput(writer);
      serializer.startDocument("UTF-8", true);
      serializer.ignorableWhitespace("\n");
      serializer.startTag("", "Robot");
      if (attribute!= null) serializer.attribute("", attribute, attributeValue);
      serializer.ignorableWhitespace("\n");
      for (ControllerConfiguration controllerConfiguration : deviceControllerConfigurations) {

        ConfigurationType type = controllerConfiguration.getConfigurationType();
        if (type == BuiltInConfigurationType.MOTOR_CONTROLLER || type == BuiltInConfigurationType.SERVO_CONTROLLER) {
          handleController(controllerConfiguration, true);
        } else if (type == BuiltInConfigurationType.LEGACY_MODULE_CONTROLLER) {
          handleLegacyModuleController((LegacyModuleControllerConfiguration)controllerConfiguration);
        } else if (type == BuiltInConfigurationType.DEVICE_INTERFACE_MODULE) {
          handleDeviceInterfaceModule((DeviceInterfaceModuleConfiguration)controllerConfiguration);
        } else if (type == BuiltInConfigurationType.LYNX_USB_DEVICE) {
          handleLynxUSBDevice((LynxUsbDeviceConfiguration)controllerConfiguration);
        }
      }
      serializer.endTag("", "Robot");
      serializer.ignorableWhitespace("\n");
      serializer.endDocument();
      return writer.toString();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void checkForDuplicates(DeviceConfiguration config) {
    if (config.isEnabled()) {
      String name = config.getName();
      if (names.contains(name)){
        duplicates.add(name);
      } else {
        names.add(name);
      }
    }
  }

  private void handleDeviceInterfaceModule(DeviceInterfaceModuleConfiguration controller) throws IOException {
    serializer.ignorableWhitespace(indentation[indent]);
    serializer.startTag("", conform(controller.getConfigurationType()));
    checkForDuplicates(controller);
    serializer.attribute("", DeviceConfiguration.XMLATTR_NAME, controller.getName());
    serializer.attribute("", ControllerConfiguration.XMLATTR_SERIAL_NUMBER, controller.getSerialNumber().toString());
    serializer.ignorableWhitespace("\n");
    indent++;

    DeviceInterfaceModuleConfiguration deviceInterfaceModuleConfiguration = (DeviceInterfaceModuleConfiguration) controller;

    for (DeviceConfiguration device : deviceInterfaceModuleConfiguration.getPwmOutputs()) {
      buildDeviceNameAndPort(device);
    }

    for (DeviceConfiguration device : deviceInterfaceModuleConfiguration.getI2cDevices()) {
      buildDeviceNameAndPort(device);
    }

    for (DeviceConfiguration device : deviceInterfaceModuleConfiguration.getAnalogInputDevices()) {
      buildDeviceNameAndPort(device);
    }

    for (DeviceConfiguration device : deviceInterfaceModuleConfiguration.getDigitalDevices()) {
      buildDeviceNameAndPort(device);
    }

    for (DeviceConfiguration device : deviceInterfaceModuleConfiguration.getAnalogOutputDevices()) {
      buildDeviceNameAndPort(device);
    }

    indent--;
    serializer.ignorableWhitespace(indentation[indent]);
    serializer.endTag("", conform(controller.getConfigurationType()));
    serializer.ignorableWhitespace("\n");
  }

  private void handleLegacyModuleController(LegacyModuleControllerConfiguration controller) throws IOException {
    serializer.ignorableWhitespace(indentation[indent]);
    serializer.startTag("", conform(controller.getConfigurationType()));
    checkForDuplicates(controller);
    serializer.attribute("", DeviceConfiguration.XMLATTR_NAME, controller.getName());
    serializer.attribute("", ControllerConfiguration.XMLATTR_SERIAL_NUMBER, controller.getSerialNumber().toString());
    serializer.ignorableWhitespace("\n");
    indent++;

    // step through the list of attached devices,
    for (DeviceConfiguration device : controller.getDevices()) {
      ConfigurationType type = device.getConfigurationType();
      if (type == BuiltInConfigurationType.MOTOR_CONTROLLER)        { handleController((MotorControllerConfiguration)device, false);
      } else if (type == BuiltInConfigurationType.SERVO_CONTROLLER) { handleController((ServoControllerConfiguration)device, false);
      } else if (type == BuiltInConfigurationType.MATRIX_CONTROLLER){ handleController((MatrixControllerConfiguration)device, false);
      } else {
        buildDeviceNameAndPort(device);
      }
    }

    indent--;
    serializer.ignorableWhitespace(indentation[indent]);
    serializer.endTag("", conform(controller.getConfigurationType()));
    serializer.ignorableWhitespace("\n");
  }

  private void handleLynxUSBDevice(LynxUsbDeviceConfiguration controller) throws IOException {
    serializer.ignorableWhitespace(indentation[indent]);
    serializer.startTag("", conform(controller.getConfigurationType()));
    checkForDuplicates(controller);
    serializer.attribute("", DeviceConfiguration.XMLATTR_NAME,                          controller.getName());
    serializer.attribute("", ControllerConfiguration.XMLATTR_SERIAL_NUMBER,             controller.getSerialNumber().toString());
    serializer.attribute("", LynxUsbDeviceConfiguration.XMLATTR_PARENT_MODULE_ADDRESS, Integer.toString(controller.getParentModuleAddress()));
    serializer.ignorableWhitespace("\n");
    indent++;

    for (DeviceConfiguration device : controller.getDevices()) {
      ConfigurationType type = device.getConfigurationType();
      if (type == BuiltInConfigurationType.LYNX_MODULE) {
        handleController((LynxModuleConfiguration)device, false);
      } else {
        buildDeviceNameAndPort(device);
      }
    }

    indent--;
    serializer.ignorableWhitespace(indentation[indent]);
    serializer.endTag("", conform(controller.getConfigurationType()));
    serializer.ignorableWhitespace("\n");
  }

  private <CONTROLLER_T extends ControllerConfiguration<? extends DeviceConfiguration>>  void handleController(CONTROLLER_T controller, boolean isUsbDevice) throws IOException {
    serializer.ignorableWhitespace(indentation[indent]);
    serializer.startTag("", conform(controller.getConfigurationType()));
    checkForDuplicates(controller);
    serializer.attribute("", DeviceConfiguration.XMLATTR_NAME, controller.getName());

    if (isUsbDevice) {
      serializer.attribute("", ControllerConfiguration.XMLATTR_SERIAL_NUMBER, controller.getSerialNumber().toString());
    } else {
      serializer.attribute("", DeviceConfiguration.XMLATTR_PORT, String.valueOf(controller.getPort())); // for lynx modules, 'port' is 'moduleAddress'
    }

    serializer.ignorableWhitespace("\n");
    indent++;

    if (controller.getConfigurationType() == BuiltInConfigurationType.LYNX_MODULE) {
      LynxModuleConfiguration moduleConfiguration = (LynxModuleConfiguration) controller;

      for (DeviceConfiguration device : moduleConfiguration.getMotors()) {
        buildDeviceNameAndPort(device);
      }
      for (DeviceConfiguration device : moduleConfiguration.getServos()) {
        buildDeviceNameAndPort(device);
      }
      for (DeviceConfiguration device : moduleConfiguration.getAnalogInputs()) {
        buildDeviceNameAndPort(device);
      }
      for (DeviceConfiguration device : moduleConfiguration.getPwmOutputs()) {
        buildDeviceNameAndPort(device);
      }
      for (DeviceConfiguration device : moduleConfiguration.getDigitalDevices()) {
        buildDeviceNameAndPort(device);
      }
      for (DeviceConfiguration device : moduleConfiguration.getI2cDevices()) {
        buildDeviceNameAndPort(device);
      }
    }

    else if(controller.getConfigurationType() == BuiltInConfigurationType.MATRIX_CONTROLLER) {
      for (DeviceConfiguration device : ((MatrixControllerConfiguration) controller).getMotors()) {
        buildDeviceNameAndPort(device);
      }
      for (DeviceConfiguration device : ((MatrixControllerConfiguration) controller).getServos()) {
        buildDeviceNameAndPort(device);
      }
    }

    else {
      for (DeviceConfiguration device : controller.getDevices()) {
        buildDeviceNameAndPort(device);
      }
    }

    indent--;
    serializer.ignorableWhitespace(indentation[indent]);
    serializer.endTag("", conform(controller.getConfigurationType()));
    serializer.ignorableWhitespace("\n");
  }

  // Emits an XML element for the device that has name and port attributes
  private void buildDeviceNameAndPort(DeviceConfiguration device) {
    if (!device.isEnabled()) {
      return;
    }
    try {
      serializer.ignorableWhitespace(indentation[indent]);
      serializer.startTag("", conform(device.getConfigurationType()));
      checkForDuplicates(device);
      device.serializeXmlAttributes(serializer);
      serializer.endTag("", conform(device.getConfigurationType()));
      serializer.ignorableWhitespace("\n");
    } catch (Exception e){
      throw new RuntimeException(e);
    }
  }

  public void writeToFile(String data, File folder, String filenameWithExt) throws RobotCoreException, IOException {
    if (duplicates.size() > 0) {
      throw new DuplicateNameException("Duplicate names: " + duplicates);
    }

    boolean success = true;

    if (!folder.exists()) {
      success = folder.mkdir();
    }
    if (success) {
      File file = new File(folder, filenameWithExt);
      FileOutputStream stream = null;
      try {
        stream = new FileOutputStream(file);
        stream.write(data.getBytes());
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        try {
          stream.close();
        } catch (IOException e) {
          // Auto-generated catch block
          e.printStackTrace();
        }
      }
    } else {
      throw new RobotCoreException("Unable to create directory");
    }
  }

  private String conform(ConfigurationType type) {
    return type.getXmlTag();
  }
}