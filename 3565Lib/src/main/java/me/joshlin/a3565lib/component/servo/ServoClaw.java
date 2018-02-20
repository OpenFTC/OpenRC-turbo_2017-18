package me.joshlin.a3565lib.component.servo;

import com.qualcomm.robotcore.hardware.Servo;

import me.joshlin.a3565lib.component.interfaces.Claw;

/**
 * Created by josh on 2/19/18.
 */

public class ServoClaw extends ServoHardwareComponent implements Claw {

    public ServoClaw(Servo servo) {
        this(servo, 1, 0);
    }

    public ServoClaw(Servo servo, double open, double close) {
        this(servo, open, close, 0);
    }

    public ServoClaw(Servo servo, double open, double close, double three) {
        super(servo, open, close, three);
    }

    @Override
    public void open() {
        positionOne();
        setStatus(Status.OPEN);
    }

    @Override
    public void close() {
        positionTwo();
        setStatus(Status.CLOSED);
    }
}
