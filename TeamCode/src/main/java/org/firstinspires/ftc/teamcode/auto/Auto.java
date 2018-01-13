package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Revbot;
import org.firstinspires.ftc.teamcode.claw.CubeClaw;
import org.firstinspires.ftc.teamcode.claw.OneServoClaw;
import org.firstinspires.ftc.teamcode.claw.RelicClaw;
import org.firstinspires.ftc.teamcode.drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.drivetrain.Slide;
import org.firstinspires.ftc.teamcode.enums.Alliance;
import org.firstinspires.ftc.teamcode.enums.Column;
import org.firstinspires.ftc.teamcode.enums.Location;
import org.firstinspires.ftc.teamcode.lift.AbstractLift;
import org.firstinspires.ftc.teamcode.lift.ArmWinch;
import org.firstinspires.ftc.teamcode.lift.CubeLift;
import org.firstinspires.ftc.teamcode.lift.RelicSlide;
import org.firstinspires.ftc.teamcode.sensor.RevbotSensors;
import org.firstinspires.ftc.teamcode.swivel.BallKnock;
import org.firstinspires.ftc.teamcode.vuforia.Vuforia;

/**
 * Created by josh on 1/13/18.
 */

public abstract class Auto extends LinearOpMode {
    Revbot robot = new Revbot();

    OneServoClaw relicClaw;
    CubeClaw cubeClaw;
    BallKnock ballKnock;
    Drivetrain drivetrain;
    AbstractLift armWinch, cubeLift, relicSlide;

    RevbotSensors sensors;

    Alliance alliance;
    Column boxLocation;
    private Location location;

    Auto(Alliance alliance, Location location) {
        this.alliance = alliance;
        this.location = location;
    }

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);
        Vuforia.init();

        relicClaw = new RelicClaw(robot.relicClaw);
        cubeClaw = new CubeClaw(robot.clawLeft, robot.clawRight, 0.2, 0.8);
        ballKnock = new BallKnock(robot.fondler);
        drivetrain = new Slide(robot);
        armWinch = new ArmWinch(robot.armWinch);
        cubeLift = new CubeLift(robot.cubeLift);
        relicSlide = new RelicSlide(robot.relicSlide);
        sensors = new RevbotSensors(robot.color);

        robot.beep();
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();
    }

    void knockBalls(Alliance alliance) {

        if ((alliance.equals(Alliance.BLUE) && sensors.isBlue())
                || (alliance.equals(Alliance.RED) && sensors.isRed())) {
            ballKnock.swivelRight();
        } else if ((alliance.equals(Alliance.BLUE) && sensors.isRed())
                || (alliance.equals(Alliance.RED) && sensors.isBlue())) {
            ballKnock.swivelLeft();
        } else {
            ballKnock.swivelRight();
            ballKnock.swivelLeft();
        }

        Revbot.sleep(1000);

    }
}
