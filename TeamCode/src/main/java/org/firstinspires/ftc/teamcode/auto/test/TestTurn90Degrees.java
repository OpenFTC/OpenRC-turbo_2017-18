package org.firstinspires.ftc.teamcode.auto.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.CVLinearOpMode;
import org.firstinspires.ftc.teamcode.hardwaremap.RevbotMecanum;

import me.joshlin.a3565lib.component.drivetrain.DriveMath;
import me.joshlin.a3565lib.enums.TurnDirection;

/**
 * Created by josh on 2/19/18.
 */

@Autonomous(name = "Test Turn 90", group = "test")
public class TestTurn90Degrees extends CVLinearOpMode {
    RevbotMecanum robot = new RevbotMecanum();

    double currentAngle;
    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);

        robot.ghostIMU.init();

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();

        robot.ghostIMU.resetAngle();

        while (opModeIsActive()) {
            do {
                currentAngle = robot.ghostIMU.getAngle();
                correctTurn(currentAngle, 90, 1);
                telemetry.addData("Current Angle", currentAngle);
                telemetry.update();
            } while (opModeIsActive() && !DriveMath.inRange(currentAngle, 90 - 1, 90 + 1));

            robot.drivetrain.stop();

            robot.beep();
            robot.ghostIMU.resetAngle();
            sleep(2000);
        }
    }

    protected void correctTurn(double currentAngle, double desiredAngle, double margin) {
        if (opModeIsActive() && currentAngle < desiredAngle - margin) {
            robot.drivetrain.turn(TurnDirection.LEFT, .1);
        } else if (opModeIsActive() && currentAngle > desiredAngle + margin) {
            robot.drivetrain.turn(TurnDirection.RIGHT, .1);
        } else {
            robot.drivetrain.stop();
        }
    }
}
