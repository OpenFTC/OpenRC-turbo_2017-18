package me.joshlin.a3565lib.component.sensor;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import me.joshlin.a3565lib.enums.Alliance;

/**
 * Created by josh on 1/6/18.
 */

public class RobotSensors {
    ColorSensor color;
    BNO055IMU imu;

    Orientation baseAngles;
    double globalAngle = 0;

    public RobotSensors(ColorSensor color) {
        this.color = color;
    }

    public Alliance getAlliance() {
        if (color.blue() > color.red()) {
            return Alliance.BLUE;
        } else if (color.red() > color.blue()) {
            return Alliance.RED;
        } else {
            return Alliance.UNKNOWN;
        }
    }

    /**
     * Resets the cumulative angle tracking to zero.
     */
    public void resetAngle() {
        baseAngles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        globalAngle = 0;
    }

    /**
     * Get current cumulative angle rotation from last reset.
     *
     * @return Angle in degrees. + = left, - = right.
     */
    public double getAngle() {
        // We experimentally determined the Z axis is the axis we want to use for heading angle.
        // We have to process the angle because the imu works in euler angles so the Z axis is
        // returned as 0 to +180 or 0 to -180 rolling back to -179 or +179 when rotation passes
        // 180 degrees. We detect this transition and track the total cumulative angle of rotation.

        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        double deltaAngle = angles.firstAngle - baseAngles.firstAngle;

        if (deltaAngle < -180) {
            deltaAngle += 360;
        } else if (deltaAngle > 180) {
            deltaAngle -= 360;
        }

        globalAngle += deltaAngle;

        baseAngles = angles;

        return globalAngle;
    }

}
