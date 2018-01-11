package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Revbot;
import org.firstinspires.ftc.teamcode.claw.CubeClaw;
import org.firstinspires.ftc.teamcode.claw.OneServoClaw;
import org.firstinspires.ftc.teamcode.claw.RelicClaw;
import org.firstinspires.ftc.teamcode.drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.drivetrain.Slide;
import org.firstinspires.ftc.teamcode.lift.AbstractLift;
import org.firstinspires.ftc.teamcode.lift.ArmWinch;
import org.firstinspires.ftc.teamcode.lift.CubeLift;
import org.firstinspires.ftc.teamcode.lift.RelicSlide;
import org.firstinspires.ftc.teamcode.swivel.BallKnock;

/**
 * Drive method (TeleOp) from which all methods extend from.
 */

public abstract class RevbotTeleOp extends LinearOpMode {
    double[] currentDirection = new double[3];
    private Revbot robot = new Revbot();
    private InputHandler inputHandler = new InputHandler();

    private Gear gear = new Gear();

    // Double[] for saving joystick position and replicating direction/power
    // [0] is left value, [1] is right value, [2] is strafe value
    private double[] savedDirection = new double[3];

    private double[] getCurrentDirection() {
        for (int i = 0; i < currentDirection.length; i++) {
            currentDirection[i] = Range.clip(currentDirection[i], -1, 1);
        }
        return currentDirection;
    }

    public abstract void setCurrentDirection(double[] direction);

    private double[] getSavedDirection() {
        return savedDirection;
    }

    public void setSavedDirection(double[] savedDirection) {
        this.savedDirection = savedDirection;
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

        Drivetrain drivetrain = new Slide(robot);

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

            setCurrentDirection(gear.gearMovement(getCurrentDirection()));

            drivetrain.move(getCurrentDirection());

            telemetry.addData("Status", "Running");
            telemetry.addData("Gearing", gear.getCurrentGear());
            telemetry.update();
        }
    }

    /**
     * Controls:
     */

    class InputHandler {
        static final double MIN_TRIGGER_VALUE = 0.1;
        OneServoClaw relicClaw;
        CubeClaw cubeClaw;
        BallKnock ballKnock;
        AbstractLift armWinch, cubeLift, relicSlide;
        boolean lockFourAxis = false;
        boolean useSavedDirection = false;

        double prevSpeed;

        void init(Revbot robot) {
            relicClaw = new RelicClaw(robot.relicClaw);
            cubeClaw = new CubeClaw(robot.clawLeft, robot.clawRight, 0.2, 0.8);
            ballKnock = new BallKnock(robot.fondler);
            armWinch = new ArmWinch(robot.armWinch);
            cubeLift = new CubeLift(robot.cubeLift);
            relicSlide = new RelicSlide(robot.relicSlide);
        }

        void handleInput() {
            if (gamepad1.a) {
                setCurrentDirection(new double[]{1., 1., 0.});
            } else if (gamepad1.b) {
                setCurrentDirection(new double[]{-1., -1., 0.});
            }

            if (gamepad1.y) {
                armWinch.raise();
            } else if (gamepad1.x) {
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

            if (gamepad1.left_bumper && prevSpeed == -1) {
                prevSpeed = gear.getCurrentGear();
                gear.setCurrentGear(0.5);
            }
            if (!gamepad1.left_bumper && prevSpeed != -1) {
                gear.setCurrentGear(prevSpeed);
                prevSpeed = -1;
            }

            lockFourAxis = gamepad1.right_bumper;

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
                cubeClaw.getClaw1().setPosition(gamepad2.left_trigger);
                cubeClaw.getClaw2().setPosition(gamepad2.right_trigger);
            }

            if (gamepad2.dpad_up) {
                relicSlide.raise();
            } else {
                relicSlide.stop();
            }

            if (gamepad2.dpad_left) {
                relicClaw.open();
            } else if (gamepad2.dpad_right) {
                relicClaw.close();
            }

            if (gamepad2.left_bumper) {
                gear.gearDown();
            } else if (gamepad2.right_bumper) {
                gear.gearUp();
            }

            if (gamepad2.left_bumper && gamepad2.right_bumper) {
                gear.setCurrentGear(gear.START_SPEED);
            }
        }

    }

    class Gear {
        final double START_SPEED;
        private final double SPEED_INCREMENT;

        private double currentGear = 1.0;
        private boolean changingGears;

        Gear() {
            this(1.0, 0.1);
        }

        Gear(double startSpeed, double speedIncrement) {
            this.START_SPEED = startSpeed;
            this.SPEED_INCREMENT = speedIncrement;
        }

        public double getCurrentGear() {
            return currentGear;
        }

        private void setCurrentGear(double currentGear) {
            this.currentGear = Range.clip(currentGear, 0, 1.0);
        }

        public void gearUp() {
            if (!changingGears) {
                changingGears = true;
                setCurrentGear(currentGear + SPEED_INCREMENT);
                changingGears = false;
            }
        }

        public void gearDown() {
            if (!changingGears) {
                changingGears = true;
                setCurrentGear(currentGear - SPEED_INCREMENT);
                changingGears = false;
            }
        }

        public double[] gearMovement(double[] movement) {
            for (int i = 0; i < movement.length; i++) {
                movement[i] *= currentGear;
            }

            return movement;
        }
    }
}