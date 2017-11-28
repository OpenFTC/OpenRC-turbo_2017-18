package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robot.RevbotHardware;
import org.firstinspires.ftc.teamcode.robot.RevbotValues;

/**
 * Drone Op Teleop code. Main teleop class.
 *
 * Controls:
 *
 *  Gamepad 1:
 *      LB: Activate smartDirect
 *      LT: Set hyperPrecision
 *
 *      RB: Save current drive/strafePower to directSave
 *      RT: (directSave) Control power
 *
 *      LStick-x: Set strafePower
 *      LStick-y: Set left/rightPower
 *
 *      RStick-x: Set turnPower
 *
 *      Up: Raise cube lift
 *      Down: Lower cube lift
 *      Left: Close claw
 *      Right: Open claw
 */

@SuppressWarnings("unused")
@TeleOp(name="Drone Op", group = "teleop")
public class DroneOp extends LinearOpMode {

    private RevbotHardware robot = new RevbotHardware();

    // Declare power variables
    private double drivePower;
    private double strafePower;
    private double turnPower;

    // TODO: fill in comment
    private double hyperPrecision;

    // Four-direction/free movement boolean toggle
    private boolean smartDirect;

    // Double[] for saving joystick position and replicating direction/power
    private double[] directSave = new double[3];

    // Strength/weight of hyperPrecision variable
    private static final double HP_STRENGTH = 4;

    // Other constants used
    private static final double MIN_DIRECTSAVE_VALUE = 0.1;

    @Override
    public void runOpMode() throws InterruptedException {

        // IMPORTANT: Initialize the hardwareMap
        robot.init(hardwareMap);

        // Init directSave array
        for (int i = 0; i < 3; i++) {
            directSave[i] = 0;
        }

        telemetry.addData("OpMode Status", "Initialized");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            hyperPrecision = gamepad1.left_trigger * HP_STRENGTH + 1;
            smartDirect = gamepad1.left_bumper;

            drivePower = -gamepad1.left_stick_y;

            turnPower = gamepad1.right_stick_x;

            strafePower = -gamepad1.left_stick_x;
            drivePower /= turnPower;

            strafePower /= hyperPrecision;
            drivePower /= hyperPrecision;
            turnPower /= hyperPrecision;

            // If directSave is on, use saved movement.
            if (gamepad1.left_trigger > MIN_DIRECTSAVE_VALUE) {

                robot.leftDrive.setPower(directSave[0] * gamepad1.right_trigger);
                robot.rightDrive.setPower(directSave[1] * gamepad1.right_trigger);
                robot.strafe.setPower(directSave[2] * gamepad1.right_trigger);

            } else {

                // Set drivePower conditions
                if (!smartDirect || (Math.abs(drivePower) >= Math.abs(strafePower))) {

                    robot.leftDrive.setPower(drivePower);
                    robot.rightDrive.setPower(drivePower);

                    if (smartDirect && Math.abs(drivePower) >= Math.abs(strafePower)) {

                        robot.strafe.setPower(0);

                    }

                }

                // Set strafePower conditions
                if (!smartDirect || (Math.abs(drivePower) < Math.abs(strafePower))) {

                    robot.strafe.setPower(strafePower);

                    if (smartDirect && Math.abs(drivePower) < Math.abs(strafePower)) {

                        robot.leftDrive.setPower(0);
                        robot.rightDrive.setPower(0);

                    }

                }

                // Save position to directSave
                if (gamepad1.right_bumper) {

                    directSave[0] = robot.leftDrive.getPower();
                    directSave[1] = robot.rightDrive.getPower();
                    directSave[2] = robot.strafe.getPower();

                }

            }

            // Set lift power
            if (gamepad1.dpad_up) {

                robot.cubeLift.setPower(1);

            } else if (gamepad1.dpad_down) {

                robot.cubeLift.setPower(-0.5);

            } else {

                robot.cubeLift.setPower(0);

            }

            // Set claw position
            if (gamepad1.dpad_left) {

                robot.clawLeft.setPosition(RevbotValues.LEFT_CLAW_CLOSED_VALUE);
                robot.clawRight.setPosition(RevbotValues.RIGHT_CLAW_CLOSED_VALUE);

            } else if (gamepad1.dpad_right) {

                robot.clawLeft.setPosition(RevbotValues.LEFT_CLAW_OPENED_VALUE);
                robot.clawRight.setPosition(RevbotValues.RIGHT_CLAW_OPENED_VALUE);

            }

            // Move fondler
            if (gamepad1.y) {

                robot.fondler.setPosition(RevbotValues.FONDLER_CENTER_VALUE);

            } else if (gamepad1.x) {

                robot.fondler.setPosition(RevbotValues.FONDLER_LEFT_VALUE);

            } else if (gamepad1.b) {

                robot.fondler.setPosition(RevbotValues.FONDLER_RIGHT_VALUE);

            }

            telemetry.addData("OpMode Status", "Running");
            telemetry.addData("Servo Position", robot.clawLeft.getPosition());
            telemetry.update();

        }

    }

}
