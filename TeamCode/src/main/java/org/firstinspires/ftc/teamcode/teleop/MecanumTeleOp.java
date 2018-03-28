package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.CVLinearOpMode;
import org.firstinspires.ftc.teamcode.hardwaremap.RevbotMecanum;

import me.joshlin.a3565lib.component.drivetrain.DriveMath;

/**
 * Created by 3565 on 2/16/2018.
 * <p>
 * 420 blazit erryday amarite?
 */

@TeleOp(name = "Mecanum TeleOp", group = "A TeleOp")
public class MecanumTeleOp extends CVLinearOpMode {
    private RevbotMecanum robot = new RevbotMecanum();

    private MecanumInputHandler inputHandler;
    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);
        inputHandler = new MecanumInputHandler(gamepad1, gamepad2);
        inputHandler.init(robot);


        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            robot.drivetrain.move(DriveMath.inputsToMotors(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x));
            inputHandler.handleInput();
            telemetry.addData("Status", "Running");
            telemetry.addData("Intake", robot.intake.getStatus());
            telemetry.addData("Flipper", robot.flipperLift.getStatus());
            telemetry.addData("Lift", robot.glyphLift.getStatus());
            telemetry.addData("Arm", robot.vertical.getStatus());
            telemetry.addData("Position", robot.flipperMotor.getCurrentPosition());

            telemetry.update();
        }
    }
}
