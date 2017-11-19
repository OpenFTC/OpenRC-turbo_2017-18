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

    public void openClaw() {

        robot.clawLeft.setPosition(RevbotValues.LEFT_CLAW_OPENED_VALUE);
        robot.clawRight.setPosition(RevbotValues.RIGHT_CLAW_OPENED_VALUE);

    }

    public void closeClaw() {

        robot.clawLeft.setPosition(RevbotValues.LEFT_CLAW_CLOSED_VALUE);
        robot.clawRight.setPosition(RevbotValues.RIGHT_CLAW_CLOSED_VALUE);

    }

    public void initFondler() {

    }
}
