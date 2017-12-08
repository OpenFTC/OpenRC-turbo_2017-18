package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Auto Red Top.
 */

@SuppressWarnings("unused")
@Autonomous(name = "Auto Red Top", group = "auto")
public class AutoRedTop extends AutoBase {

    private String boxLocation;

    @Override
    public void runOpMode() throws InterruptedException {
        // put common code in superclass
        super.runOpMode();
        waitForStart();

        // add autonomous code here
        strafeDriveLeft(1, 0.5);
        telemetry.addData("Auto", "strafeDrive Left");
        telemetry.update();

        robot.beep();
        fondleBalls("red");
        robot.cubeLift.setPower(0.5);
        sleep(500);
        robot.cubeLift.setPower(0);

        strafeDriveRight(0.5);
        backward(0.5);
        sleep(900);
        strafeDriveRight(0);
        backward(0);
        robot.beep();
        sleep(500);

        boxLocation = readImage();

        backward(500, 0.5);
        sleep(500);

        switch (boxLocation) {
            case "RIGHT":
                backward(500, 0.5);
                break;

            case "CENTER":
                backward(1000, 0.5);
                break;

            case "LEFT":
                backward(1500, 0.5);
                break;

            default:
                backward(750, 0.5);
                break;
        }

        robot.cubeLift.setPower(-0.5);
        sleep(500);
        robot.cubeLift.setPower(0);
        turnLeft(0.5, 0.75);
        sleep(500);
        forward(1000, 1);

        for (int i = 0; i < 3; i++) {
            robot.beep();
        }
    }

}
