package org.firstinspires.ftc.teamcode.lift;

/**
 * Abstract class that all Lifts should extend from.
 */

public abstract class AbstractLift {
    public abstract void raise();
    public abstract void raise(long ms);

    public abstract void lower();
    public abstract void lower(long ms);

    public abstract void stop();
}
