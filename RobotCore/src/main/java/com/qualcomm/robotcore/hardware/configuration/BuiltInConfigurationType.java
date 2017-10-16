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
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
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

import android.content.Context;
import android.support.annotation.NonNull;

import com.qualcomm.robotcore.R;
import com.qualcomm.robotcore.hardware.DeviceManager;

/**
 * {@link BuiltInConfigurationType} is an enum representing all the various types of hardware
 * whose semantics and supporting logic is built into the SDK.
 */
public enum BuiltInConfigurationType implements ConfigurationType
    {
        SERVO("Servo"),
        CONTINUOUS_ROTATION_SERVO("ContinuousRotationServo"),
        GYRO("Gyro"),
        COMPASS("Compass"),
        IR_SEEKER("IrSeeker"),
        LIGHT_SENSOR("LightSensor"),
        ACCELEROMETER("Accelerometer"),
        MOTOR_CONTROLLER("MotorController"),
        SERVO_CONTROLLER("ServoController"),
        LEGACY_MODULE_CONTROLLER("LegacyModuleController"),
        DEVICE_INTERFACE_MODULE("DeviceInterfaceModule"),
        I2C_DEVICE("I2cDevice"),
        I2C_DEVICE_SYNCH("I2cDeviceSynch"),
        ANALOG_INPUT("AnalogInput"),
        TOUCH_SENSOR("TouchSensor"),                // either a MR or an NXT touch sensor on a digital port
        MR_ANALOG_TOUCH_SENSOR("ModernRoboticsAnalogTouchSensor"),   // a MR touch sensor on an analog port
        OPTICAL_DISTANCE_SENSOR("OpticalDistanceSensor"),
        ANALOG_OUTPUT("AnalogOutput"),
        DIGITAL_DEVICE("DigitalDevice"),
        PULSE_WIDTH_DEVICE("PulseWidthDevice"),
        IR_SEEKER_V3("IrSeekerV3"),
        TOUCH_SENSOR_MULTIPLEXER("TouchSensorMultiplexer"),
        MATRIX_CONTROLLER("MatrixController"),
        ULTRASONIC_SENSOR("UltrasonicSensor"),
        ADAFRUIT_COLOR_SENSOR("AdafruitColorSensor"),
        COLOR_SENSOR("ColorSensor"),    // this is a modern robotics or an NXT color sensor
        LED("Led"),
        LYNX_COLOR_SENSOR("LynxColorSensor"),
        LYNX_USB_DEVICE("LynxUsbDevice"),
        LYNX_MODULE("LynxModule"),
        NOTHING("Nothing"),             // in the config UI, NOTHING means no device is attached
        UNKNOWN("<unknown>");           // UNKNOWN is never actually used in XML

    public final String xmlTag;

    BuiltInConfigurationType(String xmlTag)
        {
        this.xmlTag = xmlTag;
        }

    public static ConfigurationType fromXmlTag(String xmlTag)
        {
        if (xmlTag.equalsIgnoreCase(SERVO.xmlTag)) return SERVO;
        if (xmlTag.equalsIgnoreCase(CONTINUOUS_ROTATION_SERVO.xmlTag)) return CONTINUOUS_ROTATION_SERVO;
        if (xmlTag.equalsIgnoreCase(GYRO.xmlTag)) return GYRO;
        if (xmlTag.equalsIgnoreCase(COMPASS.xmlTag)) return COMPASS;
        if (xmlTag.equalsIgnoreCase(IR_SEEKER.xmlTag)) return IR_SEEKER;
        if (xmlTag.equalsIgnoreCase(LIGHT_SENSOR.xmlTag)) return LIGHT_SENSOR;
        if (xmlTag.equalsIgnoreCase(ACCELEROMETER.xmlTag)) return ACCELEROMETER;
        if (xmlTag.equalsIgnoreCase(MOTOR_CONTROLLER.xmlTag)) return MOTOR_CONTROLLER;
        if (xmlTag.equalsIgnoreCase(SERVO_CONTROLLER.xmlTag)) return SERVO_CONTROLLER;
        if (xmlTag.equalsIgnoreCase(LEGACY_MODULE_CONTROLLER.xmlTag)) return LEGACY_MODULE_CONTROLLER;
        if (xmlTag.equalsIgnoreCase(DEVICE_INTERFACE_MODULE.xmlTag)) return DEVICE_INTERFACE_MODULE;
        if (xmlTag.equalsIgnoreCase(I2C_DEVICE.xmlTag)) return I2C_DEVICE;
        if (xmlTag.equalsIgnoreCase(I2C_DEVICE_SYNCH.xmlTag)) return I2C_DEVICE_SYNCH;
        if (xmlTag.equalsIgnoreCase(ANALOG_INPUT.xmlTag)) return ANALOG_INPUT;
        if (xmlTag.equalsIgnoreCase(TOUCH_SENSOR.xmlTag)) return TOUCH_SENSOR;
        if (xmlTag.equalsIgnoreCase(MR_ANALOG_TOUCH_SENSOR.xmlTag)) return MR_ANALOG_TOUCH_SENSOR;
        if (xmlTag.equalsIgnoreCase(OPTICAL_DISTANCE_SENSOR.xmlTag)) return OPTICAL_DISTANCE_SENSOR;
        if (xmlTag.equalsIgnoreCase(ANALOG_OUTPUT.xmlTag)) return ANALOG_OUTPUT;
        if (xmlTag.equalsIgnoreCase(DIGITAL_DEVICE.xmlTag)) return DIGITAL_DEVICE;
        if (xmlTag.equalsIgnoreCase(PULSE_WIDTH_DEVICE.xmlTag)) return PULSE_WIDTH_DEVICE;
        if (xmlTag.equalsIgnoreCase(IR_SEEKER_V3.xmlTag)) return IR_SEEKER_V3;
        if (xmlTag.equalsIgnoreCase(TOUCH_SENSOR_MULTIPLEXER.xmlTag)) return TOUCH_SENSOR_MULTIPLEXER;
        if (xmlTag.equalsIgnoreCase(MATRIX_CONTROLLER.xmlTag)) return MATRIX_CONTROLLER;
        if (xmlTag.equalsIgnoreCase(ULTRASONIC_SENSOR.xmlTag)) return ULTRASONIC_SENSOR;
        if (xmlTag.equalsIgnoreCase(ADAFRUIT_COLOR_SENSOR.xmlTag)) return ADAFRUIT_COLOR_SENSOR;
        if (xmlTag.equalsIgnoreCase(COLOR_SENSOR.xmlTag)) return COLOR_SENSOR;
        if (xmlTag.equalsIgnoreCase(LYNX_COLOR_SENSOR.xmlTag)) return LYNX_COLOR_SENSOR;
        if (xmlTag.equalsIgnoreCase(LYNX_USB_DEVICE.xmlTag)) return LYNX_USB_DEVICE;
        if (xmlTag.equalsIgnoreCase(LYNX_MODULE.xmlTag)) return LYNX_MODULE;
        if (xmlTag.equalsIgnoreCase(LED.xmlTag)) return LED;
        if (xmlTag.equalsIgnoreCase(NOTHING.xmlTag)) return NOTHING;
        return UNKNOWN;
        }

    public static ConfigurationType fromString(String toString)
        {
        for (ConfigurationType configType : BuiltInConfigurationType.values())
            {
            if (toString.equalsIgnoreCase(configType.toString()))
                {
                return configType;
                }
            }
        return BuiltInConfigurationType.UNKNOWN;
        }

    public static ConfigurationType fromUSBDeviceType(DeviceManager.DeviceType type)
        {
        switch (type)
            {
            case MODERN_ROBOTICS_USB_DC_MOTOR_CONTROLLER:       return MOTOR_CONTROLLER;
            case MODERN_ROBOTICS_USB_SERVO_CONTROLLER:          return SERVO_CONTROLLER;
            case MODERN_ROBOTICS_USB_DEVICE_INTERFACE_MODULE:   return DEVICE_INTERFACE_MODULE;
            case MODERN_ROBOTICS_USB_LEGACY_MODULE:             return LEGACY_MODULE_CONTROLLER;
            case LYNX_USB_DEVICE:                               return LYNX_USB_DEVICE;
            default:                                            return UNKNOWN;
            }
        }

    @Override public boolean isDeviceFlavor(UserConfigurationType.Flavor flavor)
        {
        if (flavor == UserConfigurationType.Flavor.I2C)
            {
            switch (this)
                {
                case I2C_DEVICE:
                case I2C_DEVICE_SYNCH:
                case IR_SEEKER_V3:
                case ADAFRUIT_COLOR_SENSOR:
                case LYNX_COLOR_SENSOR:
                case COLOR_SENSOR:
                case GYRO:
                    return true;
                }
            }
        else if (flavor == UserConfigurationType.Flavor.MOTOR)
            {
            // There are no built-in motor types (anymore :-)
            }
        return false;
        }

    @Override @NonNull
    public DeviceManager.DeviceType toUSBDeviceType()
        {
        switch (this)
            {
            case MOTOR_CONTROLLER:          return DeviceManager.DeviceType.MODERN_ROBOTICS_USB_DC_MOTOR_CONTROLLER;
            case SERVO_CONTROLLER:          return DeviceManager.DeviceType.MODERN_ROBOTICS_USB_SERVO_CONTROLLER;
            case DEVICE_INTERFACE_MODULE:   return DeviceManager.DeviceType.MODERN_ROBOTICS_USB_DEVICE_INTERFACE_MODULE;
            case LEGACY_MODULE_CONTROLLER:  return DeviceManager.DeviceType.MODERN_ROBOTICS_USB_LEGACY_MODULE;
            case LYNX_USB_DEVICE:           return DeviceManager.DeviceType.LYNX_USB_DEVICE;
            default:                        return DeviceManager.DeviceType.FTDI_USB_UNKNOWN_DEVICE;
            }
        }

    @Override @NonNull
    public String getDisplayName(DisplayNameFlavor flavor, Context context)
        {
        switch (this)
            {
            case SERVO:                     return context.getString(R.string.configTypeServo);
            case CONTINUOUS_ROTATION_SERVO: return context.getString(R.string.configTypeContinuousRotationServo);
            case COMPASS:                   return context.getString(R.string.configTypeHTCompass);
            case IR_SEEKER:                 return context.getString(R.string.configTypeHTIrSeeker);
            case LIGHT_SENSOR:              return context.getString(R.string.configTypeHTLightSensor);
            case ACCELEROMETER:             return context.getString(R.string.configTypeHTAccelerometer);
            case MOTOR_CONTROLLER:          return context.getString(R.string.configTypeMotorController);
            case SERVO_CONTROLLER:          return context.getString(R.string.configTypeServoController);
            case LEGACY_MODULE_CONTROLLER:  return context.getString(R.string.configTypeLegacyModuleController);
            case DEVICE_INTERFACE_MODULE:   return context.getString(R.string.configTypeDeviceInterfaceModule);
            case I2C_DEVICE:                return context.getString(R.string.configTypeI2cDevice);
            case I2C_DEVICE_SYNCH:          return context.getString(R.string.configTypeI2cDeviceSynch);
            case ANALOG_INPUT:              return context.getString(R.string.configTypeAnalogInput);
            case OPTICAL_DISTANCE_SENSOR:   return context.getString(R.string.configTypeOpticalDistanceSensor);
            case ANALOG_OUTPUT:             return context.getString(R.string.configTypeAnalogOutput);
            case DIGITAL_DEVICE:            return context.getString(R.string.configTypeDigitalDevice);
            case PULSE_WIDTH_DEVICE:        return context.getString(R.string.configTypePulseWidthDevice);
            case IR_SEEKER_V3:              return context.getString(R.string.configTypeIrSeekerV3);
            case TOUCH_SENSOR_MULTIPLEXER:  return context.getString(R.string.configTypeHTTouchSensorMultiplexer);
            case MATRIX_CONTROLLER:         return context.getString(R.string.configTypeMatrixController);
            case ULTRASONIC_SENSOR:         return context.getString(R.string.configTypeNXTUltrasonicSensor);
            case ADAFRUIT_COLOR_SENSOR:     return context.getString(R.string.configTypeAdafruitColorSensor);
            case LED:                       return context.getString(R.string.configTypeLED);
            case LYNX_COLOR_SENSOR:         return context.getString(R.string.configTypeLynxColorSensor);
            case LYNX_USB_DEVICE:           return context.getString(R.string.configTypeLynxUSBDevice);
            case LYNX_MODULE:               return context.getString(R.string.configTypeLynxModule);
            case NOTHING:                   return context.getString(R.string.configTypeNothing);
            case MR_ANALOG_TOUCH_SENSOR:    return context.getString(R.string.configTypeMRTouchSensor);
            case TOUCH_SENSOR:
                return flavor==DisplayNameFlavor.Legacy
                        ? context.getString(R.string.configTypeNXTTouchSensor)
                        : context.getString(R.string.configTypeMRTouchSensor);
            case GYRO:
                return flavor==DisplayNameFlavor.Legacy
                        ? context.getString(R.string.configTypeHTGyro)
                        : context.getString(R.string.configTypeMRGyro);
            case COLOR_SENSOR:
                return flavor==DisplayNameFlavor.Legacy
                        ? context.getString(R.string.configTypeHTColorSensor)
                        : context.getString(R.string.configTypeMRColorSensor);
            case UNKNOWN:
            default:
                return context.getString(R.string.configTypeUnknown);
            }
        }

    @Override @NonNull public String getXmlTag()
        {
        return this.xmlTag;
        }
    }
