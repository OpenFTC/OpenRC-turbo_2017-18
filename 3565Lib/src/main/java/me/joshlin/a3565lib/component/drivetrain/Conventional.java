package me.joshlin.a3565lib.component.drivetrain;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by 3565 on 2/16/2018.
 */

public class Conventional extends Drivetrain {
    DcMotor left;
    DcMotor right;

    public Conventional(DcMotor left, DcMotor right) {
        this.left = left;
        this.right = right;
    }

    public void move(double leftPower, double rightPower) {
        left.setPower(leftPower);
        right.setPower(rightPower);
    }
}
