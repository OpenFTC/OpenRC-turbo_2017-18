package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by josh on 1/7/18.
 */

@SuppressWarnings("unused")
@TeleOp(name = "2 Joy Op", group = "teleop")
public class TwoJoyOp extends RevbotTeleOp {
    @Override
    public void setCurrentDirection(double[] direction) {
        double left = -gamepad1.left_stick_y;
        double right = -gamepad1.right_stick_y;
        double strafe = (gamepad1.left_stick_x + gamepad1.right_stick_x) / 2;

        direction[0] = left;
        direction[1] = right;
        direction[2] = strafe;

        this.currentDirection = direction;
    }
}
