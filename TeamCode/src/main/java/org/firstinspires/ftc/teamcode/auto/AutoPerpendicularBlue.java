package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import me.joshlin.a3565lib.enums.Alliance;
import me.joshlin.a3565lib.enums.Direction;
import me.joshlin.a3565lib.enums.Location;

/**
 * @author Josh
 */
@SuppressWarnings("unused")
@Autonomous(name = "Auto Blue Perpendicular", group = "Auto")
public class AutoPerpendicularBlue extends Auto {
    @Override
    public void runOpMode() throws InterruptedException {
        //=================================[Initialization]=======================================
        init(Alliance.BLUE, Location.PERPENDICULAR);
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
        //=============================[Align to read Cryptobox]====================================
        // Align horizontally with the cryptobox
        robot.drivetrain.drive(Direction.BACKWARD, .6, 1500);
        correctTurn(-90);
        robot.drivetrain.drive(Direction.BACKWARD, 1, 500);

        // Make sure we're aligned properly
        correctTurn(-90);

        //=============================[Line up with column]========================================
        alignWithCryptobox();

        //=============================[Put Glyph in Cryptobox]=====================================
        correctTurn(-175);
        putGlyphInCryptobox();

        robot.beep();

    }

}
