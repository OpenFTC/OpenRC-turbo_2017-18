package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.enums.Alliance;
import org.firstinspires.ftc.teamcode.enums.Location;

/**
 * Holds callable AutoBlueTop OpMode.
 */


@SuppressWarnings("unused")
@Autonomous(name = "Auto Blue Top", group = "auto")
public class AutoBlueTop extends AbstractAuto {
    public AutoBlueTop() {
        super(Alliance.BLUE, Location.UPPER);
    }
}
