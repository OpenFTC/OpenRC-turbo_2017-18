package org.firstinspires.ftc.teamcode.auto

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.enums.Alliance
import org.firstinspires.ftc.teamcode.enums.Location

/**
 * Holds callable AutoBlueBottom OpMode.
 */


@Autonomous(name = "Auto Blue Left", group = "auto")
class AutoBlueBottom : Auto(Alliance.BLUE, Location.LOWER)
