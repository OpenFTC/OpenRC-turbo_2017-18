package org.firstinspires.ftc.teamcode.auto

import com.qualcomm.robotcore.eventloop.opmode.Autonomous

/**
 * Holds callable AutoBlueBottom OpMode.
 */


@Autonomous(name = "Auto Blue Left", group = "auto")
class AutoBlueBottom : Auto(me.joshlin.a3565lib.enums.Alliance.BLUE, me.joshlin.a3565lib.enums.Location.LOWER)
