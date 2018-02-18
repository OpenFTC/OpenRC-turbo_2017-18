package org.firstinspires.ftc.teamcode.teleop.test;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.detectors.CryptoboxDetector;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.teleop.CVLinearOpMode;

/**
 * Created by josh on 2/18/18.
 */
@TeleOp(name = "CV Crypto Test")
public class CVCryptoTest extends CVLinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();

    private CryptoboxDetector cryptoboxDetector;

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
        // init cryptobox detector
        cryptoboxDetector = new CryptoboxDetector();
        cryptoboxDetector.init(hardwareMap.appContext, CameraViewDisplay.getInstance());

        cryptoboxDetector.downScaleFactor = 0.4;
        cryptoboxDetector.detectionMode = CryptoboxDetector.CryptoboxDetectionMode.BLUE;
        cryptoboxDetector.speed = CryptoboxDetector.CryptoboxSpeed.FAST;

        // don't rotate matrix
        cryptoboxDetector.rotateMat = false;

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
    }
}
