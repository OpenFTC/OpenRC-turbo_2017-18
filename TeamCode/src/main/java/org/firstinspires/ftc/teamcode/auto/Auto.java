package org.firstinspires.ftc.teamcode.auto;

import com.disnodeteam.dogecv.detectors.JewelDetector;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.CVLinearOpMode;
import org.firstinspires.ftc.teamcode.RevbotMecanum;

import me.joshlin.a3565lib.component.drivetrain.DriveMath;
import me.joshlin.a3565lib.component.drivetrain.Drivetrain;
import me.joshlin.a3565lib.enums.Alliance;
import me.joshlin.a3565lib.enums.Column;
import me.joshlin.a3565lib.enums.Direction;
import me.joshlin.a3565lib.enums.Location;
import me.joshlin.a3565lib.enums.TurnDirection;

/**
 * Created by josh on 2/23/18.
 */

public abstract class Auto extends CVLinearOpMode {
    //==================================[Cryptobox Values]==========================================
    // Change this to change the turn margin
    private static final double TURN_PRECISION_VALUE = 1;
    // Change this to change the cryptobox lineup margin
    private static final double CRYPTO_PRECISION_VALUE = 3;
    // Center pixel of the phone screen (determine by dividing the total width of the screen by 2)
    private static final int SCREEN_CENTER = 432;
    // Change this to correct the center (if the robot is tracking too far right, subtract; if the robot is tracking too far left, add)
    private static final int CRYPTO_CENTER_CORRECTION = 60;
    // The corrected target column position on the screen
    private static final int COLUMN_POSITION = SCREEN_CENTER + CRYPTO_CENTER_CORRECTION;
    // The id of the column the VuMark shows (0 = left, 1 = center, 2 = right)
    int targetPositionId;
    // Holds the current status of the program.
    Status status;
    // Holds the stored VuMark.
    RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.UNKNOWN;
    //=====================================[Robot Components]=======================================
    // Declare robot type.
    RevbotMecanum robot = new RevbotMecanum();
    // Holds drivetrain.
    Drivetrain drivetrain;
    // The current location of the target position on screen
    private int targetPosLocation;
    // Holds the cryptobox positions returned by the detector
    private int[] currentCryptoboxPositions;
    //
    private boolean aligned;
    //====================================[OpMode Variables]========================================
    // Holds the declared alliance of the OpMode.
    private Alliance alliance;
    // Holds the declared location of the OpMode.
    private Location location;
    // Holds the stored jewel order.
    private JewelDetector.JewelOrder jewelOrder = JewelDetector.JewelOrder.UNKNOWN;
    // Holds the current angle of the robot.
    private double currentAngle;

    /**
     * Initialization of components. Make sure to call this before anything else.
     *
     * @param alliance the alliance of the OpMode
     * @param location the location of the OpMode
     */
    public void init(Alliance alliance, Location location) {
        this.alliance = alliance;
        this.location = location;

        // Initialize computer vision.
        initVuforia();
        // TODO: Change this to actually read correct alliance for competition
        initCryptoboxDetector(Alliance.BLUE);
        initJewelDetector();

        // Initialize the robot and its components.
        robot.init(hardwareMap);

        // Set default positions for the components.
        drivetrain.stop();
        robot.flipper.down();
        robot.vertical.up();

        // Initialize the gyroscope.
        robot.ghostIMU.init();
        robot.ghostIMU.resetAngle();

        // Finish initialization and build telemetry.
        status = Status.INITIALIZED;
        buildTelemetry();
    }

    /**
     * A simplified version of {@link Auto#correctTurn(double, double, double)}; pre-fills in default values.
     *
     * @param desiredAngle the angle to turn to
     */
    void correctTurn(double desiredAngle) {
        // Pass in default arguments
        correctTurn(desiredAngle, TURN_PRECISION_VALUE, .3);
    }

