package org.firstinspires.ftc.teamcode.auto;

import org.firstinspires.ftc.teamcode.enums.Alliance;
import org.firstinspires.ftc.teamcode.enums.Direction;
import org.firstinspires.ftc.teamcode.enums.Location;
import org.firstinspires.ftc.teamcode.vuforia.Vuforia;

/**
 * Created by josh on 1/13/18.
 */

public abstract class AutoBottom extends Auto {
    AutoBottom(Alliance alliance) {
        super(alliance, Location.LOWER);
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

        ballKnock.swivelCenter();

        armWinch.lower(3500);
        cubeLift.raise(125);

        drivetrain.strafe(Direction.LEFT, 0.5, 750);
        sleep(500);

        knockBalls(alliance);
        sleep(500);

        ballKnock.swivelCenter();
        drivetrain.strafe(Direction.RIGHT, 0.5, 125);
        armWinch.raise(4000);
        sleep(500);

        drivetrain.drive(alliance.equals(Alliance.RED) ? Direction.BACKWARD : Direction.FORWARD, 0.5, 700);
        sleep(500);

        drivetrain.strafe(Direction.RIGHT, 0.5, 750);
        sleep(500);

        cubeIntoCrypotbox();
        sleep(500);

        drivetrain.drive(Direction.FORWARD, 0.5, 1000);
        sleep(500);

        cubeLift.lower(125);

        cubeClaw.open();

        for (int i = 0; i < 3; i++) {
            robot.beep();
        }
    }

    void cubeIntoCrypotbox() {
        Direction direction = alliance.equals(Alliance.RED) ? Direction.LEFT : Direction.RIGHT;
        if (alliance.equals(Alliance.RED)) {
            drivetrain.turn(direction, 0.5, 1800);
            switch (boxLocation) {
                case RIGHT:
                    drivetrain.strafe(direction, 0.5, 300);
                    break;

                case CENTER:
                    drivetrain.strafe(direction, 0.5, 550);
                    break;

                case LEFT:
                    drivetrain.strafe(direction, 0.5, 800);
                    break;
            }
        } else if (alliance.equals(Alliance.BLUE)) {
            switch (boxLocation) {
                case LEFT:
                    drivetrain.strafe(direction, 0.5, 300);
                    break;

                case CENTER:
                    drivetrain.strafe(direction, 0.5, 550);
                    break;

                case RIGHT:
                    drivetrain.strafe(direction, 0.5, 800);
                    break;
            }
        }
    }
}
