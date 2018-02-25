package me.joshlin.a3565lib.component.motor;

import com.qualcomm.robotcore.hardware.DcMotor;

import me.joshlin.a3565lib.component.interfaces.Lift;

/**
 * @author Josh
 *         An implementation of a {@link Lift} using a {@link DcMotor}.
 */

public class MotorLift extends MotorHardwareComponent implements Lift {
    /**
     * Constructor with default values.
     * <p>
     * The default values should be ok for most applications.
     * <p>
     * Extend: 1
     * Retract: -1
     * Stop: 0
     *
     * @param motor the motor to pass in
     */
    public MotorLift(DcMotor motor) {
        this(motor, 1, -1, 0);
    }

    /**
     * Constructor.
     *
     * @param motor   the motor to pass in
     * @param extend  motor power value for extending
     * @param retract motor power value for retracting
     * @param stop    motor power value for stopping
     */
    public MotorLift(DcMotor motor, double extend, double retract, double stop) {
        super(motor, extend, retract, stop);
    }

    @Override
    public void extend() {
        positionOne();
        setStatus(Status.EXTENDING);
    }

    @Override
    public void retract() {
        positionTwo();
        setStatus(Status.RETRACTING);
    }
}
