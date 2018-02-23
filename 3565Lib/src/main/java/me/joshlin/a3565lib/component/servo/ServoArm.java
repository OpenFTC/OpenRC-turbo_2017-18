package me.joshlin.a3565lib.component.servo;

import com.qualcomm.robotcore.hardware.Servo;

import me.joshlin.a3565lib.component.interfaces.Arm;

/**
 * @author Josh
 * An implementation of an {@link Arm} using a {@link Servo}.
 */
// TODO: finish commenting this
public class ServoArm extends ServoHardwareComponent implements Arm {
    /**
     *
     * @param servo
     * @param left
     * @param right
     */
    public ServoArm(Servo servo, double left, double right) {
        this(servo, left, 0.5, right);
    }

    /**
     *
     * @param servo
     * @param left
     * @param center
     * @param right
     */
    public ServoArm(Servo servo, double left, double center, double right) {
        super(servo, left, center, right);
    }

    @Override
    public void left() {
        positionOne();
    }

    @Override
    public void center() {
        positionTwo();
    }

    @Override
    public void right() {
        positionThree();
    }
}
