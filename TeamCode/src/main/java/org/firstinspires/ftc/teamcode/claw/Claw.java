package org.firstinspires.ftc.teamcode.claw;

/**
 * Abstract Claw
 */

public abstract class Claw {
    double closedPosition, openPosition;
    boolean open;

    public abstract void open();
    public abstract void close();

    public abstract double getPosition();

    public abstract void setPosition(double position);

    public boolean isOpen() {
        return open;
    }
}
