package org.firstinspires.ftc.teamcode.auto

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.enums.Alliance
import org.firstinspires.ftc.teamcode.enums.Location

/**
 * Auto Red Top.
 */

@Autonomous(name = "Auto Red Left", group = "auto")
class AutoRedTop : Auto(Alliance.RED, Location.UPPER)
