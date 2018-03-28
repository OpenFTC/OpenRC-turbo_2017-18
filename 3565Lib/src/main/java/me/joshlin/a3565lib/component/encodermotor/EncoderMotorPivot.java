package me.joshlin.a3565lib.component.encodermotor;

import com.qualcomm.robotcore.hardware.DcMotor;

import me.joshlin.a3565lib.component.interfaces.Pivot;

/**
 * @author Josh
 *         An implementation of a {@link Pivot} using a {@link DcMotor} with encoder.
 */

public class EncoderMotorPivot extends EncoderMotorHardwareComponent implements Pivot {
    /**
     * Constructor.
     *
     * @param motor  the motor to pass in
     * @param up     the up position of the pivot
     * @param center the center position of the pivot
     * @param down   the down position of the pivot
     */
    public EncoderMotorPivot(DcMotor motor, double up, double center, double down, double power) {
        super(motor, up, center, down, power);
    }

    @Override
    public void up() {
        positionOne();
        setStatus(Status.UP);
    }

    @Override
    public void center() {
        positionTwo();
        setStatus(Status.CENTER);
    }

    @Override
    public void down() {
        positionThree();
        setStatus(Status.DOWN);
    }
}
