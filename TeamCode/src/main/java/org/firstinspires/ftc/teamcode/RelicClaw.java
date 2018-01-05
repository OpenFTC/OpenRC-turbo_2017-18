package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by josh on 1/5/18.
 */

public class RelicClaw extends Claw {
    private Servo claw;

    private final double CLOSED_POSITION, OPEN_POSITION;

    public RelicClaw(Servo clawServo) {
        this.claw = clawServo;
        this.CLOSED_POSITION = 0;
        this.OPEN_POSITION = 1;
    }

    public RelicClaw(Servo clawServo, double closedPosition, double openPosition) {
        this.claw = clawServo;
        this.CLOSED_POSITION = closedPosition;
        this.OPEN_POSITION = openPosition;
    }

    @Override
    public void open() {
        claw.setPosition(OPEN_POSITION);
    }

    @Override
    public void close() {
        claw.setPosition(CLOSED_POSITION);
    }

    @Override
    public void setPosition(double position) {
        claw.setPosition(position);
    }

    @Override
    public double getPosition() {
        return claw.getPosition();
    }
}
