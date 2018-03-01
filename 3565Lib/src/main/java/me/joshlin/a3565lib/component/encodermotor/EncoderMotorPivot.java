package me.joshlin.a3565lib.component.encodermotor;

import com.qualcomm.robotcore.hardware.DcMotor;

import me.joshlin.a3565lib.component.interfaces.Pivot;

/**
 * Created by 3565 on 3/1/2018.
 */

public class EncoderMotorPivot extends EncoderMotorHardwareComponent implements Pivot {
    /**
     * Constructor with default center values.
     * Center value defaults to 0.5.
     *
     * @param motor the motor to pass in
     * @param up    the up position of the pivot
     * @param down  the down position of the pivot
     */
    public EncoderMotorPivot(DcMotor motor, double up, double down) {
        this(motor, up, 0.5, down);
    }

    /**
     * Constructor.
     *
     * @param motor  the motor to pass in
     * @param up     the up position of the pivot
     * @param center the center position of the pivot
     * @param down   the down position of the pivot
     */
    public EncoderMotorPivot(DcMotor motor, double up, double center, double down) {
        super(motor, up, center, down);
    }

    @Override
    public void up() {
        positionOne();
    }

    @Override
    public void center() {
        positionTwo();
    }

    @Override
    public void down() {
        positionThree();
    }
}
