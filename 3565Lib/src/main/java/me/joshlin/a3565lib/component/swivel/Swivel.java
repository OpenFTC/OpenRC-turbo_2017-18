package me.joshlin.a3565lib.component.swivel;

/**
 * Created by josh on 1/5/18.
 */

public abstract class Swivel {

    double left, center, right;

    Swivel() {
        this.left = 0.2;
        this.center = 0.5;
        this.right = 0.8;
    }

    Swivel(double left, double center, double right) {
        this.left = left;
        this.center = center;
        this.right = right;
    }

    public abstract void swivelLeft();

    public abstract void swivelCenter();

    public abstract void swivelRight();

    public abstract double getPosition();

    public abstract void setPosition(double position);
}
