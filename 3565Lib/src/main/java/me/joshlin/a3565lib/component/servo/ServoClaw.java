package me.joshlin.a3565lib.component.servo;

import com.qualcomm.robotcore.hardware.Servo;

import me.joshlin.a3565lib.component.interfaces.Claw;

/**
 * @author Josh
 *         An implementation of a {@link Claw} using a {@link Servo}.
 */
public class ServoClaw extends ServoHardwareComponent implements Claw {
    /**
     * Constructor with default values.
     * <p>
     * Open position defaults to 1
     * Close position defaults to 0
     *
     * @param servo the servo to pass in
     */
    public ServoClaw(Servo servo) {
        this(servo, 1, 0);
    }

    /**
     * Constructor.
     *
     * @param servo the servo to pass in
     * @param open  the open position of the claw
     * @param close the close position of the claw
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
