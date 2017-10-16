/*
 * Copyright (c) 2014, 2015 Qualcomm Technologies Inc
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * (subject to the limitations in the disclaimer below) provided that the following conditions are
 * met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions
 * and the following disclaimer in the documentation and/or other materials provided with the
 * distribution.
 *
 * Neither the name of Qualcomm Technologies Inc nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS LICENSE. THIS
 * SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
 * THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.qualcomm.hardware;

import android.content.Context;
import android.support.annotation.NonNull;

import com.qualcomm.hardware.adafruit.AdafruitI2cColorSensor;
import com.qualcomm.hardware.hitechnic.HiTechnicNxtAccelerationSensor;
import com.qualcomm.hardware.hitechnic.HiTechnicNxtColorSensor;
import com.qualcomm.hardware.hitechnic.HiTechnicNxtCompassSensor;
import com.qualcomm.hardware.hitechnic.HiTechnicNxtDcMotorController;
import com.qualcomm.hardware.hitechnic.HiTechnicNxtGyroSensor;
import com.qualcomm.hardware.hitechnic.HiTechnicNxtIrSeekerSensor;
import com.qualcomm.hardware.hitechnic.HiTechnicNxtLightSensor;
import com.qualcomm.hardware.hitechnic.HiTechnicNxtServoController;
import com.qualcomm.hardware.hitechnic.HiTechnicNxtTouchSensor;
import com.qualcomm.hardware.hitechnic.HiTechnicNxtTouchSensorMultiplexer;
import com.qualcomm.hardware.hitechnic.HiTechnicNxtUltrasonicSensor;
import com.qualcomm.hardware.lynx.LynxI2cColorRangeSensor;
import com.qualcomm.hardware.lynx.LynxI2cDeviceSynch;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.lynx.LynxUsbDevice;
import com.qualcomm.hardware.lynx.LynxUsbDeviceImpl;
import com.qualcomm.hardware.lynx.LynxUsbUtil;
import com.qualcomm.hardware.lynx.commands.core.LynxFirmwareVersionManager;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsAnalogOpticalDistanceSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsTouchSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cIrSeekerSensorV3;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsUsbDcMotorController;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsUsbDevice;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsUsbDeviceInterfaceModule;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsUsbLegacyModule;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsUsbServoController;
import com.qualcomm.hardware.modernrobotics.comm.ModernRoboticsUsbUtil;
import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.AccelerationSensor;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.AnalogInputController;
import com.qualcomm.robotcore.hardware.AnalogOutput;
import com.qualcomm.robotcore.hardware.AnalogOutputController;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.CRServoImpl;
import com.qualcomm.robotcore.hardware.CRServoImplEx;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.CompassSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorImpl;
import com.qualcomm.robotcore.hardware.DcMotorImplEx;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.DeviceManager;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DigitalChannelController;
import com.qualcomm.robotcore.hardware.DigitalChannelImpl;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceImpl;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImplOnSimple;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchSimple;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;
import com.qualcomm.robotcore.hardware.LED;
import com.qualcomm.robotcore.hardware.LegacyModule;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.PWMOutput;
import com.qualcomm.robotcore.hardware.PWMOutputController;
import com.qualcomm.robotcore.hardware.PWMOutputImpl;
import com.qualcomm.robotcore.hardware.PWMOutputImplEx;
import com.qualcomm.robotcore.hardware.RobotCoreLynxModule;
import com.qualcomm.robotcore.hardware.RobotCoreLynxUsbDevice;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.hardware.ServoImpl;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.TouchSensorMultiplexer;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import com.qualcomm.robotcore.hardware.configuration.LynxConstants;
import com.qualcomm.robotcore.hardware.configuration.MotorConfigurationType;
import com.qualcomm.robotcore.hardware.configuration.UserI2cSensorType;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDeviceImplBase;
import com.qualcomm.robotcore.hardware.usb.RobotUsbManager;
import com.qualcomm.robotcore.hardware.usb.RobotUsbManagerCombining;
import com.qualcomm.robotcore.hardware.usb.ftdi.RobotUsbManagerFtdi;
import com.qualcomm.robotcore.hardware.usb.serial.RobotUsbManagerTty;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;
import com.qualcomm.robotcore.util.ThreadPool;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.internal.system.Assert;
import org.firstinspires.ftc.robotcore.internal.usb.exception.RobotUsbException;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Scan for, and create instances of, Modern Robotics USB devices
 */
@SuppressWarnings("WeakerAccess")
public class HardwareDeviceManager implements DeviceManager {

  //------------------------------------------------------------------------------------------------
  // State
  //------------------------------------------------------------------------------------------------

  public static final String TAG = "HardwareDeviceManager";
  public static final String TAG_USB_SCAN = "USBScan";

  public  final static Object     scanDevicesLock = new Object();

  private       RobotUsbManager   usbManager;
  private final EventLoopManager  manager;
  private final Context           context;

  //------------------------------------------------------------------------------------------------
  // Construction
  //------------------------------------------------------------------------------------------------

