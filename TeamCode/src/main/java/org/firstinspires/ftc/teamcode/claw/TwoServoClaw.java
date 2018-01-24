package org.firstinspires.ftc.teamcode.claw;

import com.qualcomm.robotcore.hardware.Servo;

/**
 * An abstract 2-servo claw.
 */

public class TwoServoClaw extends Claw {
    private OneServoClaw claw1;
    private OneServoClaw claw2;

    public TwoServoClaw(Servo servo1, Servo servo2) {
        this(servo1, servo2, 0, 1);
    }

    public TwoServoClaw(Servo servo1, Servo servo2, double closedPosition, double openPosition) {
        this.closedPosition = closedPosition;
        this.openPosition = openPosition;
        claw1 = new OneServoClaw(servo1, closedPosition, openPosition);
        claw2 = new OneServoClaw(servo2, closedPosition, openPosition);
    }

    public void open() {
        claw1.open();
        claw2.open();
        open = true;
    }

    public void close() {
        claw1.close();
        claw2.close();
        open = false;
    }

    public void openClaw1() {
        claw1.open();
    }

    public void closeClaw1() {
        claw1.close();
    }

    public void openClaw2() {
        claw2.open();
    }

    public void closeClaw2() {
        claw2.close();
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

    public OneServoClaw getClaw2() {
        return claw2;
    }
}
