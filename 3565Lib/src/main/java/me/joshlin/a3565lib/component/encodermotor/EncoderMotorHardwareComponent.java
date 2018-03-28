package me.joshlin.a3565lib.component.encodermotor;

import com.qualcomm.robotcore.hardware.DcMotor;

import me.joshlin.a3565lib.Sleep;
import me.joshlin.a3565lib.component.interfaces.HardwareComponent;

/**
 * Created by josh on 3/3/18.
 */

public class EncoderMotorHardwareComponent extends HardwareComponent {
    /**
     * Holds the servo passed in to the object.
     */
    private DcMotor motor;

    private double power;

    /**
     * Constructor.
     *
     * @param motor the motor to pass in
     * @param one   the first position
     * @param two   the second position
     * @param three the third position
     */
    EncoderMotorHardwareComponent(DcMotor motor, double one, double two, double three, double power) {
        super(one, two, three);
        this.motor = motor;
        this.power = power;
    }

    /**
     * Set the motor to the first position.
     */
    public void positionOne() {
        setPosition(one, power);
    }

    /**
     * Set the motor to the second position.
     */
    public void positionTwo() {
        setPosition(two, power);
    }

    /**
     * Set the motor to the third position.
     */
    public void positionThree() {
        setPosition(three, power);
    }

    /**
     * Gets the current position of the motor.
     *
     * @return the current position of the motor (encoder value)
     */
    public double getPosition() {
        return motor.getCurrentPosition();
    }

    /**
     * @param position the position to set the motor to
     */
    public void setPosition(double position, double power) {
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setTargetPosition((int) position);
        if (getPosition() > position) {
            power = -power;
        }
        motor.setPower(power);
        while (new Sleep().opModeIsActive() && motor.isBusy()) {
            new Sleep().sleep(50);
        }
    }

    /**
     * Implementation of lock function. Sets the motor position to the current position.
     */
    public void lock() {
        setPosition(getPosition(), power);
    }
}
