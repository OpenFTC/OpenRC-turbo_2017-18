package me.joshlin.a3565lib.component.drivetrain;

/**
 * @author Leo
 *         <p>
 *         ======================================================================================
 *         Math involved in converting input values (vectors / directions) to the motor values
 *         required to move the robot in that direction using mecanum wheels.
 *         ======================================================================================
 *         <p>
 *         Made by LEO (See? I'm helping)
 *         <p>
 *         The motor values are passed around as arrays containing 4 double values:
 *         <p>
 *         {motor 1, motor 2, motor 3, motor 4}
 *         <p>
 *         The motors correspond to the array elements like so:
 *         <p>
 *         [Motor 1]                    [Motor 2]
 *         [^Front of robot^]
 *         [   ROBOT base   ]
 *         [Motor 3]                    [Motor 4]
 *         <p>
 *         (The directions of the mecanum wheels should form a diamond when viewed from above, NOT an 'X')
 *         (It is not certain if the code would still work with an 'X' arrangement, but it might)
 *         =======================================================================================
 *         <p>
 *         The length of the vectors passed to the methods do NOT correspond to drive distance.
 *         Instead, longer vectors will produce faster motion (but the motors can only handle values between -1 and 1)
 *         <p>
 *         If you are unfamiliar with vectors and how they are represented, consult the internet first.
 *         This code does not use any complicated vector math, only simple conversions and some
 *         addition
 *         =======================================================================================
 */

public class DriveMath {

    //===================================[Constants]===========================================
    //change these constants if the robot is not behaving correctly!
    private static final double[] verticalDrive = {1, -1, 1, -1};   //the motor values that drive straight forward  (maybe backwards?)
    private static final double[] horizontalDrive = {1, 1, -1, -1};   //the motor values that strafe left             (maybe right?)
    private static final double[] turnDrive = {1, 1, 1, 1};        //the motor values that turn clockwise          (maybe counterclockwise?)


    //==================================[Methods]==============================================

    /**
     * Converts a vector in polar notation (angle and length) to a vector in rectangular notation (x and y).
     * This is just vector conversion, and has no dependency on the robot itself.
     *
     * @param angle  the angle of the vector
     * @param length the length of the vector
     * @return a vector in rectangular notation
     */
    public static double[] angleToVector(double angle, double length) {

        double[] vector = new double[2];

        //uses Math.sin and Math.cos to find x and y coordinates
        double x = Math.cos(angle) * length;
        double y = Math.sin(angle) * length;

        //puts the coordinates in an array to return
        vector[0] = x;
        vector[1] = y;

        //the coordinates are returned
        return vector;
    }

    /**
     * Converts controller input (x and y) to motor values.
     *
     * @param x    controller input for the x-axis
     * @param y    controller input for the y-axis
     * @param turn controller input for turning
     * @return motor values for the four motors
     */
    public static double[] inputsToMotors(double x, double y, double turn) {

        //this part just makes editing the rest of the code easier
        double x_input = x;
        double y_input = y;
        double turn_input = ((y_input > 0) ? turn : -turn); //the turning is reversed if the robot is driving backwards

        double[] driveMatrix = new double[4];

        double[] xMatrix = new double[4];
        double[] yMatrix = new double[4];
        double[] turnMatrix = new double[4];

        //sets values of matrices in x, y, and turning directions
        for (int i = 0; i < 4; i++) {
            xMatrix[i] = horizontalDrive[i] * x_input;
            yMatrix[i] = verticalDrive[i] * y_input;
            turnMatrix[i] = turnDrive[i] * turn_input;
        }

        //adds matrices to get motor values
        for (int i = 0; i < 4; i++) {
            driveMatrix[i] = xMatrix[i] + yMatrix[i] + turnMatrix[i];
        }

        //driveMatrix = normalize(driveMatrix);     //**optional**

        return driveMatrix;
    }

    /**
     * Normalizes an input vector of any dimension (scales a vector so its length is 1 (one) unit).
     * This math is independent of the robot itself and will work even with vectors of higher dimensions.
     *
     * @param vector the vector to normalize
     * @return the normalized vector
     */
    public static double[] normalize(double[] vector) {

        //finds length of vector
        double sqrSum = 0;
        for (double d : vector) {
            sqrSum += d * d;
        }

        double length = Math.sqrt(sqrSum); //length

        //divide the vector by its length
        for (int i = 0; i < vector.length; i++) {
            vector[i] /= length;
        }


        return vector;  //returns normalized vector
    }

    /**
     * Converts a rectangular vector (x and y) to motor values.
     * same as inputsToMotors, but allows the input vector to be from a traditional coordinate plane (not the weird inverted ones from the gamepad's joystick).
     *
     * @param x    x-value of the vector
     * @param y    y-value of the vector
     * @param turn turn value for the robot
     * @return motor values for the four motors
     */
    public static double[] vectorToMotors(double x, double y, double turn) {
        //converts y and x coordinates to the (inverted) values they would be on the gamepad
        y = -y; //only y is inverted (I guess?)

        return inputsToMotors(x, y, turn); //just runs the other method
    }

    /**
     * Converts a polar vector directly to motor values.
     * This method does not include a "turn" value (defaults to 0).
     *
     * @param angle  the angle of the vector
     * @param length the length of the vector
     * @return motor values for the four motors
     */
    public static double[] angleToMotors(double angle, double length) {
        return angleToMotors(angle, length, 0);
    }

    /**
     * Converts a polar vector and turn value directly to motor values.
     * This method includes a "turn" value.
     *
     * @param angle  the angle of the vector
     * @param length the length of the vector
     * @param turn   turn value for the robot
     * @return motor values for the four motors
     */
    public static double[] angleToMotors(double angle, double length, double turn) {

        //converts polar vector to rectangular vector
        double[] vector = angleToVector(angle, length);

        //converts rectrangular vector to motor values
        double[] matrix = vectorToMotors(vector[0], vector[1], turn);

        //returns motor values
        return matrix;
    }

    /**
     * Checks to see if a number is in a range.
     *
     * @param number the number to check
     * @param lower  the lower bound of the range
     * @param upper  the upper bound of the range
     * @return true if the number is in the range
     */
    public static boolean inRange(double number, double lower, double upper) {
        return lower < number && number < upper;
    }
}
