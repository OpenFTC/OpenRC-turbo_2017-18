package org.firstinspires.ftc.teamcode;

/**
 * Created by josh on 1/5/18.
 */

public abstract class Lift {

    public abstract void raise();
    public abstract void raise(long ms);

    public abstract void lower();
    public abstract void lower(long ms);

    public abstract void stop();
}
