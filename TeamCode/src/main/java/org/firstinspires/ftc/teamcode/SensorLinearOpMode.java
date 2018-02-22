package org.firstinspires.ftc.teamcode;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.detectors.CryptoboxDetector;
import com.disnodeteam.dogecv.detectors.JewelDetector;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import me.joshlin.a3565lib.enums.Alliance;
import me.joshlin.a3565lib.vuforia.ClosableVuforiaLocalizer;

/**
 * Created by josh on 2/18/18.
 */

public abstract class SensorLinearOpMode extends LinearOpMode {
    /**
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    protected ClosableVuforiaLocalizer vuforia;
    protected VuforiaTrackables relicTrackables;

    // Create the Vuforia localizer
    protected VuforiaTrackable relicTemplate;

    protected CryptoboxDetector cryptoboxDetector;

    protected BNO055IMU imu;

    protected JewelDetector jewelDetector;

    protected ElapsedTime runtime = new ElapsedTime();

    /**
     * Initialize the Vuforia system. Call before using Vuforia.
     */
    public void initVuforia() {
        /*
         * To start up Vuforia, tell it the view that we wish to use for camera monitor (on the RC phone);
         * If no camera monitor is desired, use the parameterless constructor instead (commented out below).
         */
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        // OR...  Do Not Activate the Camera Monitor View, to save power
        // VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = BuildConfig.VUFORIA_KEY;

        /*
         * We also indicate which camera on the RC that we wish to use.
         * Here we chose the back (HiRes) camera (for greater range), but
         * for a competition robot, the front camera might be more convenient.
         */
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

        parameters.useExtendedTracking = false;

        this.vuforia = new ClosableVuforiaLocalizer(parameters);

        /**
         * Load the data set containing the VuMarks for Relic Recovery. There's only one trackable
         * in this data set: all three of the VuMarks in the game were created from this one template,
         * but differ in their instance id information.
         * @see VuMarkInstanceId
         */
        relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary
    }

    public void initCryptobox(Alliance cryptoboxColor) {
        CryptoboxDetector.CryptoboxDetectionMode mode = CryptoboxDetector.CryptoboxDetectionMode.BLUE;
        // init cryptobox detector
        cryptoboxDetector = new CryptoboxDetector();
        cryptoboxDetector.downScaleFactor = 0.4;

        switch (cryptoboxColor) {
            case RED:
                mode = CryptoboxDetector.CryptoboxDetectionMode.RED;
                break;
            case BLUE:
                mode = CryptoboxDetector.CryptoboxDetectionMode.BLUE;
                break;
        }

        cryptoboxDetector.rotateMat = false;
        cryptoboxDetector.detectionMode = mode;
        cryptoboxDetector.speed = CryptoboxDetector.CryptoboxSpeed.FAST;

        cryptoboxDetector.init(hardwareMap.appContext, CameraViewDisplay.getInstance());
    }

    public void initJewelDetector() {
        jewelDetector = new JewelDetector();
        jewelDetector.init(hardwareMap.appContext, CameraViewDisplay.getInstance());

        //Jewel Detector Settings
        jewelDetector.downScaleFactor = 0.4;
        jewelDetector.ratioWeight = 15;
        jewelDetector.perfectRatio = 1.0;
        jewelDetector.areaWeight = 0.75;
        jewelDetector.maxDiffrence = 250;
        jewelDetector.debugContours = true;
        jewelDetector.detectionMode = JewelDetector.JewelDetectionMode.PERFECT_AREA;
        jewelDetector.perfectArea = 2150;
        jewelDetector.minArea = 1800;
    }

    public void initBNO055IMU(BNO055IMU aImu) {
        this.imu = aImu;
        // Set up the parameters with which we will use our IMU. Note that integration
        // algorithm here just reports accelerations to the logcat log; it doesn't actually
        // provide positional information.
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        imu.initialize(parameters);

        while (!isStopRequested() && !imu.isGyroCalibrated()) {
            sleep(50);
            idle();
        }

    }
}
