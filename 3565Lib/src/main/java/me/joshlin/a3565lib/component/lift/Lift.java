package me.joshlin.a3565lib.component.lift;


import me.joshlin.a3565lib.Sleep;

/**
 * Abstract class that all Lifts should extend from.
 */

public abstract class Lift {
    double raiseSpeed, lowerSpeed, stopSpeed;

    // constructor method
    Lift(double down, double stop, double up) {
        this.lowerSpeed = down;
        this.stopSpeed = stop;
        this.raiseSpeed = up;
    }

    public abstract void raise();
    public void raise(long ms) {
        raise();
        new Sleep().javaIsDumb(ms);
        stop();
    }

    public abstract void lower();
    public void lower(long ms) {
        lower();
        new Sleep().javaIsDumb(ms);
        stop();
    }

    public abstract void stop();
}
