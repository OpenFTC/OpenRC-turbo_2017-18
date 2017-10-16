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
package com.qualcomm.robotcore.hardware.usb.serial;

import android.content.Context;

import com.qualcomm.robotcore.R;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.DeviceManager;
import com.qualcomm.robotcore.hardware.configuration.LynxConstants;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDeviceImplBase;
import com.qualcomm.robotcore.hardware.usb.RobotUsbManager;
import org.firstinspires.ftc.robotcore.internal.usb.exception.RobotUsbException;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;

import java.io.File;
import java.io.IOException;

/**
 * {@link RobotUsbManagerTty} is a RobotUsbManager that manages the (single) embedded 'USB' device
 * that lives on a serial port.
 */
public class RobotUsbManagerTty implements RobotUsbManager
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    public static final String TAG = "RobotUsbManagerTty";

    protected Context      context;
    protected SerialNumber serialNumberEmbedded;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public RobotUsbManagerTty(Context context)
        {
        this.context = context;
        serialNumberEmbedded = LynxConstants.SERIAL_NUMBER_EMBEDDED;
        }

    Object getLock()
        {
        return RobotUsbManagerTty.class;
        }

    //----------------------------------------------------------------------------------------------
    // RobotUsbManager
    //----------------------------------------------------------------------------------------------

    @Override public int scanForDevices() throws RobotCoreException
        {
        return 1;
        }

    @Override public int getScanCount()
        {
        return 1;
        }

    @Override public SerialNumber getDeviceSerialNumberByIndex(int index) throws RobotCoreException
        {
        switch (index)
            {
            case 0: return serialNumberEmbedded;
            default: throw  new IndexOutOfBoundsException("index too large: " + index);
            }
        }

    @Override public String getDeviceDescriptionByIndex(int index) throws RobotCoreException
        {
        switch (index)
            {
            case 0: return context.getString(R.string.descriptionLynxEmbeddedModule);
            default: throw  new IndexOutOfBoundsException("index too large: " + index);
            }
        }

    @Override
    public RobotUsbDevice openBySerialNumber(SerialNumber serialNumber) throws RobotCoreException
        {
        synchronized (getLock())
            {
            if (serialNumberEmbedded.equals(serialNumber))
                {
                if (!RobotUsbDeviceImplBase.isOpen(serialNumber))
                    {
                    File file = findSerialDevTty();
                    SerialPort serialPort = null;
                    try {
                        serialPort = new SerialPort(file, LynxConstants.SERIAL_MODULE_BAUD_RATE);
                        }
                    catch (IOException e)
                        {
                        throw RobotCoreException.createChained(e, "exception in %s.open(%s)", TAG, file.getPath());
                        }

                    RobotUsbDeviceTty deviceTTY = new RobotUsbDeviceTty(serialPort, serialNumberEmbedded, file);
                    deviceTTY.setFirmwareVersion(new RobotUsbDevice.FirmwareVersion(1,0));
                    deviceTTY.setDeviceType(DeviceManager.DeviceType.LYNX_USB_DEVICE);
                    deviceTTY.setUsbIdentifiers(RobotUsbDevice.USBIdentifiers.createLynxIdentifiers());
                    try { deviceTTY.setBaudRate(LynxConstants.SERIAL_MODULE_BAUD_RATE); } catch (RobotUsbException e) {/*ignored*/}
                    return deviceTTY;
                    }
                }
            return null;
            }
        }

    //----------------------------------------------------------------------------------------------
    // Utility
    //----------------------------------------------------------------------------------------------

    private static File findSerialDevTty()
        {
        // Older versions of Dragonboard software have the serial port named “/dev/ttyHS0”, while new
        // versions have the name “/dev/ttyHS4”. Try that guy explicitly, first.
        File result = new File("/dev/ttyHS4");
        if (result.exists())
            {
            RobotLog.vv(RobotUsbDeviceTty.TAG, "using serial tty=" + result.getAbsolutePath());
            return result;
            }

        // If we can't find that guy, that'd be odd, but let's just see who we *can* find
        // and hope for the best.
        for (int i = 0; i <= 255; i++) // per AOSP\kernel\Documentation\devicetree\bindings\tty\serial\msm_serial_hs.txt
            {
            String path = "/dev/ttyHS" + i;
            result = new File(path);
            if (result.exists())
                {
                RobotLog.vv(RobotUsbDeviceTty.TAG, "using serial tty=" + result.getAbsolutePath());
                return result;
                }
            }
        throw new RuntimeException("unable to locate Lynx serial /dev/ttyHSx");
        }

    }
