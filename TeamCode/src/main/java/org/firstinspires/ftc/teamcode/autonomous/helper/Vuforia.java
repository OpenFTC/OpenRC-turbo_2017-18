package org.firstinspires.ftc.teamcode.autonomous.helper;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.robot.RevbotValues;

/**
 * Created by josh on 11/24/17.
 */

public class Vuforia extends LinearOpMode {

    public static final String TAG = "Vuforia Navigation";

    OpenGLMatrix lastLocation = null;

    VuforiaLocalizer vuforia;

    @Override
    public void runOpMode() throws InterruptedException {

        // See comments in ConceptVuforiaNavigation.java to figure out what this all means
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = RevbotValues.VUFORIA_LICENSE_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        // Load the data
        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate");

        // Units for the robot
        float mmRobotWidth = 18 * (float) DistanceUnit.mmPerInch;
        float mmFTCFieldWidth  = (12*12 - 2) * (float) DistanceUnit.mmPerInch;

        // TODO: CHANGE THIS SO THAT BLUE CAN ALSO BE USED
        OpenGLMatrix redTopTargetLocationOnField = OpenGLMatrix
                .translation(-mmFTCFieldWidth/2, -mmFTCFieldWidth/2 + (26.5f * ((float) DistanceUnit.mmPerInch)), 1.5f * ((float) DistanceUnit.mmPerInch))
                .multiplied(Orientation.getRotationMatrix(
                        AxesReference.EXTRINSIC, AxesOrder.XZX,
                        AngleUnit.DEGREES, 90, 0, 0));

        relicTemplate.setLocation(redTopTargetLocationOnField);
        RobotLog.ii(TAG, "RedTarget=%s", format(redTopTargetLocationOnField));


    }






    /**
     * A simple utility that extracts positioning information from a transformation matrix
     * and formats it in a form palatable to a human being.
     */
    String format(OpenGLMatrix transformationMatrix) {
        return transformationMatrix.formatAsTransform();
    }
}
