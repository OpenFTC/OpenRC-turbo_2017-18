package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.robot.Revbot;
import org.firstinspires.ftc.teamcode.robot.RevbotValues;

/**
 * Base Autonomous template. All autonomous classes should extend from this one.
 */

@SuppressWarnings("unused")
@Autonomous
@Disabled
public class AutoBase extends LinearOpMode {

    Revbot robot = new Revbot();
    ElapsedTime elapsedTime = new ElapsedTime();
    VuforiaLocalizer vuforia;
    VuforiaTrackable relicTemplate;

    double distance;
    double distanceFromWall;

    @Override
    public void runOpMode() throws InterruptedException {

        robot.init(this);
        initVuforia();
        initFondler();
        closeClaw();
        robot.beep();
        telemetry.addData("Status: ", "Initialized");
        telemetry.update();

    }

    public void openClaw() {

        robot.clawLeft.setPosition(RevbotValues.LEFT_CLAW_OPENED_VALUE);
        robot.clawRight.setPosition(RevbotValues.RIGHT_CLAW_OPENED_VALUE);

    }

    public void closeClaw() {

        robot.clawLeft.setPosition(RevbotValues.LEFT_CLAW_CLOSED_VALUE);
        robot.clawRight.setPosition(RevbotValues.RIGHT_CLAW_CLOSED_VALUE);

    }

    public void initFondler() {
        robot.fondler.setPosition(RevbotValues.FONDLER_CENTER_VALUE);
    }

    public void strafeDriveLeft(double power) {
        robot.strafeDrive.setPower(power);
    }

    public void strafeDriveRight(double power) {
        strafeDriveLeft(-power);
    }

    public void stopStrafing() {
        robot.strafeDrive.setPower(0);
    }
    
    /**
     * strafeDrive left (using motor "strafeDrive")
     * @param seconds seconds to wait
     * @param power power, between 0 and 1
     */
    public void strafeDriveLeft(double seconds, double power) {
        robot.strafeDrive.setPower(power);
        sleep((long) (seconds*1000));
        robot.strafeDrive.setPower(0);
    }

    /**
     * strafeDrive right (using motor "strafeDrive")
     * @param seconds seconds to wait
     * @param power power, between 0 and 1
     */
    public void strafeDriveRight(double seconds, double power){
        strafeDriveLeft(seconds, -power);
    }

    /**
     * Drive forward (using motors "leftDrive" and "rightDrive)
     * @param ms milliseconds to wait
     * @param power power, between 0 and 1
     */
    public void forward(long ms, double power) {
        robot.leftDrive.setPower(power);
        robot.rightDrive.setPower(power);
        sleep(ms);
        robot.leftDrive.setPower(0);
        robot.rightDrive.setPower(0);
    }

    public void forward(double power) {
        robot.leftDrive.setPower(power);
        robot.rightDrive.setPower(power);
    }

    /**
     * Drive backward (using motors "leftDrive" and "rightDrive)
     * @param ms milliseconds to wait
     * @param power power, between 0 and 1
     */
    public void backward(long ms, double power){
        forward(ms, -power);
    }

    public void backward(double power) {
        forward(-power);
    }

    /**
     * Turn left (using motors "leftDrive" and "rightDrive)
     * @param seconds seconds to wait
     * @param power power, between 0 and 1
     */
    public void turnLeft(double seconds, double power) {
        robot.leftDrive.setPower(-power);
        robot.rightDrive.setPower(power);
        sleep((long) seconds*1000);
        robot.leftDrive.setPower(0);
        robot.rightDrive.setPower(0);
    }

    public void turnLeft(double power) {
        robot.leftDrive.setPower(-power);
        robot.rightDrive.setPower(power);
    }

    /**
     * Turn right (using motors "leftDrive" and "rightDrive")
     * @param seconds seconds to wait
     * @param power power, between 0 and 1
     */
    public void turnRight(double seconds, double power) {
        turnLeft(seconds, -power);
    }

    public void turnRight(double power) {
        turnLeft(-power);
    }

    public boolean isBlue() {
        return ((robot.color.blue() > robot.color.red()));
    }

    public boolean isRed() {
        return ((robot.color.red() > robot.color.blue()));
    }

    /***
     *
     * @param teamColor The color of the current alliance (yes, I know that's not how you're supposed to use "Color"s
     */

    public void fondleBalls(String teamColor) {
        if ((teamColor.equals("blue") && isBlue()) || (teamColor.equals("red") && isRed())) {
            robot.fondler.setPosition(RevbotValues.FONDLER_RIGHT_VALUE);
        } else if ((teamColor.equals("blue") && isRed()) || (teamColor.equals("red") && isBlue())) {
            robot.fondler.setPosition(RevbotValues.FONDLER_LEFT_VALUE);
        } else {
            robot.beep();
            robot.beep();
            robot.beep();
        }

        telemetry.addData("isRed", robot.color.red());
        telemetry.addData("isGreen", robot.color.green());
        telemetry.addData("isBlue", robot.color.blue());
        telemetry.update();

        sleep(1000);

    }

    // VuforiaOld Stuff
    public void initVuforia() {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = RevbotValues.VUFORIA_LICENSE_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        this.relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate");

        telemetry.addData("VuforiaOld", "Initialized");
    }

    public String readImage() {
        RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
        if (vuMark != RelicRecoveryVuMark.UNKNOWN) {
            telemetry.addData("VuMark", "%s visible",vuMark);
            return vuMark.name();
        } else {
            telemetry.addData("VuMark", "not found");
            return "[B]roke";
        }
    }

    public void raiseWinch() {
        robot.spacerArmCR.setPower(-1);
        sleep(2000);
        robot.spacerArmCR.setPower(0);
    }

    public void lowerWinch() {
        robot.spacerArmCR.setPower(1);
        sleep(2000);
        robot.spacerArmCR.setPower(0);
    }
}
