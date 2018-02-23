package me.joshlin.a3565lib.component.crservo;

import com.qualcomm.robotcore.hardware.CRServo;

import me.joshlin.a3565lib.component.interfaces.Lift;

/**
 * @author Josh
 *         An implementation of a {@link Lift} using a {@link CRServo}.
 */

public class CRServoLift extends CRServoHardwareComponent implements Lift {
    /**
     * Constructor with default values.
     * <p>
     * The default values should be ok for most applications.
     * <p>
     * Extend: 1
     * Retract: -1
     * Stop: 0
     *
     * @param servo the servo to pass in
     */
    public CRServoLift(CRServo servo) {
        this(servo, 1, -1, 0);
    }

    /**
     * Constructor.
     *
     * @param servo   the servo to pass in
     * @param extend  servo power value for extending
     * @param retract servo power value for retracting
     * @param stop    servo power value for stopping
     */
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
