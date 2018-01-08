package org.firstinspires.ftc.teamcode.lift;

import com.qualcomm.robotcore.hardware.CRServo;

import org.firstinspires.ftc.teamcode.robot.Revbot;

/**
 * Created by josh on 1/5/18.
 */

public class ArmWinch extends AbstractLift {
    CRServo servo;

    public ArmWinch(CRServo servo) {
        this.servo = servo;
    }

    @Override
    public void raise() {
        servo.setPower(1);
    }

    @Override
    public void raise(long ms) {
        servo.setPower(1);
        Revbot.sleep(ms);
        stop();
    }

    @Override
    public void lower() {
        servo.setPower(-1);
    }

    @Override
    public void lower(long ms) {
        servo.setPower(-1);
        Revbot.sleep(ms);
        stop();
    }

    @Override
    public void stop() {
        servo.setPower(0);
    }
}
