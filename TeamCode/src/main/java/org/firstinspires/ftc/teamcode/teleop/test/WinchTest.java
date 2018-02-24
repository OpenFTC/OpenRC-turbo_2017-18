package org.firstinspires.ftc.teamcode.teleop.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RevbotMecanum;

import me.joshlin.a3565lib.component.servo.ServoHardwareComponent;
import me.joshlin.a3565lib.component.servo.ServoPivot;

/**
 * Created by josh on 2/17/18.
 */

@TeleOp(name = "Winch Test", group = "conventional")
public class WinchTest extends LinearOpMode {
    private RevbotMecanum robot = new RevbotMecanum();

    private ServoHardwareComponent flipper;

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);
        flipper = (ServoPivot) robot.flipper;
        flipper.setPosition(0.5);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.dpad_up) {
                flipper.setPosition(flipper.getPosition() + 0.01);
            } else if (gamepad1.dpad_down) {
                flipper.setPosition(flipper.getPosition() - 0.01);
            }

            telemetry.addData("Position", flipper.getPosition());
            telemetry.update();

            sleep(100);
        }
    }
}