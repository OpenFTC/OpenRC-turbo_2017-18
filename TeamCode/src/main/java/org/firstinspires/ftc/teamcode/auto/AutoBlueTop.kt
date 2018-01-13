package org.firstinspires.ftc.teamcode.auto

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.enums.Alliance

/**
 * Holds callable AutoBlueTop OpMode.
 */


@Autonomous(name = "Auto Blue Right", group = "auto")
class AutoBlueTop : AutoTop(Alliance.BLUE)
