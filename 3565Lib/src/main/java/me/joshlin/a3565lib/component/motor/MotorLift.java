package me.joshlin.a3565lib.component.motor;

import com.qualcomm.robotcore.hardware.DcMotor;

import me.joshlin.a3565lib.component.interfaces.Lift;

/**
 * Created by josh on 2/19/18.
 */

public class MotorLift extends MotorHardwareComponent implements Lift {

    public MotorLift(DcMotor motor) {
        this(motor, 1, -1, 0);
    }

    public MotorLift(DcMotor motor, double extend, double retract, double stop) {
        super(motor, extend, retract, stop);
    }

    @Override
    public void extend() {
        positionOne();
        setStatus(Status.EXTENDING);
    }

    @Override
    public void retract() {
        positionTwo();
        setStatus(Status.RETRACTING);
    }
}
