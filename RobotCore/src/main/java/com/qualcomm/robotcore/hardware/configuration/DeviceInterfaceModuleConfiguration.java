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

import com.qualcomm.robotcore.util.SerialNumber;

import java.util.List;

public class DeviceInterfaceModuleConfiguration extends ControllerConfiguration<DeviceConfiguration> {

  private List<DeviceConfiguration> pwmOutputs;
  private List<DeviceConfiguration> i2cDevices;
  private List<DeviceConfiguration> analogInputDevices;
  private List<DeviceConfiguration> digitalDevices;
  private List<DeviceConfiguration> analogOutputDevices;

  public DeviceInterfaceModuleConfiguration(String name, SerialNumber serialNumber) {
    super(name, serialNumber, BuiltInConfigurationType.DEVICE_INTERFACE_MODULE);
  }

  public void setPwmOutputs(List<DeviceConfiguration> pwmDevices) {
    this.pwmOutputs = pwmDevices;
  }

  public List<DeviceConfiguration> getPwmOutputs() {
    return pwmOutputs;
  }

  public List<DeviceConfiguration> getI2cDevices() {
    return i2cDevices;
  }

  public void setI2cDevices(List<DeviceConfiguration> i2cDevices) {
    this.i2cDevices = i2cDevices;
  }

  public List<DeviceConfiguration> getAnalogInputDevices() {
    return analogInputDevices;
  }

  public void setAnalogInputDevices(List<DeviceConfiguration> analogInputDevices) {
    this.analogInputDevices = analogInputDevices;
  }

  public List<DeviceConfiguration> getDigitalDevices() {
    return digitalDevices;
  }

  public void setDigitalDevices(List<DeviceConfiguration> digitalDevices) {
    this.digitalDevices = digitalDevices;
  }

  public List<DeviceConfiguration> getAnalogOutputDevices() {
    return analogOutputDevices;
  }

  public void setAnalogOutputDevices(List<DeviceConfiguration> analogOutputDevices) {
    this.analogOutputDevices = analogOutputDevices;
  }
}
