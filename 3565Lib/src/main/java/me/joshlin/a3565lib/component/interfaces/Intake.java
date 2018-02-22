package me.joshlin.a3565lib.component.interfaces;

/**
 * @author Josh
 *         Describes required methods for intakes.
 */

public interface Intake extends RobotSystem {
    /**
     * Sets the intake to intake.
     */
    void in();

    /**
     * Sets the intake to eject.
     */
    void out();

    /**
     * Stop the intake.
     */
    void stop();

    /**
     * Enumerates statuses that an intake can have.
     */
    enum Status implements ComponentStatus {
        IN,
        OUT,
        STOPPED
    }
}
