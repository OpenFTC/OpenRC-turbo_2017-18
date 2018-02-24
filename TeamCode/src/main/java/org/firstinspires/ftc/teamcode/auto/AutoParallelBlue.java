package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import me.joshlin.a3565lib.enums.Alliance;
import me.joshlin.a3565lib.enums.Direction;
import me.joshlin.a3565lib.enums.Location;

/**
 * @author Josh
 */

@Autonomous(name = "Auto Blue Parallel", group = "Auto")
public class AutoParallelBlue extends Auto {
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
        knockJewel();
        buildTelemetry();
        //=============================[Align to read Cryptobox]====================================
        // Align horizontally with the cryptobox
        robot.drivetrain.drive(Direction.BACKWARD, 1, 1200);
        robot.drivetrain.drive(Direction.LEFT, 1, 700);

        // Make sure we're aligned properly
        correctTurn(0, 2, .2);

        //=============================[Line up with column]========================================
        alignWithCryptobox();

        //=============================[Put Glyph in Cryptobox]=====================================
        correctTurn(-90);
        putGlyphInCryptobox();

        robot.beep();

    }
}
