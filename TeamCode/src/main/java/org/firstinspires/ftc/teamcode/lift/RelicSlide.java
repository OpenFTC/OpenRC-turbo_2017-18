package org.firstinspires.ftc.teamcode.lift;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by josh on 1/5/18.
 */

public class RelicSlide extends Lift {
    Servo servo;

    public RelicSlide(Servo servo) {
        this(servo, 0, 0.5, 1);
    }

    public RelicSlide(Servo servo, double down, double stop, double up) {
        super(down, stop, up);
        this.servo = servo;
    }

    @Override
    public void move(double power) {
        servo.setPosition(power);
    }
}
