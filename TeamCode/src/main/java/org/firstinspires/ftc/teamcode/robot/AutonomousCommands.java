package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by 3565 on 12/15/2017.
 */

public class AutonomousCommands {

    private LinearOpMode myOpMode;
    private Revbot myRobot;
    private RevbotCommands commands = new RevbotCommands();
    private VuforiaCommands vuforia = new VuforiaCommands();

    public AutonomousCommands() {

    }

    public void init(LinearOpMode opMode, Revbot robot) {

        myOpMode = opMode;
        myRobot = robot;

        commands.init(opMode, robot);
        vuforia.init(opMode, robot);

    }

    // Movement methods

    public void strafeLeft(double power, long ms) {
        commands.strafeLeft(power);
        myOpMode.sleep(ms);
        commands.stopStrafing();
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
        commands.forward(power);
        myOpMode.sleep(ms);
        commands.stopDriving();
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
        commands.turnLeft(power);
        myOpMode.sleep(ms);
        commands.stopDriving();
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

    public void fondleBalls(String teamColor) {

        myOpMode.telemetry.addData("isRed", myRobot.color.red());
        myOpMode.telemetry.addData("isGreen", myRobot.color.green());
        myOpMode.telemetry.addData("isBlue", myRobot.color.blue());
        myOpMode.telemetry.update();

        if ((teamColor.equals(RevbotValues.COLOR_BLUE) && commands.isBlue()) || (teamColor.equals(RevbotValues.COLOR_RED) && commands.isRed())) {
            commands.fondleRight();
        } else if ((teamColor.equals(RevbotValues.COLOR_BLUE) && commands.isRed()) || (teamColor.equals(RevbotValues.COLOR_RED) && commands.isBlue())) {
            commands.fondleLeft();
        } else {
            commands.fondleRight();
            commands.fondleLeft();
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
        commands.raiseLift(power);
        myOpMode.sleep(ms);
        commands.stopLift();
    }

    public void lowerLift(double power, long ms) {
        raiseLift(-power, ms);
    }

    public void raiseWinch(long ms) {
        commands.raiseWinch();
        myOpMode.sleep(ms);
        commands.stopWinch();
    }

    public void lowerWinch(long ms) {
        commands.lowerWinch();
        myOpMode.sleep(4250);
        commands.stopWinch();
    }

    public String determineFBDirection(String teamColor) {
        return teamColor.equals(RevbotValues.COLOR_RED) ? RevbotValues.DIRECTION_FORWARD : RevbotValues.DIRECTION_BACKWARD;
    }

    public String determineLRDirection(String teamColor) {
        return teamColor.equals(RevbotValues.COLOR_RED) ? RevbotValues.DIRECTION_LEFT : RevbotValues.DIRECTION_RIGHT;
    }

}
