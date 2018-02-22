package me.joshlin.a3565lib.component.interfaces;

/**
 * @author Josh
 *         Describes required methods for pivots.
 */

public interface Pivot extends RobotSystem {
    /**
     * Set the pivot to the up position.
     */
    void up();

    /**
     * Set the pivot to the center position.
     */
    void center();

    /**
     * Set the pivot to the down position.
     */
    void down();

    /**
     * Enumerate the statuses that a pivot can have.
     */
    enum Status implements ComponentStatus {
        UP,
        CENTER,
        DOWN
    }
}
