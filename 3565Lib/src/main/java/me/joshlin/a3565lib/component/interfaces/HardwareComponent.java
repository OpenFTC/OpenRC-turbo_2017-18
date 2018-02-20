package me.joshlin.a3565lib.component.interfaces;

/**
 * Created by josh on 2/19/18.
 */

public abstract class HardwareComponent {
    protected double one;
    protected double two;
    protected double three;
    protected ComponentStatus status;

    protected HardwareComponent(double one, double two, double three) {
        this.one = one;
        this.two = two;
        this.three = three;
    }

    public ComponentStatus getStatus() {
        return status;
    }

    protected void setStatus(ComponentStatus status) {
        this.status = status;
    }
}
