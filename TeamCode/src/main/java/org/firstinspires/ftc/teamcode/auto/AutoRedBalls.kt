package org.firstinspires.ftc.teamcode.auto

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.enums.Alliance

/**
 * Created by josh on 1/13/18.
 */

@Autonomous(name = "Auto Red Balls", group = "emergency")
class AutoRedBalls : AutoBallOnly(Alliance.RED)