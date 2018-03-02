package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.hardwaremap.RevbotMecanum;

import me.joshlin.a3565lib.InputHandler;
import me.joshlin.a3565lib.RobotMap;

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
            robot.drivetrain.stop();
        }
    }

    @Override
    public void handleInput() {
        if (gamepad1.dpad_up || gamepad2.dpad_up) {
            robot.glyphLift.extend();
        } else if (gamepad1.dpad_down || gamepad2.dpad_down) {
            robot.glyphLift.retract();
        } else {
            robot.glyphLift.stop();
        }

        if (gamepad1.b || gamepad2.b) {
            robot.intake.in();
        } else if (gamepad1.x || gamepad2.x) {
            robot.intake.out();
        } else if (gamepad1.y || gamepad2.y) {
            robot.intake.stop();
        }

        if (gamepad1.dpad_left || gamepad2.dpad_left) {
            robot.flipperLift.extend();
        } else if (gamepad1.dpad_right|| gamepad2.dpad_right) {
            robot.flipperLift.retract();
        } else if (gamepad1.a|| gamepad2.a) {
            robot.flipperLift.lock();
        } else {
            robot.flipperLift.stop();
        }

        if (gamepad2.right_stick_y < 0) {
            robot.vertical.up();
        } else if (gamepad2.right_stick_y > 0) {
            robot.vertical.down();
        }
    }
}
