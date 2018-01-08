package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.enums.Alliance;
import org.firstinspires.ftc.teamcode.enums.Location;

/**
 * Auto Red Top.
 */

@SuppressWarnings("unused")
@Autonomous(name = "Auto Red Top", group = "auto")
public class AutoRedTop extends AbstractAuto {
    public AutoRedTop() {
        super(Alliance.RED, Location.UPPER);
    }
}
