package org.firstinspires.ftc.teamcode.auto.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.RevbotMecanum;
import org.firstinspires.ftc.teamcode.SensorLinearOpMode;

import me.joshlin.a3565lib.component.drivetrain.DriveMath;
import me.joshlin.a3565lib.component.drivetrain.Drivetrain;
import me.joshlin.a3565lib.component.drivetrain.Mecanum;
import me.joshlin.a3565lib.component.sensor.GhostIMU;
import me.joshlin.a3565lib.enums.TurnDirection;

/**
 * Created by josh on 2/19/18.
 */

@Autonomous(name = "Test turn 90", group = "test")
public class TestTurn90Degrees extends SensorLinearOpMode {
    RevbotMecanum robot = new RevbotMecanum();
    Drivetrain drivetrain;

    GhostIMU ghostImuObj;

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

        ghostImuObj = new GhostIMU(imu);

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();

        ghostImuObj.resetAngle();

        while (opModeIsActive()) {
            do {
                currentAngle = ghostImuObj.getAngle();
                correctTurn(currentAngle, 90, 1);
                telemetry.addData("Current Angle", currentAngle);
                telemetry.update();
            } while (opModeIsActive() && !DriveMath.inRange(currentAngle, 90-1, 90+1));

            drivetrain.stop();

            robot.beep();
            ghostImuObj.resetAngle();
            sleep(2000);
        }
    }
    protected void correctTurn(double currentAngle, double desiredAngle, double margin) {
        if (opModeIsActive() && currentAngle < desiredAngle-margin) {
            drivetrain.turn(TurnDirection.LEFT, .1);
        } else if (opModeIsActive() && currentAngle > desiredAngle + margin) {
            drivetrain.turn(TurnDirection.RIGHT, .1);
        } else {
            drivetrain.stop();
        }
    }
}
