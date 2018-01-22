package org.firstinspires.ftc.teamcode.lift;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by josh on 1/5/18.
 */

public class CubeLift extends Lift {
    DcMotor motor;

    public CubeLift(DcMotor motor) {
        this(motor, 1, 0, 1);
    }

    public CubeLift(DcMotor motor, double down, double stop, double up) {
        super(down, stop, up);
        this.motor = motor;
    }

    @Override
    public void move(double power) {
        motor.setPower(power);
    }
}