  /**
   * ModernRoboticsUsbDeviceManager constructor
   * @param context Context of current Android app
   * @param manager event loop manager
   * @throws RobotCoreException if unable to open FTDI D2XX manager
   */
   @SuppressWarnings("ConstantConditions")
   public HardwareDeviceManager(Context context, EventLoopManager manager) throws RobotCoreException {
    this.context = context;
    this.manager = manager;
    this.usbManager = createUsbManager(context);
  }

  public static RobotUsbManager createUsbManager(Context context) throws RobotCoreException {
    RobotUsbManager usbManager = new RobotUsbManagerFtdi(context);
    if (LynxConstants.isRevControlHub()) {
        RobotUsbManagerCombining combiner = new RobotUsbManagerCombining();
        combiner.addManager(usbManager);
        combiner.addManager(new RobotUsbManagerTty(context));
        usbManager = combiner;
    }
    return usbManager;
  }

  //------------------------------------------------------------------------------------------------
  // Scanning
  //------------------------------------------------------------------------------------------------

  /* (non-Javadoc)
   * @see com.qualcomm.hardware.DeviceManager#scanForUsbDevices()
   *
   * Returns a map from serial number to DeviceType
   */
  @Override
  public Map<SerialNumber, DeviceType> scanForUsbDevices() throws RobotCoreException {
    synchronized (scanDevicesLock) {
      long start = System.nanoTime();
      final Map<SerialNumber, DeviceType> deviceMap = new ConcurrentHashMap<SerialNumber, DeviceType>();
      int devCount = usbManager.scanForDevices();

      RobotLog.vv(TAG_USB_SCAN, "device count=%d", devCount);
      if (devCount > 0) {
        // Open all the USB devices attached to the robot controller. We do this in parallel so as to minimize latency to the user.
        ExecutorService executorService = ThreadPool.newFixedThreadPool(devCount, "hw mgr usb scan");
        final ConcurrentHashMap<SerialNumber, RobotUsbDevice> newlyFoundDevices = new ConcurrentHashMap<SerialNumber, RobotUsbDevice>();
        try {
          for (int id = 0; id < devCount; id++) {
            final SerialNumber serialNumber = usbManager.getDeviceSerialNumberByIndex(id);
            executorService.execute(new Runnable() {
              @Override public void run() {
                  try {
                    RobotLog.vv(TAG_USB_SCAN, "opening %s...", serialNumber);
                    //
                    // It turns out that ModernRoboticsUsbUtil.openUsbDevice doesn't contain any
                    // logic that is specific to ModernRobotics, but rather is generic, and so
                    // can be used even on Lynx devices
                    RobotUsbDevice device = ModernRoboticsUsbUtil.openUsbDevice(false, usbManager, serialNumber);
                    newlyFoundDevices.put(serialNumber, device);
                    //
                  } catch (Exception e) {
                    RobotLog.vv(TAG_USB_SCAN, "%s(%s) exception while opening %s", e.getClass().getSimpleName(), e.getMessage(), serialNumber.toString());
                  } finally {
                    RobotLog.vv(TAG_USB_SCAN, "... done opening %s", serialNumber);
                  }

                }
              }
            );
          }
          // Wait for all those opens to finish
          executorService.shutdown();
          ThreadPool.awaitTerminationOrExitApplication(executorService, 30, TimeUnit.SECONDS, "USB Scanning Service", "internal error");

          // Having opened everything, determine the type of each
          for (Map.Entry<SerialNumber,RobotUsbDevice> pair : newlyFoundDevices.entrySet()) {
            determineDeviceType(pair.getValue(), pair.getKey(), deviceMap);
          }

          // Also consider devices that are already open
          for (RobotUsbDevice existingDevice : RobotUsbDeviceImplBase.getExtantDevices()) {
            SerialNumber serialNumber = existingDevice.getSerialNumber();
            if (!newlyFoundDevices.containsKey(serialNumber)) {
              DeviceType deviceType = existingDevice.getDeviceType();
              if (deviceType != DeviceType.FTDI_USB_UNKNOWN_DEVICE) {
                RobotLog.vv(TAG_USB_SCAN, "added extant device %s type=%s", serialNumber.toString(), deviceType.toString());
                deviceMap.put(serialNumber, deviceType);
              }
            }
          }

        } finally {
          // On the way out, be sure to close all.
          for (Map.Entry<SerialNumber,RobotUsbDevice> pair : newlyFoundDevices.entrySet()) {
            RobotLog.vv(TAG_USB_SCAN, "closing %s", pair.getKey());
            pair.getValue().close();
          }
        }
      }

      long end = System.nanoTime();
      RobotLog.vv(TAG_USB_SCAN, "scanForUsbDevices() took %dms count=%d", (int)((end-start) / ElapsedTime.MILLIS_IN_NANO), deviceMap.size());
      return deviceMap;
    }
  }

