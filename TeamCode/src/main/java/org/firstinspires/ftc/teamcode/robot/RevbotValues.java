package org.firstinspires.ftc.teamcode.robot;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

/**
 * Values used by motors/servos/other on the robot.
 */

@SuppressWarnings("unused")
public class RevbotValues {

    // Claw values
    public static final double LEFT_CLAW_CLOSED_VALUE = 0.8;
    public static final double LEFT_CLAW_OPENED_VALUE = 0.25;
    public static final double RIGHT_CLAW_CLOSED_VALUE = 0.2;
    public static final double RIGHT_CLAW_OPENED_VALUE = 0.75;

    // Fondler values
    public static final double FONDLER_CENTER_VALUE = 0.5;
    public static final double FONDLER_LEFT_VALUE = 0.175;
    public static final double FONDLER_RIGHT_VALUE = 0.7;
    public static final double FONDLER_FULLY_RIGHT = 0.85;

    // CR values
    public static final double CR_FORWARDS_VALUE = 1;
    public static final double CR_BACKWARDS_VALUE = -1;
    public static final double CR_STOP_VALUE = 0;

    // Vuforia License Key
    public static final String VUFORIA_LICENSE_KEY = "AV+GL7P/////AAAAGV7nYsIVuU1VqFIOfsYp0KQh9xxfhpv8vYZhVm2dOSNCK0IZ89FNdUqXUDb6FTmwosSwYv2iGyNNaeH8OGd+EYA+URkJXmtxYXTSjSxlfL7ijgu118//656cnaSAP9MIVR/y49UXnlSr9iRk2N9zUunYC4EJUpPNn6cLW4wV1t4lHtxdKHu5OQ3n7hiJVkJw+5ax0SvQ9QW6H2XcR6BpNQgN0v15zs8anuqiaRoWzV5wIqBc2NWMnmNDCuRy9de9uJPRZFglQXX5Kq1wuVH7N/B+nVRpVmJ8jnIKpEVO+nM8l7HiCfOpwdteALuWimYChVWCms06HjOZ58U3UjEHjXjELlqS9w2iYMWPOvA17HMx";

    // Vuforia Constants
    public static final int     MAX_TARGETS    =   1;
    public static final double  ON_AXIS        =  10;      // Within 1.0 cm of target center-line
    public static final double  CLOSE_ENOUGH   =  20;      // Within 2.0 cm of final target standoff

    // Select which camera you want use.  The FRONT camera is the one on the same side as the screen.  Alt. is BACK
    public static final VuforiaLocalizer.CameraDirection CAMERA_CHOICE = VuforiaLocalizer.CameraDirection.BACK;

    // Error corrections
    public  static final double  YAW_GAIN       =  0.018;   // Rate at which we respond to heading error
    public  static final double  LATERAL_GAIN   =  0.0027;  // Rate at which we respond to off-axis error
    public  static final double  AXIAL_GAIN     =  0.0017;  // Rate at which we respond to target distance errors

    public static final double TARGET_DISTANCE = 400.0; // distance away from the target that the robot should end up, in mm

    public static final String DIRECTION_LEFT = "left";
    public static final String DIRECTION_RIGHT = "right";
    public static final String DIRECTION_FORWARD = "forward";
    public static final String DIRECTION_BACKWARD = "backward";
    public static final String COLOR_RED = "red";
    public static final String COLOR_BLUE = "blue";

}
