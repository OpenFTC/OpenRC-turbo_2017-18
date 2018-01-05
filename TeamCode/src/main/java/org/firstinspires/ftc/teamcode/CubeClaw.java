package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by josh on 1/5/18.
 */

public class CubeClaw extends Claw {
    private Servo leftClaw, rightClaw;

    private final double CLOSED_POSITION, OPEN_POSITION;

    public CubeClaw(Servo leftServo, Servo rightServo){
        this.leftClaw = leftServo;
        this.rightClaw = rightServo;
        this.CLOSED_POSITION = 0;
        this.OPEN_POSITION = 1;
    }

    public CubeClaw(Servo leftServo, Servo rightServo, double closedPosition, double openPosition){
        this.leftClaw = leftServo;
        this.rightClaw = rightServo;
        this.CLOSED_POSITION = closedPosition;
        this.OPEN_POSITION = openPosition;
    }

    @Override
    public void open() {
        leftClaw.setPosition(OPEN_POSITION);
        rightClaw.setPosition(OPEN_POSITION);
    }

    @Override
    public void close() {
        leftClaw.setPosition(CLOSED_POSITION);
        rightClaw.setPosition(CLOSED_POSITION);
    }

    @Override
    public void setPosition(double position) {
        leftClaw.setPosition(position);
        rightClaw.setPosition(position);
    }

    @Override
    public double getPosition() {
        return leftClaw.getPosition();
    }
}