  void determineDeviceType(RobotUsbDevice dev, SerialNumber serialNumber, Map<SerialNumber, DeviceType> deviceMap) {

    // Have we seen this guy before? A given serial number won't change its device type, so
    // we can use use a cached value if we have it
    DeviceType deviceType = RobotUsbDeviceImplBase.getDeviceType(serialNumber);

    // If not, then open the indicated device by serial number in order to determine its device type
    if (deviceType == DeviceType.UNKNOWN_DEVICE) {
      RobotUsbDevice.USBIdentifiers ids = dev.getUsbIdentifiers();
      if (ids.isModernRoboticsDevice()) {
        try {
          RobotLog.vv(TAG_USB_SCAN, "getting MR device device header %s ...", serialNumber);
          deviceType = getModernRoboticsDeviceType(dev);
          RobotLog.vv(TAG_USB_SCAN, "... done getting MR device device header %s type=%s", serialNumber, deviceType);
        } catch (RobotCoreException ignored) {
          RobotLog.vv(TAG_USB_SCAN, "exception retrieving MR device device header %s", serialNumber);
          return;
        }
      } else if (ids.isLynxDevice()) {
        RobotLog.vv(TAG_USB_SCAN, "%s is a lynx device", serialNumber);
        deviceType = getLynxDeviceType(dev);
      } else {
        // we can't figure this guy out; ignore
        return;
      }
    }

    // Record the type in the device map
    deviceMap.put(serialNumber, deviceType);
  }

  DeviceType getLynxDeviceType(RobotUsbDevice dev) {
    DeviceType deviceType = DeviceType.LYNX_USB_DEVICE;
    dev.setDeviceType(deviceType);
    return deviceType;
  }

  DeviceType getModernRoboticsDeviceType(RobotUsbDevice dev) throws RobotCoreException {
    byte[] modernRoboticsDeviceHeader = getModernRoboticsDeviceHeader(dev);
    return getModernRoboticsDeviceType(dev, modernRoboticsDeviceHeader);
  }

  DeviceType getModernRoboticsDeviceType(RobotUsbDevice dev, byte[] modernRoboticsDeviceHeader) throws RobotCoreException {
    DeviceType deviceType = ModernRoboticsUsbUtil.getDeviceType(modernRoboticsDeviceHeader);
    // Record the device type so we can retreive it later w/o needing to open USB again
    dev.setDeviceType(deviceType);
    return deviceType;
  }

  byte[] getModernRoboticsDeviceHeader(RobotUsbDevice dev) throws RobotCoreException {
    try {
      return ModernRoboticsUsbUtil.getUsbDeviceHeader(dev);
    } catch (RobotUsbException e) {
      throw RobotCoreException.createChained(e, "comm error retrieving MR device header(%s)", e.getStackTrace()[0]);
    }
  }

  //------------------------------------------------------------------------------------------------
  // Creation
  //------------------------------------------------------------------------------------------------

  /**
   * Note: unlike other creation methods, creating a Lynx USB device will succeed even if
   * the device is already open (in which case it will return a new delegate to the existing
   * instance).
   */
  @Override public RobotCoreLynxUsbDevice createLynxUsbDevice(final SerialNumber serialNumber, String name) throws RobotCoreException, InterruptedException {
    HardwareFactory.noteSerialNumberType(context, serialNumber, context.getString(R.string.moduleDisplayNameLynxUsbDevice));
    RobotLog.v("Creating %s", HardwareFactory.getDeviceDisplayName(context, serialNumber));

    ModernRoboticsUsbDevice.OpenRobotUsbDevice openRobotUsbDevice = new ModernRoboticsUsbDevice.OpenRobotUsbDevice() {
      @Override public RobotUsbDevice open() throws RobotCoreException, InterruptedException {
          RobotUsbDevice dev = null;
          try {
            dev = LynxUsbUtil.openUsbDevice(usbManager, serialNumber);
            if (!dev.getUsbIdentifiers().isLynxDevice()) {
              closeAndThrowOnFailedDeviceTypeCheck(dev, serialNumber);
            }
            DeviceType type = getLynxDeviceType(dev); Assert.assertTrue(type == DeviceType.LYNX_USB_DEVICE);
          } catch (RobotCoreException|RuntimeException e) {
            if (dev != null) dev.close(); // avoid leakage of open FT_Devices
            throw e;
          }
          return dev;
        }
    };

    return LynxUsbDeviceImpl.findOrCreateAndArm(context, serialNumber, manager, openRobotUsbDevice);
  }


