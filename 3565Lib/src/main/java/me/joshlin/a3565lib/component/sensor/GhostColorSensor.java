package me.joshlin.a3565lib.component.sensor;

import com.qualcomm.robotcore.hardware.ColorSensor;

/**
 * @author Josh
 * A wrapper class for a {@link ColorSensor}.
 * Adds methods for ease of reading colors.
 */

public class GhostColorSensor {
    /**
     * Holds the color sensor passed into the object.
     */
    private ColorSensor colorSensor;

    /**
     * Constructor.
     * @param colorSensor the color sensor to pass in
     */
    public GhostColorSensor(ColorSensor colorSensor) {
        this.colorSensor = colorSensor;
    }
}
