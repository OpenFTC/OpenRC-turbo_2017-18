package me.joshlin.a3565lib.component.interfaces;

/**
 * @author Josh
 *         Describes required methods for robot systems (any component on a robot).
 */

public interface RobotSystem {
    /**
     * Gets the current status of the system.
     *
     * @return the status
     */
    ComponentStatus getStatus();

//    /**
//     * Locks the system in its current position.
//     */
//    void lock();
}