  /* (non-Javadoc)
   * @see com.qualcomm.hardware.DeviceManager#createUsbDcMotorController(com.qualcomm.robotcore.util.SerialNumber)
   */
  @Override
  public DcMotorController createUsbDcMotorController(final SerialNumber serialNumber, String name)
      throws RobotCoreException, InterruptedException {
    HardwareFactory.noteSerialNumberType(context, serialNumber, context.getString(R.string.moduleDisplayNameMotorController));
    RobotLog.v("Creating %s", HardwareFactory.getDeviceDisplayName(context, serialNumber));

    ModernRoboticsUsbDevice.OpenRobotUsbDevice openRobotUsbDevice = new ModernRoboticsUsbDevice.OpenRobotUsbDevice() {
      @Override public RobotUsbDevice open() throws RobotCoreException, InterruptedException {
          RobotUsbDevice dev = null;
          try {
            dev = ModernRoboticsUsbUtil.openUsbDevice(true, usbManager, serialNumber);
            byte[] deviceHeader = getModernRoboticsDeviceHeader(dev);
            DeviceType type = getModernRoboticsDeviceType(dev, deviceHeader);

            if (type != DeviceType.MODERN_ROBOTICS_USB_DC_MOTOR_CONTROLLER) {
              closeAndThrowOnFailedDeviceTypeCheck(dev, serialNumber);
            }
            dev.setFirmwareVersion(getModernRoboticsFirmwareVersion(deviceHeader));
          } catch (RobotCoreException|RuntimeException e) {
            if (dev != null) dev.close(); // avoid leakage of open FT_Devices
            throw e;
          }
          return dev;
        }
    };

    ModernRoboticsUsbDcMotorController controller = new ModernRoboticsUsbDcMotorController(context, serialNumber, openRobotUsbDevice, manager);
    controller.armOrPretend();
    controller.initializeHardware();
    return controller;
  }

  @Override
  public DcMotor createDcMotor(DcMotorController controller, int portNumber, @NonNull MotorConfigurationType motorType, String name) {
    return new DcMotorImpl(controller, portNumber, DcMotor.Direction.FORWARD, motorType);
  }
  @Override
  public DcMotor createDcMotorEx(DcMotorController controller, int portNumber, @NonNull MotorConfigurationType motorType, String name) {
    return new DcMotorImplEx(controller, portNumber, DcMotor.Direction.FORWARD, motorType);
  }


  /* (non-Javadoc)
   * @see com.qualcomm.hardware.DeviceManager#createUsbServoController(com.qualcomm.robotcore.util.SerialNumber)
   */
  @Override
  public ServoController createUsbServoController(final SerialNumber serialNumber, String name)
      throws RobotCoreException, InterruptedException {
    HardwareFactory.noteSerialNumberType(context, serialNumber, context.getString(R.string.moduleDisplayNameServoController));
    RobotLog.v("Creating %s", HardwareFactory.getDeviceDisplayName(context, serialNumber));

    ModernRoboticsUsbDevice.OpenRobotUsbDevice openRobotUsbDevice = new ModernRoboticsUsbDevice.OpenRobotUsbDevice() {
      @Override public RobotUsbDevice open() throws RobotCoreException, InterruptedException {
        RobotUsbDevice dev = null;
        try {
          dev = ModernRoboticsUsbUtil.openUsbDevice(true, usbManager, serialNumber);
          byte[] deviceHeader = getModernRoboticsDeviceHeader(dev);
          DeviceType type = getModernRoboticsDeviceType(dev, deviceHeader);

          if (type != DeviceType.MODERN_ROBOTICS_USB_SERVO_CONTROLLER) {
            closeAndThrowOnFailedDeviceTypeCheck(dev, serialNumber);
          }
          dev.setFirmwareVersion(getModernRoboticsFirmwareVersion(deviceHeader));
        } catch (RobotCoreException e) {
          if (dev != null) dev.close(); // avoid leakage of open FT_Devices
          throw e;
        } catch (RuntimeException e) {
          if (dev != null) dev.close(); // avoid leakage of open FT_Devices
          throw e;
        }
        return dev;
      }
    };

    ModernRoboticsUsbServoController controller = new ModernRoboticsUsbServoController(context, serialNumber, openRobotUsbDevice, manager);
    controller.armOrPretend();
    controller.initializeHardware();
    return controller;
  }

  @Override
  public Servo createServo(ServoController controller, int portNumber, String name) {
    return new ServoImpl(controller, portNumber, Servo.Direction.FORWARD);
  }

  @Override
  public CRServo createCRServo(ServoController controller, int portNumber, String name) {
    return new CRServoImpl(controller, portNumber, DcMotor.Direction.FORWARD);
  }

  @Override
  public Servo createServoEx(ServoController controller, int portNumber, String name) {
    return new ServoImplEx(controller, portNumber, Servo.Direction.FORWARD);
  }

  @Override
  public CRServo createCRServoEx(ServoController controller, int portNumber, String name) {
    return new CRServoImplEx(controller, portNumber, DcMotor.Direction.FORWARD);
  }

