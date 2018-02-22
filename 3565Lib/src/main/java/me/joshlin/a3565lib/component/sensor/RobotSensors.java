package me.joshlin.a3565lib.component.sensor;

import com.qualcomm.robotcore.hardware.ColorSensor;

import me.joshlin.a3565lib.enums.Alliance;

/**
 * Created by josh on 1/6/18.
 */

public class RobotSensors {
    ColorSensor color;

    public RobotSensors(ColorSensor color) {
        this.color = color;
    }

    public Alliance getAlliance() {
        if (color.blue() > color.red()) {
            return Alliance.BLUE;
        } else if (color.red() > color.blue()) {
            return Alliance.RED;
        }

        return Alliance.BLUE;
    }
}
