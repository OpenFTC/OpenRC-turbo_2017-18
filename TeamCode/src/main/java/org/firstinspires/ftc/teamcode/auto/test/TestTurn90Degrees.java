package org.firstinspires.ftc.teamcode.auto.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.CVLinearOpMode;
import org.firstinspires.ftc.teamcode.RevbotMecanum;

import me.joshlin.a3565lib.component.drivetrain.DriveMath;
import me.joshlin.a3565lib.component.drivetrain.Drivetrain;
import me.joshlin.a3565lib.component.drivetrain.Mecanum;
import me.joshlin.a3565lib.component.sensor.IMU;

/**
 * Created by josh on 2/19/18.
 */

@Autonomous(name = "Test turn 90", group = "test")
public class TestTurn90Degrees extends CVLinearOpMode {
    RevbotMecanum robot = new RevbotMecanum();
    Drivetrain drivetrain;

    IMU imuObj;

    double currentAngle;
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

        initBNO055IMU(robot.imu);

        imuObj = new IMU(imu);

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();

        imuObj.resetAngle();

        while (opModeIsActive()) {
            do {
                currentAngle = imuObj.getAngle();
                drivetrain.move(DriveMath.inputsToMotors(0, 0, -.25));
                telemetry.addData("Current Angle", currentAngle);
                telemetry.update();
            } while (opModeIsActive() && currentAngle < 90);

            drivetrain.move(DriveMath.inputsToMotors(0, 0, 0));

            robot.beep();
            imuObj.resetAngle();
            sleep(2000);
        }
    }
}