  @Override
  public DeviceInterfaceModule createDeviceInterfaceModule(final SerialNumber serialNumber, String name)
    throws RobotCoreException, InterruptedException {
    HardwareFactory.noteSerialNumberType(context, serialNumber, context.getString(R.string.moduleDisplayNameCDIM));
    RobotLog.v("Creating %s", HardwareFactory.getDeviceDisplayName(context, serialNumber));

    ModernRoboticsUsbDevice.OpenRobotUsbDevice openRobotUsbDevice = new ModernRoboticsUsbDevice.OpenRobotUsbDevice() {
      @Override public RobotUsbDevice open() throws RobotCoreException, InterruptedException {
        RobotUsbDevice dev = null;
        try {
          dev = ModernRoboticsUsbUtil.openUsbDevice(true, usbManager, serialNumber);
          byte[] deviceHeader = getModernRoboticsDeviceHeader(dev);
          DeviceType type = getModernRoboticsDeviceType(dev, deviceHeader);

          if (type != DeviceType.MODERN_ROBOTICS_USB_DEVICE_INTERFACE_MODULE) {
            closeAndThrowOnFailedDeviceTypeCheck(dev, serialNumber);
          }
          dev.setFirmwareVersion(getModernRoboticsFirmwareVersion(deviceHeader));
        } catch (RobotCoreException e) {
          if (dev != null) dev.close(); // avoid leakage of open FT_Devices
          throw e;
        } catch (RuntimeException e) {
          if (dev != null) dev.close(); // avoid leakage of open FT_Devices
          throw e;
        }
        return dev;
      }
    };
    ModernRoboticsUsbDeviceInterfaceModule deviceInterfaceModule = new ModernRoboticsUsbDeviceInterfaceModule(context, serialNumber, openRobotUsbDevice, manager);
    deviceInterfaceModule.armOrPretend();
    deviceInterfaceModule.initializeHardware();
    return deviceInterfaceModule;
  }

  /* (non-Javadoc)
   * @see com.qualcomm.hardware.DeviceManager#createUsbLegacyModule(com.qualcomm.robotcore.util.SerialNumber)
   */
  @Override
  public LegacyModule createUsbLegacyModule(final SerialNumber serialNumber, String name)
      throws RobotCoreException, InterruptedException {
    HardwareFactory.noteSerialNumberType(context, serialNumber, context.getString(R.string.moduleDisplayNameLegacyModule));
    RobotLog.v("Creating %s", HardwareFactory.getDeviceDisplayName(context, serialNumber));

    ModernRoboticsUsbDevice.OpenRobotUsbDevice openRobotUsbDevice = new ModernRoboticsUsbDevice.OpenRobotUsbDevice() {
      @Override public RobotUsbDevice open() throws RobotCoreException, InterruptedException {
        RobotUsbDevice dev = null;
        try {
          dev = ModernRoboticsUsbUtil.openUsbDevice(true, usbManager, serialNumber);
          byte[] deviceHeader = getModernRoboticsDeviceHeader(dev);
          DeviceType type = getModernRoboticsDeviceType(dev, deviceHeader);

          if (type != DeviceType.MODERN_ROBOTICS_USB_LEGACY_MODULE) {
            closeAndThrowOnFailedDeviceTypeCheck(dev, serialNumber);
          }
          dev.setFirmwareVersion(getModernRoboticsFirmwareVersion(deviceHeader));
        } catch (RobotCoreException e) {
          if (dev != null) dev.close(); // avoid leakage of open FT_Devices
          throw e;
        } catch (RuntimeException e) {
          if (dev != null) dev.close(); // avoid leakage of open FT_Devices
          throw e;
        }
        return dev;
      }
    };
    ModernRoboticsUsbLegacyModule legacyModule = new ModernRoboticsUsbLegacyModule(context, serialNumber, openRobotUsbDevice, manager);
    legacyModule.armOrPretend();
    legacyModule.initializeHardware();
    return legacyModule;
  }

  /* (non-Javadoc)
   * @see com.qualcomm.hardware.DeviceManager#createNxtDcMotorController(com.qualcomm.robotcore.hardware.LegacyModule, int)
   */
  @Override
  public DcMotorController createHTDcMotorController(LegacyModule legacyModule, int physicalPort, String name) {
    RobotLog.v("Creating HiTechnic NXT DC Motor Controller - Port: " + physicalPort);
    return new HiTechnicNxtDcMotorController(context, legacyModule, physicalPort);
  }

  @Override
  public RobotCoreLynxModule createLynxModule(RobotCoreLynxUsbDevice lynxUsbDevice, int moduleAddress, boolean isParent, String name) {
    RobotLog.v("Creating Lynx Module - mod=%d parent=%s", moduleAddress, Boolean.toString(isParent));
    return new LynxModule((LynxUsbDevice)lynxUsbDevice, moduleAddress, isParent);
  }

/* (non-Javadoc)
 * @see com.qualcomm.hardware.DeviceManager#createNxtServoController(com.qualcomm.robotcore.hardware.LegacyModule, int)
 */
  @Override
  public ServoController createHTServoController(LegacyModule legacyModule, int physicalPort, String name) {
    RobotLog.v("Creating HiTechnic NXT Servo Controller - Port: " + physicalPort);
    return new HiTechnicNxtServoController(context, legacyModule, physicalPort);
  }

  /* (non-Javadoc)
   * @see com.qualcomm.hardware.DeviceManager#createNxtCompassSensor(com.qualcomm.robotcore.hardware.LegacyModule, int)
   */
  @Override
  public CompassSensor createHTCompassSensor(LegacyModule legacyModule, int physicalPort, String name) {
    RobotLog.v("Creating HiTechnic NXT Compass Sensor - Port: " + physicalPort);
    return new HiTechnicNxtCompassSensor(promote(legacyModule), physicalPort);
  }

