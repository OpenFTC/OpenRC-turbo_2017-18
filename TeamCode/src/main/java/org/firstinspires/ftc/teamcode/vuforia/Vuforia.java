package org.firstinspires.ftc.teamcode.vuforia;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuMarkInstanceId;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/**
 * Class to hold Vuforia methods
 */

public class Vuforia {
    public ClosableVuforiaLocalizer vuforia;
    private VuforiaTrackable relicTemplate;

    // Vuforia License Key
    public static final String VUFORIA_LICENSE_KEY = "AV+GL7P/////AAAAGV7nYsIVuU1VqFIOfsYp0KQh9xxfhpv8vYZhVm2dOSNCK0IZ89FNdUqXUDb6FTmwosSwYv2iGyNNaeH8OGd+EYA+URkJXmtxYXTSjSxlfL7ijgu118//656cnaSAP9MIVR/y49UXnlSr9iRk2N9zUunYC4EJUpPNn6cLW4wV1t4lHtxdKHu5OQ3n7hiJVkJw+5ax0SvQ9QW6H2XcR6BpNQgN0v15zs8anuqiaRoWzV5wIqBc2NWMnmNDCuRy9de9uJPRZFglQXX5Kq1wuVH7N/B+nVRpVmJ8jnIKpEVO+nM8l7HiCfOpwdteALuWimYChVWCms06HjOZ58U3UjEHjXjELlqS9w2iYMWPOvA17HMx";

    public void init(int cameraMonitorViewId) {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters.useExtendedTracking = false;
        // VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        /*
         * We also indicate which camera on the RC that we wish to use.
         * Here we chose the back (HiRes) camera (for greater range), but
         * for a competition robot, the front camera might be more convenient.
         */
        parameters.vuforiaLicenseKey = Vuforia.VUFORIA_LICENSE_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        vuforia = new ClosableVuforiaLocalizer(parameters);

        /**
         * Load the data set containing the VuMarks for Relic Recovery. There's only one trackable
         * in this data set: all three of the VuMarks in the game were created from this one template,
         * but differ in their instance id information.
         * @see VuMarkInstanceId
         */
        VuforiaTrackables relicTrackables = vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary
        relicTrackables.activate();
    }

    public RelicRecoveryVuMark readImage() {
        return RelicRecoveryVuMark.from(relicTemplate);
    }
}
