package me.joshlin.a3565lib.component.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.configuration.MotorConfigurationType;

import java.util.ArrayList;

/**
 * @author Josh
 *         Allows the aggregation of multiple {@link DcMotor}s into one.
 */

public class MultiDcMotor implements DcMotor {
    private ArrayList<DcMotor> motors = new ArrayList<DcMotor>();

    /**
     * @deprecated Should never be called without motors in constructor, use {@link #MultiDcMotor(DcMotor...)} instead.
     */
    @Deprecated
    public MultiDcMotor() {
    }

    /**
     * Class constructor specifying the motors that make up the robot.
     *
     * @param motors the motors to add to the multi-motor
     */
    public MultiDcMotor(DcMotor... motors) {
        for (DcMotor motor : motors) {
            add(motor);
        }
    }

    /**
     * Adds a motor to the multi-motor.
     *
     * @param motor the motor to add to the multi-motor
     * @return true if the motor was successfully added
     */
    public boolean add(DcMotor motor) {
        return motors.add(motor);
    }

    /**
     * Removes a motor from the multi-motor.
     *
     * @param motor the motor to remove from the multi-motor
     * @return true if the motor was successfully added
     */
    public boolean remove(DcMotor motor) {
        return motors.remove(motor);
    }

    /**
     * Returns an indication of the manufacturer of the first motor added.
     *
     * @return the device's manufacturer
     */
    @Override
    public Manufacturer getManufacturer() {
        return motors.get(0).getManufacturer();
    }

    /**
     * Returns a string suitable for display to the user as to the type of device.
     * Note that this is a device-type-specific name; it has nothing to do with the
     * name by which a user might have configured the device in a robot configuration.
     *
     * @return device manufacturer and name
     */
    @Override
    public String getDeviceName() {
        return motors.get(0).getDeviceName();
    }

    /**
     * Get connection information about this device in a human readable format
     *
     * @return connection info
     */
    @Override
    public String getConnectionInfo() {
        return motors.get(0).getConnectionInfo();
    }

    /**
     * Version
     *
     * @return get the version of this device
     */
    @Override
    public int getVersion() {
        return motors.get(0).getVersion();
    }

    /**
     * Resets the device's configuration to that which is expected at the beginning of an OpMode.
     * For example, motors will reset the their direction to 'forward'.
     */
    @Override
    public void resetDeviceConfigurationForOpMode() {
        for (DcMotor motor : motors) {
            motor.resetDeviceConfigurationForOpMode();
        }
    }

    /**
     * Closes this device
     */
    @Override
    public void close() {
        for (DcMotor motor : motors) {
            motor.close();
        }
    }

    /**
     * Returns the assigned type for this motor. If no particular motor type has been
     * configured, then {@link MotorConfigurationType#getUnspecifiedMotorType()} will be returned.
     * Note that the motor type for a given motor is initially assigned in the robot
     * configuration user interface, though it may subsequently be modified using methods herein.
     *
     * @return the assigned type for this motor
     */
    @Override
    public MotorConfigurationType getMotorType() {
        return motors.get(0).getMotorType();
    }

    /**
     * Sets the assigned type of this motor. Usage of this method is very rare.
     *
     * @param motorType the new assigned type for this motor
     * @see #getMotorType()
     */
    @Override
    public void setMotorType(MotorConfigurationType motorType) {
        for (DcMotor motor : motors) {
            motor.setMotorType(motorType);
        }
    }

    /**
     * Returns the underlying motor controller on which this motor is situated.
     *
     * @return the underlying motor controller on which this motor is situated.
     * @see #getPortNumber()
     */
    @Override
    public DcMotorController getController() {
        return motors.get(0).getController();
    }

    /**
     * Returns the port number on the underlying motor controller on which this motor is situated.
     *
     * @return the port number on the underlying motor controller on which this motor is situated.
     * @see #getController()
     */
    @Override
    public int getPortNumber() {
        return motors.get(0).getPortNumber();
    }

    /**
     * Returns the current behavior of the motor were a power level of zero to be applied.
     *
     * @return the current behavior of the motor were a power level of zero to be applied.
     */
    @Override
    public ZeroPowerBehavior getZeroPowerBehavior() {
        return motors.get(0).getZeroPowerBehavior();
    }

    /**
     * Sets the behavior of the motor when a power level of zero is applied.
     *
     * @param zeroPowerBehavior the new behavior of the motor when a power level of zero is applied.
     * @see ZeroPowerBehavior
     * @see #setPower(double)
     */
    @Override
    public void setZeroPowerBehavior(ZeroPowerBehavior zeroPowerBehavior) {
        for (DcMotor motor : motors) {
            motor.setZeroPowerBehavior(zeroPowerBehavior);
        }
    }

