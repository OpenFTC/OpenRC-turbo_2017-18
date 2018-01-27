package org.firstinspires.ftc.teamcode.auto;

import org.firstinspires.ftc.teamcode.enums.Alliance;
import org.firstinspires.ftc.teamcode.enums.Direction;
import org.firstinspires.ftc.teamcode.enums.Location;
import org.firstinspires.ftc.teamcode.vuforia.Vuforia;

/**
 * Created by josh on 1/13/18.
 */

public abstract class AutoBallOnly extends Auto {


    AutoBallOnly(Alliance alliance) {
        super(alliance, Location.UPPER);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        toBalls();
    }
}
