package me.joshlin.a3565lib.component.motor;

import com.qualcomm.robotcore.hardware.DcMotor;

import me.joshlin.a3565lib.component.interfaces.Intake;

/**
 * @author Josh
 *         An implementation of an {@link Intake} using a {@link DcMotor}
 */

public class MotorIntake extends MotorHardwareComponent implements Intake {
    /**
     * Constructor with default values.
     * <p>
     * The default values should be ok for most applications.
     * <p>
     * In: 1
     * Out: -1
     * Stop: 0
     *
     * @param motor the motor to pass in
     */
    public MotorIntake(DcMotor motor) {
        super(motor, 1, -1, 0);
    }

    /**
     * Constructor.
     *
     * @param motor the motor to pass in
     * @param in    motor power value for intake
     * @param out   motor power value for eject
     * @param stop  motor power value for stopping
     */
    public MotorIntake(DcMotor motor, double in, double out, double stop) {
        super(motor, in, out, stop);
    }

    @Override
    public void in() {
        positionOne();
        setStatus(Status.IN);
    }

    @Override
    public void out() {
        positionTwo();
        setStatus(Status.OUT);
    }
}
