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
        getClaw1().open();
    }

    public void closeLeft() {
        getClaw1().close();
    }

    public void openRight() {
        getClaw2().open();
    }

    public void closeRight() {
        getClaw2().close();
    }
}
