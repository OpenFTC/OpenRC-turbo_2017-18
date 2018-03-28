package me.joshlin.a3565lib.component.interfaces;

/**
 * @author Josh
 *         Describes required methods for all hardware components.
 */

public abstract class HardwareComponent implements RobotSystem {
    /**
     * First value.
     */
    protected double one;
    /**
     * Second value.
     */
    protected double two;
    /**
     * Third value.
     */
    protected double three;
    /**
     * Status of the component.
     */
    protected ComponentStatus status;

    /**
     * Constructor.
     *
     * @param one   the first value of the hardware component
     * @param two   the second value of the hardware component
     * @param three the third value of the hardware component
     */
    protected HardwareComponent(double one, double two, double three) {
        this.one = one;
        this.two = two;
        this.three = three;
    }

    @Override
    public ComponentStatus getStatus() {
        return status;
    }

    /**
     * Set the status of the component.
     * Should only be used internally.
     *
     * @param status the new status of the component
     */
    protected void setStatus(ComponentStatus status) {
        this.status = status;
    }

    //public abstract void lock();
}
