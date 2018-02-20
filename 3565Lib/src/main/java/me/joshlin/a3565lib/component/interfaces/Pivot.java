package me.joshlin.a3565lib.component.interfaces;

/**
 * Created by josh on 2/19/18.
 */

public interface Pivot extends RobotSystem {
    void up();

    void center();

    void down();

    public enum Status implements ComponentStatus {
        UP,
        CENTER,
        DOWN
    }
}
