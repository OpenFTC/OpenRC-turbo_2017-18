package org.firstinspires.ftc.teamcode.lift;

import com.qualcomm.robotcore.hardware.CRServo;

import org.firstinspires.ftc.teamcode.Revbot;

/**
 * Created by josh on 1/5/18.
 */

public class RelicSlide extends AbstractLift {
    CRServo servo;

    public RelicSlide(CRServo servo) {
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
