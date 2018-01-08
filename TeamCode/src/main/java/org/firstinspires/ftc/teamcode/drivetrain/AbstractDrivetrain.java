package org.firstinspires.ftc.teamcode.drivetrain;

import org.firstinspires.ftc.teamcode.enums.Direction;
import org.firstinspires.ftc.teamcode.robot.Revbot;

/**
 * Created by josh on 1/5/18.
 */

public abstract class AbstractDrivetrain {
    public abstract void move(double[] direction);

    public abstract void strafe(Direction direction, double power);
    public abstract void stopStrafing();
    public void strafe(double power) {
        strafe(Direction.LEFT, power);
    }
    public void strafe(Direction direction, double power, long ms) {
        strafe(direction, power);
        Revbot.sleep(ms);
        stopStrafing();
    }

    public abstract void drive(Direction direction, double power);
    public abstract void stopDriving();
    public void drive(double power) {
        drive(Direction.BACKWARD, power);
    }
    public void drive(Direction direction, double power, long ms) {
        drive(direction, power);
        Revbot.sleep(ms);
        stopDriving();
    }

    public abstract void turn(Direction direction, double power);
    public abstract void stopTurning();
    public void turn(double power) {
        turn(Direction.LEFT, power);
    }
    public void turn(Direction direction, double power, long ms) {
        turn(direction, power);
        Revbot.sleep(ms);
        stopTurning();
    }

    public void stopAllMovement() {
        stopStrafing();
        stopDriving();
        stopTurning();
    }
}
