package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import me.joshlin.a3565lib.enums.Alliance;
import me.joshlin.a3565lib.enums.Location;

/**
 * Created by josh on 2/23/18.
 */

@Autonomous(name = "Auto Jewel Blue", group = "Jewel Only")
public class AutoJewelBlue extends Auto {
    @Override
    public void runOpMode() throws InterruptedException {
        //=================================[Initialization]=======================================
        init(Alliance.BLUE, Location.PARALLEL);
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
