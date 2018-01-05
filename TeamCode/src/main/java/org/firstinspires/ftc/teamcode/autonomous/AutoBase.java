package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.robot.RevbotCommands;
import org.firstinspires.ftc.teamcode.robot.Revbot;
import org.firstinspires.ftc.teamcode.robot.RevbotValues;
import org.firstinspires.ftc.teamcode.robot.VuforiaCommands;

/**
 * Base Autonomous template. All autonomous classes should extend from this one.
 */

@SuppressWarnings("unused")
@Autonomous
@Disabled
public class AutoBase extends LinearOpMode {

    Revbot robot = new Revbot();
    RevbotCommands revbot = new RevbotCommands();
    AutonomousCommands auto = new AutonomousCommands();
    VuforiaCommands vuforia = new VuforiaCommands();

    double distance;
    double distanceFromWall;
    String boxLocation;

    private String teamColor;

    @Override
    public void runOpMode() throws InterruptedException {

        robot.init(this);
        revbot.init(this, robot);
        auto.init(this, robot);
        vuforia.init(this, robot);
        revbot.closeClaw();
        revbot.fondleCenter();
        revbot.stopDriving();
        revbot.stopStrafing();
        auto.lowerWinch(4125);
        robot.beep();
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        robot.cubeLift.setPower(0.25);
        sleep(500);
        robot.cubeLift.setPower(0);

        waitForStart();

        auto.strafeLeft(0.4, 750);
        sleep(500);

        revbot.raiseWinch();
        sleep(500);

        auto.fondleBalls("red");
        sleep(500);

        boxLocation = vuforia.readImage();

        auto.move(0.75
                , 500
                , teamColor.equals(RevbotValues.COLOR_RED) ? RevbotValues.DIRECTION_BACKWARD : RevbotValues.DIRECTION_FORWARD);
        sleep(500);

        auto.strafeRight(0.5,750);

        robot.beep();
        sleep(500);

        auto.putCubeInCorrectCryptobox(teamColor.equals(RevbotValues.COLOR_RED) ? RevbotValues.DIRECTION_BACKWARD : RevbotValues.DIRECTION_FORWARD, boxLocation);

        auto.turnLeft(0.5, 1500);
        sleep(500);

        auto.forward(0.5, 1000);
        sleep(500);

        robot.cubeLift.setPower(-0.25);
        sleep(500);
        robot.cubeLift.setPower(0);

        for (int i = 0; i < 3; i++) {
            robot.beep();
        }

    }
}
