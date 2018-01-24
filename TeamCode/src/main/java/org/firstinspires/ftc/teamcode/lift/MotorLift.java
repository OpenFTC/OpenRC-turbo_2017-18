package org.firstinspires.ftc.teamcode.lift;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by 3565 on 1/23/2018.
 */

public class MotorLift extends Lift {
    DcMotor motor;

    public MotorLift(DcMotor motor) {
        this(motor, -1, 0, 1);
    }

    public MotorLift(DcMotor motor, double down, double stop, double up) {
        super(down, stop, up);
        this.motor = motor;
    }

    @Override
    public void raise() {
        motor.setPower(raiseSpeed);
    }

    @Override
    public void lower() {
        motor.setPower(lowerSpeed);
    }

    @Override
    public void stop() {
        motor.setPower(stopSpeed);
    }
}
