package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import me.joshlin.a3565lib.component.RobotMap;

/**
 * Created by 3565 on 2/15/2018.
 */

public class RevbotMecanum extends RobotMap {

    public DcMotor backL, backR, frontL, frontR;

    @Override
    public void init(HardwareMap aHwMap) {
        this.hardwareMap = aHwMap;

        // Assign motors to DcMotor variables
        backL = hardwareMap.get(DcMotor.class, "backL");
        backR = hardwareMap.get(DcMotor.class, "backR");
        frontL = hardwareMap.get(DcMotor.class, "frontL");
        frontR = hardwareMap.get(DcMotor.class, "frontR");

        // Set motors to run with encoders
        setMode(DcMotor.RunMode.RUN_USING_ENCODER, backL, backR, frontL, frontR);
    }
}
