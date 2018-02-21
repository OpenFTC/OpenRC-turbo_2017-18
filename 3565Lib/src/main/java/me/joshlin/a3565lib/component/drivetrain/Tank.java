package me.joshlin.a3565lib.component.drivetrain;

import com.qualcomm.robotcore.hardware.DcMotor;

import me.joshlin.a3565lib.enums.Direction;
import me.joshlin.a3565lib.enums.TurnDirection;

/**
 * Created by 3565 on 2/16/2018.
 */

public class Tank implements Drivetrain {
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

    @Override
    public void drive(Direction direction, double power) {

    }

    @Override
    public void turn(TurnDirection turnDirection, double power) {

    }

    @Override
    public void stop() {

    }
}
