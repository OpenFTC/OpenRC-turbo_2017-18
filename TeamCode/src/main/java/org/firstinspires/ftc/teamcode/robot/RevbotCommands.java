package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/**
 * Created by 3565 on 12/8/2017.
 */

@SuppressWarnings("unused")
public class RevbotCommands {

    private LinearOpMode myOpMode;
    private Revbot myRobot;
    ElapsedTime elapsedTime = new ElapsedTime();
    VuforiaLocalizer vuforia;
    VuforiaTrackable relicTemplate;

    public RevbotCommands() {

    }

    public void init(LinearOpMode opMode, Revbot robot) {
        myOpMode = opMode;
        myRobot = robot;
    }


    //-----------------------------------------------------------------------
    // Claw methods

    public void openClaw() {
        myRobot.clawLeft.setPosition(RevbotValues.LEFT_CLAW_OPENED_VALUE);
        myRobot.clawRight.setPosition(RevbotValues.RIGHT_CLAW_OPENED_VALUE);
    }

    public void closeClaw() {
        myRobot.clawLeft.setPosition(RevbotValues.LEFT_CLAW_CLOSED_VALUE);
        myRobot.clawRight.setPosition(RevbotValues.RIGHT_CLAW_CLOSED_VALUE);
    }


    //-----------------------------------------------------------------------
    // Fondler methods

    public void initFondler() {
        myRobot.fondler.setPosition(RevbotValues.FONDLER_CENTER_VALUE);
    }

    public void fondleLeft() {
        myRobot.fondler.setPosition(RevbotValues.FONDLER_LEFT_VALUE);
    }

    public void fondleRight() {
        myRobot.fondler.setPosition(RevbotValues.FONDLER_RIGHT_VALUE);
    }

    public boolean isBlue() {
        return ((myRobot.color.blue() > myRobot.color.red()));
    }

    public boolean isRed() {
        return ((myRobot.color.red() > myRobot.color.blue()));
    }

    public void fondleBalls(String teamColor) {
        if ((teamColor.equals("blue") && isBlue()) || (teamColor.equals("red") && isRed())) {
            myRobot.fondler.setPosition(RevbotValues.FONDLER_RIGHT_VALUE);
        } else if ((teamColor.equals("blue") && isRed()) || (teamColor.equals("red") && isBlue())) {
            myRobot.fondler.setPosition(RevbotValues.FONDLER_LEFT_VALUE);
        } else {
            myRobot.beep();
            myRobot.beep();
            myRobot.beep();
        }

        myOpMode.telemetry.addData("isRed", myRobot.color.red());
        myOpMode.telemetry.addData("isGreen", myRobot.color.green());
        myOpMode.telemetry.addData("isBlue", myRobot.color.blue());
        myOpMode.telemetry.update();

        myOpMode.sleep(1000);

    }


    //-----------------------------------------------------------------------
    // Winch methods

    public void raiseWinch() {
        myRobot.spacerArmCR.setPower(RevbotValues.CR_FORWARDS_VALUE);
        myOpMode.sleep(4000);
        myRobot.spacerArmCR.setPower(0);
    }

    public void lowerWinch() {
        myRobot.spacerArmCR.setPower(RevbotValues.CR_BACKWARDS_VALUE);
        myOpMode.sleep(4000);
        myRobot.spacerArmCR.setPower(0);
    }


    //-----------------------------------------------------------------------
    // Strafe methods

    public void initStrafe() {
        myRobot.strafeDrive.setPower(0);
    }

    public void strafeLeft(double power) {
        myRobot.strafeDrive.setPower(power);
    }

    public void strafeRight(double power) {
        strafeLeft(-power);
    }

    public void stopStrafing() {
        initStrafe();
    }

    public void strafeLeft(long ms, double power) {
        strafeLeft(power);
        myOpMode.sleep(ms);
        stopStrafing();
    }

    public void strafeRight(long ms, double power){
        strafeLeft(ms, -power);
    }


    //-----------------------------------------------------------------------
    // Movement methods

    public void initDrive() {
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

    public void stopDriving() {
        initDrive();
    }

    public void forward(long ms, double power) {
        forward(power);
        myOpMode.sleep(ms);
        stopDriving();
    }

    public void backward(long ms, double power){
        forward(ms, -power);
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

    public void turnLeft(long ms, double power) {
        turnLeft(power);
        myOpMode.sleep(ms);
        stopDriving();
    }

    public void turnRight(long ms, double power) {
        turnLeft(ms, -power);
    }

    //-----------------------------------------------------------------------
    // Vuforia methods

    public void initVuforia() {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = RevbotValues.VUFORIA_LICENSE_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        this.relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate");

        myOpMode.telemetry.addData("VuforiaOld", "Initialized");
    }

    public String readImage() {
        RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
        if (vuMark != RelicRecoveryVuMark.UNKNOWN) {
            myOpMode.telemetry.addData("VuMark", "%s visible",vuMark);
            return vuMark.name();
        } else {
            myOpMode.telemetry.addData("VuMark", "not found");
            return "[B]roke";
        }
    }

}
