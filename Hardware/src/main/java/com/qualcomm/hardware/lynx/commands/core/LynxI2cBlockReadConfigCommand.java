/*
Copyright (c) 2017 Craig MacFarlane

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Craig MacFarlane nor the names of his contributors may be used to
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
package com.qualcomm.hardware.lynx.commands.core;

import com.qualcomm.hardware.lynx.LynxModuleIntf;
import com.qualcomm.hardware.lynx.commands.LynxDatagram;
import com.qualcomm.hardware.lynx.commands.LynxInterfaceResponse;
import com.qualcomm.hardware.lynx.commands.LynxMessage;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.configuration.LynxConstants;

import java.nio.ByteBuffer;

public class LynxI2cBlockReadConfigCommand extends LynxDekaInterfaceCommand<LynxI2cWriteStatusQueryResponse>
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    public final static int cbPayload = 5;

    private byte i2cBus;
    private byte i2cAddr7Bit;
    private byte startAddr;
    private byte cbToRead;
    private byte readInterval;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public LynxI2cBlockReadConfigCommand(LynxModuleIntf module)
        {
        super(module);
        this.response = new LynxI2cWriteStatusQueryResponse(module);
        }

    public LynxI2cBlockReadConfigCommand(LynxModuleIntf module, int busZ, I2cAddr i2cAddr, int i2cStartAddr, int cbToRead, int readInterval)
        {
        this(module);
        LynxConstants.validateI2cBusZ(busZ);
        this.i2cBus = (byte)busZ;
        this.i2cAddr7Bit = (byte)i2cAddr.get7Bit();
        this.startAddr = (byte)i2cStartAddr;
        this.cbToRead = (byte)cbToRead;
        this.readInterval = (byte)readInterval;
        }

    //----------------------------------------------------------------------------------------------
    // Operations
    //----------------------------------------------------------------------------------------------

    public static Class<? extends LynxInterfaceResponse> getResponseClass()
        {
        return LynxI2cWriteStatusQueryResponse.class;
        }

    @Override
    public boolean isResponseExpected()
        {
        return true;
        }

    @Override
    public void onResponseReceived(LynxMessage generic)
        {
        super.onResponseReceived(generic);
        // TODO: process the status byte therein
        }

    @Override
    public byte[] toPayloadByteArray()
        {
        ByteBuffer buffer = ByteBuffer.allocate(cbPayload).order(LynxDatagram.LYNX_ENDIAN);
        buffer.put(this.i2cBus);
        buffer.put(this.i2cAddr7Bit);
        buffer.put(this.startAddr);
        buffer.put(this.cbToRead);
        buffer.put(this.readInterval);
        return buffer.array();
        }

    @Override
    public void fromPayloadByteArray(byte[] rgb)
        {
        ByteBuffer buffer = ByteBuffer.wrap(rgb).order(LynxDatagram.LYNX_ENDIAN);
        this.i2cBus = buffer.get();
        this.i2cAddr7Bit = buffer.get();
        this.startAddr = buffer.get();
        this.cbToRead = buffer.get();
        this.readInterval = buffer.get();
        }
    }
