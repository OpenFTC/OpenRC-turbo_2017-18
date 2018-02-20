package me.joshlin.a3565lib.component.servo;

import com.qualcomm.robotcore.hardware.Servo;

import me.joshlin.a3565lib.component.interfaces.HardwareComponent;

/**
 * Created by josh on 2/19/18.
 */

public abstract class ServoHardwareComponent extends HardwareComponent {

    Servo servo;

    public ServoHardwareComponent(Servo servo, double one, double two, double three) {
        super(one, two, three);
        this.servo = servo;
    }

    public void positionOne() {
        servo.setPosition(one);
    }

    public void positionTwo() {
        servo.setPosition(two);
    }

    public void positionThree() {
        servo.setPosition(three);
    }

    public double getPosition() {
        return servo.getPosition();
    }

    public void setPosition(double position) {
        servo.setPosition(position);
    }
}
