package org.firstinspires.ftc.teamcode.swivel;

/**
 * Created by josh on 1/5/18.
 */

public abstract class AbstractSwivel {;
    double left, center, right;

    AbstractSwivel() {
        this.left = 0.2;
        this.center = 0.5;
        this.right = 0.8;
    }

    AbstractSwivel(double left, double center, double right) {
        this.left = left;
        this.center = center;
        this.right = right;
    }

    public abstract void swivelLeft();
    public abstract void swivelCenter();
    public abstract void swivelRight();

    public abstract void setPosition(double position);
    public abstract double getPosition();
}
