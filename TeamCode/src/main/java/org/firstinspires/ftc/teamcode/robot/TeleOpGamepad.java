package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by josh on 12/7/17.
 */

public class TeleOpGamepad {

    private LinearOpMode myOpMode;
    private Revbot myRobot;
    private RevbotCommands commands = new RevbotCommands();

    public TeleOpGamepad() {
        
    }
    
    public void init(LinearOpMode opMode, Revbot robot) {

        myOpMode = opMode;
        myRobot = robot;

        commands.init(opMode, robot);
        
    }
    
    public void pollInput(boolean useAlternateStrafeButtons) {
        // Set lift power
        if (myOpMode.gamepad1.dpad_up) {

            myRobot.cubeLift.setPower(1);

        } else if (myOpMode.gamepad1.dpad_down) {

            myRobot.cubeLift.setPower(-0.5);

        } else {
            
            myRobot.cubeLift.setPower(0);

        }

        // Set claw position
        if (myOpMode.gamepad1.dpad_left) {

            myRobot.clawLeft.setPosition(RevbotValues.LEFT_CLAW_CLOSED_VALUE);
            myRobot.clawRight.setPosition(RevbotValues.RIGHT_CLAW_CLOSED_VALUE);

        } else if (myOpMode.gamepad1.dpad_right) {

            myRobot.clawLeft.setPosition(RevbotValues.LEFT_CLAW_OPENED_VALUE);
            myRobot.clawRight.setPosition(RevbotValues.RIGHT_CLAW_OPENED_VALUE);

        }

        // Move fondler
        if (useAlternateStrafeButtons) {
            if (myOpMode.gamepad1.x) {
                commands.strafeLeft(1);
            } else if (myOpMode.gamepad1.b) {
                commands.strafeRight(1);
            } else {
                commands.stopStrafing();
            }

        } else {
            if (myOpMode.gamepad1.y) {

                myRobot.fondler.setPosition(RevbotValues.FONDLER_CENTER_VALUE);

            } else if (myOpMode.gamepad1.x) {

                myRobot.fondler.setPosition(RevbotValues.FONDLER_LEFT_VALUE);

            } else if (myOpMode.gamepad1.b) {

                myRobot.fondler.setPosition(RevbotValues.FONDLER_RIGHT_VALUE);

            }
        }


        // Gamepad 2 claw fine control
        if (myOpMode.gamepad2.dpad_left) {

            myRobot.clawLeft.setPosition(RevbotValues.LEFT_CLAW_OPENED_VALUE);

        } else if (myOpMode.gamepad2.dpad_right) {

            myRobot.clawRight.setPosition(RevbotValues.RIGHT_CLAW_OPENED_VALUE);

        } else if (myOpMode.gamepad2.dpad_up) {

            myRobot.clawLeft.setPosition(RevbotValues.LEFT_CLAW_CLOSED_VALUE);
            myRobot.clawRight.setPosition(RevbotValues.RIGHT_CLAW_CLOSED_VALUE);

        }

        if (myOpMode.gamepad2.a) {
            myRobot.spacerArmCR.setPower(1);
        } else if (myOpMode.gamepad2.x) {
            myRobot.spacerArmCR.setPower(-1);
        } else {
            myRobot.spacerArmCR.setPower(0);
        }
    }

}