    /**
     * Turns the robot to a specified degree.
     *
     * @param desiredAngle the angle to turn to
     * @param margin       the margin of error acceptable
     * @param power        the power the turn should be at
     */
    void correctTurn(double desiredAngle, double margin, double power) {
        do {
            // Get the current angle of the robot.
            currentAngle = robot.ghostIMU.getAngle();
            // If the robot is too far to the right...
            if (opModeIsActive() && currentAngle < (desiredAngle - margin)) {
                // ...then correct left.
                drivetrain.turn(TurnDirection.LEFT, power);
                // If the robot is too far to the left...
            } else if (opModeIsActive() && currentAngle > (desiredAngle + margin)) {
                // ...then correct right.
                drivetrain.turn(TurnDirection.RIGHT, power);
                // Otherwise...
            } else {
                // ...stop the robot.
                drivetrain.stop();
            }
            buildTelemetry();
        }
        while (opModeIsActive() && !DriveMath.inRange(currentAngle, desiredAngle - margin, desiredAngle + margin));
    }

    /**
     * Build the telemetry output.
     */
    void buildTelemetry() {
        telemetry.addData("Alliance", "%s | Location: %s", alliance, location);
        telemetry.addData("Status", "%s, %s seconds", status, runtime.time());
        telemetry.addData("Current Heading", currentAngle);
        telemetry.addData("VuMark", "%s visible | Jewel Order: %s", vuMark, jewelOrder);

        telemetry.addData("isCryptoBoxDetected", "%s | isColumnDetected: %s",
                cryptoboxDetector.isCryptoBoxDetected(),
                cryptoboxDetector.isColumnDetected());
        telemetry.addData("Column Left ", "%s | Column Center: %s | Column Right: %s",
                cryptoboxDetector.getCryptoBoxLeftPosition(),
                cryptoboxDetector.getCryptoBoxCenterPosition(),
                cryptoboxDetector.getCryptoBoxRightPosition());
        telemetry.addData("Target Position", "%s | Target Column: %s", COLUMN_POSITION, targetPositionId);

        telemetry.update();
    }

    /**
     * Read the VuMark.
     * This function works while start hasn't been pressed.
     */
    void readVuMark() {
        // activate the trackable so that it'll actually read the VuMark
        relicTrackables.activate();

        do {
            // Get the currently seen VuMark.
            vuMark = RelicRecoveryVuMark.from(relicTemplate);
            buildTelemetry();
        } while (!isStarted() && vuMark == RelicRecoveryVuMark.UNKNOWN);
        // Close the Vuforia camera to use other computer vision
        vuforia.close();

        switch (vuMark) {
            case LEFT:
                targetPositionId = Column.LEFT;
                break;
            case CENTER:
                targetPositionId = Column.CENTER;
                break;
            case RIGHT:
                targetPositionId = Column.RIGHT;
                break;
        }

        buildTelemetry();
    }

    /**
     * Knock the correct jewel from the jewel holder.
     */
    void knockJewel() {
        drivetrain.stop();
        // Enable the jewel detector
        jewelDetector.enable();

        // Lower the arm
        robot.flipper.down();
        buildTelemetry();

        do {
            // Get the currently seen jewel order.
            jewelOrder = jewelDetector.getCurrentOrder();
            buildTelemetry();
        }
        while (opModeIsActive() && (JewelDetector.JewelOrder.UNKNOWN == jewelOrder) && (runtime.time() < 5));

        // Get the last known order of the jewels, just to be safe
        jewelOrder = jewelDetector.getLastOrder();
        buildTelemetry();

        // Move based on the jewel order
        switch (jewelOrder) {
            // this is disgusting but it works
            // TODO: fix this so that there's more factoring
            case BLUE_RED:
                if (alliance.equals(Alliance.RED)) {
                    drivetrain.drive(Direction.FORWARD, .5, 300);
                    drivetrain.drive(Direction.BACKWARD, .5, 300);
                } else {
                    drivetrain.drive(Direction.BACKWARD, .5, 300);
                    drivetrain.drive(Direction.FORWARD, .5, 300);
                }
                break;
            case RED_BLUE:
                if (alliance.equals(Alliance.BLUE)) {
                    drivetrain.drive(Direction.FORWARD, .5, 300);
                    drivetrain.drive(Direction.BACKWARD, .5, 300);
                } else {
                    drivetrain.drive(Direction.BACKWARD, .5, 300);
                    drivetrain.drive(Direction.FORWARD, .5, 300);
                }
                break;
            case UNKNOWN:
                // If it doesn't see jewels then just don't knock either to be safe
                break;
        }
        buildTelemetry();

        // Raise the arm
        robot.vertical.up();

        // Wait for the arm to finish raising
        sleep(500);

        // Close the jewel detector to use other computer vision
        jewelDetector.disable();
    }

