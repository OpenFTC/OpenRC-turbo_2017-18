package me.joshlin.a3565lib.component.drivetrain;

import me.joshlin.a3565lib.Sleep;
import me.joshlin.a3565lib.enums.Direction;
import me.joshlin.a3565lib.enums.TurnDirection;

/**
 * @author Josh
 * Describes required methods for drivetrains.
 */

public interface Drivetrain {
    /**
     * Moves the robot using raw motor values.
     * @param driveMatrix a matrix that carries the values for each motor
     */
    void move(double[] driveMatrix);

    /**
     * Moves the robot in a specified direction at a specified power.
     * Works for both strafing and non-strafing robots (return @link
     * IllegalArgumentException if invalid)
     * @param direction the direction for a robot to move
     * @param power
     * @see Direction
     */
    void drive(Direction direction, double power);

    default void drive(Direction direction, double power, long milliseconds) {
        drive(direction, power);
        new Sleep().sleep(milliseconds);
        stop();
    }

    void turn(TurnDirection turnDirection, double power);

    default void turn(TurnDirection turnDirection, double power, long milliseconds) {
        turn(turnDirection, power);
        new Sleep().sleep(milliseconds);
        stop();
    }

    void stop();
}
