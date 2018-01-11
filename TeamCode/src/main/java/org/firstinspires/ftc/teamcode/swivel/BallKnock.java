package org.firstinspires.ftc.teamcode.swivel;

import com.qualcomm.robotcore.hardware.Servo;

/**
 * BallKnock class. Has knockBalls(Alliance alliance)
 */

public class BallKnock extends Swivel {
    private Servo servo;

    public BallKnock(Servo servo) {
        super();
        this.servo = servo;
    }

    public BallKnock(Servo servo, double left, double center, double right) {
        super(left, center, right);
        this.servo = servo;
    }

    @Override
    public void swivelLeft() {
        servo.setPosition(left);
    }

    @Override
    public void swivelCenter() {
        servo.setPosition(center);
    }

    @Override
    public void swivelRight() {
        servo.setPosition(right);
    }

    @Override
    public double getPosition() {
        return servo.getPosition();
    }

    @Override
    public void setPosition(double position) {
        servo.setPosition(position);
    }
}
