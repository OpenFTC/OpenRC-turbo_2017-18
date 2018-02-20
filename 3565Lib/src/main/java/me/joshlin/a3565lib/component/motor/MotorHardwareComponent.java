package me.joshlin.a3565lib.component.motor;

import com.qualcomm.robotcore.hardware.DcMotor;

import me.joshlin.a3565lib.component.interfaces.HardwareComponent;
import me.joshlin.a3565lib.component.interfaces.Lift;

/**
 * Created by josh on 2/19/18.
 */

public abstract class MotorHardwareComponent extends HardwareComponent {
    DcMotor motor;

    public MotorHardwareComponent(DcMotor motor, double one, double two, double stop) {
        super(one, two, stop);
        this.motor = motor;
    }

    public void positionOne() {
        motor.setPower(one);
    }

    public void positionTwo() {
        motor.setPower(two);
    }

    public void stop() {
        motor.setPower(three);
        setStatus(Lift.Status.STOPPED);
    }

    public double getPower() {
        return motor.getPower();
    }

    public void setPower(double power) {
        motor.setPower(power);
    }
}
