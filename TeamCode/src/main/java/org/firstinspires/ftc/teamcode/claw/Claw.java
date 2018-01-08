package org.firstinspires.ftc.teamcode.claw;

/**
 * Abstract Claw
 */

public abstract class Claw {
    double CLOSED_POSITION, OPEN_POSITION;

    public abstract void open();
    public abstract void close();

    public abstract void setPosition(double position);
    public abstract double getPosition();
}
