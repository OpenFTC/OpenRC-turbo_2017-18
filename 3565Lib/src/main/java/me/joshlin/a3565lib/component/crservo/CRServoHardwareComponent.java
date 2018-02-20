package me.joshlin.a3565lib.component.crservo;

import com.qualcomm.robotcore.hardware.CRServo;

import me.joshlin.a3565lib.component.interfaces.HardwareComponent;
import me.joshlin.a3565lib.component.interfaces.Lift;

/**
 * Created by josh on 2/19/18.
 */

public abstract class CRServoHardwareComponent extends HardwareComponent {
    CRServo servo;

    public CRServoHardwareComponent(CRServo servo, double one, double two, double stop) {
        super(one, two, stop);
        this.servo = servo;
    }

    public void positionOne() {
        servo.setPower(one);
    }

    public void positionTwo() {
        servo.setPower(two);
    }

    public void stop() {
        servo.setPower(three);
        setStatus(Lift.Status.STOPPED);
    }

    public double getPower() {
        return servo.getPower();
    }

    public void setPower(double power) {
        servo.setPower(power);
    }
}
