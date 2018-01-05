package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.robot.Revbot;
import org.firstinspires.ftc.teamcode.robot.RevbotCommands;
import org.firstinspires.ftc.teamcode.robot.RevbotValues;

/**
 * Created by josh on 12/7/17.
 */

public class TeleOpCommands {

    private LinearOpMode myOpMode;
    private Revbot myRobot;
    private RevbotCommands commands = new RevbotCommands();

    // Double[] for saving joystick position and replicating direction/power
    private double[] directionSave = new double[3];

    double left;
    double right;
    double strafe;

    public TeleOpCommands() {
        
    }
    
    public void init(LinearOpMode opMode, Revbot robot) {

        myOpMode = opMode;
        myRobot = robot;

        commands.init(opMode, robot);

        for (int i = 0; i < 3; i++) {
            directionSave[i] = 0;
        }
        
    }

    //---------------------------------------
    // Drive methods
    public void DroneOp() {
        left = -myOpMode.gamepad1.left_stick_y;
        right = -myOpMode.gamepad1.left_stick_y;
        strafe = -myOpMode.gamepad1.left_stick_x;

        left += myOpMode.gamepad1.right_stick_x;
        right -= myOpMode.gamepad1.right_stick_x;

        left /= gearing();
        right /= gearing();
        strafe /= gearing();

        myRobot.moveRobot(left, right, strafe);
    }

    public void TwoJoyOp() {
        left = -myOpMode.gamepad1.left_stick_y;
        right = -myOpMode.gamepad1.right_stick_y;
        strafe = (myOpMode.gamepad1.left_stick_x + myOpMode.gamepad1.right_stick_x)/2;

        left /= gearing();
        right /= gearing();
        strafe /= gearing();

        myRobot.moveRobot(left, right, strafe);
    }

    public double gearing() {
        final double HP_STRENGTH = 3;

        return myOpMode.gamepad1.left_trigger * HP_STRENGTH + 1;
    }

    public void pollSaveDirection() {
        if (myOpMode.gamepad1.right_bumper) {
            directionSave[0] = myRobot.leftDrive.getPower();
            directionSave[1] = myRobot.rightDrive.getPower();
            directionSave[2] = myRobot.strafeDrive.getPower();
        }
    }

    public void pollUseSavedDirection() {
        final double MIN_DIRECTSAVE_VALUE = 0.1;

        if (myOpMode.gamepad1.right_trigger > MIN_DIRECTSAVE_VALUE) {
            myRobot.leftDrive.setPower(directionSave[0] * myOpMode.gamepad1.right_trigger);
            myRobot.rightDrive.setPower(directionSave[1] * myOpMode.gamepad1.right_trigger);
            myRobot.strafeDrive.setPower(directionSave[2] * myOpMode.gamepad1.right_trigger);
        }
    }

    public void pollLockFourAxis() {

    }

    //---------------------------------------
    // User Input methods

    public void pollAllButtons() {
        // Robot functions
        pollClawButtons();
        pollFineControlClawButtons();
        pollWinchButtons();
        pollFondlerButtons();
        pollCubeLiftButtons();

        // Movement helper functions
        pollSaveDirection();
        pollUseSavedDirection();
        pollDriveModeSwap();
    }

    /**
     *
     */
    public void pollClawButtons() {
        // Set claw position
        if (myOpMode.gamepad1.dpad_left) {
            commands.clawOpen();
        } else if (myOpMode.gamepad1.dpad_right) {
            commands.clawClose();
        }
    }

    public void pollFineControlClawButtons() {
        // Gamepad 2 claw fine control
        if (myOpMode.gamepad2.dpad_left) {
            myRobot.clawLeft.setPosition(RevbotValues.LEFT_CLAW_OPENED_VALUE);
        } else if (myOpMode.gamepad2.dpad_right) {
            myRobot.clawRight.setPosition(RevbotValues.RIGHT_CLAW_OPENED_VALUE);
        } else if (myOpMode.gamepad2.dpad_up) {
            commands.clawClose();
        }
    }

    public void pollWinchButtons() {
        if (myOpMode.gamepad2.a) {
            commands.raiseWinch();
        } else if (myOpMode.gamepad2.x) {
            commands.lowerWinch();
        } else {
            commands.stopWinch();
        }
    }

    public void pollFondlerButtons() {
        // Move fondler
        if (myOpMode.gamepad1.y) {
           commands.fondleCenter();
        } else if (myOpMode.gamepad1.x) {
            commands.fondleLeft();
        } else if (myOpMode.gamepad1.b) {
            commands.fondleRight();
        }
    }

    public void pollCubeLiftButtons(){
        // Set lift power
        if (myOpMode.gamepad1.dpad_up) {
            commands.raiseLift(1);
        } else if (myOpMode.gamepad1.dpad_down) {
            commands.lowerLift(0.5);
        } else {
            commands.stopLift();
        }
    }

    public void pollDriveModeSwap() {
        // TODO: Figure out how to implement this properly
    }

}
