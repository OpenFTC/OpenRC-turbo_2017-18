package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robot.Revbot;

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
    private TeleOpCommands teleOp = new TeleOpCommands();

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
    private double[] directionSave = new double[3];

    // Strength/weight of hyperPrecision variable
    private static final double HP_STRENGTH = 3;

    // Other constants used
    private static final double MIN_DIRECTSAVE_VALUE = 0.1;



    @Override
    public void runOpMode() throws InterruptedException {

        // IMPORTANT: Initialize the hardwareMap
        robot.init(this);
        teleOp.init(this, robot);

        // Init directSave array


        telemetry.addData("OpMode Status", "Initialized");
        telemetry.update();

        waitForStart();

        robot.beep(); //Leo did this!

        while (opModeIsActive()) {

            hyperPrecision = gamepad1.left_trigger * HP_STRENGTH + 1;
            smartDirect = gamepad1.left_bumper;



            strafeDrivePower /= hyperPrecision;
            leftPower /= hyperPrecision;
            rightPower /= hyperPrecision;

            // If directSave is on, use saved movement.
            if (gamepad1.right_trigger > MIN_DIRECTSAVE_VALUE) {



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

                    robot.leftDrive.setPower(directionSave[0] * gamepad1.right_trigger);
                    robot.rightDrive.setPower(directionSave[1] * gamepad1.right_trigger);
                    robot.strafeDrive.setPower(directionSave[2] * gamepad1.right_trigger);

                }

            }

            teleOp.pollAllButtons();

            telemetry.addData("OpMode Status", "Running");
            telemetry.addData("Hyper Precision: ", hyperPrecision);
            telemetry.update();

        }

    }

}
