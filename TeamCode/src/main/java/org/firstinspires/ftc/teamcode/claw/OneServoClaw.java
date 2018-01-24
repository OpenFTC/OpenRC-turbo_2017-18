package org.firstinspires.ftc.teamcode.claw;

import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by josh on 1/5/18.
 */

public class OneServoClaw extends Claw {
    private Servo clawServo;

    public OneServoClaw(Servo servo) {
        this(servo,0,1);
    }

    public OneServoClaw(Servo servo, double closedPosition, double openPosition) {
        this.closedPosition = closedPosition;
        this.openPosition = openPosition;
        this.clawServo = servo;
    }

    public void open() {
        clawServo.setPosition(openPosition);
        open = true;
    }
    public void close() {
        clawServo.setPosition(closedPosition);
        open = false;
    }

    public double getPosition() {
        return clawServo.getPosition();
    }

    public void setPosition(double position) {
        clawServo.setPosition(position);
    }
}
