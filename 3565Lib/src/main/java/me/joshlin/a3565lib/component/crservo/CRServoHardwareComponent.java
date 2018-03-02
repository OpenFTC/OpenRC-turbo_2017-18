package me.joshlin.a3565lib.component.crservo;

import com.qualcomm.robotcore.hardware.CRServo;

import me.joshlin.a3565lib.component.interfaces.HardwareComponent;
import me.joshlin.a3565lib.component.interfaces.Lift;

/**
 * @author Josh
 *         Implements required {@link HardwareComponent} methods for {@link CRServo}s.
 */

public abstract class CRServoHardwareComponent extends HardwareComponent {
    /**
     * Holds the servo passed in to the object.
     */
    protected CRServo servo;

    /**
     * Constructor.
     *
     * @param servo the servo to pass in
     * @param one   the first speed
     * @param two   the second speed
     * @param stop  the stop speed
     */
    CRServoHardwareComponent(CRServo servo, double one, double two, double stop) {
        super(one, two, stop);
        this.servo = servo;
    }

    /**
     * Set the servo to the first speed.
     */
    public void positionOne() {
        setPower(one);
    }

    /**
     * Set the servo to the second speed.
     */
    public void positionTwo() {
        setPower(two);
    }

    /**
     * Set the servo to the stop speed.
     */
    public void stop() {
        setPower(three);
        setStatus(Lift.Status.STOPPED);
    }

    /**
     * Gets the current power of the servo.
     *
     * @return the current power of the servo (-1.0 to 1.0)
     */
    public double getPower() {
        return servo.getPower();
    }

    /**
     * Sets the current power of the servo.
     *
     * @param power the desired power of the servo
     */
    public void setPower(double power) {
        servo.setPower(power);
    }

    /**
     * CR Servos can't lock, sorry.
     */
    public void lock() {
        // Nothing here
    }
}
