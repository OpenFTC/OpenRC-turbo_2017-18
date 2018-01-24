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

        boxLocation = Vuforia.readImage();
        telemetry.addData("Vuforia Target Seen:", boxLocation);
        telemetry.update();

        cubeClaw.close();

        telemetry.addData("Cube Claw", cubeClaw.getPosition());
        telemetry.update();

        servoSwivel.swivelCenter();

        armWinch.lower(3500);
        cubeLift.raise(125);

        drivetrain.strafe(Direction.LEFT, 0.5, 750);
        safeSleep(500);

        knockBalls(alliance);
        safeSleep(500);

        servoSwivel.swivelCenter();
        drivetrain.strafe(Direction.RIGHT, 0.5, 125);
        armWinch.raise(4000);
        safeSleep(500);
    }
}
