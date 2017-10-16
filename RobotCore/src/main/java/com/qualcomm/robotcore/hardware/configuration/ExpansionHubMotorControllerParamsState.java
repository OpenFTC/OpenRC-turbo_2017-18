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
package com.qualcomm.robotcore.hardware.configuration;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.internal.system.Assert;

import java.io.Serializable;

/**
 * {@link ExpansionHubMotorControllerParamsState} captures state that is declared in
 * Expansion Hub Motor Controller parameter attributes.
 *
 * @see ExpansionHubMotorControllerVelocityParams
 * @see ExpansionHubMotorControllerPositionParams
 */
@SuppressWarnings("WeakerAccess")
public class ExpansionHubMotorControllerParamsState implements Serializable, Cloneable
    {
    /** Only {@link DcMotor.RunMode#RUN_USING_ENCODER} and {@link DcMotor.RunMode#RUN_TO_POSITION}
     * are legal for {@link #mode}. */
    public @Expose @Nullable DcMotor.RunMode mode = null;
    public @Expose double p = 0;
    public @Expose double i = 0;
    public @Expose double d = 0;

    public ExpansionHubMotorControllerParamsState()
        {
        Assert.assertTrue(this.isDefault());
        }

    public ExpansionHubMotorControllerParamsState(@NonNull DcMotor.RunMode mode, double p, double i, double d)
        {
        Assert.assertTrue(mode.isPIDMode() && mode.migrate()==mode);
        this.mode = mode;
        this.p = p;
        this.i = i;
        this.d = d;
        }

    public ExpansionHubMotorControllerParamsState(@NonNull ExpansionHubMotorControllerPositionParams params)
        {
        this.mode = DcMotor.RunMode.RUN_TO_POSITION;
        this.p = params.P();
        this.i = params.I();
        this.d = params.D();
        }

    public ExpansionHubMotorControllerParamsState(@Nullable ExpansionHubMotorControllerVelocityParams params)
        {
        this.mode = DcMotor.RunMode.RUN_USING_ENCODER;
        this.p = params.P();
        this.i = params.I();
        this.d = params.D();
        }

    public ExpansionHubMotorControllerParamsState clone()
        {
        try {
            return (ExpansionHubMotorControllerParamsState)super.clone();
            }
        catch (CloneNotSupportedException e)
            {
            throw new RuntimeException("internal error: Parameters not cloneable");
            }
        }

    public boolean isDefault()
        {
        return mode==null;
        }

    @Override public boolean equals(Object o)
        {
        if (o instanceof ExpansionHubMotorControllerParamsState)
            {
            ExpansionHubMotorControllerParamsState them = (ExpansionHubMotorControllerParamsState) o;
            return this.mode == them.mode && this.p == them.p && this.i == them.i && this.d == them.d;
            }
        else
            return false;
        }

    @Override public int hashCode()
        {
        return mode.hashCode() ^ (hash(p) << 3) ^ (hash(i) << 6) ^ (hash(d) << 9) ^ 0xCCAE348C;
        }

    protected int hash(double d)
        {
        return (new Double(d)).hashCode();
        }

    @Override public String toString()
        {
        return String.format("mode=%s,p=%d,i=%d,d=%d", mode, p, i, d);
        }
    }
