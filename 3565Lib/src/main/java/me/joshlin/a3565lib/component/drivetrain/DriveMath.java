package me.joshlin.a3565lib.component.drivetrain;

/**
 * Created by 3565 on 2/15/2018.
 */

public class DriveMath {

    private static double[] verticalDrive = {1, -1, 1, -1}; //drives straight forward
    private static double[] horizontalDrive = {1, 1, -1, -1}; //drives side-to-side
    private static double[] turnDrive = {1,1,1,1}; //turns in an unknown direction

    //converts angle to a vector
    public static double[] angleToVector(double angle, double length) {
        double[] vector = new double[2];

        double x = Math.cos(angle) * length;
        double y = Math.sin(angle) * length;

        vector[0] = x;
        vector[1] = y;

        return vector;
    }

    //converts controller input to motor values
    public static double[] vectorToMotors(double x, double y, double turn) {

        double x_input = x;
        double y_input = y;
        double turn_input = ((y_input>0) ? -turn:turn);

        double[] driveMatrix = new double[4];

        double[] xMatrix = new double[4];
        double[] yMatrix = new double[4];
        double[] turnMatrix = new double[4];

        //sets values of matrices in x, y, and z directions
        for (int i = 0; i < 4; i++) {
            xMatrix[i] = horizontalDrive[i] * x_input;
            yMatrix[i] = verticalDrive[i] * y_input;
            turnMatrix[i] = turnDrive[i] * turn_input;
        }

        //adds matrices to get motor values
        for (int i = 0; i < 4; i++) {
            driveMatrix[i] = xMatrix[i] + yMatrix[i] + turnMatrix[i];
        }

        //driveMatrix = normalize(driveMatrix);

        return driveMatrix;
    }

    private static double[] normalize(double[] vector) {    //normalizes an input vector of any dimension

        //finds length of vector
        double sqrSum = 0;
        for (double d : vector) {
            sqrSum += d * d;
        }
        double length = Math.sqrt(sqrSum);

        //divide the vector by its length
        for (int i = 0; i < vector.length; i++) {
            vector[i] /= length;
        }

        return vector;
    }
}
