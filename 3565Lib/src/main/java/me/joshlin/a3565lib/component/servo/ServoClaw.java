package me.joshlin.a3565lib.component.servo;

import com.qualcomm.robotcore.hardware.Servo;

import me.joshlin.a3565lib.component.interfaces.Claw;

/**
 * @author Josh
 * An implementation of a {@link Claw} using a {@link Servo}.
 */
// TODO: fill out comments
public class ServoClaw extends ServoHardwareComponent implements Claw {
    /**
     *
     * @param servo
     */
    public ServoClaw(Servo servo) {
        this(servo, 1, 0);
    }

    /**
     *
     * @param servo
     * @param open
     * @param close
     */
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
