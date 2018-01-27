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

        toBalls();

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

        drivetrain.drive(Direction.BACKWARD, 0.5, 125);

        for (int i = 0; i < 3; i++) {
            robot.beep();
        }
    }

    void cubeIntoCrypotbox() {
        Direction turnDirection = alliance.equals(Alliance.RED) ? Direction.LEFT : Direction.RIGHT;
        Direction direction = alliance.equals(Alliance.RED) ? Direction.BACKWARD : Direction.FORWARD;
        drivetrain.turn(turnDirection, 0.5, 800);
        int baseMs = 200;
        int incrementMs = 250;
        if (alliance.equals(Alliance.RED)) {
            switch (boxLocation) {
                case RIGHT:
                    drivetrain.drive(direction, 0.5, baseMs + incrementMs);
                    break;

                case CENTER:
                    drivetrain.drive(direction, 0.5, baseMs + 2 *incrementMs);
                    break;

                case LEFT:
                    drivetrain.drive(direction, 0.5, baseMs + 3 * incrementMs);
                    break;
            }

        } else if (alliance.equals(Alliance.BLUE)) {
            switch (boxLocation) {
                case LEFT:
                    drivetrain.drive(direction, 0.5, baseMs + incrementMs);
                    break;

                case CENTER:
                    drivetrain.drive(direction, 0.5, baseMs + 2 * incrementMs);
                    break;

                case RIGHT:
                    drivetrain.drive(direction, 0.5, baseMs + 3 * incrementMs);
                    break;
            }
        }

        drivetrain.turn(Direction.LEFT, 0.5, 800);
    }
}
