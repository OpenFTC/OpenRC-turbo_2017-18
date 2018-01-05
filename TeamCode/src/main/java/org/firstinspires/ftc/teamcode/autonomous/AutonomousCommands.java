package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.robot.Revbot;
import org.firstinspires.ftc.teamcode.robot.RevbotCommands;
import org.firstinspires.ftc.teamcode.robot.RevbotValues;
import org.firstinspires.ftc.teamcode.robot.VuforiaCommands;

/**
 * Created by 3565 on 12/15/2017.
 */

public class AutonomousCommands {

    private LinearOpMode myOpMode;
    private Revbot myRobot;
    private RevbotCommands revbot = new RevbotCommands();
    private VuforiaCommands vuforia = new VuforiaCommands();
    public AutonomousCommands() {

    }

    public void init(LinearOpMode opMode, Revbot robot) {

        myOpMode = opMode;
        myRobot = robot;

        revbot.init(opMode, robot);
        vuforia.init(opMode, robot);
    }

    //-----------------------------------------------------------------------
    // Movement methods
    
    public void stop() {
        revbot.closeClaw();
        revbot.stopDriving();
        revbot.stopLift();
        revbot.stopStrafing();
        revbot.stopWinch();
    }

    public void strafeLeft(double power, long ms) {
        revbot.strafeLeft(power);
        myOpMode.sleep(ms);
        revbot.stopStrafing();
    }

    public void strafeRight(double power, long ms){
        strafeLeft(-power, ms);
    }

    public void strafe(double power, long ms, String direction) {
        if (direction.equals(RevbotValues.DIRECTION_LEFT)) {
            strafeLeft(power, ms);
        } else if (direction.equals(RevbotValues.DIRECTION_RIGHT)) {
            strafeRight(power, ms);
        }
    }

    public void forward(double power, long ms) {
        revbot.forward(power);
        myOpMode.sleep(ms);
        revbot.stopDriving();
    }

    public void backward(double power, long ms){
        forward(-power, ms);
    }

    public void move(double power, long ms, String direction) {
        if (direction.equals(RevbotValues.DIRECTION_FORWARD)) {
            forward(power, ms);
        } else if (direction.equals(RevbotValues.DIRECTION_BACKWARD)) {
            backward(power, ms);
        }
    }

    public void turnLeft(double power, long ms) {
        revbot.turnLeft(power);
        myOpMode.sleep(ms);
        revbot.stopDriving();
    }

    public void turnRight(double power, long ms) {
        turnLeft(-power, ms);
    }

    public void turn(double power, long ms, String direction) {
        if (direction.equals(RevbotValues.DIRECTION_LEFT)) {
            turnLeft(power, ms);
        } else if (direction.equals(RevbotValues.DIRECTION_RIGHT)) {
            turnRight(power, ms);
        }
    }

    //-----------------------------------------------------------------------
    // Other methods

    public void fondleBalls(String teamColor) {

        myOpMode.telemetry.addData("isRed", myRobot.color.red());
        myOpMode.telemetry.addData("isGreen", myRobot.color.green());
        myOpMode.telemetry.addData("isBlue", myRobot.color.blue());
        myOpMode.telemetry.update();

        if ((teamColor.equals(RevbotValues.COLOR_BLUE) && revbot.isBlue()) || (teamColor.equals(RevbotValues.COLOR_RED) && revbot.isRed())) {
            revbot.fondleRight();
        } else if ((teamColor.equals(RevbotValues.COLOR_BLUE) && revbot.isRed()) || (teamColor.equals(RevbotValues.COLOR_RED) && revbot.isBlue())) {
            revbot.fondleLeft();
        } else {
            revbot.fondleRight();
            revbot.fondleLeft();
        }

        myOpMode.sleep(1000);

    }

    public void putCubeInCorrectCryptobox(String teamColor, String boxLocation) {
        String direction = teamColor.equals(RevbotValues.COLOR_RED) ? RevbotValues.DIRECTION_FORWARD : RevbotValues.DIRECTION_BACKWARD;
        switch (boxLocation) {
            case "RIGHT":
                move(0.5, 500, direction);
                break;

            case "CENTER":
                move(0.5, 1000, direction);
                break;

            case "LEFT":
                move(0.5, 1500, direction);
                break;

            default:
                move(0.5, 750, direction);
                //help will has me trapped in a room where i can't do anything but code
                break;
        }

    }

    public void raiseLift(double power, long ms) {
        revbot.raiseLift(power);
        myOpMode.sleep(ms);
        revbot.stopLift();
    }

    public void lowerLift(double power, long ms) {
        raiseLift(-power, ms);
    }

    public void raiseWinch(long ms) {
        revbot.raiseWinch();
        myOpMode.sleep(ms);
        revbot.stopWinch();
    }

    public void lowerWinch(long ms) {
        revbot.lowerWinch();
        myOpMode.sleep(4250);
        revbot.stopWinch();
    }
}
