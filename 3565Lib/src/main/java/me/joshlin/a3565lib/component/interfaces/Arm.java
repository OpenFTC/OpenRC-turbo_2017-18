package me.joshlin.a3565lib.component.interfaces;

/**
 * @author Josh
 *         Describes required methods for arms.
 */

public interface Arm extends RobotSystem {
    /**
     * Sets the arm to the left position.
     */
    void left();

    /**
     * Sets the arm to the center position.
     */
    void center();

    /**
     * Sets the arm to the right position.
     */
    void right();

    /**
     * Enumerates statuses that an arm can have.
     */
    enum Status implements ComponentStatus {
        LEFT,
        CENTER,
        RIGHT
    }
}
