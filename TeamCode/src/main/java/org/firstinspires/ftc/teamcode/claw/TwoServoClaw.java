package org.firstinspires.ftc.teamcode.claw;

import com.qualcomm.robotcore.hardware.Servo;

/**
 * An abstract 2-servo claw.
 */

public class TwoServoClaw extends Claw {
    private OneServoClaw claw1;
    private OneServoClaw claw2;

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
        OPEN = true;
    }

    public void close() {
        claw1.close();
        claw2.close();
        OPEN = false;
    }

    public double getPosition() {
        return claw1.getPosition();
    }

    public void setPosition(double position) {
        claw1.setPosition(position);
        claw2.setPosition(position);
    }

    public OneServoClaw getClaw1() {
        return claw1;
    }

    private void setClaw1(OneServoClaw claw1) {
        this.claw1 = claw1;
    }

    public OneServoClaw getClaw2() {
        return claw2;
    }

    private void setClaw2(OneServoClaw claw2) {
        this.claw2 = claw2;
    }
}
