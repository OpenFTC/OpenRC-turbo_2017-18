package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.Revbot;
import org.firstinspires.ftc.teamcode.claw.OneServoClaw;
import org.firstinspires.ftc.teamcode.claw.TwoServoClaw;
import org.firstinspires.ftc.teamcode.drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.drivetrain.Slide;
import org.firstinspires.ftc.teamcode.enums.Alliance;
import org.firstinspires.ftc.teamcode.enums.Direction;
import org.firstinspires.ftc.teamcode.enums.Location;
import org.firstinspires.ftc.teamcode.lift.CRServoLift;
import org.firstinspires.ftc.teamcode.lift.Lift;
import org.firstinspires.ftc.teamcode.lift.MotorLift;
import org.firstinspires.ftc.teamcode.sensor.RobotSensors;
import org.firstinspires.ftc.teamcode.swivel.ServoSwivel;
import org.firstinspires.ftc.teamcode.vuforia.Vuforia;

/**
 * Created by josh on 1/13/18.
 */

public abstract class Auto extends LinearOpMode {
    Revbot robot = new Revbot();

    Vuforia vuforia = new Vuforia();

    // Claw for relic
    OneServoClaw relicClaw;

    // Claw for
    OneServoClaw glyphGrip;
    TwoServoClaw cubeClaw;
    ServoSwivel servoSwivel;
    Drivetrain drivetrain;
    Lift armWinch, cubeLift, relicSlide;

    RobotSensors sensors;

    Alliance alliance;
    RelicRecoveryVuMark boxLocation;
    private Location location;

    Auto(Alliance alliance, Location location) {
        this.alliance = alliance;
        this.location = location;
    }

    /** Main class that runs whenever the OpMode is called from the phone
     *
     * @throws InterruptedException Exception is thrown
     */
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
        sensors = new RobotSensors(robot.color);
        glyphGrip = new OneServoClaw(robot.glyphGrip, 0.2, 1);

        glyphGrip.close();

        robot.beep();
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();

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

    /**
     *
     * @param alliance
     */
    // TODO: Fix this now
    void knockBalls(Alliance alliance) {
        if (alliance.equals(sensors.getAlliance())) {
            //Knocks off right ball
            servoSwivel.swivelRight();
        } else if (alliance.equals(sensors.getAlliance())) {
            //Knocks off left ball
            servoSwivel.swivelLeft();
        } else {
            //Knocks off both balls
            servoSwivel.swivelRight();
            servoSwivel.swivelLeft();
        }

        sleep(1000);
    }

    /**
     *
     */
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

    void cubeIntoCrypotbox() {

        // Time it takes to reach the first column of the box
        int baseMs = location.equals(Location.UPPER) ? 50 : 200;

        // Time it takes to travel between columns
        int incrementMs = 250;

        // Time it takes to turn 180˚
        int turnMs = 750;

        Direction direction = alliance.equals(Alliance.RED) ? Direction.BACKWARD : Direction.FORWARD;

        Direction turnDirection = alliance.equals(Alliance.RED) ? Direction.LEFT : Direction.RIGHT;

        if (location.equals(Location.LOWER)) {
            // Align the robot so that the camera is facing the cryptobox
            drivetrain.turn(turnDirection, 0.5, turnMs);
        }

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

        // Turn left to face the cryptobox
        drivetrain.turn(Direction.LEFT, 0.5, turnMs);
    }
}
