package me.joshlin.a3565lib.component.drivetrain;

import me.joshlin.a3565lib.Sleep;
import me.joshlin.a3565lib.enums.Direction;
import me.joshlin.a3565lib.enums.TurnDirection;

/**
 * TODO: Write info
 */

public interface Drivetrain {
    void move(double[] driveMatrix);

    default void move(double[] driveMatrix, long milliseconds) {
        move(driveMatrix);
        new Sleep().sleep(milliseconds);
        stop();
    }

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
