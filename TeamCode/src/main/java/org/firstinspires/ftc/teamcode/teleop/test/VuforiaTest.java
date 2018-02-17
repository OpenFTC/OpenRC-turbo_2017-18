package org.firstinspires.ftc.teamcode.teleop.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RevbotMecanum;

import me.joshlin.a3565lib.component.drivetrain.Mecanum;

/**
 * Created by josh on 2/17/18.
 */

@TeleOp(name = "Vuforia Test", group = "mecanum")
public class VuforiaTest extends LinearOpMode {
    private RevbotMecanum robot = new RevbotMecanum();
    private Mecanum drivetrain;

    //

    /**
     * Override this method and place your code here.
     * <p>
     * Please do not swallow the InterruptedException, as it is used in cases
     * where the op mode needs to be terminated early.
     *
     * @throws InterruptedException
     */
    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);
        drivetrain = new Mecanum(robot.frontL, robot.frontR, robot.backL, robot.backR);

    }

    public void initVuforia() {

    }
}
