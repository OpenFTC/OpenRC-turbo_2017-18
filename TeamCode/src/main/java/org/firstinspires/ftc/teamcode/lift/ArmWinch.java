package org.firstinspires.ftc.teamcode.lift;

import com.qualcomm.robotcore.hardware.CRServo;

/**
 * Created by josh on 1/5/18.
 */

public class ArmWinch extends Lift {
    CRServo servo;

    public ArmWinch(CRServo servo) {
        this(servo, 0, 0.5, 1);
    }

    public ArmWinch(CRServo servo, double down, double stop, double up) {
        super(down, stop, up);
        this.servo = servo;
    }

    @Override
    public void move(double power) {
        servo.setPower(power);
    }
}
