package me.joshlin.a3565lib.component.interfaces;

import me.joshlin.a3565lib.Sleep;

/**
 * @author Josh
 *         Describes required methods for lifts.
 */

public interface Lift extends RobotSystem {
    /**
     * Extends the lift.
     */
    void extend();

    /**
     * Extends the lift for a specified amount of time.
     *
     * @param milliseconds the amount of ms to extend the lift for.
     */
    default void extend(long milliseconds) {
        extend();
        new Sleep().sleep(milliseconds);
        stop();
    }

    /**
     * Retracts the lift.
     */
    void retract();

    /**
     * Retracts the lift for a specified amount of time.
     *
     * @param milliseconds the amount of ms to retract the lift for
     */
    default void retract(long milliseconds) {
        retract();
        new Sleep().sleep(milliseconds);
        stop();
    }

    /**
     * Stops the lift.
     */
    void stop();

    /**
     * Enumerates statuses that a lift can have.
     */
    enum Status implements ComponentStatus {
        EXTENDING,
        RETRACTING,
        STOPPED
    }
}
