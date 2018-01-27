package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.Revbot;
import org.firstinspires.ftc.teamcode.claw.OneServoClaw;
import org.firstinspires.ftc.teamcode.claw.TwoServoClaw;
import org.firstinspires.ftc.teamcode.drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.drivetrain.Slide;
import org.firstinspires.ftc.teamcode.enums.Alliance;
import org.firstinspires.ftc.teamcode.enums.Column;
import org.firstinspires.ftc.teamcode.enums.Direction;
import org.firstinspires.ftc.teamcode.enums.Location;
import org.firstinspires.ftc.teamcode.lift.CRServoLift;
import org.firstinspires.ftc.teamcode.lift.Lift;
import org.firstinspires.ftc.teamcode.lift.MotorLift;
import org.firstinspires.ftc.teamcode.sensor.RevbotSensors;
import org.firstinspires.ftc.teamcode.swivel.ServoSwivel;
import org.firstinspires.ftc.teamcode.vuforia.Vuforia;

/**
 * Created by josh on 1/13/18.
 */

public abstract class Auto extends LinearOpMode {
    Revbot robot = new Revbot();

    OneServoClaw relicClaw;
    OneServoClaw glyphGrip;
    TwoServoClaw cubeClaw;
    ServoSwivel servoSwivel;
    Drivetrain drivetrain;
    Lift armWinch, cubeLift, relicSlide;

    Vuforia vuforia = new Vuforia();

    RevbotSensors sensors;

    Alliance alliance;
    RelicRecoveryVuMark boxLocation;
    private Location location;

    Auto(Alliance alliance, Location location) {
        this.alliance = alliance;
        this.location = location;
    }

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);
        vuforia.init(hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName()));

        glyphGrip = new OneServoClaw(robot.glyphGrip);
        relicClaw = new OneServoClaw(robot.relicClaw);
        cubeClaw = new TwoServoClaw(robot.clawLeft, robot.clawRight, 0.2, 0.8);
        servoSwivel = new ServoSwivel(robot.ballKnock);
        drivetrain = new Slide(robot);
        armWinch = new CRServoLift(robot.armWinch, -1, 0, 1);
        cubeLift = new MotorLift(robot.cubeLift, -1, 0, 1);
        relicSlide = new CRServoLift(robot.relicSlide, -1, 0, 1);
        sensors = new RevbotSensors(robot.color);
        glyphGrip = new OneServoClaw(robot.glyphGrip, 0.2, 1);

        glyphGrip.close();

        robot.beep();
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();
    }

    //Knocks the ball off the stand
    void knockBalls(Alliance alliance) {

        //Knocks off right ball
        if ((alliance.equals(Alliance.BLUE) && sensors.isBlue())
                || (alliance.equals(Alliance.RED) && sensors.isRed())) {
            servoSwivel.swivelRight();
            //Knocks off left ball
        } else if ((alliance.equals(Alliance.BLUE) && sensors.isRed())
                || (alliance.equals(Alliance.RED) && sensors.isBlue())) {
            servoSwivel.swivelLeft();
            //Knocks off both balls
        } else {
            servoSwivel.swivelRight();
            servoSwivel.swivelLeft();
        }

        sleep(1000);
    }

    void toBalls() {
        cubeClaw.close();
        sleep(500);
        glyphGrip.open();

        telemetry.addData("Cube Claw", cubeClaw.getPosition());
        telemetry.update();

        servoSwivel.swivelCenter();

        armWinch.lower(3500);
        cubeLift.raise(125);

        // drivetrain.strafe(Direction.LEFT, 0.5, 100);
        // sleep(1000);
        // boxLocation = vuforia.readImage();
        boxLocation = RelicRecoveryVuMark.CENTER;
        telemetry.addData("Column", boxLocation);
        telemetry.update();
        sleep(2000);

        drivetrain.strafe(Direction.LEFT, 0.5, 1000);

        sleep(500);

        knockBalls(alliance);
        sleep(500);

        servoSwivel.swivelCenter();
        drivetrain.strafe(Direction.RIGHT, 0.5, 125);
        armWinch.raise(4500);
        sleep(500);
    }
}
