package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

 /**
 * Auto Red Top.
 */

@SuppressWarnings("unused")
@Autonomous(name = "Auto Red Top", group = "auto")
public class AutoRedTop extends AutoBase {

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        auto.strafeLeft(750, 0.4);
        sleep(500);

        auto.raiseWinch();
        sleep(500);

        auto.fondleBalls("red");
        sleep(500);

        auto.backward(500,0.75);
        sleep(500);

        auto.strafeRight(750,0.5);

        robot.beep();
        sleep(500);

        boxLocation = auto.readImage();

        telemetry.addData("VuMark seen", boxLocation);

        auto.backward(500, 1);
        sleep(500);

        switch (boxLocation) {
            case "RIGHT":
                auto.backward(500, 0.5);
                break;

            case "CENTER":
                auto.backward(1000, 0.5);
                break;

            case "LEFT":
                auto.backward(1500, 0.5);
                break;

            default:
                auto.backward(750, 0.5);
                //help will has me trapped in a room where i can't do anything but code
                break;
        }
        sleep(500);

        auto.turnLeft(1500, 0.5);
        sleep(500);

        auto.forward(1000, 0.5);
        sleep(500);

        robot.cubeLift.setPower(-0.25);
        sleep(500);
        robot.cubeLift.setPower(0);

        for (int i = 0; i < 3; i++) {
            robot.beep();
        }
    }

}
