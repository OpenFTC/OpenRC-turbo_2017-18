package org.firstinspires.ftc.teamcode.teleop.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RevbotMecanum;

/**
 * Created by josh on 2/17/18.
 */

@TeleOp(name = "Winch Test", group = "conventional")
public class WinchTest extends LinearOpMode {
    RevbotMecanum robot = new RevbotMecanum();

    /**
     * Override this method and place your code here.
     * <p>
     * Please do not swallow the InterruptedException, as it is used in cases
     * where the op mode needs to be terminated early.
     *
     * @throws InterruptedException
     */
    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);
        robot.flipper.setPosition(0.5);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.dpad_up) {
                robot.flipper.setPosition(robot.flipper.getPosition() + 0.01);
            } else if (gamepad1.dpad_down) {
                robot.flipper.setPosition(robot.flipper.getPosition() - 0.01);
            }

            telemetry.addData("Position", robot.flipper.getPosition());
            telemetry.update();

            sleep(100);
        }
    }
}
