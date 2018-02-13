package me.joshlin.a3565lib.component.drivetrain;


import me.joshlin.a3565lib.enums.Direction;

/**
 * TODO: Write info
 */

public abstract class Drivetrain {

    /** Move the robot using values.
     * Should be the only method that directly references motors.
     * @param direction an array[2] that takes [forward, right] as parameters
     */
    public abstract void move(double[] direction);

    public void strafe(Direction direction, double power) {
        double[] motorValues = new double[] {0., power};
        switch (direction) {
            case LEFT:
                motorValues[1] = -motorValues[1];
                //fall through
            case RIGHT:
                move(motorValues);
            default:
                throw new IllegalArgumentException("Invalid direction");
        }
    }

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

    // TODO: Implement this with a generic array system
    public void turn(Direction direction, double power, long ms) {
        turn(direction, power);
        new Sleep().javaIsDumb(ms);
        stopTurning();
    }

    public void turn(Direction direction, double power, double targetAngle) {
        turn(direction, power);

    }

    public void stopAllMovement() {
        stopStrafing();
        stopDriving();
        stopTurning();
    }
}
