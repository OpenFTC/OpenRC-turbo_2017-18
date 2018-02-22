package me.joshlin.a3565lib.component.servo;

import com.qualcomm.robotcore.hardware.Servo;

import me.joshlin.a3565lib.component.interfaces.Arm;

/**
 * Created by josh on 2/21/18.
 */

public class ServoArm extends ServoHardwareComponent implements Arm {

    public ServoArm(Servo servo, double left, double right) {
        this(servo, left, 0.5, right);
    }

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
