package org.firstinspires.ftc.teamcode.lift;

import com.qualcomm.robotcore.hardware.CRServo;

/**
 * Created by josh on 1/19/18.
 */

public class CRServoLift extends Lift {
    CRServo servo;

    public CRServoLift(CRServo servo) {
        this(servo, -1, 0, 1);
    }

    public CRServoLift(CRServo crServo, double down, double stop, double up) {
        super(down, stop, up);
        this.servo = crServo;
    }

    @Override
    public void raise() {
        servo.setPower(raiseSpeed);
    }

    @Override
    public void lower() {
        servo.setPower(lowerSpeed);
    }

    @Override
    public void stop() {
        servo.setPower(stopSpeed);
    }
}
