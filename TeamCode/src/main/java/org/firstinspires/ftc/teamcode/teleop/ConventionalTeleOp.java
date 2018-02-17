package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RevbotConventional;

import me.joshlin.a3565lib.component.drivetrain.Conventional;
import me.joshlin.a3565lib.component.lift.Lift;
import me.joshlin.a3565lib.component.lift.MotorLift;
import me.joshlin.a3565lib.component.swivel.ServoSwivel;

/**
 * Created by 3565 on 2/16/2018.
 */

@TeleOp(name = "Conventional TeleOp", group = "conventional")
public class ConventionalTeleOp extends LinearOpMode {
    private RevbotConventional robot = new RevbotConventional();
    private Conventional drivetrain;

    private Lift glyphLift;
    private ServoSwivel flipper;

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);
        drivetrain = new Conventional(robot.left, robot.right);

        glyphLift = new MotorLift(robot.lift);
        flipper = new ServoSwivel(robot.glyphPivot, 0.32, 0.35, 0.38);

        robot.beep();

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            drivetrain.move(-gamepad1.left_stick_y, -gamepad1.right_stick_y);

            if (gamepad1.dpad_up) {
                glyphLift.raise();
            } else if (gamepad1.dpad_down) {
                glyphLift.lower();
            } else {
                glyphLift.stop();
            }

            if (gamepad1.dpad_left) {
                flipper.swivelLeft();
            } else if (gamepad1.dpad_right) {
                flipper.swivelRight();
            } else if (gamepad1.a) {
                flipper.swivelCenter();
            }

            if (gamepad1.b) {
                robot.sucking.setPower(1);
            } else if (gamepad1.x) {
                robot.sucking.setPower(0);
            } else if (gamepad1.y) {
                robot.sucking.setPower(-1);
            }

            telemetry.addData("Position", flipper.getServo().getPosition());
            telemetry.update();
        }
    }
}
