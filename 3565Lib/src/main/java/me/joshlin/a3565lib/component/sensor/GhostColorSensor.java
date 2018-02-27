package me.joshlin.a3565lib.component.sensor;

import com.qualcomm.robotcore.hardware.ColorSensor;

import me.joshlin.a3565lib.enums.Alliance;

/**
 * @author Josh
 *         A wrapper class for a {@link ColorSensor}.
 *         Adds methods for ease of reading colors in FTC.
 *         Note: Ignores the color green.
 */

public class GhostColorSensor implements GhostSensor {
    /**
     * Holds the color sensor passed into the object.
     */
    private ColorSensor color;

    /**
     * Constructor.
     *
     * @param colorSensor the color sensor to pass in
     */
    public GhostColorSensor(ColorSensor colorSensor) {
        this.color = colorSensor;
    }

    /**
     * Check if the color sensor sees red.
     *
     * @return true if red has the largest RGB value
     */
    public boolean isRed() {
        return color.red() > color.blue();
    }

    /**
     * Check if the color sensor sees blue.
     *
     * @return true if blue has the largest RGB value
     */
    public boolean isBlue() {
        return color.blue() > color.red();
    }

    /**
     * Return the color of the alliance that the color sensor reads.
     * Checks by seeing if blue == true and !red == true; otherwise, returns red
     * May not be 100% accurate!
     *
     * @return the alliance that the sensor sees
     */
    public Alliance readColor() {
        if (isBlue() && !isRed()) {
            return Alliance.BLUE;
        } else {
            return Alliance.RED;
        }
    }

    @Override
    public void init() {
        // Color sensor wrapper doesn't need initialization right now
        // Any initialization steps should go here
    }
}