  /* (non-Javadoc)
   * @see com.qualcomm.hardware.DeviceManager#createDigitalTouchSensor(com.qualcomm.robotcore.hardware.LegacyModule, int)
   */
  @Override
  public TouchSensor createMRDigitalTouchSensor(DigitalChannelController digitalChannelController, int physicalPort, String name) {
    RobotLog.v("Creating Modern Robotics digital Touch Sensor - Port: " + physicalPort);
    return new ModernRoboticsTouchSensor(digitalChannelController, physicalPort);
  }

  @Override
  public TouchSensor createMRAnalogTouchSensor(AnalogInputController analogInputController, int physicalPort, String name) {
    RobotLog.v("Creating Modern Robotics analog Touch Sensor - Port: " + physicalPort);
    return new ModernRoboticsTouchSensor(analogInputController, physicalPort);
  }

/* (non-Javadoc)
   * @see com.qualcomm.hardware.DeviceManager#createNxtAccelerationSensor(com.qualcomm.robotcore.hardware.LegacyModule, int)
   */
  @Override
  public AccelerationSensor createHTAccelerationSensor(LegacyModule legacyModule, int physicalPort, String name) {
    RobotLog.v("Creating HiTechnic NXT Acceleration Sensor - Port: " + physicalPort);
    return new HiTechnicNxtAccelerationSensor(promote(legacyModule), physicalPort);
  }

  /* (non-Javadoc)
   * @see com.qualcomm.hardware.DeviceManager#createNxtLightSensor(com.qualcomm.robotcore.hardware.LegacyModule, int)
   */
  @Override
  public LightSensor createHTLightSensor(LegacyModule legacyModule, int physicalPort, String name) {
    RobotLog.v("Creating HiTechnic NXT Light Sensor - Port: " + physicalPort);
    return new HiTechnicNxtLightSensor(promote(legacyModule), physicalPort);
  }

  /* (non-Javadoc)
   * @see com.qualcomm.hardware.DeviceManager#createNxtLightSensor(com.qualcomm.robotcore.hardware.LegacyModule, int)
   */
  @Override
  public GyroSensor createHTGyroSensor(LegacyModule legacyModule, int physicalPort, String name) {
    RobotLog.v("Creating HiTechnic NXT Gyro Sensor - Port: " + physicalPort);
    return new HiTechnicNxtGyroSensor(promote(legacyModule), physicalPort);
  }

  /* (non-Javadoc)
   * @see com.qualcomm.hardware.DeviceManager#createNxtIrSeekerSensor(com.qualcomm.robotcore.hardware.LegacyModule, int)
   */
  @Override
  public IrSeekerSensor createHTIrSeekerSensor(LegacyModule legacyModule, int physicalPort, String name) {
    RobotLog.v("Creating HiTechnic NXT IR Seeker Sensor - Port: " + physicalPort);
    return new HiTechnicNxtIrSeekerSensor(promote(legacyModule), physicalPort);
  }

  @Override
  public IrSeekerSensor createMRI2cIrSeekerSensorV3(I2cController i2cController, DeviceConfiguration.I2cChannel channel, String name) {
    RobotLog.v("Creating Modern Robotics I2C IR Seeker Sensor V3 - " + channel);
    return new ModernRoboticsI2cIrSeekerSensorV3(createI2cDeviceSynch(i2cController, channel.channel, name));
  }

  @Override
  public IrSeekerSensor createMRI2cIrSeekerSensorV3(RobotCoreLynxModule lynxModule, DeviceConfiguration.I2cChannel channel, String name) {
    RobotLog.v("Creating Modern Robotics I2C IR Seeker Sensor V3 - mod=%d bus=%d", lynxModule.getModuleAddress(), channel.channel);
    return new ModernRoboticsI2cIrSeekerSensorV3(createI2cDeviceSynch(lynxModule, channel, name));
  }

  @Override
  public UltrasonicSensor createNxtUltrasonicSensor(LegacyModule legacyModule, int physicalPort, String name) {
    RobotLog.v("Creating HiTechnic NXT Ultrasonic Sensor - Port: " + physicalPort);
    return new HiTechnicNxtUltrasonicSensor(promote(legacyModule), physicalPort);
  }

  @Override
  public OpticalDistanceSensor createMRAnalogOpticalDistanceSensor(AnalogInputController analogInputController, int physicalPort, String name) {
    RobotLog.v("Creating Modern Robotics Analog Optical Distance Sensor - Port: " + physicalPort);
    return new ModernRoboticsAnalogOpticalDistanceSensor(analogInputController, physicalPort);
  }

  @Override
  public TouchSensor createNxtTouchSensor(LegacyModule legacyModule, int physicalPort, String name) {
    RobotLog.v("Creating HiTechnic NXT Touch Sensor - Port: " + physicalPort);
    return new HiTechnicNxtTouchSensor(promote(legacyModule), physicalPort);
  }

