package me.joshlin.a3565lib.component.drivetrain;

import me.joshlin.a3565lib.Sleep;

/**
 * TODO: Write info
 */

public abstract class Drivetrain {
    public abstract void move(double[] driveMatrix);

    public void move(double[] driveMatrix, long milliseconds) {
        move(driveMatrix);
        new Sleep().javaIsDumb(milliseconds);
        move(DriveMath.vectorToMotors(0, 0, 0));
    }
}
