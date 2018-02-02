package org.firstinspires.ftc.teamcode.auto

import com.qualcomm.robotcore.eventloop.opmode.Autonomous

import org.firstinspires.ftc.teamcode.enums.Alliance
import org.firstinspires.ftc.teamcode.enums.Location

/**
 * Auto Red Bottom.
 */

@Autonomous(name = "Auto Red Right", group = "auto")
class AutoRedBottom : Auto(Alliance.RED, Location.LOWER)
