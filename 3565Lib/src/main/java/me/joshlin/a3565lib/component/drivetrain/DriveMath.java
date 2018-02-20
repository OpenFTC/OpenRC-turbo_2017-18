package me.joshlin.a3565lib.component.drivetrain;

/**
 * Created by 3565 on 2/15/2018.
 *
 * Made by LEO (See? I'm helping)
 *
 * ======================================================================================
 * Math involved in converting input values (vectors / directions) to the motor values
 * required to move the robot in that direction using mechanum wheels.
 * ======================================================================================
 *
 * The motor values are passed around as arrays containing 4 double values:
 *
 *      {motor 1, motor 2, motor 3, motor 4}
 *
 * The motors correspond to the array elements like so:
 *
 *      [Motor 1]                    [Motor 2]
 *                [^Front of robot^]
 *                [ROBOT base      ]
 *      [Motor 3]                    [Motor 4]
 *
 * (The directions of the mechanum wheels should form a diamond when viewed from above, NOT an 'X')
 * (It is not certain if the code would still work with an 'X' arrangement, but it might)
 * =======================================================================================
 *
 * The length of the vectors passed to the methods do NOT correspond to drive distance.
 * Instead, longer vectors will produce faster motion (but the motors can only handle values between -1 and 1)
 *
 * If you are unfamiliar with vectors and how they are represented, consult the internet first.
 * This code does not use any complicated vector math, onyl simple conversions and some
 * addition
 * =======================================================================================
 */

public class DriveMath {

//===================================[Constants]===========================================
    //change these constants if the robot is not behaving correctly!
    private static final double[] verticalDrive =   {1, -1, 1, -1};   //the motor values that drive straight forward  (maybe backwards?)
    private static final double[] horizontalDrive = {1, 1, -1, -1};   //the motor values that strafe left             (maybe right?)
    private static final double[] turnDrive =       {1,1,1,1};        //the motor values that turn clockwise          (maybe counterclockwise?)


//==================================[Methods]==============================================
    //converts a vector in polar notation (angle and length) to a vector in rectangular notation (x and y)
    //This is just vector conversion, and has no dependency on the robot itself
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


    //converts controller input (y and x) to motor values
    public static double[] inputsToMotors(double x, double y, double turn) {

        //this part just makes editing the rest of the code easier
        double x_input = x;
        double y_input = y;
        double turn_input = ((y_input>0) ? -turn:turn); //the turning is reversed if the robot is driving backwards

        double[] driveMatrix = new double[4];

        double[] xMatrix = new double[4];
        double[] yMatrix = new double[4];
        double[] turnMatrix = new double[4];

        //sets values of matrices in x, y, and turning directions
        for (int i = 0; i < 4; i++) {
            xMatrix[i] =    horizontalDrive[i]  *   x_input;
            yMatrix[i] =    verticalDrive[i]    *   y_input;
            turnMatrix[i] = turnDrive[i]        *   turn_input;
        }

        //adds matrices to get motor values
        for (int i = 0; i < 4; i++) {
            driveMatrix[i] = xMatrix[i] + yMatrix[i] + turnMatrix[i];
        }

        //driveMatrix = normalize(driveMatrix);     //**optional**

        return driveMatrix;
    }


    //normalizes an input vector of any dimension
    //(scales a vector so its length is 1 (one) unit)
    //this math is independent of the robot itself and will work even with vectors of higher dimensions
    private static double[] normalize(double[] vector) {

        //finds length of vector
        double sqrSum = 0;
        for (double d : vector)
            sqrSum += d * d;

        double length = Math.sqrt(sqrSum); //length

        //divide the vector by its length
        for (int i = 0; i < vector.length; i++)
            vector[i] /= length;


        return vector;  //returns normalized vector
    }


    //same as inputsToMotors, but allows the input vector to be from a traditional coordinate plane (not the weird inverted ones from the gamepad's joystick)
    public static double[] vectorToMotors(double x, double y, double turn){
        //converts y and x coordinates to the (inverted) values they would be on the gamepad
        y = -y; //only y is inverted (I guess?)

        return inputsToMotors(x, y, turn); //just runs the other method
    }


    //converts a polar vector directly to the motor values
    //this method includes a "turn" value
    public static double[] angleToMotors(double angle, double length, double turn){

        //converts polar vector to rectangular vector
        double[] vector = angleToVector(angle, length);

        //converts rectrangular vector to motor values
        double[] matrix = vectorToMotors(vector[0], vector[1], turn);

        //returns motor values
        return matrix;
    }


    //converts a polar vector directly to the motor values
    //this method does not inclue a "turn" value (defaults to 0)
    public static double[] angleToMotors(double angle, double length){
        return angleToMotors(angle, length, 0);
    }
}
