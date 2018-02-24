package org.firstinspires.ftc.teamcode;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.detectors.CryptoboxDetector;
import com.disnodeteam.dogecv.detectors.JewelDetector;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import me.joshlin.a3565lib.cv.ClosableVuforiaLocalizer;
import me.joshlin.a3565lib.enums.Alliance;

/**
 * Created by josh on 2/18/18.
 */

public abstract class CVLinearOpMode extends LinearOpMode {
    /**
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    protected ClosableVuforiaLocalizer vuforia;
    protected VuforiaTrackables relicTrackables;

    // Create the Vuforia localizer
    protected VuforiaTrackable relicTemplate;

    protected CryptoboxDetector cryptoboxDetector;

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

    // TODO: comment this
    public void initCryptoboxDetector(Alliance cryptoboxColor) {
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

    // TODO: comment this
    public void initJewelDetector() {
        jewelDetector = new JewelDetector();
        jewelDetector.init(hardwareMap.appContext, CameraViewDisplay.getInstance());

        //Jewel Detector Settings
        jewelDetector.downScaleFactor = 0.5;
        jewelDetector.ratioWeight = 20;
        jewelDetector.perfectRatio = 1.0;
        jewelDetector.areaWeight = .1;
        jewelDetector.maxDiffrence = 200;
        jewelDetector.debugContours = true;
        jewelDetector.detectionMode = JewelDetector.JewelDetectionMode.PERFECT_AREA;
        jewelDetector.perfectArea = 975;
        jewelDetector.minArea = 850;
    }
}
