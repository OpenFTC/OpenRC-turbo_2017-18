package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by 3565 on 12/8/2017.
 */

@SuppressWarnings("unused")
@Autonomous(name = "Auto Blue Top", group = "auto")
public class AutoBlueTop extends AutoBase {
    
    private static final double DEFAULT_MOTOR_SPEED = 0.5;

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        auto.strafeLeft(750, 0.4);
        sleep(1000);

        auto.raiseWinch();
        sleep(1000);

        auto.fondleBalls("blue");
        sleep(1000);

        auto.forward(750,0.5);
        sleep(1000);

        auto.strafeRight(750,0.5);

        robot.beep();
        sleep(1000);

        boxLocation = auto.readImage();

        telemetry.addData("VuMark seen", boxLocation);

        auto.forward(500, 1);
        sleep(1000);

        switch (boxLocation) {
            case "RIGHT":
                auto.forward(500, DEFAULT_MOTOR_SPEED);
                break;

            case "CENTER":
                auto.forward(1000, DEFAULT_MOTOR_SPEED);
                break;

            case "LEFT":
                auto.forward(1500, DEFAULT_MOTOR_SPEED);
                break;

            default:
                auto.forward(750, DEFAULT_MOTOR_SPEED);
                //help will has me trapped in a room where i can't do anything but code
                break;
        }
        sleep(1000);

        auto.turnLeft(750, DEFAULT_MOTOR_SPEED);
        sleep(1000);

        auto.forward(750, DEFAULT_MOTOR_SPEED);
        sleep(1000);

        robot.cubeLift.setPower(-0.25);
        sleep(500);
        robot.cubeLift.setPower(0);

        for (int i = 0; i < 3; i++) {
            robot.beep();
        }
    }
}
