package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/**
 * Created by 3565 on 12/15/2017.
 */


public class VuforiaCommands {

    private LinearOpMode myOpMode;
    private Revbot myRobot;
    private RevbotCommands commands;

    VuforiaLocalizer vuforia;
    VuforiaTrackable relicTemplate;

    public void init(LinearOpMode opMode, Revbot robot) {

        myOpMode = opMode;
        myRobot = robot;

        commands.init(opMode, robot);

    }

    //-----------------------------------------------------------------------
    // Vuforia methods

    public void initVuforia() {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = RevbotValues.VUFORIA_LICENSE_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        this.relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate");

        myOpMode.telemetry.addData("VuforiaOld", "Initialized");
    }

    public String readImage() {
        RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
        if (vuMark != RelicRecoveryVuMark.UNKNOWN) {
            myOpMode.telemetry.addData("VuMark", "%s visible",vuMark);
            return vuMark.name();
        } else {
            myOpMode.telemetry.addData("VuMark", "not found");
            return "[B]roke";
        }
    }
}
