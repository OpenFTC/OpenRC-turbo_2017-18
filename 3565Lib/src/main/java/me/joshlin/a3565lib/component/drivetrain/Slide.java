package me.joshlin.a3565lib.component.drivetrain;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by josh on 1/5/18.
 */

public class Slide extends Drivetrain {
    DcMotor left;
    DcMotor strafe;
    DcMotor right;

    public Slide(DcMotor left, DcMotor right, DcMotor strafe) {
        this.left = left;
        this.right = right;
        this.strafe = strafe;
    }

    @Override
    public void move(double[] direction) {
        if (direction.length != 2) {
            throw new IllegalArgumentException("Array not correct length");
        }
        left.setPower(direction[0]);
        right.setPower(direction[0]);
        strafe.setPower(direction[1]);
    }
}
