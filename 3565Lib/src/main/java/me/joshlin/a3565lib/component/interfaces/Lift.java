package me.joshlin.a3565lib.component.interfaces;

import me.joshlin.a3565lib.Sleep;

/**
 * Created by josh on 2/19/18.
 */

public interface Lift extends RobotSystem {
    void extend();

    default void extend(long milliseconds) {
        extend();
        new Sleep().sleep(milliseconds);
        stop();
    }

    void retract();

    default void retract(long milliseconds) {
        retract();
        new Sleep().sleep(milliseconds);
        stop();
    }

    void stop();

    public enum Status implements ComponentStatus {
        EXTENDING,
        RETRACTING,
        STOPPED
    }
}