    /**
     * Sets the zero power behavior of the motor to {@link ZeroPowerBehavior#FLOAT FLOAT}, then
     * applies zero power to that motor.
     * <p>
     * <p>Note that the change of the zero power behavior to {@link ZeroPowerBehavior#FLOAT FLOAT}
     * remains in effect even following the return of this method. <STRONG>This is a breaking
     * change</STRONG> in behavior from previous releases of the SDK. Consider, for example, the
     * following code sequence:</p>
     * <p>
     * <pre>
     *     motor.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE); // method not available in previous releases
     *     motor.setPowerFloat();
     *     motor.setPower(0.0);
     * </pre>
     * <p>
     * <p>Starting from this release, this sequence of code will leave the motor floating. Previously,
     * the motor would have been left braked.</p>
     *
     * @see #setPower(double)
     * @see #getPowerFloat()
     * @see #setZeroPowerBehavior(ZeroPowerBehavior)
     * @deprecated This method is deprecated in favor of direct use of
     * {@link #setZeroPowerBehavior(ZeroPowerBehavior) setZeroPowerBehavior()} and
     * {@link #setPower(double) setPower()}.
     */
    @Override
    public void setPowerFloat() {
        for (DcMotor motor : motors) {
            motor.setPowerFloat();
        }
    }

    /**
     * Returns whether the motor is currently in a float power level.
     *
     * @return whether the motor is currently in a float power level.
     * @see #setPowerFloat()
     */
    @Override
    public boolean getPowerFloat() {
        return motors.get(0).getPowerFloat();
    }

    /**
     * Returns the current target encoder position for this motor.
     *
     * @return the current target encoder position for this motor.
     * @see #setTargetPosition(int)
     */
    @Override
    public int getTargetPosition() {
        return motors.get(0).getTargetPosition();
    }

    /**
     * Sets the desired encoder target position to which the motor should advance or retreat
     * and then actively hold thereat. This behavior is similar to the operation of a servo.
     * The maximum speed at which this advance or retreat occurs is governed by the power level
     * currently set on the motor. While the motor is advancing or retreating to the desired
     * taget position, {@link #isBusy()} will return true.
     * <p>
     * <p>Note that adjustment to a target position is only effective when the motor is in
     * {@link RunMode#RUN_TO_POSITION RUN_TO_POSITION}
     * RunMode. Note further that, clearly, the motor must be equipped with an encoder in order
     * for this mode to function properly.</p>
     *
     * @param position the desired encoder target position
     * @see #getCurrentPosition()
     * @see #setMode(RunMode)
     * @see RunMode#RUN_TO_POSITION
     * @see #getTargetPosition()
     * @see #isBusy()
     */
    @Override
    public void setTargetPosition(int position) {
        for (DcMotor motor : motors) {
            motor.setTargetPosition(position);
        }
    }

    /**
     * Returns true if the motor is currently advancing or retreating to a target position.
     *
     * @return true if the motor is currently advancing or retreating to a target position.
     * @see #setTargetPosition(int)
     */
    @Override
    public boolean isBusy() {
        return motors.get(0).isBusy();
    }

    /**
     * Returns the current reading of the encoder for this motor. The units for this reading,
     * that is, the number of ticks per revolution, are specific to the motor/encoder in question,
     * and thus are not specified here.
     *
     * @return the current reading of the encoder for this motor
     * @see #getTargetPosition()
     * @see RunMode#STOP_AND_RESET_ENCODER
     */
    @Override
    public int getCurrentPosition() {
        return motors.get(0).getCurrentPosition();
    }

    /**
     * Returns the current run mode for this motor
     *
     * @return the current run mode for this motor
     * @see RunMode
     * @see #setMode(RunMode)
     */
    @Override
    public RunMode getMode() {
        return motors.get(0).getMode();
    }

    /**
     * Sets the current run mode for this motor
     *
     * @param mode the new current run mode for this motor
     * @see RunMode
     * @see #getMode()
     */
    @Override
    public void setMode(RunMode mode) {
        for (DcMotor motor : motors) {
            motor.setMode(mode);
        }
    }

    /**
     * Returns the current logical direction in which this motor is set as operating.
     *
     * @return the current logical direction in which this motor is set as operating.
     * @see #setDirection(Direction)
     */
    @Override
    public Direction getDirection() {
        return motors.get(0).getDirection();
    }

    /**
     * Sets the logical direction in which this motor operates.
     *
     * @param direction the direction to set for this motor
     * @see #getDirection()
     */
    @Override
    public void setDirection(Direction direction) {
        for (DcMotor motor : motors) {
            motor.setDirection(direction);
        }
    }

    /**
     * Returns the current configured power level of the motor.
     *
     * @return the current level of the motor, a value in the interval [0.0, 1.0]
     * @see #setPower(double)
     */
    @Override
    public double getPower() {
        return motors.get(0).getPower();
    }

    /**
     * Sets the power level of the motor, expressed as a fraction of the maximum
     * possible power / speed supported according to the run mode in which the
     * motor is operating.
     * <p>
     * <p>Setting a power level of zero will brake the motor</p>
     *
     * @param power the new power level of the motor, a value in the interval [-1.0, 1.0]
     * @see #getPower()
     * @see DcMotor#setMode(RunMode)
     * @see DcMotor#setPowerFloat()
     */
    @Override
    public void setPower(double power) {
        for (DcMotor motor : motors) {
            motor.setPower(power);
        }
    }
}
