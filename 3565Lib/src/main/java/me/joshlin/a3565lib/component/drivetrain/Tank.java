package me.joshlin.a3565lib.component.drivetrain;

import com.qualcomm.robotcore.hardware.DcMotor;

import me.joshlin.a3565lib.enums.Direction;
import me.joshlin.a3565lib.enums.TurnDirection;

/**
 * @author Josh
 *         A general tank/2-motor drive class, implementing Drivetrain.
 */

public class Tank implements Drivetrain {
    private DcMotor left;
    private DcMotor right;

    /**
     * Constructor.
     *
     * @param left  the left motor
     * @param right the right motor
     */
    public Tank(DcMotor left, DcMotor right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public void move(double[] driveMatrix) {
        left.setPower(driveMatrix[0]);
        right.setPower(driveMatrix[1]);
    }

    @Override
    public void drive(Direction direction, double power) {
        switch (direction) {
            case BACKWARD:
                power = -power;
                // fall through
            case FORWARD:
                move(new double[]{power, power});
                break;
            case LEFT:
                // fall through
            case RIGHT:
                throw new IllegalArgumentException("Tank drives can't strafe!");
        }
    }

    @Override
    public void turn(TurnDirection turnDirection, double power) {
        switch (turnDirection) {
            case LEFT:
                power = -power;
                // fall through
            case RIGHT:
                move(new double[]{power, -power});
                break;
        }
    }

    @Override
    public void stop() {
        move(new double[]{0, 0});
    }
}
