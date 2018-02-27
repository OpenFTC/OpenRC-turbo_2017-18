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
    RevbotMecanum robot;

    public MecanumInputHandler(Gamepad gamepad1, Gamepad gamepad2) {
        super(gamepad1, gamepad2);
    }

    @Override
    public void init(RobotMap robotMap) {
        if (robotMap instanceof RevbotMecanum) {
            this.robot = (RevbotMecanum) robotMap;
        }
    }

    @Override
    public void handleInput() {
        if (gamepad1.dpad_up) {
            robot.glyphLift.extend();
        } else if (gamepad1.dpad_down) {
            robot.glyphLift.retract();
        } else {
            robot.glyphLift.stop();
        }

        if (gamepad1.b) {
            robot.intake.in();
        } else if (gamepad1.x) {
            robot.intake.out();
        } else if (gamepad1.y) {
            robot.intake.stop();
        }

        if (gamepad1.dpad_left) {
            robot.flipper.up();
        } else if (gamepad1.dpad_right) {
            robot.flipper.down();
        } else if (gamepad1.a) {
            robot.flipper.center();
        }

        if (gamepad2.dpad_up) {
            robot.vertical.up();
        } else if (gamepad2.dpad_down) {
            robot.vertical.down();
        }
    }
}
