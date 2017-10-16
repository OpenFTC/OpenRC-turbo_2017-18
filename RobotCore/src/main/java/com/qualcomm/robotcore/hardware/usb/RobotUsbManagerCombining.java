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
package com.qualcomm.robotcore.hardware.usb;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.util.SerialNumber;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@link RobotUsbManagerCombining} merges together result from zero or more other managers
 */
@SuppressWarnings("WeakerAccess")
public class RobotUsbManagerCombining implements RobotUsbManager
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    protected class ManagerInfo
        {
        public RobotUsbManager  manager;
        public int              scanCount;
        }

    protected List<ManagerInfo>                 managers;
    protected Map<SerialNumber, ManagerInfo>    enumeratedSerialNumbers;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public RobotUsbManagerCombining()
        {
        this.managers = new ArrayList<ManagerInfo>();
        this.enumeratedSerialNumbers = new ConcurrentHashMap<SerialNumber, ManagerInfo>();
        }

    public void addManager(RobotUsbManager manager)
        {
        ManagerInfo info = new ManagerInfo();
        info.manager = manager;
        info.scanCount = 0;
        this.managers.add(info);
        }

    //----------------------------------------------------------------------------------------------
    // RobotUsbManager
    //----------------------------------------------------------------------------------------------

    @Override public synchronized int scanForDevices() throws RobotCoreException
        {
        int result = 0;
        for (ManagerInfo info : managers)
            {
            info.scanCount = info.manager.scanForDevices();
            result += info.scanCount;
            }
        return result;
        }

    @Override public synchronized int getScanCount()
        {
        int result = 0;
        for (ManagerInfo info : managers)
            {
            result += info.scanCount;
            }
        return result;
        }

    @Override public synchronized SerialNumber getDeviceSerialNumberByIndex(final int initialIndex) throws RobotCoreException
        {
        int index = initialIndex;
        for (ManagerInfo info : managers)
            {
            if (index < info.scanCount)
                {
                // Remember who served up which serial numbers so we know who to ask to open again later
                SerialNumber serialNumber = info.manager.getDeviceSerialNumberByIndex(index);
                enumeratedSerialNumbers.put(serialNumber, info);
                return serialNumber;
                }
            index -= info.scanCount;
            }
        throw new IndexOutOfBoundsException("index too large: " + initialIndex);
        }

    @Override public String getDeviceDescriptionByIndex(final int initialIndex) throws RobotCoreException
        {
        int index = initialIndex;
        for (ManagerInfo info : managers)
            {
            if (index < info.scanCount)
                {
                return info.manager.getDeviceDescriptionByIndex(index);
                }
            index -= info.scanCount;
            }
        throw new IndexOutOfBoundsException("index too large: " + initialIndex);
        }

    @Override public synchronized RobotUsbDevice openBySerialNumber(SerialNumber serialNumber) throws RobotCoreException
        {
        ManagerInfo info = enumeratedSerialNumbers.get(serialNumber);
        if (info != null)
            {
            RobotUsbDevice device = info.manager.openBySerialNumber(serialNumber);
            if (device != null)
                return device;
            }
        throw new RobotCoreException("Combiner unable to open device with serialNumber = " + serialNumber.toString());
        }
    }
