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
package org.firstinspires.ftc.robotcore.internal.hardware;

import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DigitalChannelController;
import com.qualcomm.robotcore.util.LastKnown;
import com.qualcomm.robotcore.util.RobotLog;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * {@link DragonboardGPIOPin} controls an exported GPIO pin on the Dragonboard
 */
@SuppressWarnings("WeakerAccess")
public abstract class DragonboardGPIOPin implements DigitalChannel
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    protected abstract String getTag();

    public enum Active { LOW, HIGH }

    public static final String TAG = "DragonboardGPIOPin";

    protected final int             gpioPinNumber;
    protected final File            path;
    protected final Active          active;
    protected LastKnown<DigitalChannel.Mode> lastKnownMode;
    protected DigitalChannel.Mode   defaultMode;
    protected boolean               defaultStateIfOutput;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public DragonboardGPIOPin(int gpioPinNumber)
        {
        this(gpioPinNumber, Mode.INPUT, false, Active.HIGH);
        }

    public DragonboardGPIOPin(int gpioPinNumber, boolean initialState, Active active)
        {
        this(gpioPinNumber, Mode.OUTPUT, initialState, active);
        }

    private DragonboardGPIOPin(int gpioPinNumber, DigitalChannel.Mode defaultMode, boolean defaultStateIfOutput, Active active)
        {
        this.gpioPinNumber = gpioPinNumber;
        this.path = new File(String.format("/sys/class/gpio/gpio%d", gpioPinNumber + 902)); // +902 is magic from Dragonboard spec
        this.active = active;
        //
        this.lastKnownMode = new LastKnown<Mode>();
        this.lastKnownMode.invalidate();
        this.defaultMode = defaultMode;
        this.defaultStateIfOutput = defaultStateIfOutput;
        }

    //----------------------------------------------------------------------------------------------
    // Operations
    //----------------------------------------------------------------------------------------------

    /** If it's an input pin, then read the value directly from the device. Otherwise, return
     * what we last wrote */
    @Override public boolean getState()
        {
        return adjustActive(getRawState());
        }

    protected synchronized boolean getRawState()
        {
        // We can read 'value' even for output pins
        try {
            String zeroOrOne = readAspect("value");
            return Integer.parseInt(zeroOrOne) != 0;
            }
        catch (IOException e)
            {
            return false;
            }
        }

    @Override public synchronized void setState(boolean state)
        {
        if (getMode() == Mode.OUTPUT)
            {
            try {
                String zeroOrOne = adjustActive(state) ? "1" : "0";
                writeAspect("value", zeroOrOne);
                }
            catch (IOException e)
                {
                // ignored
                }
            }
        }

    @Override public synchronized Mode getMode()
        {
        Mode result = lastKnownMode.getValue();
        if (result == null)
            {
            result = getRawMode();
            lastKnownMode.setValue(result);
            }
        return result;
        }

    protected synchronized Mode getRawMode()
        {
        try {
            String direction = readAspect("direction");
            switch (direction)
                {
                case "out": return Mode.OUTPUT;
                case "in": default: return Mode.INPUT;
                }
            }
        catch (IOException e)
            {
            return Mode.INPUT;  // arbitrary
            }
        }

    @Override public synchronized void setMode(Mode mode)
        {
        try {
            String contents = mode==Mode.INPUT ? "in" : "out";
            writeAspect("direction", contents);
            lastKnownMode.setValue(mode);
            }
        catch (IOException e)
            {
            RobotLog.logExceptionHeader(TAG, e, "exception in setMode(); ignored");
            }
        }

    @Override @Deprecated public void setMode(DigitalChannelController.Mode mode)
        {
        setMode(mode.migrate());
        }

    //----------------------------------------------------------------------------------------------
    // HardwareDevice
    //----------------------------------------------------------------------------------------------

    @Override public Manufacturer getManufacturer()
        {
        return Manufacturer.Other;
        }

    @Override public String getDeviceName()
        {
        return "DB GPIO Pin";
        }

    @Override public String getConnectionInfo()
        {
        return String.format("GPIO pin#", gpioPinNumber);
        }

    @Override public int getVersion()
        {
        return 1;
        }

    @Override public synchronized void resetDeviceConfigurationForOpMode()
        {
        // Nothing to do
        }

    public void setDefaultState()
        {
        setMode(defaultMode);
        if (defaultMode == Mode.OUTPUT)
            {
            setState(defaultStateIfOutput);
            }
        }

    @Override public void close()
        {
        // Nothing to do
        }

    //----------------------------------------------------------------------------------------------
    // Utility
    //----------------------------------------------------------------------------------------------

    protected boolean adjustActive(boolean state)
        {
        //noinspection SimplifiableConditionalExpression
        return (active == Active.HIGH) ? state : !state;
        }

    protected String readAspect(String aspect) throws IOException
        {
        File aspectFile = new File(getPath(), aspect);
        try (BufferedReader reader = new BufferedReader(new FileReader(aspectFile)))
            {
            return reader.readLine();
            }
        }

    protected void writeAspect(String aspect, String value) throws IOException
        {
        File aspectFile = new File(getPath(), aspect);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(aspectFile)))
            {
            RobotLog.vv(getTag(), "writing aspect=%s value=%s", aspectFile.getAbsolutePath(), value);
            writer.write(value);
            }
        }

    protected File getPath()
        {
        return path;
        }
    }
