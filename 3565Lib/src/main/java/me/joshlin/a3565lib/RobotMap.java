package me.joshlin.a3565lib;

import android.media.AudioManager;
import android.media.ToneGenerator;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import me.joshlin.a3565lib.component.drivetrain.Drivetrain;

/**
 * Marks a class as a hardware map.
 */

public abstract class RobotMap {

    protected HardwareMap hardwareMap;

    public Drivetrain drivetrain;

    private ToneGenerator tone = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);

    /**
     * Make the robot play a tone.
     */
    public void beep() {
        tone.startTone(ToneGenerator.TONE_CDMA_KEYPAD_VOLUME_KEY_LITE);
    }

    /**
     * Initializes the robot.
     * @param aHwMap the hardware map, obtained from the OpMode.
     */
    public abstract void init(HardwareMap aHwMap);

    /**
     *
     * @param mode
     * @param motors
     */
    protected void setMode(DcMotor.RunMode mode, DcMotor... motors) {
        for (DcMotor motor : motors) {
            motor.setMode(mode);
        }
    }

    protected void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior zeroPowerBehavior, DcMotor... motors) {
        for (DcMotor motor : motors) {
            motor.setZeroPowerBehavior(zeroPowerBehavior);
        }
    }
}
