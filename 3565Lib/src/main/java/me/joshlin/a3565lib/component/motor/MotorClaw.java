package me.joshlin.a3565lib.component.motor;

import com.qualcomm.robotcore.hardware.DcMotor;

import me.joshlin.a3565lib.component.interfaces.Claw;

/**
 * Created by josh on 2/19/18.
 */

public class MotorClaw extends MotorHardwareComponent implements Claw {
    public MotorClaw(DcMotor motor) {
        super(motor, 1, -1, 0);
    }

    public MotorClaw(DcMotor motor, double one, double two, double stop) {
        super(motor, one, two, stop);
    }

    @Override
    public void open() {
        positionOne();
        setStatus(Status.OPEN);
    }

    @Override
    public void close() {
        positionTwo();
        setStatus(Status.CLOSED);
    }
}
