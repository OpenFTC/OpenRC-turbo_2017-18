package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robot.RevbotHardware;
import org.firstinspires.ftc.teamcode.robot.RevbotValues;

/**
 * Base Autonomous template. All autonomous classes should extend from this one.
 */

@SuppressWarnings("unused")
@Autonomous
@Disabled
public class AutoBase extends LinearOpMode {

    RevbotHardware robot = new RevbotHardware();
    ElapsedTime elapsedTime = new ElapsedTime();

    @Override
    public void runOpMode() throws InterruptedException {

    }

    /**
     * 
     */
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

    public void strafeLeft(double power) {
        robot.strafe.setPower(power);
    }

    public void strafeRight(double power) {
        strafeLeft(power);
    }

    public void stopStrafing() {
        robot.strafe.setPower(0);
    }
    
    /**
     * Strafe left (using motor "strafe")
     * @param seconds seconds to wait
     * @param power power, between 0 and 1
     */
    public void strafeLeft(double seconds, double power) {
        robot.strafe.setPower(power);
        sleep((long) seconds*1000);
        robot.strafe.setPower(0);
    }

    /**
     * Strafe right (using motor "strafe")
     * @param seconds seconds to wait
     * @param power power, between 0 and 1
     */
    public void strafeRight(double seconds, double power){
        strafeLeft(seconds, -power);
    }

    /**
     * Drive forward (using motors "leftDrive" and "rightDrive)
     * @param seconds seconds to wait
     * @param power power, between 0 and 1
     */
    public void forward(double seconds, double power) {
        robot.leftDrive.setPower(power);
        robot.rightDrive.setPower(power);
        sleep((long) seconds*1000);
        robot.leftDrive.setPower(0);
        robot.rightDrive.setPower(0);
    }

    /**
     * Drive backward (using motors "leftDrive" and "rightDrive)
     * @param seconds seconds to wait
     * @param power power, between 0 and 1
     */
    public void backward(double seconds, double power){
        forward(seconds, -power);
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

    /**
     * Turn right (using motors "leftDrive" and "rightDrive)
     * @param seconds seconds to wait
     * @param power power, between 0 and 1
     */
    public void turnRight(double seconds, double power) {
        turnLeft(seconds, -power);
    }
}