  @Override
  public TouchSensorMultiplexer createHTTouchSensorMultiplexer(LegacyModule legacyModule, int port, String name) {
    RobotLog.v("Creating HiTechnic NXT Touch Sensor Multiplexer - Port: " + port);
    return new HiTechnicNxtTouchSensorMultiplexer(promote(legacyModule), port);
  }

  @Override
  public AnalogInput createAnalogInputDevice(AnalogInputController controller, int channel, String name) {
    RobotLog.v("Creating Analog Input Device - Port: " + channel);
    return new AnalogInput(controller, channel);
  }

  @Override
  public AnalogOutput createAnalogOutputDevice(AnalogOutputController controller, int channel, String name) {
    RobotLog.v("Creating Analog Output Device - Port: " + channel);
    return new AnalogOutput(controller, channel);
  }

  @Override
  public DigitalChannel createDigitalChannelDevice(DigitalChannelController controller, int channel, String name) {
    RobotLog.v("Creating Digital Channel Device - Port: " + channel);
    return new DigitalChannelImpl(controller, channel);
  }

  @Override
  public PWMOutput createPwmOutputDevice(PWMOutputController controller, int channel, String name) {
    RobotLog.v("Creating PWM Output Device - Port: " + channel);
    return new PWMOutputImpl(controller, channel);
  }

  @Override
  public PWMOutput createPwmOutputDeviceEx(PWMOutputController controller, int channel, String name) {
    RobotLog.v("Creating PWM Output Device - Port: " + channel);
    return new PWMOutputImplEx(controller, channel);
  }

  @Override
  public I2cDevice createI2cDevice(I2cController controller, DeviceConfiguration.I2cChannel channel, String name) {
    RobotLog.v("Creating I2C Device - " + channel);
    return new I2cDeviceImpl(controller, channel.channel);
  }

  @Override
  public HardwareDevice createUserI2cDevice(I2cController controller, DeviceConfiguration.I2cChannel channel, UserI2cSensorType type, String name) {
    RobotLog.v("Creating user sensor %s - Channel: %d", type.getName(), channel.channel);
    try {
      return type.createInstance(controller, channel.channel);
    } catch (InvocationTargetException e) {
      RobotLog.v("Creating user sensor %s failed: ", type.getName());
      Exception eToLog = e.getTargetException()!=null && (e.getTargetException() instanceof Exception) ? ((Exception)e.getTargetException()) : e;
      RobotLog.logStacktrace(eToLog);
      return null;
    }
  }

  @Override
  public HardwareDevice createUserI2cDevice(final RobotCoreLynxModule lynxModule, final DeviceConfiguration.I2cChannel bus, final UserI2cSensorType type, final String name) {
    RobotLog.v("Creating user sensor %s - on Lynx module=%d bus=%d", type.getName(), lynxModule.getModuleAddress(), bus.channel);
    try {
      return type.createInstance(lynxModule,
              new Func<I2cDeviceSynchSimple>() {
                  @Override public I2cDeviceSynchSimple value() {
                    return createI2cDeviceSynchSimple(lynxModule, bus, name);
                  }},
              new Func<I2cDeviceSynch>() {
                  @Override public I2cDeviceSynch value() {
                    return createI2cDeviceSynch(lynxModule, bus, name);
                  }});
    } catch (InvocationTargetException e) {
      RobotLog.v("Creating user sensor %s failed: ", type.getName());
      Exception eToLog = e.getTargetException()!=null && (e.getTargetException() instanceof Exception) ? ((Exception)e.getTargetException()) : e;
      RobotLog.logStacktrace(eToLog);
      return null;
    }
  }

  @Override
  public ColorSensor createAdafruitI2cColorSensor(I2cController controller, DeviceConfiguration.I2cChannel channel, String name) {
    RobotLog.v("Creating Adafruit I2C Color Sensor - " + channel);
    return new AdafruitI2cColorSensor(createI2cDeviceSynchSimple(controller, channel.channel, name));
  }

  @Override
  public ColorSensor createAdafruitI2cColorSensor(RobotCoreLynxModule lynxModule, DeviceConfiguration.I2cChannel channel, String name) {
    RobotLog.v("Creating Adafruit Color Sensor (Lynx) - mod=%d bus=%d", lynxModule.getModuleAddress(), channel.channel);
    return new AdafruitI2cColorSensor(createI2cDeviceSynchSimple(lynxModule, channel, name));
  }

  @Override
  public ColorSensor createLynxColorRangeSensor(RobotCoreLynxModule lynxModule, DeviceConfiguration.I2cChannel channel, String name) {
    RobotLog.v("Creating Lynx Color/Range Sensor - mod=%d bus=%d", lynxModule.getModuleAddress(), channel.channel);
    return new LynxI2cColorRangeSensor(createI2cDeviceSynchSimple(lynxModule, channel, name));
  }

  @Override
  public ColorSensor createHTColorSensor(LegacyModule controller, int channel, String name) {
    RobotLog.v("Creating HiTechnic NXT Color Sensor - Port: " + channel);
    return new HiTechnicNxtColorSensor(createI2cDeviceSynch(controller, channel, name));
  }

