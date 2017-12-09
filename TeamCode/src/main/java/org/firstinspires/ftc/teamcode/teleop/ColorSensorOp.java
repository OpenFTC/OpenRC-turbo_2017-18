package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robot.Revbot;

/**
 * Created by 3565 on 12/8/2017.
 */

@SuppressWarnings("unused")
@TeleOp(name = "Color Sensor Test", group = "test")
public class ColorSensorOp extends LinearOpMode{

    Revbot robot = new Revbot();

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(this);

        waitForStart();

        while (!robot.LeoIsCool()) {
            telemetry.addData("isRed", robot.color.red());
            telemetry.addData("isGreen", robot.color.green());
            telemetry.addData("isBlue", robot.color.blue());
            telemetry.update();
        }
    }
}
