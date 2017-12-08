package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
 mCZ';. '
import org.firstinspires.ftc.teamcode.robot.RevbotCommands;
import org.firstinspires.ftc.teamcode.robot.Revbot;

/**
 * Base Autonomous template. All autonomous classes should extend from this one.
 */

@SuppressWarnings("unused")
@Autonomous
@Disabled
public class AutoBase extends LinearOpMode {

    Revbot robot = new Revbot();
    RevbotCommands auto = new RevbotCommands();

    double distance;
    double distanceFromWall;
    String boxLocation;

    private boolean teamIsRed;
    private boolean positionIsTop;

    @Override
    public void runOpMode() throws InterruptedException {

        robot.init(this);
        auto.init(this, robot);
        auto.initVuforia();
        auto.initFondler();
        auto.initDrive();
        auto.initStrafe();
        auto.lowerWinch();
        auto.closeClaw();
        robot.beep();
        telemetry.addData("Status: ", "Initialized");
        telemetry.update();

        robot.cubeLift.setPower(0.5);
        sleep(500);
        robot.cubeLift.setPower(0);

        waitForStart();

    }
}
