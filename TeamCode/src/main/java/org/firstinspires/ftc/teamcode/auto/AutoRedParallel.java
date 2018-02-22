package org.firstinspires.ftc.teamcode.auto;

import com.disnodeteam.dogecv.detectors.JewelDetector;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.RevbotMecanum;
import org.firstinspires.ftc.teamcode.SensorLinearOpMode;

import me.joshlin.a3565lib.component.crservo.CRServoLift;
import me.joshlin.a3565lib.component.drivetrain.DriveMath;
import me.joshlin.a3565lib.component.drivetrain.Drivetrain;
import me.joshlin.a3565lib.component.drivetrain.Mecanum;
import me.joshlin.a3565lib.component.interfaces.Arm;
import me.joshlin.a3565lib.component.interfaces.Lift;
import me.joshlin.a3565lib.component.interfaces.Pivot;
import me.joshlin.a3565lib.component.sensor.IMU;
import me.joshlin.a3565lib.component.servo.ServoArm;
import me.joshlin.a3565lib.component.servo.ServoPivot;
import me.joshlin.a3565lib.enums.Alliance;
import me.joshlin.a3565lib.enums.Direction;
import me.joshlin.a3565lib.enums.TurnDirection;

/**
 * @author Josh
 *
 */

@Autonomous(name = "Auto Red Parallel", group = "Auto")
public class AutoRedParallel extends SensorLinearOpMode {
    //=======================================[Constants]============================================
    // Parameters
    static final int LEFT_POSITION = 0;
    static final int CENTER_POSITION = 1;
    static final int RIGHT_POSITION = 2;

    // Change these to calibrate values =======
    // Change this to change the turn margin
    static final double TURN_PRECISION_VALUE = .5;
    // Change this to change the cryptobox lineup margin
    static final double CRYPTO_PRECISION_VALUE = 3;
    // Change this to correct the center (if the robot is tracking too far right, subtract; if the robot is tracking too far left, add
    static final double CRYPTO_CENTER_CORRECTION = 20;
    static final double COLUMN_POSITION = 432 + CRYPTO_CENTER_CORRECTION;
    // 475 is center position
    RelicRecoveryVuMark vuMark;

    Status status;
    RevbotMecanum robot = new RevbotMecanum();
    Drivetrain drivetrain;
    int targetPosition = CENTER_POSITION;
    int targetPosLocation;
    int[] currentCryptoboxPositions;
    boolean aligned = false;
    IMU imuObj;
    double currentAngle;
    Pivot flipper;
    Lift armLift;
    Arm ballKnock;
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
        //=================================[Initialization]=======================================
        initVuforia();
        initCryptobox(Alliance.BLUE);
        initJewelDetector();

        robot.init(hardwareMap);
        drivetrain = new Mecanum(robot.frontL, robot.frontR, robot.backL, robot.backR);
        flipper = new ServoPivot(robot.flipper, 0.40, 0.38, 0.35);
        armLift = new CRServoLift(robot.spool);
        ballKnock = new ServoArm(robot.knock, 1, 0.5, 0);

        flipper.down();

        initBNO055IMU(robot.imu);
        imuObj = new IMU(imu);
        imuObj.resetAngle();

        status = Status.INITIALIZED;
        buildTelemetry();

        //===================================[Read VuMark]==========================================

        relicTrackables.activate();

        do {
            vuMark = RelicRecoveryVuMark.from(relicTemplate);
            buildTelemetry();
        } while (!isStarted() && vuMark == RelicRecoveryVuMark.UNKNOWN);

        waitForStart();

        //===================================[Run program]==========================================
        vuforia.close();
        status = Status.RUNNING;
        runtime.reset();

        buildTelemetry();

        ballKnock.center();

        armLift.extend(2200);

        buildTelemetry();

        //===================================[Knock Jewel]==========================================

        jewelDetector.enable();

        jewelOrder = jewelDetector.getCurrentOrder();
        buildTelemetry();

        ballKnock.left();

        sleep(500);

        ballKnock.center();

        armLift.retract(3000);

        ballKnock.left();

        jewelDetector.disable();

        sleep(1000);
        correctTurn(0);

        //=============================[Align to read Cryptobox]====================================

        drivetrain.drive(Direction.FORWARD, 1, 1250);
        drivetrain.drive(Direction.LEFT, 1, 700);

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

        //=============================[Line up with column]========================================
        cryptoboxDetector.enable();
        // This keeps crashing and I don't know why
        do {
            if (opModeIsActive() && !cryptoboxDetector.isCryptoBoxDetected() && vuMark.equals(RelicRecoveryVuMark.RIGHT)) {
                targetPosition = CENTER_POSITION;
            } else if (opModeIsActive() && cryptoboxDetector.isCryptoBoxDetected() && vuMark.equals(RelicRecoveryVuMark.RIGHT)) {
                targetPosition = RIGHT_POSITION;
            }

            try {
                currentCryptoboxPositions = cryptoboxDetector.getCryptoBoxPositions();
            } catch (NullPointerException ex) {
                telemetry.addData("Null pointer exception!", "Cryptobox Detector");
                telemetry.update();
            }
            if (currentCryptoboxPositions != null) {
                targetPosLocation = currentCryptoboxPositions[targetPosition];
            } else {
                targetPosLocation = (int) COLUMN_POSITION;
            }

            if (!cryptoboxDetector.isColumnDetected()) {
                switch (targetPosition) {
                    case LEFT_POSITION:
                        drivetrain.drive(Direction.BACKWARD, .1);
                        break;
                    case RIGHT_POSITION:
                        drivetrain.drive(Direction.FORWARD, .1);
                        break;
                    case CENTER_POSITION:
                        drivetrain.stop();
                        break;
                }
            } else {
                if (DriveMath.inRange(targetPosLocation, COLUMN_POSITION - CRYPTO_PRECISION_VALUE, COLUMN_POSITION + CRYPTO_PRECISION_VALUE)) {
                    aligned = true;
                } else if (targetPosLocation < COLUMN_POSITION - CRYPTO_PRECISION_VALUE) {
                    drivetrain.drive(Direction.FORWARD, .1);
                } else if (targetPosLocation > COLUMN_POSITION + CRYPTO_PRECISION_VALUE) {
                    drivetrain.drive(Direction.BACKWARD, .1);
                }
            }

            buildTelemetry();
        } while (opModeIsActive() && !aligned);

        drivetrain.stop();

        if (opModeIsActive()) {
            cryptoboxDetector.disable();
        }

        buildTelemetry();

        sleep(2000);

        //=============================[Put Relic in Cryptobox]=====================================
        correctTurn(-85);

        drivetrain.stop();

        sleep(1000);

        drivetrain.drive(Direction.FORWARD, 1, 1000);

        flipper.up();

        sleep(1000);

        drivetrain.drive(Direction.BACKWARD, .15, 2000);

        drivetrain.drive(Direction.FORWARD, .5, 600);

        sleep(1000);

        drivetrain.drive(Direction.BACKWARD, 1, 100);

        flipper.down();

        buildTelemetry();

        sleep(1000);

        buildTelemetry();

        robot.beep();
    }

    //====================================[Other Methods]===========================================
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
        telemetry.addData("Status", "%s, %s seconds", status, runtime.time());
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

    private enum Status {
        INITIALIZED,
        RUNNING
    }
}
