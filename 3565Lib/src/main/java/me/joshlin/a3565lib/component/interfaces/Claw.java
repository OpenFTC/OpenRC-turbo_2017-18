package me.joshlin.a3565lib.component.interfaces;

/**
 * @author Josh
 *         Describes required methods for claws.
 */

public interface Claw extends RobotSystem {
    /**
     * Opens the claw.
     */
    void open();

    /**
     * Closes the claw.
     */
    void close();

    /**
     * Enumerates statuses that a claw can have.
     */
    enum Status implements ComponentStatus {
        OPEN,
        CLOSED
    }
}
