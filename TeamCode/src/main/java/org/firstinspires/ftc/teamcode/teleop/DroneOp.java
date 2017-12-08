package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robot.Revbot;
import org.firstinspires.ftc.teamcode.robot.TeleOpGamepad;

/**
 * Drone Op Teleop code. Main teleop class.
 *
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

@SuppressWarnings("unused")
@TeleOp(name="Drone Op", group = "teleop")
public class DroneOp extends LinearOpMode {

    private Revbot robot = new Revbot();
    private TeleOpGamepad teleOp = new TeleOpGamepad();

    // Declare power variables
    private double leftPower;
    private double rightPower;
    private double strafeDrivePower;
    private double turnPower;

    // TODO: fill in comment
    // Four-direction/free movement boolean togg
    private double hyperPrecision;

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
        robot.init(this);
        teleOp.init(this, robot);

        // Init directSave array
        for (int i = 0; i < 3; i++) {
            directSave[i] = 0;
        }

        telemetry.addData("OpMode Status", "Initialized");
        telemetry.update();

        waitForStart();

        robot.beep(); //Leo did this!

        while (opModeIsActive()) {

            hyperPrecision = gamepad1.left_trigger * HP_STRENGTH + 1;
            smartDirect = gamepad1.left_bumper;

            leftPower = -gamepad1.left_stick_y;
            rightPower = -gamepad1.left_stick_y;

            turnPower = gamepad1.right_stick_x;

            strafeDrivePower = -gamepad1.left_stick_x;
            leftPower += turnPower;
            rightPower -= turnPower;

            strafeDrivePower /= hyperPrecision;
            leftPower /= hyperPrecision;
            rightPower /= hyperPrecision;

            // If directSave is on, use saved movement.
            if (gamepad1.right_trigger > MIN_DIRECTSAVE_VALUE) {

                robot.leftDrive.setPower(directSave[0] * gamepad1.right_trigger);
                robot.rightDrive.setPower(directSave[1] * gamepad1.right_trigger);
                robot.strafeDrive.setPower(directSave[2] * gamepad1.right_trigger);

            } else {

                // Set drivePower conditions
                if (!smartDirect || (Math.abs(leftPower) >= Math.abs(strafeDrivePower))) {

                    robot.leftDrive.setPower(leftPower);
                    robot.rightDrive.setPower(rightPower);

                    if (smartDirect && Math.abs(leftPower) >= Math.abs(strafeDrivePower)) {

                        robot.strafeDrive.setPower(0);

                    }

                }

                // Set strafeDrivePower conditions
                if (!smartDirect || (Math.abs(leftPower) < Math.abs(strafeDrivePower))) {

                    robot.strafeDrive.setPower(strafeDrivePower);

                    if (smartDirect && Math.abs(leftPower) < Math.abs(strafeDrivePower)) {

                        robot.leftDrive.setPower(0);
                        robot.rightDrive.setPower(0);

                    }

                }

                // Save position to directSave
                if (gamepad1.right_bumper) {

                    directSave[0] = robot.leftDrive.getPower();
                    directSave[1] = robot.rightDrive.getPower();
                    directSave[2] = robot.strafeDrive.getPower();

                }

            }

            teleOp.pollInput(false);

            telemetry.addData("OpMode Status", "Running");
            telemetry.addData("Hyper Precision: ", hyperPrecision);
            telemetry.update();

        }

    }

}
