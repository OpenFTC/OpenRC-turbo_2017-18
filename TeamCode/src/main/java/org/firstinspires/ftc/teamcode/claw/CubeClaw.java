package org.firstinspires.ftc.teamcode.claw;

import com.qualcomm.robotcore.hardware.Servo;

/**
 * A cube claw.
 */

public class CubeClaw extends TwoServoClaw {
    public CubeClaw(Servo leftClaw, Servo rightClaw) {
        super(leftClaw, rightClaw);
    }

    public CubeClaw(Servo leftClaw, Servo rightClaw, double closedPosition, double openPosition) {
        super(leftClaw, rightClaw, closedPosition, openPosition);
    }

    public void openLeft() {
        claw1.open();
    }

    public void closeLeft() {
        claw1.close();
    }

    public void openRight() {
        claw2.open();
    }

    public void closeRight() {
        claw2.close();
    }
}
