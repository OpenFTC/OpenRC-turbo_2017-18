package org.firstinspires.ftc.teamcode.auto;

import com.disnodeteam.dogecv.detectors.JewelDetector;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.RevbotMecanum;
import org.firstinspires.ftc.teamcode.SensorLinearOpMode;

import me.joshlin.a3565lib.component.drivetrain.DriveMath;
import me.joshlin.a3565lib.component.drivetrain.Drivetrain;
import me.joshlin.a3565lib.component.drivetrain.Mecanum;
import me.joshlin.a3565lib.component.interfaces.Pivot;
import me.joshlin.a3565lib.component.sensor.IMU;
import me.joshlin.a3565lib.component.servo.ServoPivot;
import me.joshlin.a3565lib.enums.Alliance;
import me.joshlin.a3565lib.enums.Direction;
import me.joshlin.a3565lib.enums.TurnDirection;

/**
 * Created by josh on 2/19/18.
 */

@Autonomous(name = "Auto Red Right", group = "Auto")
public class AutoRedRight extends SensorLinearOpMode {
    static final int LEFT_POSITION = 0;
    static final int CENTER_POSITION = 1;
    static final int RIGHT_POSITION = 2;
    static final int TURN_PRECISION_VALUE = 2;
    static final int CRYPTO_PRECISION_VALUE = 6;
    static final int CRYPTO_CENTER_CORRECTION = -160;
    RelicRecoveryVuMark vuMark;
    RevbotMecanum robot = new RevbotMecanum();
    Drivetrain drivetrain;
    int targetPosition = CENTER_POSITION;
    int targetPosLocation;
    int[] currentCryptoboxPositions;
    boolean aligned = false;
    IMU imuObj;
    double currentAngle;
    Pivot flipper;
    JewelDetector.JewelOrder jewelOrder;

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
        initVuforia();
        initCryptobox(Alliance.BLUE);
        initJewelDetector();

        robot.init(hardwareMap);
        drivetrain = new Mecanum(robot.frontL, robot.frontR, robot.backL, robot.backR);
        flipper = new ServoPivot(robot.flipper, 0.40, 0.38, 0.35);
        flipper.down();

        initBNO055IMU(robot.imu);
        imuObj = new IMU(imu);
        imuObj.resetAngle();

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        drivetrain.drive(Direction.RIGHT, .5, 200);

        currentAngle = imuObj.getAngle();

        while (opModeIsActive() && !DriveMath.inRange(currentAngle, -TURN_PRECISION_VALUE, TURN_PRECISION_VALUE)) {
            currentAngle = imuObj.getAngle();
            correctTurn(currentAngle, 0, 1);

            telemetry.addData("Current Heading", currentAngle);
            telemetry.update();
        }

        relicTrackables.activate();

        do {
            vuMark = RelicRecoveryVuMark.from(relicTemplate);
            telemetry.addData("VuMark", "%s visible", vuMark);
            telemetry.update();
        } while (opModeIsActive() && vuMark == RelicRecoveryVuMark.UNKNOWN);

        vuforia.close();

        jewelDetector.enable();

        jewelOrder = jewelDetector.getCurrentOrder();
        telemetry.addData("Jewel Order", jewelOrder);
        telemetry.update();

        jewelDetector.disable();

        drivetrain.drive(Direction.LEFT, .5, 200);

        sleep(1000);

        drivetrain.drive(Direction.FORWARD, .5, 2100);
        drivetrain.drive(Direction.LEFT, .5, 800);

        currentAngle = imuObj.getAngle();

        while (opModeIsActive() && !DriveMath.inRange(currentAngle, -TURN_PRECISION_VALUE, TURN_PRECISION_VALUE)) {
            currentAngle = imuObj.getAngle();

            correctTurn(currentAngle, 0);

            telemetry.addData("Current Heading", currentAngle);
            telemetry.update();
        }

        drivetrain.stop();

