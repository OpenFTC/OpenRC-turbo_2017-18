package org.firstinspires.ftc.teamcode.robot;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This is the hardware map for the 2017-2018 robot.
 */
@SuppressWarnings("unused")
public class RevbotHardware {

    public DcMotor leftDrive = null;
    public DcMotor rightDrive = null;
    public DcMotor strafe = null;
    public DcMotor cubeLift = null;

    public Servo clawRight = null;
    public Servo clawLeft = null;
    public Servo fondler = null;

    public ColorSensor color = null;

    public ElapsedTime elapsedTime = new ElapsedTime();

    HardwareMap hwMap = null;
    public Resources res = null;

    public RevbotHardware() {

    }

    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        leftDrive  = hwMap.get(DcMotor.class, "leftDrive");
        rightDrive = hwMap.get(DcMotor.class, "rightDrive");
        strafe = hwMap.get(DcMotor.class, "strafe");
        cubeLift = hwMap.get(DcMotor.class, "cubeLift");

        clawRight = hwMap.get(Servo.class, "clawRight");
        clawLeft = hwMap.get(Servo.class, "clawLeft");
        fondler = hwMap.get(Servo.class, "fondler");

        color = hwMap.get(ColorSensor.class, "color");

        leftDrive.setDirection(DcMotor.Direction.REVERSE);
        rightDrive.setDirection(DcMotor.Direction.FORWARD);
        strafe.setDirection(DcMotor.Direction.FORWARD);
        cubeLift.setDirection(DcMotor.Direction.REVERSE);

        res = hwMap.appContext.getResources();
    }
}
