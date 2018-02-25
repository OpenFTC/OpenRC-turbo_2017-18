package me.joshlin.a3565lib.component.servo;

import com.qualcomm.robotcore.hardware.Servo;

import me.joshlin.a3565lib.component.interfaces.Pivot;

/**
 * @author Josh
 *         An implementation of a {@link Pivot} using a {@link Servo}.
 */

public class ServoPivot extends ServoHardwareComponent implements Pivot {
    /**
     * Constructor with default center values.
     * Center value defaults to 0.5.
     *
     * @param servo the servo to pass in
     * @param up    the up position of the pivot
     * @param down  the down position of the pivot
     */
    public ServoPivot(Servo servo, double up, double down) {
        this(servo, up, 0.5, down);
    }

    /**
     * Constructor.
     *
     * @param servo  the servo to pass in
     * @param up     the up position of the pivot
     * @param center the center position of the pivot
     * @param down   the down position of the pivot
     */
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
