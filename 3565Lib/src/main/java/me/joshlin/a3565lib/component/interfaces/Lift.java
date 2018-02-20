package me.joshlin.a3565lib.component.interfaces;

/**
 * Created by josh on 2/19/18.
 */

public interface Lift extends RobotSystem {
    void extend();

    void retract();

    void stop();

    public enum Status implements ComponentStatus {
        EXTENDING,
        RETRACTING,
        STOPPED
    }
}
