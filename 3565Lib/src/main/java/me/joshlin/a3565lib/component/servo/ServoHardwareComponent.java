package me.joshlin.a3565lib.component.servo;

import com.qualcomm.robotcore.hardware.Servo;

import me.joshlin.a3565lib.component.interfaces.HardwareComponent;

/**
 * @author Josh
 *         Implements required {@link HardwareComponent} methods for {@link Servo}s.
 */

public abstract class ServoHardwareComponent extends HardwareComponent {
    /**
     * Holds the servo passed in to the object.
     */
    private Servo servo;

    /**
     * Constructor.
     *
     * @param servo the servo to pass in
     * @param one   the first position
     * @param two   the second position
     * @param three the third position
     */
    ServoHardwareComponent(Servo servo, double one, double two, double three) {
        super(one, two, three);
        this.servo = servo;
    }

    /**
     * Set the servo to the first position.
     */
    public void positionOne() {
        setPosition(one);
    }

    /**
     * Set the servo to the second position.
     */
    public void positionTwo() {
        setPosition(two);
    }

    /**
     * Set the servo to the third position.
     */
    public void positionThree() {
        setPosition(three);
    }

    /**
     * Gets the current position of the servo.
     *
     * @return the current position of the servo (0.0 to 1.0)
     */
    public double getPosition() {
        return servo.getPosition();
    }

    /**
     * @param position the position to set the servo to
     */
    public void setPosition(double position) {
        servo.setPosition(position);
    }

    /**
     * Implementation of lock function. Sets the servo position to the current position.
     */
    public void lock() {
        setPosition(getPosition());
    }
}
