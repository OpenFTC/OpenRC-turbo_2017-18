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
package com.qualcomm.ftccommon.configuration;

import com.qualcomm.robotcore.hardware.DeviceManager;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;

import java.util.HashMap;
import java.util.Map;

/**
 * {@link ScannedDevices} is a simple distinguished kind of map of serial numbers
 * to device types. Simple serialization and deserialization logic is provided.
 */
public class ScannedDevices extends HashMap<SerialNumber, DeviceManager.DeviceType>
    {
    public ScannedDevices(Map<SerialNumber, DeviceManager.DeviceType> map)
        {
        super(map);
        }

    public ScannedDevices()
        {
        super();
        }

    private static final String pairSeparatorWrite = "|";
    private static final String pairSeparatorSplit = "\\|";  // '|' is a a regex metachar, so we have to escape
    private static final String keyValueSeparatorWrite = ",";
    private static final String keyValueSeparatorSplit = ",";

    public String toSerializationString()
        {
        StringBuilder result = new StringBuilder();
        for (Entry<SerialNumber, DeviceManager.DeviceType> entry : this.entrySet())
            {
            if (result.length() > 0) result.append(pairSeparatorWrite);
            result.append(entry.getKey().toString());
            result.append(keyValueSeparatorWrite);
            result.append(entry.getValue().toString());
            }
        return result.toString();
        }

    public static ScannedDevices fromSerializationString(String string)
        {
        ScannedDevices result = new ScannedDevices();
        //
        string = string.trim(); // paranoia
        if (string.length() > 0)
            {
            String[] pairs = string.split(pairSeparatorSplit);
            for (String pair : pairs)
                {
                String[] keyValue = pair.split(keyValueSeparatorSplit);
                SerialNumber             serialNumber = new SerialNumber(keyValue[0]);
                DeviceManager.DeviceType deviceType   = DeviceManager.DeviceType.valueOf(keyValue[1]);
                result.put(serialNumber, deviceType);
                }
            }
        //
        return result;
        }
    }