  @Override
  public ColorSensor createModernRoboticsI2cColorSensor(I2cController controller, DeviceConfiguration.I2cChannel channel, String name) {
    RobotLog.v("Creating Modern Robotics I2C Color Sensor - " + channel);
    return new ModernRoboticsI2cColorSensor(createI2cDeviceSynch(controller, channel.channel, name));
  }

  @Override
  public ColorSensor createModernRoboticsI2cColorSensor(RobotCoreLynxModule lynxModule, DeviceConfiguration.I2cChannel channel, String name) {
    RobotLog.v("Creating Modern Robotics I2C Color Sensor - mod=%d bus=%d", lynxModule.getModuleAddress(), channel.channel);
    return new ModernRoboticsI2cColorSensor(createI2cDeviceSynch(lynxModule, channel, name));
  }

  @Override
  public GyroSensor createModernRoboticsI2cGyroSensor(I2cController controller, DeviceConfiguration.I2cChannel channel, String name) {
    RobotLog.v("Creating Modern Robotics I2C Gyro Sensor - " + channel);
    return new ModernRoboticsI2cGyro(createI2cDeviceSynch(controller, channel.channel, name));
  }

  @Override
  public GyroSensor createModernRoboticsI2cGyroSensor(RobotCoreLynxModule lynxModule, DeviceConfiguration.I2cChannel channel, String name) {
    RobotLog.v("Creating Modern Robotics I2C Gyro Sensor - mod=%d bus=%d", lynxModule.getModuleAddress(), channel.channel);
    return new ModernRoboticsI2cGyro(createI2cDeviceSynch(lynxModule, channel, name));
  }

  @Override
  public LED createLED(DigitalChannelController controller, int channel, String name) {
    RobotLog.v("Creating LED - Port: " + channel);
    return new LED(controller, channel);
  }

  @Override
  public I2cDeviceSynch createI2cDeviceSynch(RobotCoreLynxModule lynxModule, DeviceConfiguration.I2cChannel channel, String name) {
    RobotLog.v("Creating I2cDeviceSynch (Lynx) - mod=%d bus=%d", lynxModule.getModuleAddress(), channel.channel);
    I2cDeviceSynchSimple i2cDeviceSynchSimple = createI2cDeviceSynchSimple(lynxModule, channel, name);
    I2cDeviceSynch result = new I2cDeviceSynchImplOnSimple(i2cDeviceSynchSimple, true);
    return result;
  }

  //------------------------------------------------------------------------------------------------
  // Utility
  //------------------------------------------------------------------------------------------------

  protected I2cDeviceSynchSimple createI2cDeviceSynchSimple(I2cController controller, int channel, String name) {
    return createI2cDeviceSynch(controller, channel, name);
  }

  protected I2cDeviceSynchSimple createI2cDeviceSynchSimple(RobotCoreLynxModule lynxModule, DeviceConfiguration.I2cChannel channel, String name) {
    I2cDeviceSynchSimple result = LynxFirmwareVersionManager.createLynxI2cDeviceSynch(context, (LynxModule)lynxModule, channel.channel);
    result.setUserConfiguredName(name);
    return result;
  }

  protected I2cDeviceSynch createI2cDeviceSynch(I2cController controller, int channel, String name) {
    I2cDevice      i2cDevice      = new I2cDeviceImpl(controller, channel);
    I2cDeviceSynch i2cDeviceSynch = new I2cDeviceSynchImpl(i2cDevice, true);
    i2cDeviceSynch.setUserConfiguredName(name);
    return i2cDeviceSynch;
  }

  private RobotUsbDevice.FirmwareVersion getModernRoboticsFirmwareVersion(byte[] modernRoboticsDeviceHeader) {
    return new RobotUsbDevice.FirmwareVersion(modernRoboticsDeviceHeader[0]);
  }

  private ModernRoboticsUsbLegacyModule promote(LegacyModule module) {
    if (!(module instanceof ModernRoboticsUsbLegacyModule)) {
      throw new IllegalArgumentException("Modern Robotics Device Manager needs a Modern Robotics LegacyModule");
    }

    return (ModernRoboticsUsbLegacyModule) module;
  }

  private ModernRoboticsUsbDeviceInterfaceModule promote(DeviceInterfaceModule module) {
    if (!(module instanceof ModernRoboticsUsbDeviceInterfaceModule)) {
      throw new IllegalArgumentException("Modern Robotics Device Manager needs a Modern Robotics Device Interface Module");
    }

    return (ModernRoboticsUsbDeviceInterfaceModule) module;
  }

  private void closeAndThrowOnFailedDeviceTypeCheck(RobotUsbDevice dev, SerialNumber serialNumber) throws RobotCoreException {
    String msg = String.format("%s is returning garbage data on the USB bus", HardwareFactory.getDeviceDisplayName(context, serialNumber));
    dev.close();
    logAndThrow(msg);
  }

  private void logAndThrow(String errMsg) throws RobotCoreException {
    System.err.println(errMsg);
    throw new RobotCoreException(errMsg);
  }
}
