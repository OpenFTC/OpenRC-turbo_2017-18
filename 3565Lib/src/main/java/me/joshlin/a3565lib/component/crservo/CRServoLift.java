package me.joshlin.a3565lib.component.crservo;

import com.qualcomm.robotcore.hardware.CRServo;

import me.joshlin.a3565lib.component.interfaces.Lift;

/**
 * Created by josh on 2/19/18.
 */

public class CRServoLift extends CRServoHardwareComponent implements Lift {
    public CRServoLift(CRServo servo) {
        this(servo, 1, -1, 0);
    }

    public CRServoLift(CRServo servo, double extend, double retract, double stop) {
        super(servo, extend, retract, stop);
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
