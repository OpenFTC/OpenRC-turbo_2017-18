package me.joshlin.a3565lib.component.interfaces;

/**
 * Created by josh on 2/19/18.
 */

public interface Arm extends RobotSystem {
    void left();

    void center();

    void right();

    public enum Status implements ComponentStatus {
        LEFT,
        CENTER,
        RIGHT
    }
}
