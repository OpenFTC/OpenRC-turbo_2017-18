package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RevbotMecanum;

import me.joshlin.a3565lib.component.drivetrain.DriveMath;
import me.joshlin.a3565lib.component.drivetrain.Mecanum;

/**
 * Created by 3565 on 2/16/2018.
 *
 * 420 blazit erryday amarite?
 */

@TeleOp(name = "Mecanum TeleOp")
public class MecanumTeleOp extends LinearOpMode {
    private RevbotMecanum robot = new RevbotMecanum();
    private Mecanum drivetrain;

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);
        drivetrain = new Mecanum(robot.frontL, robot.frontR, robot.backL, robot.backR);

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            drivetrain.move(DriveMath.vectorToMotors(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x));
            telemetry.addData("Status", "Running");
            telemetry.update();
        }
    }
}
