package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.hardwaremap.RevbotMecanum;

import me.joshlin.a3565lib.InputHandler;
import me.joshlin.a3565lib.RobotMap;
import me.joshlin.a3565lib.component.motor.MotorHardwareComponent;

/**
 * @author Josh
 *         Handles the input for all mecanum drives.
 */

public class MecanumInputHandler extends InputHandler {
    private RevbotMecanum robot;

    public MecanumInputHandler(Gamepad gamepad1, Gamepad gamepad2) {
        super(gamepad1, gamepad2);
    }

    @Override
    public void init(RobotMap robotMap) {
        if (robotMap instanceof RevbotMecanum) {
            this.robot = (RevbotMecanum) robotMap;

            // Initialize components to starting position
            robot.vertical.up();
            robot.flipperLift.stop();
            robot.drivetrain.stop();
            robot.intake.stop();
        }
    }

    @Override
    public void handleInput() {
        // Move the lift
        if (gamepad1.dpad_up || gamepad2.dpad_up) {
            robot.glyphLift.extend();
        } else if (gamepad1.dpad_down || gamepad2.dpad_down) {
            robot.glyphLift.retract();
        } else {
            robot.glyphLift.stop();
        }

        // Move the intake
        if (gamepad1.b) {
            robot.intake.in();
        } else if (gamepad1.x) {
            robot.intake.out();
        } else if (gamepad1.y) {
            robot.intake.stop();
        }

//        if (gamepad2.b) {
//            robot.flipperMotor.setTargetPosition(0);
//        } else if (gamepad2.y) {
//            robot.flipperMotor.setTargetPosition(200);
//        }




        // Move the flipper
        if (gamepad1.dpad_left || gamepad2.dpad_left) {
            robot.flipperLift.extend();
        } else if (gamepad1.dpad_right|| gamepad2.dpad_right) {
            robot.flipperLift.retract();
        } else if (gamepad1.a|| gamepad2.a) {
            // robot.flipperLift.lock();
        } else if (gamepad2.b) {
            robot.flipper.down();
        } else if (gamepad2.x) {
            robot.flipper.up();
        } else if (gamepad2.y) {
            robot.flipper.center();
        } else {
            robot.flipperLift.stop();
        }

        // Move the arm
        if (gamepad2.right_stick_y < 0.05) {
            robot.vertical.up();
        } else if (gamepad2.right_stick_y > 0.05) {
            robot.vertical.down();
        }

        // Precise control of the flipper
        if (Math.abs(gamepad2.left_stick_y) > 0.05) {
            if (robot.flipperLift instanceof MotorHardwareComponent) {
                MotorHardwareComponent mhc = (MotorHardwareComponent) robot.flipperLift;
                mhc.setPower((-gamepad2.left_stick_y) / 2);
            }
        }
    }
}
