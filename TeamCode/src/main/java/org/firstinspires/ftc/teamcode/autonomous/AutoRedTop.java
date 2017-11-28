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
        // put common code in superclass
        super.runOpMode();
        waitForStart();

        // add autonomous code here
        strafeLeft(0.5, 1);
        telemetry.addData("Auto", "Strafe Left");
        telemetry.update();

        robot.beep();
        fondleBalls("red");
        robot.beep();

        strafeRight(0.5, 1);

        while (opModeIsActive()) {
            telemetry.addData("Blue", robot.color.blue());
            telemetry.addData("Red", robot.color.red());
            telemetry.update();
        }

    }

}
