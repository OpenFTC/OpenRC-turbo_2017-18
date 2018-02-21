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
    // Parameters
    static final int LEFT_POSITION = 0;
    static final int CENTER_POSITION = 1;
    static final int RIGHT_POSITION = 2;

    // Change these to calibrate values =======
    // Change this to change the turn margin
    static final double TURN_PRECISION_VALUE = .5;
    // Change this to change the cryptobox lineup margin
    static final double CRYPTO_PRECISION_VALUE = 3;
    // Change this to correct the center
    static final double CRYPTO_CENTER_CORRECTION = 40;
    static final double COLUMN_POSITION = 432;
    static final double CORRECTED_COLUMN_POSITION = COLUMN_POSITION + CRYPTO_CENTER_CORRECTION;
    // 475 is center position
    RelicRecoveryVuMark vuMark;
    RevbotMecanum robot = new RevbotMecanum();
    Drivetrain drivetrain;
    int targetPosition = CENTER_POSITION;
    int targetPosLocation;
    double columnPosition = COLUMN_POSITION;
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

        runtime.reset();

        drivetrain.drive(Direction.RIGHT, .5, 200);
        buildTelemetry();

        correctTurn(0, 2);

        relicTrackables.activate();

        do {
            vuMark = RelicRecoveryVuMark.from(relicTemplate);
            buildTelemetry();
        } while (opModeIsActive() && vuMark == RelicRecoveryVuMark.UNKNOWN);

        vuforia.close();

        jewelDetector.enable();

        jewelOrder = jewelDetector.getCurrentOrder();
        buildTelemetry();

        jewelDetector.disable();

        sleep(1000);
        correctTurn(0);

        drivetrain.drive(Direction.FORWARD, .5, 2100);
        correctTurn(0);
        drivetrain.drive(Direction.LEFT, .5, 1200);

        correctTurn(0);

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
        do {
            if (opModeIsActive() && !cryptoboxDetector.isCryptoBoxDetected() && vuMark.equals(RelicRecoveryVuMark.RIGHT)) {
                targetPosition = CENTER_POSITION;
                columnPosition = CORRECTED_COLUMN_POSITION;
            } else if (opModeIsActive() && cryptoboxDetector.isCryptoBoxDetected() && vuMark.equals(RelicRecoveryVuMark.RIGHT)) {
                targetPosition = RIGHT_POSITION;
                columnPosition = COLUMN_POSITION;
            }

            currentCryptoboxPositions = cryptoboxDetector.getCryptoBoxPositions();
            if (currentCryptoboxPositions != null) {
                targetPosLocation = currentCryptoboxPositions[targetPosition];
            } else {
                targetPosLocation = (int) COLUMN_POSITION;
            }

            if (opModeIsActive() && !cryptoboxDetector.isColumnDetected()) {
                drivetrain.stop();
            } else {
                if (opModeIsActive() && DriveMath.inRange(targetPosLocation, COLUMN_POSITION - CRYPTO_PRECISION_VALUE, COLUMN_POSITION + CRYPTO_PRECISION_VALUE)) {
                    aligned = true;
                } else if (opModeIsActive() && targetPosLocation < COLUMN_POSITION - CRYPTO_PRECISION_VALUE) {
                    drivetrain.drive(Direction.FORWARD, .07);
                } else if (opModeIsActive() && targetPosLocation > COLUMN_POSITION + CRYPTO_PRECISION_VALUE) {
                    drivetrain.drive(Direction.BACKWARD, .07);
                }
            }

            buildTelemetry();
        } while (opModeIsActive() && !aligned);

        drivetrain.stop();


        sleep(2000);


        if (opModeIsActive()) {
            cryptoboxDetector.disable();
        }

        buildTelemetry();

        correctTurn(-86);

        drivetrain.stop();

        sleep(1000);

        drivetrain.drive(Direction.FORWARD, .3, 2500);

        flipper.up();

        sleep(1000);

        drivetrain.drive(Direction.BACKWARD, .3, 750);

        sleep(1000);

        drivetrain.drive(Direction.FORWARD, .2, 750);

        sleep(1000);

        drivetrain.drive(Direction.BACKWARD, .2, 750);

        flipper.down();

        buildTelemetry();

        sleep(1000);

        buildTelemetry();

        robot.beep();
    }

    protected void correctTurn(double desiredAngle) {
        correctTurn(desiredAngle, TURN_PRECISION_VALUE);
    }

    protected void correctTurn(double desiredAngle, double margin) {
        do {
            currentAngle = imuObj.getAngle();
            if (opModeIsActive() && currentAngle < desiredAngle - margin) {
                drivetrain.turn(TurnDirection.LEFT, .2);
            } else if (opModeIsActive() && currentAngle > desiredAngle + margin) {
                drivetrain.turn(TurnDirection.RIGHT, .2);
            } else {
                drivetrain.stop();
            }
            buildTelemetry();
        }
        while (opModeIsActive() && !DriveMath.inRange(currentAngle, desiredAngle - margin, desiredAngle + margin));
    }

    protected void buildTelemetry() {
        telemetry.addData("Status", "Running, %s seconds", runtime.time());
        telemetry.addData("Current Heading", currentAngle);
        telemetry.addData("VuMark", "%s visible", vuMark);
        telemetry.addData("Jewel Order", jewelOrder);

        telemetry.addData("isCryptoBoxDetected", cryptoboxDetector.isCryptoBoxDetected());
        telemetry.addData("isColumnDetected ", cryptoboxDetector.isColumnDetected());

        telemetry.addData("Column Left ", cryptoboxDetector.getCryptoBoxLeftPosition());
        telemetry.addData("Column Center ", cryptoboxDetector.getCryptoBoxCenterPosition());
        telemetry.addData("Column Right ", cryptoboxDetector.getCryptoBoxRightPosition());

        telemetry.addData("Target Position | Target Column", "%s|%s", COLUMN_POSITION, targetPosition);

        telemetry.update();
    }
}
