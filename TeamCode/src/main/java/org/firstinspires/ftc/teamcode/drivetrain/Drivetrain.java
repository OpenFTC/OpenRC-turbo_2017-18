package org.firstinspires.ftc.teamcode.drivetrain;

import org.firstinspires.ftc.teamcode.Revbot;
import org.firstinspires.ftc.teamcode.Sleep;
import org.firstinspires.ftc.teamcode.enums.Direction;

/**
 * Generic drivetrain class.
 */

public abstract class Drivetrain {
    public abstract void move(double[] direction);

    public abstract void strafe(Direction direction, double power);

    public abstract void stopStrafing();

    public void strafe(Direction direction, double power, long ms) {
        strafe(direction, power);
        new Sleep().javaIsDumb(ms);
        stopStrafing();
    }

    public abstract void drive(Direction direction, double power);

    public abstract void stopDriving();

    public void drive(Direction direction, double power, long ms) {
        drive(direction, power);
        new Sleep().javaIsDumb(ms);
        stopDriving();
    }

    public abstract void turn(Direction direction, double power);

    public abstract void stopTurning();

    public void turn(Direction direction, double power, long ms) {
        turn(direction, power);
        new Sleep().javaIsDumb(ms);
        stopTurning();
    }

    public void stopAllMovement() {
        stopStrafing();
        stopDriving();
        stopTurning();
    }
}
