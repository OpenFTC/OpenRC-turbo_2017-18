package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robot.Revbot;
import org.firstinspires.ftc.teamcode.robot.RevbotCommands;

/**
 * Created by josh on 12/2/17.
 */

@SuppressWarnings("unused")
@TeleOp(name="2 Joy Op", group = "teleop")
public class TwoJoyOp extends LinearOpMode {

    Revbot robot = new Revbot();
    private TeleOpCommands teleOp = new TeleOpCommands();
    private RevbotCommands commands = new RevbotCommands();

    @Override
    public void runOpMode() throws InterruptedException {

        robot.init(this);
        teleOp.init(this, robot);
        telemetry.addData("OpMode Status", "Initialized");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {



            teleOp.pollAllButtons();

        }
    }
}
