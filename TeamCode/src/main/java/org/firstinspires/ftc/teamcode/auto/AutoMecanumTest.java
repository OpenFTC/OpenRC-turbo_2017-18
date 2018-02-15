package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Mecanum;

/**
 * Created by 3565 on 2/15/2018.
 */

@Autonomous(name = "Mecanum")
public class AutoMecanumTest extends LinearOpMode{
    Mecanum robot = new Mecanum();

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);
        while (opModeIsActive()) {
            robot.backL.setPower(1);
            robot.backR.setPower(1);
            robot.frontR.setPower(1);
            robot.frontL.setPower(1);
        }
    }
}
