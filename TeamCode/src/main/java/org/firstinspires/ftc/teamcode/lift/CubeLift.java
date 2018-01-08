package org.firstinspires.ftc.teamcode.lift;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.robot.Revbot;

/**
 * Created by josh on 1/5/18.
 */

public class CubeLift extends AbstractLift {
    DcMotor motor;

    public CubeLift(DcMotor motor) {
        this.motor = motor;
    }

    @Override
    public void raise() {
        motor.setPower(1);
    }

    @Override
    public void raise(long ms) {
        motor.setPower(1);
        Revbot.sleep(ms);
        stop();
    }

    @Override
    public void lower() {
        motor.setPower(-1);
    }

    @Override
    public void lower(long ms) {
        motor.setPower(-1);
        Revbot.sleep(ms);
        stop();
    }

    @Override
    public void stop() {
        motor.setPower(0);
    }
}
