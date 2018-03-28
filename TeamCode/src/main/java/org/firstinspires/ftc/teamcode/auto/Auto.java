package org.firstinspires.ftc.teamcode.auto;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.CVLinearOpMode;
import org.firstinspires.ftc.teamcode.hardwaremap.RevbotMecanum;

import java.util.ArrayList;

import me.joshlin.a3565lib.component.drivetrain.DriveMath;
import me.joshlin.a3565lib.component.motor.MotorHardwareComponent;
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
    private static final int RED_CENTER_CORRECTION = 39;
    // Change this to correct the center (if the robot is tracking too far left, subtract; if the robot is tracking too far right, add)
    private static final int BLUE_CENTER_CORRECTION = 40;
    // The corrected target column position on the screen
    private int columnPosition = SCREEN_CENTER + RED_CENTER_CORRECTION;
    // Holds the current status of the program.
    private Status status;
    //=====================================[Robot Components]=======================================
    // Declare robot type.
    RevbotMecanum robot = new RevbotMecanum();
    // The id of the column the VuMark shows (0 = left, 1 = center, 2 = right)
    private int targetPositionId;
    // Holds the stored VuMark.
    private RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.UNKNOWN;
    // The current location of the target position on screen
    private int targetPosLocation;
    // Holds the cryptobox positions returned by the detector
    private int[] currentCryptoboxPositions;
    //
    private boolean aligned;
    //====================================[OpMode Variables]========================================
    // Holds the declared alliance of the OpMode.
    private Alliance alliance;
    // Holds the color of the jewel.
    private Alliance jewelColor;
    // Holds the declared location of the OpMode.
    private Location location;
    // Holds the current angle of the robot.
    private double currentAngle;

    /**
     * Get the current status.
     * @return the current status of the program
     */
    protected Status getStatus() {
        return status;
    }

    /**
     * Set the current status.
     * @param status the status to set
     */
    protected void setStatus(Status status) {
        this.status = status;
    }

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
        initCryptoboxDetector(alliance);
        // initCryptoboxDetector(Alliance.BLUE);

        // Initialize the robot and its components.
        robot.init(hardwareMap);

        // Set default positions for the components.
        robot.drivetrain.stop();
        robot.vertical.up();

        // Initialize the gyroscope.
        robot.ghostIMU.init();
        robot.ghostIMU.resetAngle();

        // Figure out which correction position to use based on the defined alliance
        switch (alliance) {
            case BLUE:
                columnPosition = SCREEN_CENTER + BLUE_CENTER_CORRECTION;
                break;
            case RED:
                columnPosition = SCREEN_CENTER + RED_CENTER_CORRECTION;
                break;
        }

        // Finish initialization and build telemetry.
        setStatus(Status.INITIALIZED);
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
                robot.drivetrain.turn(TurnDirection.LEFT, power);
                // If the robot is too far to the left...
            } else if (opModeIsActive() && currentAngle > (desiredAngle + margin)) {
                // ...then correct right.
                robot.drivetrain.turn(TurnDirection.RIGHT, power);
                // Otherwise...
            } else {
                // ...stop the robot.
                robot.drivetrain.stop();
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
        telemetry.addData("Status", "%s, %s seconds", getStatus(), runtime.time());
        telemetry.addData("Current Heading", currentAngle);
        telemetry.addData("VuMark", "%s visible | Jewel Order: %s", vuMark, jewelColor);

        telemetry.addData("isCryptoBoxDetected", "%s | isColumnDetected: %s",
                cryptoboxDetector.isCryptoBoxDetected(),
                cryptoboxDetector.isColumnDetected());
        telemetry.addData("Column Left ", "%s | Column Center: %s | Column Right: %s",
                cryptoboxDetector.getCryptoBoxLeftPosition(),
                cryptoboxDetector.getCryptoBoxCenterPosition(),
                cryptoboxDetector.getCryptoBoxRightPosition());
        telemetry.addData("Target Position", "%s | Target Column: %s", columnPosition, targetPositionId);

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
    void readAndKnockJewel() {
        robot.drivetrain.stop();

        // Lower the arm
        robot.vertical.down();
        robot.beep();
        // Wait for the arm to go down
        sleep(1500);
        buildTelemetry();
        // Move based on the jewel order
        knockJewel(readJewel());
        robot.drivetrain.stop();
        buildTelemetry();

        sleep(1000);
    }

    Direction readJewel() {
        // Get the detected color of the color sensor
        ArrayList<Alliance> sensorColors = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            sensorColors.add(robot.ghostColor.readColor());
        }

        jewelColor = getMostCommon(sensorColors);

        if (jewelColor.equals(alliance)) {
            return Direction.FORWARD;
        } else {
            return Direction.BACKWARD;
        }
    }

    void knockJewel(Direction direction) {
        switch (direction) {
            case FORWARD:
                robot.drivetrain.drive(Direction.FORWARD, .3, 300);
                // Raise the arm
                robot.vertical.up();
                robot.drivetrain.drive(Direction.BACKWARD, .3, 300);
                break;
            case BACKWARD:
                robot.drivetrain.drive(Direction.BACKWARD, .3, 300);
                // Raise the arm
                robot.vertical.up();
                robot.drivetrain.drive(Direction.FORWARD, .3, 300);
                break;
        }
        buildTelemetry();
    }

    Alliance getMostCommon(ArrayList<Alliance> alliances) {
        int red = 0;
        int blue = 0;
        for (Alliance alliance : alliances) {
            if (alliance.equals(Alliance.RED)) {
                red++;
            } else if (alliance.equals(Alliance.BLUE)) {
                blue++;
            }
        }

        if (red > blue) {
            return Alliance.RED;
        } else if (red < blue) {
            return Alliance.BLUE;
        } else {
            return Alliance.UNKNOWN;
        }
    }

    /**
     * Align properly with the correct column, assuming that the robot is lined up correctly with the box.
     */
    void alignWithCryptobox() {
        robot.drivetrain.stop();
        sleep(1000);
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
                        robot.drivetrain.drive(Direction.BACKWARD, .1);
                        break;
                    case RIGHT:
                        robot.drivetrain.drive(Direction.FORWARD, .1);
                        break;
                    case CENTER:
                        robot.drivetrain.stop();
                        break;
                }
            } else {
                // If it's aligned, then break out of the loop.
                if (DriveMath.inRange(targetPosLocation, columnPosition - CRYPTO_PRECISION_VALUE, columnPosition + CRYPTO_PRECISION_VALUE)) {
                    aligned = true;
                    // Otherwise, correct for the column as appropriate.
                } else if (targetPosLocation < columnPosition - CRYPTO_PRECISION_VALUE) {
                    robot.drivetrain.drive(Direction.FORWARD, .07);
                } else if (targetPosLocation > columnPosition + CRYPTO_PRECISION_VALUE) {
                    robot.drivetrain.drive(Direction.BACKWARD, .07);
                }
            }

            buildTelemetry();
        } while (opModeIsActive() && !aligned);

        // Stop moving.
        robot.drivetrain.stop();

        // Only disable the cryptobox if the OpMode is active, to prevent crashing
        if (opModeIsActive()) {
            cryptoboxDetector.disable();
        }

        buildTelemetry();
    }

    /**
     * Put the glyph into cryptobox, after alignment and turni'=ng to face the box.
     */
    void putGlyphInCryptobox() {
        robot.drivetrain.stop();

        sleep(500);
        robot.intake.in();

        robot.drivetrain.drive(Direction.FORWARD, 1, 750);

        robot.drivetrain.drive(Direction.BACKWARD, .2);
        sleep(100);
        MotorHardwareComponent mhc = (MotorHardwareComponent) robot.flipperLift;
        mhc.setPower(0.35);

        sleep(1000);
        robot.flipperLift.stop();

        robot.drivetrain.drive(Direction.BACKWARD, .15, 1000);

        robot.intake.stop();

        robot.drivetrain.drive(Direction.FORWARD, .5, 600);

        sleep(500);

        robot.drivetrain.drive(Direction.BACKWARD, 1, 100);

        robot.flipperLift.retract();

        buildTelemetry();

        sleep(700);

        buildTelemetry();
    }

    protected enum Status {
        INITIALIZED,
        RUNNING
    }
}
