package org.firstinspires.ftc.teamcode.teleop.test;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.CVLinearOpMode;

import me.joshlin.a3565lib.enums.Alliance;

/**
 * Created by josh on 2/18/18.
 */
@TeleOp(name = "CV Crypto Test")
public class CVCryptoTest extends CVLinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();

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
        initDogeCV(Alliance.BLUE);

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
            telemetry.addData("Screen Resolution", cryptoboxDetector.getFrameSize().width);
            telemetry.update();
        }

        cryptoboxDetector.disable();
    }
}
