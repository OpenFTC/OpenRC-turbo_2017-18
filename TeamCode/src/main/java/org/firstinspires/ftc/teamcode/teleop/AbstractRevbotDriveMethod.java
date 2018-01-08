package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.claw.OneServoClaw;
import org.firstinspires.ftc.teamcode.claw.CubeClaw;
import org.firstinspires.ftc.teamcode.claw.RelicClaw;
import org.firstinspires.ftc.teamcode.drivetrain.AbstractDrivetrain;
import org.firstinspires.ftc.teamcode.drivetrain.RevbotDrivetrain;
import org.firstinspires.ftc.teamcode.lift.AbstractLift;
import org.firstinspires.ftc.teamcode.lift.ArmWinch;
import org.firstinspires.ftc.teamcode.lift.CubeLift;
import org.firstinspires.ftc.teamcode.lift.RelicSlide;
import org.firstinspires.ftc.teamcode.robot.Revbot;
import org.firstinspires.ftc.teamcode.swivel.BallKnock;

/**
 * Drive method (TeleOp) from which all methods extend from.
 */

public abstract class AbstractRevbotDriveMethod extends LinearOpMode {
    private Revbot robot = new Revbot();
    private InputHandler inputHandler = new InputHandler();

    // Double[] for saving joystick position and replicating direction/power
    // [0] is left value, [1] is right value, [2] is strafe value
    private double[] savedDirection = new double[2];
    double[] currentDirection = new double[2];
    private double gearValue = 1.0;


    private void setGearValue(double gearValue) {
        if (gearValue <= 1.0 || gearValue >= 0.0) {
            this.gearValue = gearValue;
        } else {
            this.gearValue = Math.round(gearValue);
        }
    }

    private double getGearValue() {
        return gearValue;
    }

    private double[] gear(double[] direction) {
        for (int i = 1; i < direction.length; i++)
            direction[i] *= gearValue;
        return direction;        
    }

    public abstract void setCurrentDirection(double[] direction);
    private double[] getCurrentDirection() {
        return currentDirection;
    }

    private void setSavedDirection(double[] direction) {
        this.savedDirection = direction;
    }
    private double[] getSavedDirection() {
        return savedDirection;
    }

    private double[] lockFourAxis(double[] direction) {
        if (Math.abs(direction[0]) >= Math.abs(direction[2])) {
            direction[2] = 0;
        } else {
            direction[0] = 0;
            direction[1] = 0;
        }

        return direction;
    }

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);
        inputHandler.init(robot);

        AbstractDrivetrain drivetrain = new RevbotDrivetrain(robot);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            setCurrentDirection(getCurrentDirection());
            inputHandler.handleInput();

            if (inputHandler.useSavedDirection) {
                setCurrentDirection(getSavedDirection());
            }

            if (inputHandler.lockFourAxis) {
                setCurrentDirection(lockFourAxis(getCurrentDirection()));
            }

            setCurrentDirection(gear(getCurrentDirection()));

            drivetrain.move(getCurrentDirection());
        }
    }

    /**
     * Controls:
     *
     *  Gamepad 1:
     *      LB: Activate smartDirect
     *      LT: Set hyperPrecision
     *
     *      RB: Save current drive/strafeDrivePower to directSave
     *      RT: (directSave) Control power
     *
     *      LStick-x: Set strafeDrivePower
     *      LStick-y: Set left/rightPower
     *
     *      RStick-x: Set turnPower
     *
     *      Up: Raise cube lift
     *      Down: Lower cube lift
     *      Left: Close claw
     *      Right: Open claw
     */

    class InputHandler {
        OneServoClaw relicClaw;
        CubeClaw cubeClaw;
        BallKnock ballKnock;
        AbstractLift armWinch, cubeLift, relicSlide;

        boolean lockFourAxis = false;
        boolean useSavedDirection = false;

        static final double MIN_TRIGGER_VALUE = 0.1;

        void init(Revbot robot) {
            relicClaw = new RelicClaw(robot.relicClaw);
            cubeClaw = new CubeClaw(robot.clawLeft, robot.clawRight, 0.2, 0.8);
            ballKnock = new BallKnock(robot.fondler, robot.color);
            armWinch = new ArmWinch(robot.armWinch);
            cubeLift = new CubeLift(robot.cubeLift);
            relicSlide = new RelicSlide(robot.relicSlide);
        }

        void handleInput() {
            if (gamepad1.x) {
                armWinch.raise();
            } else if (gamepad1.a) {
                armWinch.lower();
            } else {
                armWinch.stop();
            }

            if (gamepad1.dpad_up) {
                cubeLift.raise();
            } else if (gamepad1.dpad_down) {
                cubeLift.lower();
            } else {
                cubeLift.stop();
            }

            if (gamepad1.dpad_left) {
                cubeClaw.open();
            } else if (gamepad1.dpad_right) {
                cubeClaw.close();
            }
            
            if (gamepad1.left_bumper) {
                setGearValue(0.5);
            }
            
            if (gamepad1.right_bumper) {
                lockFourAxis(currentDirection);
            }

            if (gamepad1.left_trigger > MIN_TRIGGER_VALUE)
                setSavedDirection(getCurrentDirection());

            useSavedDirection = gamepad1.right_trigger > MIN_TRIGGER_VALUE;


            if (gamepad2.x) {
                ballKnock.swivelLeft();
            } else if (gamepad2.y) {
                ballKnock.swivelCenter();
            } else if (gamepad2.b) {
                ballKnock.swivelRight();
            }
            
            if (gamepad2.a) {
               if (gamepad2.left_trigger > MIN_TRIGGER_VALUE) {
                   cubeClaw.openLeft();
               } else {
                   cubeClaw.closeLeft();
               }
                
               if (gamepad2.right_trigger > MIN_TRIGGER_VALUE) {
                    cubeClaw.openRight();
               } else {
                   cubeClaw.closeRight();
               }
            }

            if (gamepad2.dpad_up) {
                relicSlide.raise();
            } else if (gamepad2.dpad_down) {
                relicSlide.lower();
            } else {
                relicSlide.stop();
            }

            if (gamepad2.dpad_left) {
                relicClaw.open();
            } else if (gamepad2.dpad_right) {
                relicClaw.close();
            }

            if (gamepad2.left_bumper) {
                setGearValue(getGearValue() - 0.1);
            } else if (gamepad2.right_bumper) {
                setGearValue(getGearValue() + 0.1);
            }
        }
    }
}