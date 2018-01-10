package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.claw.OneServoClaw;
import org.firstinspires.ftc.teamcode.claw.CubeClaw;
import org.firstinspires.ftc.teamcode.claw.RelicClaw;
import org.firstinspires.ftc.teamcode.drivetrain.AbstractDrivetrain;
import org.firstinspires.ftc.teamcode.drivetrain.RevbotDrivetrain;
import org.firstinspires.ftc.teamcode.enums.Alliance;
import org.firstinspires.ftc.teamcode.enums.Column;
import org.firstinspires.ftc.teamcode.enums.Direction;
import org.firstinspires.ftc.teamcode.enums.Location;
import org.firstinspires.ftc.teamcode.lift.AbstractLift;
import org.firstinspires.ftc.teamcode.lift.ArmWinch;
import org.firstinspires.ftc.teamcode.lift.CubeLift;
import org.firstinspires.ftc.teamcode.lift.RelicSlide;
import org.firstinspires.ftc.teamcode.robot.Revbot;
import org.firstinspires.ftc.teamcode.swivel.BallKnock;
import org.firstinspires.ftc.teamcode.vuforia.Vuforia;

/**
 * Abstract Autonomous class from which all Auto classes extend from.
 */

public abstract class AbstractAuto extends LinearOpMode {
    private Revbot robot = new Revbot();

    private OneServoClaw relicClaw;
    private CubeClaw cubeClaw;
    private BallKnock ballKnock;
    private AbstractDrivetrain drivetrain;
    private AbstractLift armWinch, cubeLift, relicSlide;

    private Alliance alliance;
    private Location location;
    private Column boxLocation;

    AbstractAuto(Alliance alliance, Location location) {
        this.alliance = alliance;
        this.location = location;
    }

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);
        Vuforia.init();

        relicClaw = new RelicClaw(robot.relicClaw);
        cubeClaw = new CubeClaw(robot.clawLeft, robot.clawRight, 0.2, 0.8);
        ballKnock = new BallKnock(robot.fondler, robot.color);
        drivetrain = new RevbotDrivetrain(robot);
        armWinch = new ArmWinch(robot.armWinch);
        cubeLift = new CubeLift(robot.cubeLift);
        relicSlide = new RelicSlide(robot.relicSlide);


        cubeClaw.close();
        telemetry.addData("Cube Claw", cubeClaw.getPosition());
        ballKnock.swivelCenter();

        armWinch.lower(4000);
        cubeLift.raise(125);

        robot.beep();
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        boxLocation = Vuforia.readImage();

        drivetrain.strafe(Direction.LEFT, 0.75, 750);
        sleep(500);

        ballKnock.knockBalls(alliance);
        sleep(500);

        drivetrain.strafe(Direction.RIGHT, 0.25, 1000);
        ballKnock.swivelCenter();
        armWinch.raise(4500);
        drivetrain.stopStrafing();
        sleep(500);

        drivetrain.drive(alliance.equals(Alliance.RED) ? Direction.BACKWARD : Direction.FORWARD, 0.5, 750);
        sleep(500);

        drivetrain.strafe(Direction.RIGHT, 0.5, 750);
        sleep(500);

        cubeIntoCrypotbox();
        sleep(500);

        drivetrain.turn(Direction.LEFT, 0.5, 1500);
        sleep(500);

        drivetrain.drive(Direction.FORWARD, 0.5, 1000);
        sleep(500);

        cubeLift.lower(125);

        cubeClaw.open();

        for (int i = 0; i < 3; i++) {
            robot.beep();
        }
    }

    private void cubeIntoCrypotbox() {
        Direction direction = alliance.equals(Alliance.RED) ? Direction.BACKWARD : Direction.FORWARD;
        switch (boxLocation) {
            case RIGHT:
                drivetrain.drive(direction, 0.5, 500);
                break;

            case CENTER:
                drivetrain.drive(direction, 0.5, 1000);
                break;

            case LEFT:
                drivetrain.drive(direction, 0.5, 1500);
                break;
        }
    }
}
