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
package com.qualcomm.robotcore.hardware;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

/**
 * The DcMotorEx interface provides enhanced motor functionality which is available with some
 * hardware devices. The DcMotorEx interface is typically used as a second interface on an object
 * whose primary interface is DcMotor. To access it, cast your DcMotor object
 * to DcMotorEx. However, it is perhaps prudent to first test whether the cast will succeed by
 * testing using 'instanceof'.
 * @see PwmControl
 */
public interface DcMotorEx extends DcMotor
    {
    /**
     * Individually energizes this particular motor
     * @see #setMotorDisable()
     * @see #isMotorEnabled()
     */
    void setMotorEnable();

    /**
     * Individually de-energizes this particular motor
     * @see #setMotorEnable()
     * @see #isMotorEnabled()
     */
    void setMotorDisable();

    /**
     * Returns whether this motor is energized
     * @see #setMotorEnable()
     * @see #setMotorDisable()
     */
    boolean isMotorEnabled();

    /**
     * Sets the velocity of the motor
     * @param angularRate   the desired angular rate, in units per second
     * @param unit          the units in which angularRate is expressed
     *
     * @see #getVelocity(AngleUnit)
     */
    void setVelocity(double angularRate, AngleUnit unit);

    /**
     * Returns the current velocity of the motor, in angular units per second
     * @param unit          the units in which the angular rate is desired
     * @return              the current velocity of the motor
     *
     * @see #setVelocity(double, AngleUnit)
     */
    double getVelocity(AngleUnit unit);

    /**
     * Sets the PID control coefficients for one of the PID modes of this motor.
     * Note that in some controller implementations, setting the PID coefficients for one
     * mode on a motor might affect other modes on that motor, or might affect the PID
     * coefficients used by other motors on the same controller.
     *
     * @param mode either {@link RunMode#RUN_USING_ENCODER} or {@link RunMode#RUN_TO_POSITION}
     * @param pidCoefficients the new coefficients to use when in that mode on this motor
     *
     * @see #getPIDCoefficients(RunMode)
     */
    void setPIDCoefficients(RunMode mode,  PIDCoefficients pidCoefficients);

    /**
     * Returns the PID control coefficients used when running in the indicated mode
     * on this motor.
     * @param mode either {@link RunMode#RUN_USING_ENCODER} or {@link RunMode#RUN_TO_POSITION}
     * @return the PID control coefficients used when running in the indicated mode on this motor
     */
    PIDCoefficients getPIDCoefficients(RunMode mode);

    /**
     * Sets the target positioning tolerance of this motor
     * @param tolerance the desired tolerance, in encoder ticks
     * @see DcMotor#setTargetPosition(int)
     */
    void setTargetPositionTolerance(int tolerance);

    /**
     * Returns the current target positioning tolerance of this motor
     * @return the current target positioning tolerance of this motor
     */
    int getTargetPositionTolerance();
    }
