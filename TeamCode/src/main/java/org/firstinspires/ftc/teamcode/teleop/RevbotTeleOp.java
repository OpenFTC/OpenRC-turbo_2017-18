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
    private double[] currentDirection = new double[3];
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

    private void setCurrentDirection(double[] currentDirection) {
        this.currentDirection = currentDirection;
    }

    public abstract double[] getUserInput();

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
            setCurrentDirection(getUserInput());
            inputHandler.handleInput();
            /*
            if (inputHandler.useSavedDirection) {
                setCurrentDirection(getSavedDirection());
            }

            if (inputHandler.lockFourAxis) {
                setCurrentDirection(lockFourAxis(getCurrentDirection()));
            }

            setCurrentDirection(gear.gearMovement(getCurrentDirection()));
            */

            drivetrain.move(getCurrentDirection());

            telemetry.addData("Status", "Running");
            telemetry.addData("Gearing", gear.getCurrentGear());
            telemetry.update();
        }
    }

    /**
     * Controls:
     * See github.com
     */

    class InputHandler {
        static final double MIN_TRIGGER_VALUE = 0.1;
        OneServoClaw relicClaw;
        CubeClaw cubeClaw;
        BallKnock ballKnock;
        AbstractLift armWinch, cubeLift, relicSlide;

        void init(Revbot robot) {
            relicClaw = new RelicClaw(robot.relicClaw);
            cubeClaw = new CubeClaw(robot.clawLeft, robot.clawRight, 0.2, 0.8);
            ballKnock = new BallKnock(robot.fondler);
            armWinch = new ArmWinch(robot.armWinch);
            cubeLift = new CubeLift(robot.cubeLift);
            relicSlide = new RelicSlide(robot.relicSlide);
        }

        void handleInput() {
            // D-Pad control (g1, g2)
            if (gamepad1.dpad_up || gamepad2.dpad_up) {
                cubeLift.raise();
            } else if (gamepad1.dpad_down || gamepad2.dpad_down) {
                cubeLift.lower();
            } else {
                cubeLift.stop();
            }

            // Face button control (Relic/Endgame) (g1)
            if (gamepad1.a) {
                setCurrentDirection(new double[]{1., 1., 0.});
            }

            if (gamepad1.b) {
                relicClaw.close();
            } else if (gamepad1.x) {
                relicClaw.open();
            }

            if (gamepad1.y) {
                relicSlide.raise();
            }

            // Shoulder button control (g1)
            if (gamepad1.left_bumper) {
                // Lock four axes
            }

            if (gamepad1.left_trigger > MIN_TRIGGER_VALUE) {
                // Vincent's analog gearing
            }

            if (gamepad1.right_bumper) {
                // Save movement
            } else if (gamepad1.right_trigger > MIN_TRIGGER_VALUE) {
                // Recall movement
            }

            // Face button control (g2)
            if (gamepad2.b) {
                cubeClaw.getClaw2().open();
            } else if (gamepad2.x) {
                cubeClaw.getClaw1().open();
            }

            // Shoulder button control (g2)
            if (gamepad2.left_bumper && gamepad2.right_bumper) {
                gear.setCurrentGear(gear.START_SPEED);
            } else if (gamepad2.left_bumper) {
                gear.gearDown();
            } else if (gamepad2.right_bumper) {
                gear.gearUp();
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

        /**
         * Returns the gear that the robot will be at in 15 seconds' time
         * @param currentGear not the current gear
         */
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