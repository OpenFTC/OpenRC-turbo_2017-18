package me.joshlin.a3565lib;

import android.media.AudioManager;
import android.media.ToneGenerator;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Marks a class as a hardware map.
 */

public abstract class RobotMap {
    public HardwareMap hardwareMap;
    private ToneGenerator tone = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);

    /**
     * void beep() Make the robot play a tone.
     */
    public void beep() {
        tone.startTone(ToneGenerator.TONE_CDMA_KEYPAD_VOLUME_KEY_LITE);
    }

    public abstract void init(HardwareMap aHwMap);

    public void setMode(DcMotor.RunMode mode, DcMotor... motors) {
        for (DcMotor motor : motors) {
            motor.setMode(mode);
        }
    }

    public void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior zeroPowerBehavior, DcMotor... motors) {
        for (DcMotor motor : motors) {
            motor.setZeroPowerBehavior(zeroPowerBehavior);
        }
    }
}
