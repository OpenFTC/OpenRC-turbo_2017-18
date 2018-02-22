package me.joshlin.a3565lib.component.drivetrain;

import me.joshlin.a3565lib.Sleep;
import me.joshlin.a3565lib.enums.Direction;
import me.joshlin.a3565lib.enums.TurnDirection;

/**
 * @author Josh
 *         Describes required methods for drivetrains.
 */

public interface Drivetrain {
    /**
     * Moves the robot using raw motor values.
     *
     * @param driveMatrix a matrix that carries the values for each motor
     */
    void move(double[] driveMatrix);

    /**
     * Moves the robot in a specified direction at a specified power.
     * Works for both strafing and non-strafing robots (throws IllegalArgumentException if invalid)
     *
     * @param direction the direction for a robot to move
     * @param power     the power (between -1 and 1) for the robot to move at
     * @see Direction
     */
    void drive(Direction direction, double power);

    /**
     * Moves the robot in a specified direction at a specified power for a specified time.
     * Works for both strafing and non-strafing robots (throws IllegalArgumentException if invalid)
     *
     * @param direction    the direction for the robot to move
     * @param power        the power (between -1 and 1) for the robot to move at
     * @param milliseconds the duration of the move
     * @see Direction
     */
    default void drive(Direction direction, double power, long milliseconds) {
        drive(direction, power);
        new Sleep().sleep(milliseconds);
        stop();
    }

    /**
     * Turns the robot in a specified direction at a specified power.
     *
     * @param turnDirection the direction for the robot to turn
     * @param power         the power (betwen -1 and 1) for the robot to turn at
     * @see TurnDirection
     */
    void turn(TurnDirection turnDirection, double power);

    /**
     * Turns the robot in a specified direction at a specified power.
     *
     * @param turnDirection the direction for the robot to turn
     * @param power         the power (betwen -1 and 1) for the robot to turn at
     * @param milliseconds  the duration of the turn
     * @see TurnDirection
     */
    default void turn(TurnDirection turnDirection, double power, long milliseconds) {
        turn(turnDirection, power);
        new Sleep().sleep(milliseconds);
        stop();
    }

    /**
     * Stops the robot.
     */
    void stop();
}
