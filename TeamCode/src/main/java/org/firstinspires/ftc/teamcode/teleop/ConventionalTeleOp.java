package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RevbotConventional;

import me.joshlin.a3565lib.component.drivetrain.Conventional;
import me.joshlin.a3565lib.component.lift.Lift;
import me.joshlin.a3565lib.component.lift.MotorLift;
import me.joshlin.a3565lib.component.swivel.ServoSwivel;
import me.joshlin.a3565lib.component.swivel.Swivel;

/**
 * Created by 3565 on 2/16/2018.
 */

@TeleOp(name = "Conventional TeleOp")
public class ConventionalTeleOp extends LinearOpMode {
    private RevbotConventional robot = new RevbotConventional();
    private Conventional drivetrain;

    private Lift glyphLift;
    private Swivel flipper;

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);
        drivetrain = new Conventional(robot.left, robot.right);

        glyphLift = new MotorLift(robot.lift);
        flipper = new ServoSwivel(robot.glyphPivot, 0.01, 0.04, 0.07);

        robot.beep();

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            drivetrain.move(gamepad1.left_stick_y, gamepad1.right_stick_y);

            if (gamepad1.dpad_up) {
                glyphLift.raise();
            } else if (gamepad1.dpad_down) {
                glyphLift.lower();
            } else {
                glyphLift.stop();
            }

            if (gamepad1.dpad_left) {
                flipper.swivelRight();
            } else if (gamepad1.dpad_right) {
                flipper.swivelLeft();
            } else if (gamepad1.a) {
                flipper.swivelCenter();
            }

            if (gamepad1.b) {
                robot.sucking.setPower(1);
            } else if (gamepad1.x) {
                robot.sucking.setPower(0);
            }
        }
    }
}
