package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Drone Op Teleop code. Main teleop class.
 */

@SuppressWarnings("unused")
@TeleOp(name="Drone Op", group = "teleop")
public class DroneOp extends AbstractRevbotTeleOp {
    @Override
    public void setCurrentDirection(double[] direction) {
        double left = -gamepad1.left_stick_y;
        double right = -gamepad1.left_stick_y;
        double turn = gamepad1.right_stick_x;
        double strafe = -gamepad1.left_stick_x;

        left += turn;
        right -= turn;

        direction[0] = left;
        direction[1] = right;
        direction[2] = strafe;

        this.currentDirection = direction;
    }
}
