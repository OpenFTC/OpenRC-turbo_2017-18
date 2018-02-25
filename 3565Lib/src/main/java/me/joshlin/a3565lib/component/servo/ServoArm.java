package me.joshlin.a3565lib.component.servo;

import com.qualcomm.robotcore.hardware.Servo;

import me.joshlin.a3565lib.component.interfaces.Arm;

/**
 * @author Josh
 *         An implementation of an {@link Arm} using a {@link Servo}.
 */
public class ServoArm extends ServoHardwareComponent implements Arm {
    /**
     * Constructor with default center value.
     * Center value defaults to 0.5.
     *
     * @param servo the servo to pass in
     * @param left  the left position of the arm
     * @param right the right position of the arm
     */
    public ServoArm(Servo servo, double left, double right) {
        this(servo, left, 0.5, right);
    }

    /**
     * Constructor.
     *
     * @param servo  the servo to pass in
     * @param left   the left position of the arm
     * @param center the center position of the arm
     * @param right  the right position of the arm
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
