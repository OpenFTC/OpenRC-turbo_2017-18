package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RevbotMecanum;

import me.joshlin.a3565lib.component.drivetrain.DriveMath;
import me.joshlin.a3565lib.component.drivetrain.Mecanum;

/**
 * Created by 3565 on 2/16/2018.
 * <p>
 * 420 blazit erryday amarite?
 */

@TeleOp(name = "Mecanum TeleOp", group = "mecanum")
public class MecanumTeleOp extends LinearOpMode {
    private RevbotMecanum robot = new RevbotMecanum();
    private Mecanum drivetrain;

    private MecanumInputHandler inputHandler;

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
        drivetrain = new Mecanum(robot.frontL, robot.frontR, robot.backL, robot.backR);
        inputHandler = new MecanumInputHandler(gamepad1, gamepad2);
        inputHandler.init(robot);


        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            drivetrain.move(DriveMath.inputsToMotors(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x));
            inputHandler.handleInput();
            telemetry.addData("Status", "Running");
            telemetry.addData("Intake", inputHandler.getIntake().getStatus());
            telemetry.addData("Flipper", inputHandler.getFlipper().getStatus());
            telemetry.addData("Lift", inputHandler.getGlyphLift().getStatus());
            telemetry.update();
        }
    }
}
