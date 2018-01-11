package org.firstinspires.ftc.teamcode.claw;

import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by josh on 1/5/18.
 */

public class OneServoClaw extends Claw {
    private Servo clawServo;

    OneServoClaw(Servo servo) {
        this(servo,0,1);
    }

    OneServoClaw(Servo servo, double closedPosition, double openPosition) {
        this.CLOSED_POSITION = closedPosition;
        this.OPEN_POSITION = openPosition;
        this.clawServo = servo;
    }

    public void open() {
        clawServo.setPosition(OPEN_POSITION);
        OPEN = true;
    }
    public void close() {
        clawServo.setPosition(CLOSED_POSITION);
        OPEN = false;
    }

    public double getPosition() {
        return clawServo.getPosition();
    }

    public void setPosition(double position) {
        clawServo.setPosition(position);
    }
}
