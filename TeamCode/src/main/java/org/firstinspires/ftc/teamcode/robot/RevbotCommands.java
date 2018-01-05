package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by 3565 on 12/8/2017.
 */

@SuppressWarnings("unused")
public class RevbotCommands {

    private LinearOpMode myOpMode;
    private Revbot myRobot;

    public RevbotCommands() {

    }

    public void init(LinearOpMode opMode, Revbot robot) {
        myOpMode = opMode;
        myRobot = robot;
    }


    //-----------------------------------------------------------------------
    // Claw methods

    public void clawOpen() {
        myRobot.clawLeft.setPosition(RevbotValues.LEFT_CLAW_OPENED_VALUE);
        myRobot.clawRight.setPosition(RevbotValues.RIGHT_CLAW_OPENED_VALUE);
    }

    public void clawClose() {
        myRobot.clawLeft.setPosition(RevbotValues.LEFT_CLAW_CLOSED_VALUE);
        myRobot.clawRight.setPosition(RevbotValues.RIGHT_CLAW_CLOSED_VALUE);
    }


    //-----------------------------------------------------------------------
    // Fondler methods

    public void fondleCenter() {
        myRobot.fondler.setPosition(RevbotValues.FONDLER_CENTER_VALUE);
    }

    public void fondleLeft() {
        myRobot.fondler.setPosition(RevbotValues.FONDLER_LEFT_VALUE);
    }

    public void fondleRight() {
        myRobot.fondler.setPosition(RevbotValues.FONDLER_FULLY_RIGHT);
    }

    public boolean isBlue() {
        return ((myRobot.color.blue() > myRobot.color.red()));
    }

    public boolean isRed() {
        return ((myRobot.color.red() > myRobot.color.blue()));
    }


    //-----------------------------------------------------------------------
    // Strafe methods

    public void strafeLeft(double power) {
        myRobot.strafeDrive.setPower(power);
    }

    public void strafeRight(double power) {
        strafeLeft(-power);
    }

    public void stopStrafing() {
        myRobot.strafeDrive.setPower(0);
    }


    //-----------------------------------------------------------------------
    // Movement methods

    public void stopDriving() {
        myRobot.leftDrive.setPower(0);
        myRobot.rightDrive.setPower(0);
    }

    public void forward(double power) {
        myRobot.leftDrive.setPower(power);
        myRobot.rightDrive.setPower(power);
    }

    public void backward(double power) {
        forward(-power);
    }


    //-----------------------------------------------------------------------
    // Turning methods

    public void turnLeft(double power) {
        myRobot.leftDrive.setPower(-power);
        myRobot.rightDrive.setPower(power);
    }

    public void turnRight(double power) {
        turnLeft(-power);
    }

    //-----------------------------------------------------------------------
    // Cube Lift methods

    public void raiseLift(double power) {
        myRobot.cubeLift.setPower(power);
    }

    public void lowerLift(double power) {
        raiseLift(-power);
    }

    public void stopLift() {
        myRobot.cubeLift.setPower(0);
    }


    //-----------------------------------------------------------------------
    // Winch methods

    public void raiseWinch() {
        myRobot.spacerArmCR.setPower(RevbotValues.CR_FORWARDS_VALUE);
    }

    public void lowerWinch() {
        myRobot.spacerArmCR.setPower(RevbotValues.CR_BACKWARDS_VALUE);
    }

    public void stopWinch() {
        myRobot.spacerArmCR.setPower(0);
    }

}
