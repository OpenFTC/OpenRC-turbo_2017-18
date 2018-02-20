package me.joshlin.a3565lib.component.servo;

import com.qualcomm.robotcore.hardware.Servo;

import me.joshlin.a3565lib.component.interfaces.Pivot;

/**
 * Created by josh on 2/19/18.
 */

public class ServoPivot extends ServoHardwareComponent implements Pivot {

    public ServoPivot(Servo servo, double up, double down) {
        this(servo, up, 0.5, down);
    }

    public ServoPivot(Servo servo, double up, double center, double down) {
        super(servo, up, center, down);
    }

    @Override
    public void up() {
        positionOne();
        setStatus(Status.UP);
    }

    @Override
    public void center() {
        positionTwo();
        setStatus(Status.CENTER);
    }

    @Override
    public void down() {
        positionThree();
        setStatus(Status.DOWN);
    }
}
