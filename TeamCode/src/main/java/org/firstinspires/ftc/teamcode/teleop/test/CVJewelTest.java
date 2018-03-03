package org.firstinspires.ftc.teamcode.teleop.test;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.CVLinearOpMode;

/**
 * Created by josh on 2/18/18.
 */
@TeleOp(name = "CV Jewel Test", group = "Test")
public class CVJewelTest extends CVLinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        // init jewel detector
        initJewelDetector();

        jewelDetector.enable();

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            telemetry.addData("Status", "Run Time: " + runtime.toString());

            telemetry.addData("Current Order", "Jewel Order: " + jewelDetector.getCurrentOrder().toString()); // Current Result
            telemetry.addData("Last Order", "Jewel Order: " + jewelDetector.getLastOrder().toString()); // Last Known Result
            telemetry.update();
        }

        jewelDetector.disable();
    }
}
