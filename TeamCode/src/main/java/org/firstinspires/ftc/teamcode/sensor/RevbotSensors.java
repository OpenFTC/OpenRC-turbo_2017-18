package org.firstinspires.ftc.teamcode.sensor;

import com.qualcomm.robotcore.hardware.ColorSensor;

/**
 * Created by josh on 1/6/18.
 */

public class RevbotSensors {
    ColorSensor color;

    public RevbotSensors(ColorSensor color) {
        this.color = color;
    }

    public boolean isBlue() {
        return ((color.blue() > color.red()));
    }

    public boolean isRed() {
        return ((color.red() > color.blue()));
    }
}
