package me.joshlin.a3565lib.component.claw;

/**
 */

public abstract class Claw {
    double closedPosition, openPosition;
    boolean open;

    /**
     * Opens the claw
     */
    public abstract void open();

    /**
     * Closes the claw
     */
    public abstract void close();

    /**
     * Gets the current position of the claw
     *
     * @return the current location of the claw
     */
    public abstract double getPosition();

    /**
     * Sets the position of the claw.
     *
     * @param position the position to set the claw to
     */
    public abstract void setPosition(double position);

    /**
     * @return
     */
    public boolean isOpen() {
        return open;
    }
}
