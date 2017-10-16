/*
Copyright (c) 2017 Robert Atkinson

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
package org.firstinspires.ftc.robotcore.internal.stellaris;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.Consumer;
import org.firstinspires.ftc.robotcore.internal.ui.ProgressParameters;
import org.firstinspires.ftc.robotcore.internal.usb.exception.RobotUsbException;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * {@link FlashLoaderManager} manages the process of writing a new firmware image.
 * to a Stellaris flash loader. The target system must be put into firmware update mode
 * by external means before using the functionality herein.
 */
@SuppressWarnings("WeakerAccess")
public class FlashLoaderManager
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    public static final String TAG = "FlashLoaderManager";
    public static boolean DEBUG = false;

    protected byte[]            firmwareImage;
    protected RobotUsbDevice    robotUsbDevice;
    protected int               retryCount = 4;
    protected int               msRetryPause = 40;
    protected int               msReadTimeout = 500;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public FlashLoaderManager(RobotUsbDevice robotUsbDevice, byte[] firmwareImage)
        {
        this.robotUsbDevice = robotUsbDevice;
        this.firmwareImage = firmwareImage;
        }

    //----------------------------------------------------------------------------------------------
    // Protocol
    //----------------------------------------------------------------------------------------------

    /**
     * Updates the firmware found on the USB device.
     *
     * @param fractionCompleteFeedback called periodically with the fraction completion
     *
     * @throws IOException
     * @throws InterruptedException
     * @throws TimeoutException
     * @throws FlashLoaderProtocolException
     */
    public void updateFirmware(Consumer<ProgressParameters> fractionCompleteFeedback) throws IOException, InterruptedException, TimeoutException, FlashLoaderProtocolException
        {
        doAutobaud();
        sendWithRetries(new FlashLoaderPingCommand());
        verifyStatus();

        sendWithRetries(new FlashLoaderDownloadCommand(0x0000, firmwareImage.length));
        verifyStatus();

        for (int ib = 0; ib < firmwareImage.length; ib += FlashLoaderSendDataCommand.QUANTUM)
            {
            fractionCompleteFeedback.accept(new ProgressParameters(ib, firmwareImage.length));
            //
            sendWithRetries(new FlashLoaderSendDataCommand(firmwareImage, ib));
            verifyStatus();
            }

        fractionCompleteFeedback.accept(new ProgressParameters(firmwareImage.length, firmwareImage.length));
        sendWithRetries(new FlashLoaderResetCommand());
        }

    //----------------------------------------------------------------------------------------------
    // Commands
    //----------------------------------------------------------------------------------------------

    protected void doAutobaud() throws IOException, InterruptedException, FlashLoaderProtocolException
        {
        for (int i = 0; i < retryCount; i++)
            {
            if (i > 0) Thread.sleep(msRetryPause);

            write(new byte[] { 0x55, 0x55 });
            if (readAckOrNack())
                {
                return;
                }
            }

        throw new FlashLoaderProtocolException(makeExceptionMessage("unable to execute autobaud"));
        }

    /** Sends a Get Status command and reads the response, returning the status code byte */
    protected byte readStatus() throws IOException, TimeoutException, FlashLoaderProtocolException, InterruptedException
        {
        for (int i = 0; i < retryCount; i++)
            {
            if (i > 0) Thread.sleep(msRetryPause);

            FlashLoaderGetStatusCommand command = new FlashLoaderGetStatusCommand();
            sendWithRetries(command);

            FlashLoaderGetStatusResponse response = new FlashLoaderGetStatusResponse();
            read(response.data);
            if (response.isChecksumValid())
                {
                // RobotLog.vv(TAG, "readStatus: xsum valid");
                sendAck();
                return response.data[FlashLoaderGetStatusResponse.IB_PAYLOAD];
                }

            RobotLog.vv(TAG, "readStatus: xsum invalid");
            sendNak();
            }
        throw new FlashLoaderProtocolException(makeExceptionMessage("unable to retrieve status"));
        }

    protected void verifyStatus() throws IOException, TimeoutException, FlashLoaderProtocolException, InterruptedException
        {
        byte status = readStatus();
        if (status != FlashLoaderGetStatusResponse.STATUS_SUCCESS)
            {
            throw new FlashLoaderProtocolException(makeExceptionMessage("invalid status: 0x%02x", status));
            }
        }

    //----------------------------------------------------------------------------------------------
    // Utility
    //----------------------------------------------------------------------------------------------

    /** Sends the command until it either gets an ack or exhausts its retry count, whereupon
     * it throws a protocol exception. */
    protected void sendWithRetries(FlashLoaderCommand command) throws IOException, TimeoutException, FlashLoaderProtocolException, InterruptedException
        {
        command.updateChecksum();

        for (int i = 0; i < retryCount; i++)
            {
            if (i > 0) Thread.sleep(msRetryPause);

            write(command.data);
            if (readAckOrNack())
                {
                return;
                }
            }

        throw new FlashLoaderProtocolException(makeExceptionMessage("unable to send command"), command);
        }

    protected void sendAck() throws IOException, InterruptedException
        {
        write(new byte[] { FlashLoaderDatagram.ACK });
        }

    protected void sendNak() throws IOException, InterruptedException
        {
        write(new byte[] { FlashLoaderDatagram.NAK });
        }

    protected boolean readAckOrNack()
        {
        try {
            return readAckOrNackOrException();
            }
        catch (TimeoutException|IOException e)
            {
            RobotLog.logExceptionHeader(TAG, e, "readAckOrNack exception");
            return false;
            }
        catch (InterruptedException e)
            {
            Thread.currentThread().interrupt();
            return false;
            }
        }

    protected boolean readAckOrNackOrException() throws IOException, TimeoutException, InterruptedException
        {
        while (true)
            {
            byte[] payload = new byte[1];
            read(payload);
            switch (payload[0])
                {
                case 0:                         continue;       // filler byte?
                case FlashLoaderDatagram.ACK:   return true;    // explicit ack
                case FlashLoaderDatagram.NAK:   return false;   // explicit nak
                default:
                    RobotLog.ee(TAG, "readAckOrNackOrException: unexpected: 0x%02x", payload[0]);
                    return false;   // unexpected byte: treat as nak
                }
            }
        }

    protected void write(byte[] data) throws IOException, InterruptedException
        {
        if (DEBUG) RobotLog.logBytes(TAG, "sent", data, data.length);
        try {
            robotUsbDevice.write(data);
            }
        catch (RobotUsbException e)
            {
            throw new IOException(makeExceptionMessage("unable to write to flash loader"), e);
            }
        }

    @SuppressLint("DefaultLocale")
    protected void read(byte[] data) throws IOException, TimeoutException, InterruptedException
        {
        if (data.length > 0)
            {
            try {
                int cbRead = robotUsbDevice.read(data, 0, data.length, msReadTimeout, null);
                if (DEBUG) RobotLog.logBytes(TAG, "received", data, cbRead);

                if (cbRead == 0)
                    {
                    throw new TimeoutException(makeExceptionMessage("unable to read %d bytes from flash loader", data.length));
                    }
                }
            catch (RobotUsbException e)
                {
                throw new IOException(makeExceptionMessage("unable to read %d bytes from flash loader", data.length), e);
                }
            }
        }

    protected String makeExceptionMessage(String format, Object... args)
        {
        String message = String.format(format, args);
        return String.format("flash loader(serial=%s) : %s", robotUsbDevice.getSerialNumber(), message);
        }

    }


