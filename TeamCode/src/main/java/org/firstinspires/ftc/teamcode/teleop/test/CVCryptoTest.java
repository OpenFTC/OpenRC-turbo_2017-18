package org.firstinspires.ftc.teamcode.teleop.test;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.CVLinearOpMode;

import me.joshlin.a3565lib.enums.Alliance;

/**
 * Created by josh on 2/18/18.
 */
@TeleOp(name = "CV Crypto Test", group = "Test")
public class CVCryptoTest extends CVLinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        // init jewel detector
        initCryptoboxDetector(Alliance.BLUE);

        cryptoboxDetector.enable();

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            telemetry.addData("Status", "Run Time: " + runtime.toString());

            telemetry.addData("isCryptoBoxDetected", cryptoboxDetector.isCryptoBoxDetected());
            telemetry.addData("isColumnDetected ", cryptoboxDetector.isColumnDetected());

            telemetry.addData("Column Left ", cryptoboxDetector.getCryptoBoxLeftPosition());
            telemetry.addData("Column Center ", cryptoboxDetector.getCryptoBoxCenterPosition());
            telemetry.addData("Column Right ", cryptoboxDetector.getCryptoBoxRightPosition());
            telemetry.update();
        }

        cryptoboxDetector.disable();
    }
}
