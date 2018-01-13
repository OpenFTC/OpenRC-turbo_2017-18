package org.firstinspires.ftc.teamcode.auto

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.enums.Alliance

/**
 * Created by josh on 1/13/18.
 */

@Autonomous(name = "Auto Blue Balls", group = "emergency")
class AutoBlueBalls : AutoBallOnly(Alliance.BLUE)