    /**
     * Align properly with the correct column, assuming that the robot is lined up correctly with the box.
     */
    void alignWithCryptobox() {
        drivetrain.stop();
        // Enable cryptobox detector
        cryptoboxDetector.enable();

        do {
            // Error mitigation: if it can't see the full cryptobox and it's trying to go for the
            // right column, make it go to the center column instead
            if (opModeIsActive() && !cryptoboxDetector.isCryptoBoxDetected() && vuMark.equals(RelicRecoveryVuMark.RIGHT)) {
                targetPositionId = Column.CENTER;
                // Once it sees the full box again make sure it goes for the correct column
            } else if (opModeIsActive() && cryptoboxDetector.isCryptoBoxDetected() && vuMark.equals(RelicRecoveryVuMark.RIGHT)) {
                targetPositionId = Column.RIGHT;
            }

            try {
                // Get the current on-screen positions of the columns
                currentCryptoboxPositions = cryptoboxDetector.getCryptoBoxPositions();
                // Sometimes the app crashes when you try to read from the array because it returns a null array;
                // if the array is null, catch the NullPointerException so that the app doesn't crash,
                // and return the old positions.
            } catch (NullPointerException ex) {
                telemetry.addData("Null pointer exception!", "Cryptobox Detector");
                telemetry.update();
            }

            // Error mitigation: Don't read from the array if it's null
            if (currentCryptoboxPositions != null) {
                // Update the position to the new detected position
                targetPosLocation = currentCryptoboxPositions[targetPositionId];
            }

            // If the detector can't see a column...
            if (!cryptoboxDetector.isColumnDetected()) {
                switch (vuMark) {
                    // ... correct position based on the VuMark (because the id is unreliable)
                    case LEFT:
                        drivetrain.drive(Direction.BACKWARD, .1);
                        break;
                    case RIGHT:
                        drivetrain.drive(Direction.FORWARD, .1);
                        break;
                    case CENTER:
                        drivetrain.stop();
                        break;
                }
            } else {
                // If it's aligned, then break out of the loop.
                if (DriveMath.inRange(targetPosLocation, COLUMN_POSITION - CRYPTO_PRECISION_VALUE, COLUMN_POSITION + CRYPTO_PRECISION_VALUE)) {
                    aligned = true;
                    // Otherwise, correct for the column as appropriate.
                } else if (targetPosLocation < COLUMN_POSITION - CRYPTO_PRECISION_VALUE) {
                    drivetrain.drive(Direction.FORWARD, .1);
                } else if (targetPosLocation > COLUMN_POSITION + CRYPTO_PRECISION_VALUE) {
                    drivetrain.drive(Direction.BACKWARD, .1);
                }
            }

            buildTelemetry();
        } while (opModeIsActive() && !aligned);

        // Stop moving.
        drivetrain.stop();

        // Only disable the cryptobox if the OpMode is active, to prevent crashing
        if (opModeIsActive()) {
            cryptoboxDetector.disable();
        }

        buildTelemetry();
    }

    /**
     * Put the glyph into cryptobox, after alignment and turning to face the box.
     */
    void putGlyphInCryptobox() {
        drivetrain.stop();

        sleep(500);

        drivetrain.drive(Direction.FORWARD, 1, 750);

        robot.flipper.up();

        sleep(500);

        drivetrain.drive(Direction.BACKWARD, .15, 2000);

        drivetrain.drive(Direction.FORWARD, .5, 600);

        sleep(500);

        drivetrain.drive(Direction.BACKWARD, 1, 100);

        robot.flipper.down();

        buildTelemetry();

        sleep(500);

        buildTelemetry();
    }

    protected enum Status {
        INITIALIZED,
        RUNNING
    }
}
