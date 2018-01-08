package org.firstinspires.ftc.teamcode.claw;

import com.qualcomm.robotcore.hardware.Servo;

/**
 * An abstract 2-servo claw.
 */

public class TwoServoClaw extends Claw {
    OneServoClaw claw1, claw2;

    TwoServoClaw(Servo servo1, Servo servo2) {
        this(servo1, servo2, 0,1);
    }

    TwoServoClaw(Servo servo1, Servo servo2, double closedPosition, double openPosition) {
        this.CLOSED_POSITION = closedPosition;
        this.OPEN_POSITION = openPosition;
        claw1 = new OneServoClaw(servo1, closedPosition, openPosition);
        claw2 = new OneServoClaw(servo2, closedPosition, openPosition);
    }

    public void open() {
        claw1.open();
        claw2.open();
    }

    public void close() {
        claw1.close();
        claw2.close();
    }

    public void setPosition(double position) {
        claw1.setPosition(position);
        claw2.setPosition(position);
    }

    public double getPosition() {
        return claw1.getPosition();
    }
}
