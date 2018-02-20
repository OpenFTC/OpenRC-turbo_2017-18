package me.joshlin.a3565lib.component.interfaces;

/**
 * Created by josh on 2/19/18.
 */

public interface Intake extends RobotSystem {
    void in();

    void out();

    void stop();

    public enum Status implements ComponentStatus {
        IN,
        OUT,
        STOPPED
    }
}
