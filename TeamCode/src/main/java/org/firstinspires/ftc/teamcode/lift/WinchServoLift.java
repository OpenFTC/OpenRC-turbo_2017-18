package org.firstinspires.ftc.teamcode.lift;

import com.qualcomm.robotcore.hardware.CRServo;

/**
 * Created by josh on 1/19/18.
 */

public class WinchServoLift extends CRServoLift {
    public WinchServoLift(CRServo crServo, double down, double stop, double up) {
        super(crServo, down, stop, up);
    }

    @Override
    public void raise() {
        super.raise();
    }

    @Override
    public void lower() {
        super.lower();
    }
}
