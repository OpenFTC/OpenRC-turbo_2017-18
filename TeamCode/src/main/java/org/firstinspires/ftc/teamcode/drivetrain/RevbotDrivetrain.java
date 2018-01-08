package org.firstinspires.ftc.teamcode.drivetrain;

import org.firstinspires.ftc.teamcode.enums.Direction;
import org.firstinspires.ftc.teamcode.robot.Revbot;

/**
 * Created by josh on 1/5/18.
 */

public class RevbotDrivetrain extends AbstractDrivetrain {
    Revbot robot;

    public RevbotDrivetrain(Revbot robot) {
        this.robot = robot;
    }

    @Override
    public void move(double[] direction) {
        robot.leftDrive.setPower(direction[0]);
        robot.rightDrive.setPower(direction[1]);
        robot.strafeDrive.setPower(direction[2]);
    }

    @Override
    public void strafe(Direction direction, double power) {
        switch (direction) {
            case LEFT:
                robot.strafeDrive.setPower(power);
                break;
            case RIGHT:
                robot.strafeDrive.setPower(-power);
                break;
            default:
                robot.beep();
                break;
        }
    }

    @Override
    public void stopStrafing() {
        robot.strafeDrive.setPower(0);
    }

    @Override
    public void drive(Direction direction, double power) {
        switch (direction) {
            case FORWARD:
                robot.leftDrive.setPower(power);
                robot.rightDrive.setPower(power);
                break;
            case BACKWARD:
                robot.leftDrive.setPower(-power);
                robot.rightDrive.setPower(-power);
                break;
            default:
                robot.beep();
                break;
        }
    }

    @Override
    public void stopDriving() {
        robot.leftDrive.setPower(0);
        robot.rightDrive.setPower(0);
    }

    @Override
    public void turn(Direction direction, double power) {
        switch (direction) {
            case LEFT:
                robot.leftDrive.setPower(-power);
                robot.rightDrive.setPower(power);
                break;
            case RIGHT:
                robot.leftDrive.setPower(power);
                robot.rightDrive.setPower(-power);
                break;
            default:
                robot.beep();
                break;
        }
    }

    @Override
    public void stopTurning() {
        robot.leftDrive.setPower(0);
        robot.rightDrive.setPower(0);
    }
}
