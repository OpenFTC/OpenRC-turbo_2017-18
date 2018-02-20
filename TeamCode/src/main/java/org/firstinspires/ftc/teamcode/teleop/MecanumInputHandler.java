package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.RevbotMecanum;

import me.joshlin.a3565lib.InputHandler;
import me.joshlin.a3565lib.component.RobotMap;
import me.joshlin.a3565lib.component.hardware.MultiDcMotor;
import me.joshlin.a3565lib.component.interfaces.Intake;
import me.joshlin.a3565lib.component.interfaces.Lift;
import me.joshlin.a3565lib.component.interfaces.Pivot;
import me.joshlin.a3565lib.component.motor.MotorIntake;
import me.joshlin.a3565lib.component.motor.MotorLift;
import me.joshlin.a3565lib.component.servo.ServoPivot;

/**
 * Created by josh on 2/18/18.
 */

public class MecanumInputHandler extends InputHandler {
    RevbotMecanum robot;

    private Pivot flipper;
    private DcMotor intakeMotor;
    private Lift glyphLift;
    private Intake intake;

    public MecanumInputHandler(Gamepad gamepad1, Gamepad gamepad2) {
        super(gamepad1, gamepad2);
        this.robot = robot;
    }

    public Pivot getFlipper() {
        return flipper;
    }

    public Lift getGlyphLift() {
        return glyphLift;
    }

    public Intake getIntake() {
        return intake;
    }

    @Override
    public void init(RobotMap robotMap) {
        if (robotMap instanceof RevbotMecanum) {
            this.robot = (RevbotMecanum) robotMap;
            flipper = new ServoPivot(robot.flipper, 0.40, 0.38, 0.35);
            intakeMotor = new MultiDcMotor(robot.leftSpinner, robot.rightSpinner);
            glyphLift = new MotorLift(robot.lift);
            intake = new MotorIntake(intakeMotor);
        }
    }

    @Override
    public void handleInput() {
        if (gamepad1.dpad_up) {
            glyphLift.extend();
        } else if (gamepad1.dpad_down) {
            glyphLift.retract();
        } else {
            glyphLift.stop();
        }

        if (gamepad1.b) {
            intake.in();
        } else if (gamepad1.x) {
            intake.out();
        } else if (gamepad1.y) {
            intake.stop();
        }

        if (gamepad1.dpad_left) {
            flipper.up();
        } else if (gamepad1.dpad_right) {
            flipper.down();
        } else if (gamepad1.a) {
            flipper.center();
        }
    }
}