        switch (vuMark) {
            case LEFT:
                targetPosition = LEFT_POSITION;
                break;
            case CENTER:
                targetPosition = CENTER_POSITION;
                break;
            case RIGHT:
                targetPosition = RIGHT_POSITION;
                break;
        }

        sleep(1000);

        cryptoboxDetector.enable();



        // This keeps crashing and I don't know why
        while (opModeIsActive() && !aligned) {
            currentCryptoboxPositions = cryptoboxDetector.getCryptoBoxPositions();
            if (currentCryptoboxPositions != null) {
                targetPosLocation = currentCryptoboxPositions[targetPosition];
            } else {
                targetPosLocation = (int) cryptoboxDetector.getFrameSize().width;
            }

            if (opModeIsActive() && !cryptoboxDetector.isColumnDetected()) {
                drivetrain.move(DriveMath.inputsToMotors(0, 0, 0));
            } else {
                if (opModeIsActive() && DriveMath.inRange(targetPosLocation, (cryptoboxDetector.getFrameSize().width + CRYPTO_CENTER_CORRECTION) - CRYPTO_PRECISION_VALUE, (cryptoboxDetector.getFrameSize().width + CRYPTO_CENTER_CORRECTION) + CRYPTO_PRECISION_VALUE)) {
                    aligned = true;
                } else if (opModeIsActive() && targetPosLocation < (cryptoboxDetector.getFrameSize().width + CRYPTO_CENTER_CORRECTION) - CRYPTO_CENTER_CORRECTION) {
                    drivetrain.drive(Direction.FORWARD, .07);
                } else if (opModeIsActive() && targetPosLocation > (cryptoboxDetector.getFrameSize().width + CRYPTO_CENTER_CORRECTION) + CRYPTO_PRECISION_VALUE) {
                    drivetrain.drive(Direction.BACKWARD, .07);
                }
            }

            telemetry.addData("isCryptoBoxDetected", cryptoboxDetector.isCryptoBoxDetected());
            telemetry.addData("isColumnDetected ", cryptoboxDetector.isColumnDetected());

            telemetry.addData("Column Left ", cryptoboxDetector.getCryptoBoxLeftPosition());
            telemetry.addData("Column Center ", cryptoboxDetector.getCryptoBoxCenterPosition());
            telemetry.addData("Column Right ", cryptoboxDetector.getCryptoBoxRightPosition());

            telemetry.addData("Target Position", targetPosLocation);

            telemetry.update();
        }

        drivetrain.stop();


        sleep(2000);


        cryptoboxDetector.disable();


        currentAngle = imuObj.getAngle();
        telemetry.addData("Status", "Aligned");
        telemetry.addData("Angle", currentAngle);
        telemetry.update();


        while (opModeIsActive() && !DriveMath.inRange(currentAngle, -90 - TURN_PRECISION_VALUE, -90 + TURN_PRECISION_VALUE)) {
            currentAngle = imuObj.getAngle();
            correctTurn(currentAngle, -90);
            telemetry.addData("Turning", "");
            telemetry.addData("Angle", currentAngle);
            telemetry.update();
        }

        drivetrain.stop();

        sleep(1000);

        drivetrain.drive(Direction.FORWARD, .3, 2000);

        flipper.up();

        sleep(1000);

        drivetrain.drive(Direction.BACKWARD, .3, 1000);

        sleep(2000);

        drivetrain.drive(Direction.FORWARD, .2, 500);

        flipper.down();

        robot.beep();
    }

    protected void correctTurn(double currentAngle, double desiredAngle) {
        correctTurn(currentAngle, desiredAngle, TURN_PRECISION_VALUE);
    }

    protected void correctTurn(double currentAngle, double desiredAngle, double margin) {
        if (opModeIsActive() && currentAngle < -margin) {
            drivetrain.turn(TurnDirection.LEFT, .1);
        } else if (opModeIsActive() && currentAngle > margin) {
            drivetrain.turn(TurnDirection.RIGHT, .1);
        } else {
            drivetrain.stop();
        }
    }
}
