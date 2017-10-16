// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.Locale;
import org.firstinspires.ftc.robotcore.external.matrices.MatrixF;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

/**
 * A class that provides JavaScript access to {@link OpenGLMatrix}.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
class OpenGLMatrixAccess extends Access {

  OpenGLMatrixAccess(BlocksOpMode blocksOpMode, String identifier) {
    super(blocksOpMode, identifier);
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public OpenGLMatrix create() {
    checkIfStopRequested();
    try {
      return new OpenGLMatrix();
    } catch (Exception e) {
      RobotLog.e("OpenGLMatrix.create - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public OpenGLMatrix create_withMatrixF(Object matrixF) {
    checkIfStopRequested();
    try {
      if (matrixF instanceof MatrixF) {
        return new OpenGLMatrix((MatrixF) matrixF);
      } else {
        RobotLog.e("OpenGLMatrix.create - parameter is not a MatrixF");
      }
    } catch (Exception e) {
      RobotLog.e("OpenGLMatrix.create - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public OpenGLMatrix rotation(String angleUnitString, float angle, float dx, float dy, float dz) {
    checkIfStopRequested();
    try {
      AngleUnit angleUnit = AngleUnit.valueOf(angleUnitString.toUpperCase(Locale.ENGLISH));
      return OpenGLMatrix.rotation(angleUnit, angle, dx, dy, dz);
    } catch (Exception e) {
      RobotLog.e("OpenGLMatrix.rotation - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public OpenGLMatrix rotation_withAxesArgs(
      String axesReferenceString, String axesOrderString, String angleUnitString,
      float firstAngle, float secondAngle, float thirdAngle) {
    checkIfStopRequested();
    try {
      AxesReference axesReference =
          AxesReference.valueOf(axesReferenceString.toUpperCase(Locale.ENGLISH));
      AxesOrder axesOrder = AxesOrder.valueOf(axesOrderString.toUpperCase(Locale.ENGLISH));
      AngleUnit angleUnit = AngleUnit.valueOf(angleUnitString.toUpperCase(Locale.ENGLISH));
      return OpenGLMatrix.rotation(
          axesReference, axesOrder, angleUnit, firstAngle, secondAngle, thirdAngle);
    } catch (Exception e) {
      RobotLog.e("OpenGLMatrix.rotation - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public OpenGLMatrix translation(float dx, float dy, float dz) {
    checkIfStopRequested();
    try {
      return OpenGLMatrix.translation(dx, dy, dz);
    } catch (Exception e) {
      RobotLog.e("OpenGLMatrix.translation - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public OpenGLMatrix identityMatrix() {
    checkIfStopRequested();
    try {
      return OpenGLMatrix.identityMatrix();
    } catch (Exception e) {
      RobotLog.e("OpenGLMatrix.identityMatrix - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void scale_with3(Object matrix, float scaleX, float scaleY, float scaleZ) {
    checkIfStopRequested();
    try {
      if (matrix instanceof OpenGLMatrix) {
        ((OpenGLMatrix) matrix).scale(scaleX, scaleY, scaleZ);
      } else {
        RobotLog.e("OpenGLMatrix.scale - matrix is not an OpenGLMatrix");
      }
    } catch (Exception e) {
      RobotLog.e("OpenGLMatrix.scale - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void scale_with1(Object matrix, float scale) {
    checkIfStopRequested();
    try {
      if (matrix instanceof OpenGLMatrix) {
        ((OpenGLMatrix) matrix).scale(scale);
      } else {
        RobotLog.e("OpenGLMatrix.scale - matrix is not an OpenGLMatrix");
      }
    } catch (Exception e) {
      RobotLog.e("OpenGLMatrix.scale - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void translate(Object matrix, float dx, float dy, float dz) {
    checkIfStopRequested();
    try {
      if (matrix instanceof OpenGLMatrix) {
        ((OpenGLMatrix) matrix).translate(dx, dy, dz);
      } else {
        RobotLog.e("OpenGLMatrix.translate - matrix is not an OpenGLMatrix");
      }
    } catch (Exception e) {
      RobotLog.e("OpenGLMatrix.translate - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void rotate(Object matrix, String angleUnitString, float angle, float dx, float dy, float dz) {
    checkIfStopRequested();
    try {
      if (matrix instanceof OpenGLMatrix) {
        AngleUnit angleUnit = AngleUnit.valueOf(angleUnitString.toUpperCase(Locale.ENGLISH));
        ((OpenGLMatrix) matrix).rotate(angleUnit, angle, dx, dy, dz);
      } else {
        RobotLog.e("OpenGLMatrix.rotate - matrix is not an OpenGLMatrix");
      }
    } catch (Exception e) {
      RobotLog.e("OpenGLMatrix.rotate - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void rotate_withAxesArgs(
      Object matrix,
      String axesReferenceString, String axesOrderString, String angleUnitString,
      float firstAngle, float secondAngle, float thirdAngle) {
    checkIfStopRequested();
    try {
      if (matrix instanceof OpenGLMatrix) {
        AxesReference axesReference =
            AxesReference.valueOf(axesReferenceString.toUpperCase(Locale.ENGLISH));
        AxesOrder axesOrder = AxesOrder.valueOf(axesOrderString.toUpperCase(Locale.ENGLISH));
        AngleUnit angleUnit = AngleUnit.valueOf(angleUnitString.toUpperCase(Locale.ENGLISH));
          ((OpenGLMatrix) matrix).rotate(
              axesReference, axesOrder, angleUnit, firstAngle, secondAngle, thirdAngle);
      } else {
        RobotLog.e("OpenGLMatrix.rotate - parameter is not an OpenGLMatrix");
      }
    } catch (Exception e) {
      RobotLog.e("OpenGLMatrix.rotate - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public OpenGLMatrix scaled_with3(Object matrix, float scaleX, float scaleY, float scaleZ) {
    checkIfStopRequested();
    try {
      if (matrix instanceof OpenGLMatrix) {
        return ((OpenGLMatrix) matrix).scaled(scaleX, scaleY, scaleZ);
      } else {
        RobotLog.e("OpenGLMatrix.scaled - matrix is not an OpenGLMatrix");
      }
    } catch (Exception e) {
      RobotLog.e("OpenGLMatrix.scaled - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public OpenGLMatrix scaled_with1(Object matrix, float scale) {
    checkIfStopRequested();
    try {
      if (matrix instanceof OpenGLMatrix) {
        return ((OpenGLMatrix) matrix).scaled(scale);
      } else {
        RobotLog.e("OpenGLMatrix.scaled - matrix is not an OpenGLMatrix");
      }
    } catch (Exception e) {
      RobotLog.e("OpenGLMatrix.scaled - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public OpenGLMatrix translated(Object matrix, float dx, float dy, float dz) {
    checkIfStopRequested();
    try {
      if (matrix instanceof OpenGLMatrix) {
        return ((OpenGLMatrix) matrix).translated(dx, dy, dz);
      } else {
        RobotLog.e("OpenGLMatrix.translated - matrix is not an OpenGLMatrix");
      }
    } catch (Exception e) {
      RobotLog.e("OpenGLMatrix.translated - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public OpenGLMatrix rotated(
      Object matrix, String angleUnitString, float angle, float dx, float dy, float dz) {
    checkIfStopRequested();
    try {
      if (matrix instanceof OpenGLMatrix) {
        AngleUnit angleUnit = AngleUnit.valueOf(angleUnitString.toUpperCase(Locale.ENGLISH));
        return ((OpenGLMatrix) matrix).rotated(angleUnit, angle, dx, dy, dz);
      } else {
        RobotLog.e("OpenGLMatrix.rotated - matrix is not an OpenGLMatrix");
      }
    } catch (Exception e) {
      RobotLog.e("OpenGLMatrix.rotated - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public OpenGLMatrix rotated_withAxesArgs(
      Object matrix, String axesReferenceString, String axesOrderString, String angleUnitString,
      float firstAngle, float secondAngle, float thirdAngle) {
    checkIfStopRequested();
    try {
      if (matrix instanceof OpenGLMatrix) {
        AxesReference axesReference =
            AxesReference.valueOf(axesReferenceString.toUpperCase(Locale.ENGLISH));
        AxesOrder axesOrder = AxesOrder.valueOf(axesOrderString.toUpperCase(Locale.ENGLISH));
        AngleUnit angleUnit = AngleUnit.valueOf(angleUnitString.toUpperCase(Locale.ENGLISH));
        return ((OpenGLMatrix) matrix).rotated(
            axesReference, axesOrder, angleUnit, firstAngle, secondAngle, thirdAngle);
      } else {
        RobotLog.e("OpenGLMatrix.rotated - matrix is not an OpenGLMatrix");
      }
    } catch (Exception e) {
      RobotLog.e("OpenGLMatrix.rotated - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public OpenGLMatrix multiplied(Object matrix1, Object matrix2) {
    checkIfStopRequested();
    try {
      if (matrix1 instanceof OpenGLMatrix) {
        if (matrix2 instanceof OpenGLMatrix) {
          return ((OpenGLMatrix) matrix1).multiplied((OpenGLMatrix) matrix2);
        } else {
          RobotLog.e("OpenGLMatrix.multiplied - matrix2 is not an OpenGLMatrix");
        }
      } else {
        RobotLog.e("OpenGLMatrix.multiplied - matrix1 is not an OpenGLMatrix");
      }
    } catch (Exception e) {
      RobotLog.e("OpenGLMatrix.multiplied - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void multiply(Object matrix1, Object matrix2) {
    checkIfStopRequested();
    try {
      if (matrix1 instanceof OpenGLMatrix) {
        if (matrix2 instanceof OpenGLMatrix) {
          ((OpenGLMatrix) matrix1).multiply((OpenGLMatrix) matrix2);
        } else {
          RobotLog.e("OpenGLMatrix.multiply - matrix2 is not an OpenGLMatrix");
        }
      } else {
        RobotLog.e("OpenGLMatrix.multiply - matrix1 is not an OpenGLMatrix");
      }
    } catch (Exception e) {
      RobotLog.e("OpenGLMatrix.multiply - caught " + e);
    }
  }
}
