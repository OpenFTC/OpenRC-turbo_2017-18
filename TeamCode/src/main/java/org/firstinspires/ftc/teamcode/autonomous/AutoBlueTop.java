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

        auto.strafeLeft(0.4, 750);
        sleep(1000);

        auto.raiseWinch(4250);
        sleep(1000);

        auto.fondleBalls("blue");
        sleep(1000);

        auto.forward(0.5,750);
        sleep(1000);

        auto.strafeRight(0.5,750);

        robot.beep();
        sleep(1000);



        telemetry.addData("VuMark seen", boxLocation);

        auto.forward(500, 1);
        sleep(1000);


        sleep(1000);

        auto.turnLeft(0.5, 750);
        sleep(1000);

        auto.forward(0.5, 750);
        sleep(1000);

        robot.cubeLift.setPower(-0.25);
        sleep(500);
        robot.cubeLift.setPower(0);

        for (int i = 0; i < 3; i++) {
            robot.beep();
        }
    }
}
