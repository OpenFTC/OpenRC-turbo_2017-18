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
    public CRServo spacerArmCR;

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
        spacerArmCR = myOpMode.hardwareMap.get(CRServo.class, "winch");

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

        moveRobot(0,0,0);
        spacerArmCR.setPower(0);
    }

    /**
     * void manualDrive() A basic drive system utilizing the axial, lateral, and yaw variables. Can be used in autonomous testing.
     */
    public void manualDrive()  {
        // In this mode the Left stick moves the robot fwd & back, and Right & Left.
        // The Right stick rotates CCW and CW.

        //  (note: The joystick goes negative when pushed forwards, so negate it)
        setAxial(-myOpMode.gamepad1.left_stick_y);
        setLateral(myOpMode.gamepad1.left_stick_x);
        setYaw(-myOpMode.gamepad1.right_stick_x);
    }

    /**
     * void moveRobot(double axial, double lateral, double yaw)
     * Set speed levels to motors based on axes requests
     * @param axial     Speed in Fwd Direction
     * @param lateral   Speed in lateral direction (+ve to right)
     * @param yaw       Speed of Yaw rotation.  (+ve is CCW)
     */
    public void moveRobot(double axial, double lateral, double yaw) {
        setAxial(axial);
        setLateral(lateral);
        setYaw(yaw);
        moveRobot();
    }

    /***
     * void moveRobot()
     * This method will calculate the motor speeds required to move the robot according to the
     * speeds that are stored in the three Axis variables: driveAxial, driveLateral, driveYaw.
     * This code is setup for a five wheeled OMNI-drive but it could be modified for any sort of omni drive.
     *
     * The code assumes the following conventions.
     * 1) Positive speed on the Axial axis means move FORWARD.
     * 2) Positive speed on the Lateral axis means move RIGHT.
     * 3) Positive speed on the Yaw axis means rotate COUNTER CLOCKWISE.
     *
     * This convention should NOT be changed.  Any new drive system should be configured to react accordingly.
     */
    public void moveRobot() {
        // calculate required motor speeds to acheive axis motions
        double strafe = driveYaw - driveLateral;
        double left = driveYaw + driveAxial;
        double right = driveYaw + driveAxial;

        // normalize all motor speeds so no values exceeds 100%.
        double max = Math.max(Math.abs(strafe), Math.abs(right));
        max = Math.max(max, Math.abs(left));
        if (max > 1.0)
        {
            strafe /= max;
            right /= max;
            left /= max;
        }

        // Set drive motor power levels.
        strafeDrive.setPower(strafe);
        leftDrive.setPower(left);
        rightDrive.setPower(right);

        // Display Telemetry
        myOpMode.telemetry.addData("Axes  ", "A[%+5.2f], L[%+5.2f], Y[%+5.2f]", driveAxial, driveLateral, driveYaw);
        myOpMode.telemetry.addData("Wheels", "L[%+5.2f], R[%+5.2f], B[%+5.2f]", left, right, strafe);
    }

    public void setAxial(double axial)      {driveAxial = Range.clip(axial, -1, 1);}
    public void setLateral(double lateral)  {driveLateral = Range.clip(lateral, -1, 1); }
    public void setYaw(double yaw)          {driveYaw = Range.clip(yaw, -1, 1); }

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
}
