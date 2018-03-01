package me.joshlin.a3565lib.component.encodermotor;

import com.qualcomm.robotcore.hardware.DcMotor;

import me.joshlin.a3565lib.component.interfaces.HardwareComponent;

/**
 * Created by 3565 on 3/1/2018.
 */

public abstract class EncoderMotorHardwareComponent extends HardwareComponent {
    /**
     * Holds the motor passed in to the object.
     */
    private DcMotor motor;

    /**
     * Constructor.
     *
     * @param motor the motor to pass in
     * @param one   the first position
     * @param two   the second position
     * @param three the third position
     */
    EncoderMotorHardwareComponent(DcMotor motor, double one, double two, double three) {
        super(one, two, three);
        this.motor = motor;
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
        return motor.getCurrentPosition();
    }

    /**
     * @param position the position to set the servo to
     */
    public void setPosition(double position) {
        motor.setTargetPosition((int) position);
    }
}
