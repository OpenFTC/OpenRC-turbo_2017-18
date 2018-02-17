package me.joshlin.a3565lib.component.drivetrain;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by 3565 on 2/16/2018.
 */

public class Mecanum extends Drivetrain {
    private DcMotor frontL;
    private DcMotor frontR;
    private DcMotor backL;
    private DcMotor backR;

    public Mecanum(DcMotor frontL, DcMotor frontR, DcMotor backL, DcMotor backR) {
        this.backR = backR;
        this.backL = backL;
        this.frontR = frontR;
        this.frontL = frontL;
    }

    public void move(double[] driveMatrix) {
        frontL.setPower(driveMatrix[0]);
        frontR.setPower(driveMatrix[1]);
        backL.setPower(driveMatrix[2]);
        backR.setPower(driveMatrix[3]);
    }
}
