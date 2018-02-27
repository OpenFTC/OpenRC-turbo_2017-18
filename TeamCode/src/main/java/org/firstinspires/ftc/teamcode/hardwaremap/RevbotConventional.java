package org.firstinspires.ftc.teamcode.hardwaremap;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import me.joshlin.a3565lib.RobotMap;

/**
 * Created by 3565 on 2/16/2018.
 */

public class RevbotConventional extends RobotMap {
    public DcMotor left, right;
    public DcMotor lift, sucking;
    public Servo glyphPivot;

    @Override
    public void init(HardwareMap aHwMap) {
        this.hardwareMap = aHwMap;

        left = hardwareMap.get(DcMotor.class, "DriveLeft");
        right = hardwareMap.get(DcMotor.class, "DriveRight");
        lift = hardwareMap.get(DcMotor.class, "Lifter");
        sucking = hardwareMap.get(DcMotor.class, "Feeder");
        glyphPivot = hardwareMap.get(Servo.class, "GlyphPivot");

        sucking.setDirection(DcMotorSimple.Direction.REVERSE);

        left.setDirection(DcMotorSimple.Direction.REVERSE);
    }
}
