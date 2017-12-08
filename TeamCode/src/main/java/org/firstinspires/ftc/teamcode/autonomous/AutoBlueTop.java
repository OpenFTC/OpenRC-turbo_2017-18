package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by 3565 on 12/8/2017.
 */

@SuppressWarnings("unused")
@Autonomous(name = "Auto Blue Top", group = "auto")
public class AutoBlueTop extends AutoBase {

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        auto.strafeLeft(1000, 0.5);
        auto.fondleBalls("blue");
        auto.raiseWinch();
        auto.strafeRight(0.5);
        auto.forward(0.5);
        sleep(900);
        auto.strafeLeft(0);
        auto.forward(0);
        robot.beep();
        sleep(500);

        boxLocation = auto.readImage();

        auto.forward(500, 0.5);
        sleep(500);

        switch (boxLocation) {
            case "RIGHT":
                auto.forward(500, 0.5);
                break;

            case "CENTER":
                auto.forward(1000, 0.5);
                break;

            case "LEFT":
                auto.forward(1500, 0.5);
                break;

            default:
                auto.forward(750, 0.5);
                //help will has me trapped in a room where i can't do anything but code
                break;
        }

        robot.cubeLift.setPower(-0.5);
        sleep(500);
        robot.cubeLift.setPower(0);
        auto.turnRight(500, 0.75);
        sleep(500);
        auto.forward(1000, 1);


        for (int i = 0; i < 3; i++) {
            robot.beep();
        }
    }
}
