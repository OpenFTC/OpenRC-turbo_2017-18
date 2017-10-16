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

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.robocol.Command;
import com.qualcomm.robotcore.util.ClassUtil;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.Predicate;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.network.NetworkConnectionHandler;
import org.firstinspires.ftc.robotcore.internal.network.RobotCoreCommandList;
import org.firstinspires.ftc.robotcore.internal.opmode.ClassFilter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * {@link UserConfigurationTypeManager} is responsible for managing user-defined device types.
 *
 * @see I2cSensor
 * @see MotorType
 */
@SuppressWarnings("WeakerAccess")
@SuppressLint("StaticFieldLeak")
public class UserConfigurationTypeManager implements ClassFilter
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    public static final String TAG = "UserDeviceTypeManager";
    public static boolean DEBUG = true;

    protected static UserConfigurationTypeManager theInstance = new UserConfigurationTypeManager();

    public static UserConfigurationTypeManager getInstance()
        {
        return theInstance;
        }

    protected Context                       context;
    protected Map<String, UserConfigurationType> mapTagToUserType = new HashMap<String, UserConfigurationType>();
    protected Set<String>                   existingXmlTags = new HashSet<String>();
    protected Set<String>                   existingTypeDisplayNames = new HashSet<String>();
    protected static String                 unspecifiedMotorTypeXmlTag = getXmlTag(UnspecifiedMotor.class.getAnnotation(MotorType.class));

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public UserConfigurationTypeManager()
        {
        this.context = AppUtil.getInstance().getApplication();
        addBuiltinConfigurationTypes();
        }

    //----------------------------------------------------------------------------------------------
    // Retrieval
    //----------------------------------------------------------------------------------------------

    public MotorConfigurationType getUnspecifiedMotorType()
        {
        return (MotorConfigurationType)userTypeFromTag(unspecifiedMotorTypeXmlTag);
        }

    public ConfigurationType configurationTypeFromTag(String xmlTag)
        {
        ConfigurationType result = BuiltInConfigurationType.fromXmlTag(xmlTag);
        if (result == BuiltInConfigurationType.UNKNOWN)
            {
            result = userTypeFromTag(xmlTag);
            if (result == null)
                {
                result = BuiltInConfigurationType.UNKNOWN;
                }
            }
        return result;
        }

    public @Nullable UserConfigurationType userTypeFromTag(@NonNull String xmlTag)
        {
        return mapTagToUserType.get(xmlTag);
        }

    public @Nullable
    UserConfigurationType userTypeFromClass(UserConfigurationType.Flavor flavor, Class<?> clazz)
        {
        String xmlTag = null;
        switch (flavor)
            {
            case I2C:
                I2cSensor i2cSensor = clazz.getAnnotation(I2cSensor.class);
                if (i2cSensor != null)
                    {
                    xmlTag = getXmlTag(i2cSensor);
                    }
                break;
            case MOTOR:
                MotorType motorType = clazz.getAnnotation(MotorType.class);
                if (motorType != null)
                    {
                    xmlTag = getXmlTag(motorType);
                    }
                break;
            }
        return xmlTag==null ? null : userTypeFromTag(xmlTag);
        }

    public @Nullable UserConfigurationType userTypeFromTag(String xmlTag, UserConfigurationType.Flavor flavor)
        {
        UserConfigurationType result = mapTagToUserType.get(xmlTag);
        if (result != null && result.getFlavor() != flavor)
            {
            result = null;
            }
        return result;
        }

    public @NonNull Collection<UserConfigurationType> allUserTypes(UserConfigurationType.Flavor flavor)
        {
        List<UserConfigurationType> result = new LinkedList<UserConfigurationType>();
        for (UserConfigurationType userConfigurationType : mapTagToUserType.values())
            {
            if (userConfigurationType.flavor == flavor)
                {
                result.add(userConfigurationType);
                }
            }
        return result;
        }

    //----------------------------------------------------------------------------------------------
    // Serialization
    //----------------------------------------------------------------------------------------------

    protected Gson newGson()
        {
        RuntimeTypeAdapterFactory<UserConfigurationType> userDeviceTypeAdapterFactory
            = RuntimeTypeAdapterFactory.of(UserConfigurationType.class, "flavor")
                .registerSubtype(UserI2cSensorType.class, UserConfigurationType.Flavor.I2C.toString())
                .registerSubtype(MotorConfigurationType.class, UserConfigurationType.Flavor.MOTOR.toString());

        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .registerTypeAdapterFactory(userDeviceTypeAdapterFactory)
                .create();
        }

    public String serializeUserDeviceTypes()
        {
        return newGson().toJson(mapTagToUserType.values());
        }

    public void sendUserDeviceTypes()
        {
        String userDeviceTypes = this.serializeUserDeviceTypes();
        NetworkConnectionHandler.getInstance().sendCommand(new Command(RobotCoreCommandList.CMD_NOTIFY_USER_DEVICE_LIST, userDeviceTypes));
        }


    // Replace the current user device types with the ones contained in the serialization
    public void deserializeUserDeviceTypes(String serialization)
        {
        clearUserTypes();
        //
        UserConfigurationType[] newTypes = newGson().fromJson(serialization, UserConfigurationType[].class);
        for (UserConfigurationType deviceType : newTypes)
            {
            add(deviceType);
            }

        if (DEBUG)
            {
            for (Map.Entry<String, UserConfigurationType> pair : mapTagToUserType.entrySet())
                {
                RobotLog.vv(TAG, "deserialized: xmltag=%s name=%s class=%s", pair.getValue().getXmlTag(), pair.getValue().getName(), pair.getValue().getClass().getSimpleName());
                }
            }
        }

    protected void addBuiltinConfigurationTypes()
        {
        for (BuiltInConfigurationType type : BuiltInConfigurationType.values())
            {
            existingXmlTags.add(type.getXmlTag());
            existingTypeDisplayNames.add(type.getDisplayName(ConfigurationType.DisplayNameFlavor.Normal, this.context));
            existingTypeDisplayNames.add(type.getDisplayName(ConfigurationType.DisplayNameFlavor.Legacy, this.context));
            }
        }

    protected void add(UserConfigurationType deviceType)
        {
        mapTagToUserType.put(deviceType.getXmlTag(), deviceType);
        existingTypeDisplayNames.add(deviceType.getName());
        existingXmlTags.add(deviceType.getXmlTag());
        }

    protected void clearUserTypes()
        {
        List<UserConfigurationType> extant = new ArrayList<>(mapTagToUserType.values()); // capture to avoid deleting while iterating

        for (UserConfigurationType userType : extant)
            {
            existingTypeDisplayNames.remove(userType.getName());
            existingXmlTags.remove(userType.getXmlTag());
            mapTagToUserType.remove(userType.getXmlTag());
            }
        }

    protected void clearOnBotJavaTypes()
        {
        List<UserConfigurationType> extantUserTypes = new ArrayList<>(mapTagToUserType.values());  // capture to avoid deleting while iterating

        for (UserConfigurationType userType : extantUserTypes)
            {
            if (userType.isOnBotJava)
                {
                existingTypeDisplayNames.remove(userType.getName());
                existingXmlTags.remove(userType.getXmlTag());
                mapTagToUserType.remove(userType.getXmlTag());
                }
            }
        }

    //----------------------------------------------------------------------------------------------
    // Annotation parsing
    //----------------------------------------------------------------------------------------------

    @Override public void filterAllClassesStart()
        {
        clearUserTypes();
        }

    @Override public void filterOnBotJavaClassesStart()
        {
        clearOnBotJavaTypes();
        }

    @Override public void filterClass(Class clazz)
        {
        filterMotorType(clazz);
        filterI2c(clazz);
        }

    @Override public void filterOnBotJavaClass(Class clazz)
        {
        filterClass(clazz);
        }

    @Override public void filterAllClassesComplete()
        {
        // Nothing to do
        }

    @Override public void filterOnBotJavaClassesComplete()
        {
        filterAllClassesComplete();
        }

    public void filterMotorType(Class clazz)
        {
        MotorConfigurationType motorType = isUserMotorType(clazz);
        if (motorType != null)
            {
            // There's some things we need to check about the actual class
            if (!checkMotorTypeClassConstraints(motorType))
                return;

            add(motorType);
            }
        }

    protected void filterI2c(Class clazz)
        {
        if (isHardwareDevice(clazz))
            {
            Class<? extends HardwareDevice> clazzHardwareDevice = (Class<? extends HardwareDevice>)clazz;

            // Is this an I2C sensor?
            UserI2cSensorType sensorType = isUserI2cSensor(clazzHardwareDevice);
            if (sensorType != null)
                {
                // There's some things we need to check about the actual class
                if (!checkI2cSensorClassConstraints(sensorType))
                    return;

                add(sensorType);
                }
            }
        }

    MotorConfigurationType isUserMotorType(Class<?> clazz)
        {
        if (clazz.isAnnotationPresent(MotorType.class))
            {
            MotorType motorType = clazz.getAnnotation(MotorType.class);
            MotorConfigurationType result = new MotorConfigurationType(clazz, getXmlTag(motorType));
            result.processAnnotation(motorType);
            result.processAnnotation(findAnnotation(clazz, ModernRoboticsMotorControllerParams.class));
            result.processAnnotation(findAnnotation(clazz, ExpansionHubMotorControllerVelocityParams.class));
            result.processAnnotation(findAnnotation(clazz, ExpansionHubMotorControllerPositionParams.class));
            result.processAnnotation(findAnnotation(clazz, DistributorInfo.class));
            result.finishedAnnotations(clazz);
            return result;
            }
        return null;
        }

    UserI2cSensorType isUserI2cSensor(Class<? extends HardwareDevice> clazz)
        {
        if (clazz.isAnnotationPresent(I2cSensor.class))
            {
            I2cSensor i2cSensor = clazz.getAnnotation(I2cSensor.class);
            UserI2cSensorType result = new UserI2cSensorType(clazz, getXmlTag(i2cSensor));
            result.processAnnotation(i2cSensor);
            result.finishedAnnotations(clazz);
            return result;
            }
        return null;
        }

    /** Allow annotations to be inherited if we want them to. */
    protected <A extends Annotation> A findAnnotation(Class<?> clazz, final Class<A> annotationType)
        {
        final ArrayList<A> result = new ArrayList<A>(1);
        result.add(null);

        ClassUtil.searchInheritance(clazz, new Predicate<Class<?>>()
            {
            @Override public boolean test(Class<?> aClass)
                {
                A annotation = aClass.getAnnotation(annotationType);
                if (annotation != null)
                    {
                    result.set(0, annotation);
                    return true;
                    }
                else
                    return false;
                }
            });

        return result.get(0);
        }

    protected boolean checkMotorTypeClassConstraints(MotorConfigurationType motorType)
        {
        // Check the user-visible form of the sensor name
        if (!isLegalMotorTypeName(motorType.getName()))
            {
            reportConfigurationError("\"%s\" is not a legal motor type name", motorType.getName());
            return false;
            }
        if (existingTypeDisplayNames.contains(motorType.getName()))
            {
            reportConfigurationError("the motor type \"%s\" is already defined", motorType.getName());
            return false;
            }

        // Check the XML tag
        if (!isLegalXmlTag(motorType.getXmlTag()))
            {
            reportConfigurationError("\"%s\" is not a legal XML tag for the motor type \"%s\"", motorType.getXmlTag(), motorType.getName());
            return false;
            }
        if (existingXmlTags.contains(motorType.getXmlTag()))
            {
            reportConfigurationError("the XML tag \"%s\" is already defined", motorType.getXmlTag());
            return false;
            }

        return true;
        }

    protected boolean checkI2cSensorClassConstraints(UserI2cSensorType sensorType)
        {
        // If the class doesn't extend HardwareDevice, that's an error, we'll ignore it
        if (!isHardwareDevice(sensorType.getClazz()))
            {
            reportConfigurationError("'%s' class doesn't inherit from the class 'HardwareDevice'", sensorType.getClazz().getSimpleName());
            return false;
            }

        // If it's not 'public', it can't be loaded by the system and won't work. We report
        // the error and ignore
        if (!Modifier.isPublic(sensorType.getClazz().getModifiers()))
            {
            reportConfigurationError("'%s' class is not declared 'public'", sensorType.getClazz().getSimpleName());
            return false;
            }

        // Can we instantiate?
        if (!sensorType.hasConstructors())
            {
            reportConfigurationError("'%s' class lacks necessary constructor", sensorType.getClazz().getSimpleName());
            return false;
            }

        // Check the user-visible form of the sensor name
        if (!isLegalSensorName(sensorType.getName()))
            {
            reportConfigurationError("\"%s\" is not a legal sensor name", sensorType.getName());
            return false;
            }
        if (existingTypeDisplayNames.contains(sensorType.getName()))
            {
            reportConfigurationError("the sensor \"%s\" is already defined", sensorType.getName());
            return false;
            }

        // Check the XML tag
        if (!isLegalXmlTag(sensorType.getXmlTag()))
            {
            reportConfigurationError("\"%s\" is not a legal XML tag for the sensor \"%s\"", sensorType.getXmlTag(), sensorType.getName());
            return false;
            }
        if (existingXmlTags.contains(sensorType.getXmlTag()))
            {
            reportConfigurationError("the XML tag \"%s\" is already defined", sensorType.getXmlTag());
            return false;
            }

        return true;
        }

    protected boolean isLegalMotorTypeName(String name)
        {
        if (!isGoodString(name))
            return false;

        return true;
        }

    protected boolean isLegalSensorName(String name)
        {
        if (!isGoodString(name))
            return false;

        return true;
        }

    //----------------------------------------------------------------------------------------------
    // Utility
    //----------------------------------------------------------------------------------------------

    protected  boolean isGoodString(String string)
        {
        if (string == null)
            return false;
        if (!string.trim().equals(string))
            return false;
        if (string.length()==0)
            return false;

        return true;
        }

    protected void reportConfigurationError(String format, Object... args)
        {
        String message = String.format(format, args);
        RobotLog.ee(TAG, String.format("configuration error: %s", message));
        RobotLog.setGlobalErrorMsg(message);
        }

    protected boolean isHardwareDevice(Class clazz)
        {
        return ClassUtil.inheritsFrom(clazz, HardwareDevice.class);
        }

    protected boolean isLegalXmlTag(String xmlTag)
        {
        if (!isGoodString(xmlTag))
            return false;

        // For simplicity, we only allow a restricted subset of what XML allows
        //  https://www.w3.org/TR/REC-xml/#NT-NameStartChar
        String nameStartChar = "\\p{Alpha}_:";
        String nameChar      = nameStartChar + "0-9\\-\\.";

        if (!xmlTag.matches("^["+ nameStartChar +"]["+ nameChar +"]*$"))
            return false;

        return true;
        }

    public static String getXmlTag(I2cSensor i2cSensor)
        {
        return ClassUtil.decodeStringRes(i2cSensor.xmlTag().trim());
        }
    public static String getXmlTag(MotorType motorType)
        {
        return ClassUtil.decodeStringRes(motorType.xmlTag().trim());
        }
    }
