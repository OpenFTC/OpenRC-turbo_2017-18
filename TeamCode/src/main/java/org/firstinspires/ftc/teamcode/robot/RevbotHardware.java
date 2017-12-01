package org.firstinspires.ftc.teamcode.robot;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.preference.PreferenceManager;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import static org.firstinspires.ftc.teamcode.robot.RevbotValues.*;

/**
 * This is the hardware map for the 2017-2018 robot.
 */
@SuppressWarnings("unused")
public class RevbotHardware {

    public DcMotor leftDrive;
    public DcMotor rightDrive;
    public DcMotor strafe;
    public DcMotor cubeLift;

    public Servo clawRight;
    public Servo clawLeft;
    public Servo fondler;

    public ColorSensor color;
    public DistanceSensor distance;

    public ElapsedTime elapsedTime = new ElapsedTime();

    HardwareMap hwMap;
    public Resources res;

    private ToneGenerator tone;

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
        distance = hwMap.get(DistanceSensor.class, "color");

        leftDrive.setDirection(DcMotor.Direction.REVERSE);
        rightDrive.setDirection(DcMotor.Direction.FORWARD);
        strafe.setDirection(DcMotor.Direction.FORWARD);
        cubeLift.setDirection(DcMotor.Direction.REVERSE);

        res = hwMap.appContext.getResources();

        tone = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
    }

    public void beep() {
        tone.startTone(ToneGenerator.TONE_CDMA_KEYPAD_VOLUME_KEY_LITE);
    }
}
