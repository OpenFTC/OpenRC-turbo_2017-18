package me.joshlin.a3565lib.component.motor;

import com.qualcomm.robotcore.hardware.DcMotor;

import me.joshlin.a3565lib.component.interfaces.Intake;

/**
 * Created by josh on 2/19/18.
 */

public class MotorIntake extends MotorHardwareComponent implements Intake {
    public MotorIntake(DcMotor motor) {
        super(motor, 1, -1, 0);
    }

    public MotorIntake(DcMotor motor, double in, double out, double stop) {
        super(motor, in, out, stop);
    }

    @Override
    public void in() {
        positionOne();
        setStatus(Status.IN);
    }

    @Override
    public void out() {
        positionTwo();
        setStatus(Status.OUT);
    }
}
