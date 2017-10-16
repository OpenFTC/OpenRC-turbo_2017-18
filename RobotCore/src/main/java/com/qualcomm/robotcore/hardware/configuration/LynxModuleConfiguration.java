/*
Copyright (c) 2016 Robert Atkinson

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Robert Atkinson nor the names of his contributors may be used to
endorse or promote products derived from this software without specific prior
written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESSFOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package com.qualcomm.robotcore.hardware.configuration;

import com.qualcomm.robotcore.util.SerialNumber;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by bob on 2016-03-11.
 */
@SuppressWarnings("WeakerAccess")
public class LynxModuleConfiguration extends ControllerConfiguration<DeviceConfiguration>
    {
    private boolean                   isParent       = false;
    private List<MotorConfiguration>  motors         = new LinkedList<>();
    private List<ServoConfiguration>  servos         = new LinkedList<>();
    private List<DeviceConfiguration> pwmOutputs     = new LinkedList<>();
    private List<DeviceConfiguration> digitalDevices = new LinkedList<>();
    private List<DeviceConfiguration> analogInputs   = new LinkedList<>();
    private List<LynxI2cDeviceConfiguration> i2cDevices  = new LinkedList<>();

    // A note: unlike everything else, we don't have a fixed number of attachable i2c devices.
    // Rather, we have four i2c busses, to each of which may be attached any number of i2c devices
    // so long as no two i2c devices with the same address reside on the same bus.
    //
    // In each i2c DeviceConfiguration, the 'port' is the 0-based bus-number.
    //
    // This will have consequences for the configuration editor!

    public LynxModuleConfiguration()
        {
        this("");
        }

    public LynxModuleConfiguration(String name)
        {
        super(name, new ArrayList<DeviceConfiguration>(), new SerialNumber(), BuiltInConfigurationType.LYNX_MODULE);
        }

    public void setModuleAddress(int moduleAddress)
        {
        this.setPort(moduleAddress);
        }
    public int getModuleAddress()
        {
        return this.getPort();
        }

    public void setParent(boolean isParent)
        {
        this.isParent = isParent;
        }
    public boolean isParent()
        {
        return this.isParent;
        }

    public List<ServoConfiguration> getServos()
        {
        return servos;
        }
    public void setServos(List<ServoConfiguration> servos)
        {
        this.servos = servos;
        }

    public List<MotorConfiguration> getMotors()
        {
        return motors;
        }
    public void setMotors(List<MotorConfiguration> motors)
        {
        this.motors = motors;
        }

    public List<DeviceConfiguration> getAnalogInputs()
        {
        return this.analogInputs;
        }
    public void setAnalogInputs(List<DeviceConfiguration> inputs)
        {
        this.analogInputs = inputs;
        }

    public List<DeviceConfiguration> getPwmOutputs()
        {
        return this.pwmOutputs;
        }
    public void setPwmOutputs(List<DeviceConfiguration> pwmOutputs)
        {
        this.pwmOutputs = pwmOutputs;
        }

    public List<LynxI2cDeviceConfiguration> getI2cDevices()
        {
        return i2cDevices;
        }
    public void setI2cDevices(List<LynxI2cDeviceConfiguration> i2cDevices)
        {
        this.i2cDevices = new LinkedList<LynxI2cDeviceConfiguration>();
        for (LynxI2cDeviceConfiguration i2cDevice : i2cDevices)
            {
            if (i2cDevice.isEnabled() && i2cDevice.getPort() >= 0 && i2cDevice.getPort() <  0 + LynxConstants.NUMBER_OF_I2C_BUSSES)
                {
                this.i2cDevices.add(i2cDevice);
                }
            }
        }

    public List<LynxI2cDeviceConfiguration> getI2cDevices(int busZ)
        {
        List<LynxI2cDeviceConfiguration> result = new LinkedList<LynxI2cDeviceConfiguration>();
        for (LynxI2cDeviceConfiguration configuration : this.i2cDevices)
            {
            if (configuration.getBus() == busZ)
                {
                result.add(configuration);
                }
            }
        return result;
        }

    public void setI2cDevices(int busZ, List<LynxI2cDeviceConfiguration> devices)
        {
        List<LynxI2cDeviceConfiguration> result = new LinkedList<LynxI2cDeviceConfiguration>();
        for (LynxI2cDeviceConfiguration configuration : this.i2cDevices)
            {
            if (configuration.getBus() != busZ)
                {
                result.add(configuration);
                }
            }
        for (LynxI2cDeviceConfiguration configuration : devices)
            {
            if (configuration.isEnabled())
                {
                configuration.setBus(busZ);
                result.add(configuration);
                }
            }
        this.i2cDevices = result;
        }

    public List<DeviceConfiguration> getDigitalDevices()
        {
        return this.digitalDevices;
        }
    public void setDigitalDevices(List<DeviceConfiguration> digitalDevices)
        {
        this.digitalDevices = digitalDevices;
        }
    }