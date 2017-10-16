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
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

/**
 * A class that provides JavaScript access to {@link Orientation}.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
class OrientationAccess extends Access {

  OrientationAccess(BlocksOpMode blocksOpMode, String identifier) {
    super(blocksOpMode, identifier);
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public String getAxesReference(Object orientation) {
    checkIfStopRequested();
    try {
      if (orientation instanceof Orientation) {
        AxesReference axesReference = ((Orientation) orientation).axesReference;
        if (axesReference != null) {
          return axesReference.toString();
        }
      } else {
        RobotLog.e("Orientation.getAxesReference - orientation is not an Orientation");
      }
    } catch (Exception e) {
      RobotLog.e("Orientation.getAxesReference - caught " + e);
    }
    return "";
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public String getAxesOrder(Object orientation) {
    checkIfStopRequested();
    try {
      if (orientation instanceof Orientation) {
        AxesOrder axesOrder = ((Orientation) orientation).axesOrder;
        if (axesOrder != null) {
          return axesOrder.toString();
        }
      } else {
        RobotLog.e("Orientation.getAxesOrder - orientation is not an Orientation");
      }
    } catch (Exception e) {
      RobotLog.e("Orientation.getAxesOrder - caught " + e);
    }
    return "";
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public String getAngleUnit(Object orientation) {
    checkIfStopRequested();
    try {
      if (orientation instanceof Orientation) {
        AngleUnit angleUnit = ((Orientation) orientation).angleUnit;
        if (angleUnit != null) {
          return angleUnit.toString();
        }
      } else {
        RobotLog.e("Orientation.getAngleUnit - orientation is not an Orientation");
      }
    } catch (Exception e) {
      RobotLog.e("Orientation.getAngleUnit - caught " + e);
    }
    return "";
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public float getFirstAngle(Object orientation) {
    checkIfStopRequested();
    try {
      if (orientation instanceof Orientation) {
        return ((Orientation) orientation).firstAngle;
      } else {
        RobotLog.e("Orientation.getFirstAngle - orientation is not an Orientation");
      }
    } catch (Exception e) {
      RobotLog.e("Orientation.getFirstAngle - caught " + e);
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public float getSecondAngle(Object orientation) {
    checkIfStopRequested();
    try {
      if (orientation instanceof Orientation) {
        return ((Orientation) orientation).secondAngle;
      } else {
        RobotLog.e("Orientation.getSecondAngle - orientation is not an Orientation");
      }
    } catch (Exception e) {
      RobotLog.e("Orientation.getSecondAngle - caught " + e);
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public float getThirdAngle(Object orientation) {
    checkIfStopRequested();
    try {
      if (orientation instanceof Orientation) {
        return ((Orientation) orientation).thirdAngle;
      } else {
        RobotLog.e("Orientation.getThirdAngle - orientation is not an Orientation");
      }
    } catch (Exception e) {
      RobotLog.e("Orientation.getThirdAngle - caught " + e);
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public long getAcquisitionTime(Object orientation) {
    checkIfStopRequested();
    try {
      if (orientation instanceof Orientation) {
        return ((Orientation) orientation).acquisitionTime;
      } else {
        RobotLog.e("Orientation.getAcquisitionTime - orientation is not an Orientation");
      }
    } catch (Exception e) {
      RobotLog.e("Orientation.getAcquisitionTime - caught " + e);
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public Orientation create() {
    checkIfStopRequested();
    try {
      return new Orientation();
    } catch (Exception e) {
      RobotLog.e("Orientation.create - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public Orientation create_withArgs(
      String axesReferenceString, String axesOrderString, String angleUnitString, float firstAngle,
      float secondAngle, float thirdAngle, long acquisitionTime) {
    checkIfStopRequested();
    try {
      AxesReference axesReference =
          AxesReference.valueOf(axesReferenceString.toUpperCase(Locale.ENGLISH));
      AxesOrder axesOrder =
          AxesOrder.valueOf(axesOrderString.toUpperCase(Locale.ENGLISH));
      AngleUnit angleUnit =
          AngleUnit.valueOf(angleUnitString.toUpperCase(Locale.ENGLISH));
      return new Orientation(
          axesReference, axesOrder, angleUnit, firstAngle, secondAngle, thirdAngle,
          acquisitionTime);
    } catch (Exception e) {
      RobotLog.e("Orientation.create - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public Orientation toAngleUnit(Object orientation, String angleUnitString) {
    checkIfStopRequested();
    try {
      if (orientation instanceof Orientation) {
        AngleUnit angleUnit =
            AngleUnit.valueOf(angleUnitString.toUpperCase(Locale.ENGLISH));
        return ((Orientation) orientation).toAngleUnit(angleUnit);
      } else {
        RobotLog.e("Orientation.toAngleUnit - orientation is not an Orientation");
      }
    } catch (Exception e) {
      RobotLog.e("Orientation.toAngleUnit - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public Orientation toAxesReference(Object orientation, String axesReferenceString) {
    checkIfStopRequested();
    try {
      if (orientation instanceof Orientation) {
        AxesReference axesReference =
            AxesReference.valueOf(axesReferenceString.toUpperCase(Locale.ENGLISH));
        return ((Orientation) orientation).toAxesReference(axesReference);
      } else {
        RobotLog.e("Orientation.toAxesReference - orientation is not an Orientation");
      }
    } catch (Exception e) {
      RobotLog.e("Orientation.toAxesReference - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public Orientation toAxesOrder(Object orientation, String axesOrderString) {
    checkIfStopRequested();
    try {
      if (orientation instanceof Orientation) {
        AxesOrder axesOrder =
            AxesOrder.valueOf(axesOrderString.toUpperCase(Locale.ENGLISH));
        return ((Orientation) orientation).toAxesOrder(axesOrder);
      } else {
        RobotLog.e("Orientation.toAxesOrder - orientation is not an Orientation");
      }
    } catch (Exception e) {
      RobotLog.e("Orientation.toAxesOrder - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public String toText(Object orientation) {
    checkIfStopRequested();
    try {
      if (orientation instanceof Orientation) {
        return ((Orientation) orientation).toString();
      } else {
        RobotLog.e("Orientation.toText - orientation is not an Orientation");
      }
    } catch (Exception e) {
      RobotLog.e("Orientation.toText - caught " + e);
    }
    return "";
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public OpenGLMatrix getRotationMatrix(Object orientation) {
    checkIfStopRequested();
    try {
      if (orientation instanceof Orientation) {
        return ((Orientation) orientation).getRotationMatrix();
      } else {
        RobotLog.e("Orientation.getRotationMatrix - orientation is not an Orientation");
      }
    } catch (Exception e) {
      RobotLog.e("Orientation.getRotationMatrix - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public OpenGLMatrix getRotationMatrix_withArgs(
      String axesReferenceString, String axesOrderString, String angleUnitString, float firstAngle,
      float secondAngle, float thirdAngle) {
    checkIfStopRequested();
    try {
      AxesReference axesReference =
          AxesReference.valueOf(axesReferenceString.toUpperCase(Locale.ENGLISH));
      AxesOrder axesOrder =
          AxesOrder.valueOf(axesOrderString.toUpperCase(Locale.ENGLISH));
      AngleUnit angleUnit =
          AngleUnit.valueOf(angleUnitString.toUpperCase(Locale.ENGLISH));
      return Orientation.getRotationMatrix(
          axesReference, axesOrder, angleUnit, firstAngle, secondAngle, thirdAngle);
    } catch (Exception e) {
      RobotLog.e("Orientation.getRotationMatrix - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public Orientation getOrientation(
      Object matrix, String axesReferenceString, String axesOrderString, String angleUnitString) {
    checkIfStopRequested();
    try {
      if (matrix instanceof MatrixF) {
        AxesReference axesReference =
            AxesReference.valueOf(axesReferenceString.toUpperCase(Locale.ENGLISH));
        AxesOrder axesOrder =
            AxesOrder.valueOf(axesOrderString.toUpperCase(Locale.ENGLISH));
        AngleUnit angleUnit =
            AngleUnit.valueOf(angleUnitString.toUpperCase(Locale.ENGLISH));
        return Orientation.getOrientation((MatrixF) matrix, axesReference, axesOrder, angleUnit);
      } else {
        RobotLog.e("Orientation.getOrientation - orientation is not an Orientation");
      }
    } catch (Exception e) {
      RobotLog.e("Orientation.getOrientation - caught " + e);
    }
    return null;
  }
}
