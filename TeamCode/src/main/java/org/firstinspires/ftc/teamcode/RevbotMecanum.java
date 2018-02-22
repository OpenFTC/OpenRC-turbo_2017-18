package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import me.joshlin.a3565lib.RobotMap;

/**
 * Created by 3565 on 2/15/2018.
 */

public class RevbotMecanum extends RobotMap {

    // Declare drive motors
    public DcMotor frontL;
    public DcMotor frontR;
    public DcMotor backL;
    public DcMotor backR;

    // Declare other motors
    public DcMotor lift;
    public DcMotor leftSpinner;
    public DcMotor rightSpinner;

    // Declare servos
    public Servo flipper;
    public Servo vertical;
    public Servo knock;

    // Declare other sensors
    public BNO055IMU imu;

    @Override
    public void init(HardwareMap aHwMap) {
        this.hardwareMap = aHwMap;

        // Assign motors to DcMotor variables
        frontL = hardwareMap.get(DcMotor.class, "frontL");
        frontR = hardwareMap.get(DcMotor.class, "frontR");
        backL = hardwareMap.get(DcMotor.class, "backL");
        backR = hardwareMap.get(DcMotor.class, "backR");

        lift = hardwareMap.get(DcMotor.class, "lift");
        leftSpinner = hardwareMap.get(DcMotor.class, "leftSpinner");
        rightSpinner = hardwareMap.get(DcMotor.class, "rightSpinner");

        // Assign servos to Servo variables
        flipper = hardwareMap.get(Servo.class, "flipper");
        vertical = hardwareMap.get(Servo.class, "vertical");
        knock = hardwareMap.get(Servo.class, "knock");

        // Assign sensors to sensor variables
        imu = hardwareMap.get(BNO055IMU.class, "imu");

        // Set motors to run with encoders
        setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER, frontL, frontR);
        setMode(DcMotor.RunMode.RUN_USING_ENCODER, backL, backR);

        // Set all motors to brake
        setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE, frontL, frontR, backL, backR);

        // Reverse right motor
        rightSpinner.setDirection(DcMotorSimple.Direction.REVERSE);
    }
}
