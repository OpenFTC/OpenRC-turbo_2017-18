package org.firstinspires.ftc.teamcode.robot;

import android.media.AudioManager;
import android.media.ToneGenerator;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

/**
 * This is the hardware map for the 2017-2018 robot.
 */
@SuppressWarnings("unused")
public class Revbot {

    // Global opMode variable
    private LinearOpMode myOpMode;

    // Drive speeds
    private double  driveAxial      = 0 ;   // Positive is forward
    private double  driveLateral    = 0 ;   // Positive is right
    private double  driveYaw        = 0 ;   // Positive is CCW

    // Define hardware
    public DcMotor leftDrive;
    public DcMotor rightDrive;
    public DcMotor strafeDrive;
    public DcMotor cubeLift;

    public Servo clawRight;
    public Servo clawLeft;
    public Servo fondler;
    public CRServo armWinch;

    public ColorSensor color;
    public DistanceSensor distance;

    public ElapsedTime elapsedTime = new ElapsedTime();

    private ToneGenerator tone;

    public Revbot() {

    }

    /**
     * void init() Initializes the hardware, call this method first
     * @param opMode pass in "this" - current opMode
     */
    public void init(LinearOpMode opMode) {
        // Save reference to opMode
        myOpMode = opMode;

        // Initalize drive motors
        leftDrive  = myOpMode.hardwareMap.get(DcMotor.class, "leftDrive");
        rightDrive = myOpMode.hardwareMap.get(DcMotor.class, "rightDrive");
        strafeDrive = myOpMode.hardwareMap.get(DcMotor.class, "strafe");

        // Initialize other motors
        cubeLift = myOpMode.hardwareMap.get(DcMotor.class, "cubeLift");

        // Initialize servos
        clawRight = myOpMode.hardwareMap.get(Servo.class, "clawRight");
        clawLeft = myOpMode.hardwareMap.get(Servo.class, "clawLeft");
        fondler = myOpMode.hardwareMap.get(Servo.class, "fondler");
        armWinch = myOpMode.hardwareMap.get(CRServo.class, "winch");

        // Initialize sensors
        color = myOpMode.hardwareMap.get(ColorSensor.class, "color");
        distance = myOpMode.hardwareMap.get(DistanceSensor.class, "color");

        //Create a tone
        tone = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);

        // Set motor directions
        leftDrive.setDirection(DcMotor.Direction.REVERSE);
        rightDrive.setDirection(DcMotor.Direction.FORWARD);
        strafeDrive.setDirection(DcMotor.Direction.FORWARD);
        cubeLift.setDirection(DcMotor.Direction.REVERSE);

        // Initialize the motors to run without encoders.
        setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        clawRight.setDirection(Dire);
    }

    public void moveRobot(double left, double right, double strafe) {
        leftDrive.setPower(left);
        rightDrive.setPower(right);
        strafeDrive.setPower(strafe);
    }

    /**
     * void beep() Make the robot play a tone.
     */
    public void beep() {
        tone.startTone(ToneGenerator.TONE_CDMA_KEYPAD_VOLUME_KEY_LITE);
    }

    /**
     * void setMode(DcMotor.RunMode mode) Set all drive motors to the same mode.
     * @param mode desired motor mode.
     */
    public void setMode(DcMotor.RunMode mode) {
        leftDrive.setMode(mode);
        rightDrive.setMode(mode);
        strafeDrive.setMode(mode);
    }

    public boolean LeoIsCool(){return false;} //Leo made this!
}
