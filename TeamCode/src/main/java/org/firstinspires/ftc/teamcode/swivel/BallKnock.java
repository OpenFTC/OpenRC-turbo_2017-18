package org.firstinspires.ftc.teamcode.swivel;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.enums.Alliance;
import org.firstinspires.ftc.teamcode.robot.Revbot;
import org.firstinspires.ftc.teamcode.sensor.RevbotSensors;

/**
 * BallKnock class. Has knockBalls(Alliance alliance)
 */

public class BallKnock extends AbstractSwivel {
    private Servo servo;
    private RevbotSensors sensors;

    public BallKnock(Servo servo, ColorSensor color) {
        super();
        this.servo = servo;
        this.sensors = new RevbotSensors(color);
    }

    public BallKnock(Servo servo, ColorSensor color, double left, double center, double right) {
        super(left, center, right);
        this.servo = servo;
    }

    @Override
    public void swivelLeft() {
        servo.setPosition(left);
    }

    @Override
    public void swivelCenter() {
        servo.setPosition(center);
    }

    @Override
    public void swivelRight() {
        servo.setPosition(right);
    }

    @Override
    public void setPosition(double position) {
        servo.setPosition(position);
    }

    @Override
    public double getPosition() {
        return servo.getPosition();
    }

    public void knockBalls(Alliance alliance) {

        if ((alliance.equals(Alliance.BLUE) && sensors.isBlue())
                || (alliance.equals(Alliance.RED) && sensors.isRed())) {
            swivelRight();
        } else if ((alliance.equals(Alliance.BLUE) && sensors.isRed())
                || (alliance.equals(Alliance.RED) && sensors.isBlue())) {
            swivelLeft();
        } else {
            swivelRight();
            swivelLeft();
        }

        Revbot.sleep(1000);

    }
}
