package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by josh on 12/7/17.
 */

public class TeleOpCommands {

    private LinearOpMode myOpMode;
    private Revbot myRobot;
    private RevbotCommands commands = new RevbotCommands();

    public TeleOpCommands() {
        
    }
    
    public void init(LinearOpMode opMode, Revbot robot) {

        myOpMode = opMode;
        myRobot = robot;

        commands.init(opMode, robot);
        
    }

    public void TwoJoyOp() {}

    public void DroneOp() {}

    public double gearingValue() {return 0.0;}

    public void pollAllButtons() {
        pollClawButtons();
        pollFineControlClawButtons();
        pollWinchButtons();
        pollFondlerButtons();
        pollCubeLiftButtons();
    }

    public void pollClawButtons() {
        // Set claw position
        if (myOpMode.gamepad1.dpad_left) {
            commands.closeClaw();
        } else if (myOpMode.gamepad1.dpad_right) {
            commands.openClaw();
        }
    }

    public void pollFineControlClawButtons() {
        // Gamepad 2 claw fine control
        if (myOpMode.gamepad2.dpad_left) {
            myRobot.clawLeft.setPosition(RevbotValues.LEFT_CLAW_OPENED_VALUE);
        } else if (myOpMode.gamepad2.dpad_right) {
            myRobot.clawRight.setPosition(RevbotValues.RIGHT_CLAW_OPENED_VALUE);
        } else if (myOpMode.gamepad2.dpad_up) {
            commands.closeClaw();
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

}
