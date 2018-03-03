package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import me.joshlin.a3565lib.enums.Alliance;
import me.joshlin.a3565lib.enums.Location;

/**
 * Created by josh on 2/23/18.
 */

@Autonomous(name = "Auto Jewel Red", group = "Jewel Only")
public class AutoJewelRed extends Auto {
    @Override
    public void runOpMode() throws InterruptedException {
        //=================================[Initialization]=======================================
        init(Alliance.RED, Location.PARALLEL);
        //===================================[Read VuMark]==========================================
        readVuMark();

        waitForStart();
        //===================================[Run program]==========================================
        setStatus(Status.RUNNING);

        runtime.reset();

        buildTelemetry();
        //===================================[Knock Jewel]==========================================
        readAndKnockJewel();
        buildTelemetry();
        while (opModeIsActive()) {
            sleep(50);
        }
    }
}
