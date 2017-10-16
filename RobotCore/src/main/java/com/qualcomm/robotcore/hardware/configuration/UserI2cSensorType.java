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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceImpl;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchSimple;
import com.qualcomm.robotcore.hardware.RobotCoreLynxModule;
import com.qualcomm.robotcore.util.ClassUtil;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;

/**
 * {@link UserI2cSensorType} contains the meta-data for a user-defined I2c sensor.
 */
@SuppressWarnings("WeakerAccess")
public final class UserI2cSensorType extends UserConfigurationType // final because of serialization concerns
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    protected Class<? extends HardwareDevice>   clazz;
    protected List<Constructor>                 constructors;
    protected @Expose String                    description;

    protected static final Class<?>[] ctorI2cDeviceSynchSimple  = new Class<?>[]{I2cDeviceSynchSimple.class};
    protected static final Class<?>[] ctorI2cDeviceSynch        = new Class<?>[]{I2cDeviceSynch.class};
    protected static final Class<?>[] ctorI2cDevice             = new Class<?>[]{I2cDevice.class};
    protected static final Class<?>[] ctorI2cControllerPort     = new Class<?>[]{I2cController.class, int.class};

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public UserI2cSensorType(Class<? extends HardwareDevice> clazz, String xmlTag)
        {
        super(clazz, Flavor.I2C, xmlTag);
        this.clazz = clazz;
        this.constructors = findConstructors();
        }

    public static UserI2cSensorType getLynxEmbeddedIMUType()
        {
        return (UserI2cSensorType) UserConfigurationTypeManager.getInstance().userTypeFromTag(AppUtil.getDefContext().getString(com.qualcomm.robotcore.R.string.lynx_embedded_imu_xmltag));
        }

    // Used by gson deserialization
    public UserI2cSensorType()
        {
        super(Flavor.I2C);
        }

    public void processAnnotation(@Nullable I2cSensor i2cSensor)
        {
        if (i2cSensor != null)
            {
            if (name==null || name.isEmpty())
                {
                name = ClassUtil.decodeStringRes(i2cSensor.name().trim());
                }
            this.description = ClassUtil.decodeStringRes(i2cSensor.description());
            }
        }

    public void finishedAnnotations(Class clazz)
        {
        if (name==null || name.isEmpty())
            {
            name = clazz.getSimpleName();
            }
        }


    //----------------------------------------------------------------------------------------------
    // Accessing
    //----------------------------------------------------------------------------------------------

    public String getDescription()
        {
        return this.description;
        }

    public boolean hasConstructors()
        {
        return this.constructors.size() > 0;
        }

    public Class<? extends HardwareDevice> getClazz()
        {
        return this.clazz;
        }

    //----------------------------------------------------------------------------------------------
    // Instance creation
    //----------------------------------------------------------------------------------------------

    public @NonNull HardwareDevice createInstance(RobotCoreLynxModule lynxModule,
            Func<I2cDeviceSynchSimple> simpleSynchFunc,
            Func<I2cDeviceSynch> synchFunc) throws InvocationTargetException
        {
        try {
            Constructor ctor;

            ctor = findMatch(ctorI2cDeviceSynchSimple);
            if (null != ctor)
                {
                I2cDeviceSynchSimple i2cDeviceSynchSimple = simpleSynchFunc.value();
                return (HardwareDevice)ctor.newInstance(i2cDeviceSynchSimple);
                }

            ctor = findMatch(ctorI2cDeviceSynch);
            if (null != ctor)
                {
                I2cDeviceSynch i2cDeviceSynch = synchFunc.value();
                return (HardwareDevice)ctor.newInstance(i2cDeviceSynch);
                }
            }
         catch (IllegalAccessException|InstantiationException e)
             {
             throw new RuntimeException("internal error: exception");
             }
        throw new RuntimeException("internal error: unable to locate constructor for user sensor type");
        }

    public @NonNull
    HardwareDevice createInstance(I2cController controller, int port) throws InvocationTargetException
        {
        try {
            Constructor ctor;

            ctor = findMatch(ctorI2cDeviceSynch);
            if (null == ctor) ctor = findMatch(ctorI2cDeviceSynchSimple);
            if (null != ctor)
                {
                I2cDevice      i2cDevice      = new I2cDeviceImpl(controller, port);
                I2cDeviceSynch i2cDeviceSynch = new I2cDeviceSynchImpl(i2cDevice, true);
                return (HardwareDevice)ctor.newInstance(i2cDeviceSynch);
                }

            ctor = findMatch(ctorI2cDevice);
            if (null != ctor)
                {
                I2cDevice i2cDevice = new I2cDeviceImpl(controller, port);
                return (HardwareDevice)ctor.newInstance(i2cDevice);
                }

            ctor = findMatch(ctorI2cControllerPort);
            if (null != ctor)
                {
                return (HardwareDevice)ctor.newInstance(controller, port);
                }
            }
         catch (IllegalAccessException|InstantiationException e)
             {
             throw new RuntimeException("internal error: exception");
             }
        throw new RuntimeException("internal error: unable to locate constructor for user sensor type");
        }

    protected Constructor findMatch(Class<?>[] prototype)
        {
        for (Constructor ctor : this.constructors)
            {
            Class<?>[] parameters = ctor.getParameterTypes();
            if (match(parameters, prototype))
                {
                return ctor;
                }
            }
        return null;
        }

    protected List<Constructor> findConstructors()
        {
        List<Constructor> result = new LinkedList<Constructor>();
        List<Constructor> constructors = ClassUtil.getDeclaredConstructors(clazz);
        for (Constructor<?> ctor : constructors)
            {
            int requiredModifiers = Modifier.PUBLIC;
            int prohibitedModifiers = Modifier.STATIC | Modifier.ABSTRACT;
            if (!((ctor.getModifiers() & requiredModifiers) == requiredModifiers && (ctor.getModifiers() & prohibitedModifiers) == 0))
                continue;

            Class<?>[] parameters = ctor.getParameterTypes();
            switch (flavor)
                {
                case I2C:
                    if (match(parameters, ctorI2cControllerPort)
                            || match(parameters, ctorI2cDevice)
                            || match(parameters, ctorI2cDeviceSynch)
                            || match(parameters, ctorI2cDeviceSynchSimple))
                        {
                        result.add(ctor);
                        }
                    break;
                }
            }
        return result;
        }

    protected boolean match(Class<?>[] declared, Class<?>[] desired)
        {
        if (declared.length == desired.length)
            {
            for (int i = 0; i < declared.length; i++)
                {
                if (!declared[i].equals(desired[i]))
                    {
                    return false;
                    }
                }
            return true;
            }
        else
            return false;
        }

    //----------------------------------------------------------------------------------------------
    // Serialization (used in local marshalling during configuration editing)
    //----------------------------------------------------------------------------------------------

    private Object writeReplace()
        {
        return new SerializationProxy(this);
        }
    }
