package org.firstinspires.ftc.teamcode.teleop

import com.qualcomm.robotcore.eventloop.opmode.TeleOp

/**
 * Created by josh on 1/7/18.
 */

@TeleOp(name = "2 Joy Op", group = "teleop")
class TwoJoyOp : RevbotTeleOp() {
    override fun getUserInput():DoubleArray {
        val direction = DoubleArray(3)
        val left = -gamepad1.left_stick_y.toDouble()
        val right = -gamepad1.right_stick_y.toDouble()
        val strafe = ((gamepad1.left_stick_x + gamepad1.right_stick_x) / 2).toDouble()

        direction[0] = left
        direction[1] = right
        direction[2] = strafe

        return direction
    }
}
