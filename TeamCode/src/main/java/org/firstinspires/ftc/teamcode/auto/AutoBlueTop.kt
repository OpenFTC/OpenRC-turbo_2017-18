package org.firstinspires.ftc.teamcode.auto

import com.qualcomm.robotcore.eventloop.opmode.Autonomous

import org.firstinspires.ftc.teamcode.enums.Alliance
import org.firstinspires.ftc.teamcode.enums.Location

/**
 * Holds callable AutoBlueTop OpMode.
 */


@Autonomous(name = "Auto Blue Top", group = "auto")
class AutoBlueTop : AutoTop(Alliance.BLUE, Location.UPPER)
