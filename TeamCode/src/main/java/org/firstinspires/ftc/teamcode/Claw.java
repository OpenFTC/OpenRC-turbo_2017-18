package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by josh on 1/5/18.
 */

public abstract class Claw {
    public abstract void open();
    public abstract void close();

    public abstract void setPosition(double position);
    public abstract double getPosition();
}
