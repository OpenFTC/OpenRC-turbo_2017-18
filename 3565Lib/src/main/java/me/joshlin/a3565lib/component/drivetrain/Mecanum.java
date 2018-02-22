package me.joshlin.a3565lib.component.drivetrain;

import com.qualcomm.robotcore.hardware.DcMotor;

import me.joshlin.a3565lib.enums.Direction;
import me.joshlin.a3565lib.enums.TurnDirection;

/**
 * @author Josh
 *         A mecanum drive class, implementing Drivetrain.
 */

public class Mecanum implements Drivetrain {
    private DcMotor frontL;
    private DcMotor frontR;
    private DcMotor backL;
    private DcMotor backR;

    /**
     * Constructor.
     *
     * @param frontL the front-left motor
     * @param frontR the front-right motor
     * @param backL  the back-left motor
     * @param backR  the back-right motor
     */
    public Mecanum(DcMotor frontL, DcMotor frontR, DcMotor backL, DcMotor backR) {
        this.frontL = frontL;
        this.frontR = frontR;
        this.backL = backL;
        this.backR = backR;
    }

    @Override
    public void move(double[] driveMatrix) {
        frontL.setPower(driveMatrix[0]);
        frontR.setPower(driveMatrix[1]);
        backL.setPower(driveMatrix[2]);
        backR.setPower(driveMatrix[3]);
    }

    @Override
    public void drive(Direction direction, double power) {
        switch (direction) {
            case BACKWARD:
                power = -power;
                // fall through
            case FORWARD:
                move(DriveMath.vectorToMotors(0, power, 0));
                break;
            case LEFT:
                power = -power;
                // fall through
            case RIGHT:
                move(DriveMath.vectorToMotors(power, 0, 0));
                break;
        }
    }

    @Override
    public void turn(TurnDirection turnDirection, double power) {
        switch (turnDirection) {
            case LEFT:
                power = -power;
                // fall through
            case RIGHT:
                move(DriveMath.vectorToMotors(0, 0, power));
                break;
        }
    }

    @Override
    public void stop() {
        move(DriveMath.vectorToMotors(0, 0, 0));
    }
}
