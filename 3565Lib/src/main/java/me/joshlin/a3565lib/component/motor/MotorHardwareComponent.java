package me.joshlin.a3565lib.component.motor;

import com.qualcomm.robotcore.hardware.DcMotor;

import me.joshlin.a3565lib.component.interfaces.HardwareComponent;
import me.joshlin.a3565lib.component.interfaces.Lift;

/**
 * @author Josh
 *         Implements required {@link HardwareComponent} methods for {@link DcMotor}s.
 */

public abstract class MotorHardwareComponent extends HardwareComponent {
    /**
     * Holds the motor passed in to the object.
     */
    private DcMotor motor;

    /**
     * Constructor.
     *
     * @param motor the motor to pass in
     * @param one   the first speed
     * @param two   the second speed
     * @param stop  the stop speed
     */
    MotorHardwareComponent(DcMotor motor, double one, double two, double stop) {
        super(one, two, stop);
        this.motor = motor;
    }

    /**
     * Set the motor to the first speed.
     */
    public void positionOne() {
        setPower(one);
    }

    /**
     * Set the motor to the second speed.
     */
    public void positionTwo() {
        setPower(two);
    }

    /**
     * Set the motor to the stop speed.
     */
    public void stop() {
        setPower(three);
        setStatus(Lift.Status.STOPPED);
    }

    /**
     * Gets the current power of the motor.
     *
     * @return the current power of the motor (-1.0 to 1.0)
     */
    public double getPower() {
        return motor.getPower();
    }

    /**
     * Sets the current power of the motor.
     *
     * @param power the desired power of the motor
     */
    public void setPower(double power) {
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor.setPower(power);
    }
}
