package me.joshlin.a3565lib.component.interfaces;

/**
 * Created by josh on 2/19/18.
 */

public interface Claw extends RobotSystem {
    void open();

    void close();

    public enum Status implements ComponentStatus {
        OPEN,
        CLOSED
    }
}
