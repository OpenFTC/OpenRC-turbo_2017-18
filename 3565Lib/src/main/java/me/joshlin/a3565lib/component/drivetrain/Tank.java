package me.joshlin.a3565lib.component.drivetrain;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by 3565 on 2/16/2018.
 */

public class Tank extends Drivetrain {
    DcMotor left;
    DcMotor right;

    public Tank(DcMotor left, DcMotor right) {
        this.left = left;
        this.right = right;
    }

    public void move(double[] driveMatrix) {
        left.setPower(driveMatrix[0]);
        right.setPower(driveMatrix[1]);
    }
}
