// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.Locale;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.robotcore.external.navigation.Axis;

/**
 * A class that provides JavaScript access to {@link AngularVelocity}.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
class AngularVelocityAccess extends Access {

  AngularVelocityAccess(BlocksOpMode blocksOpMode, String identifier) {
    super(blocksOpMode, identifier);
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = AngularVelocity.class, fieldName = "unit")
  public String getAngleUnit(Object angularVelocity) {
    checkIfStopRequested();
    try {
      if (angularVelocity instanceof AngularVelocity) {
        AngleUnit angleUnit = ((AngularVelocity) angularVelocity).unit;
        if (angleUnit != null) {
          return angleUnit.toString();
        }
      } else {
        RobotLog.e("AngularVelocity.getAngleUnit - angularVelocity is not an AngularVelocity");
      }
    } catch (Exception e) {
      RobotLog.e("AngularVelocity.getAngleUnit - caught " + e);
    }
    return "";
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = AngularVelocity.class, fieldName = "xRotationRate")
  public float getXRotationRate(Object angularVelocity) {
    checkIfStopRequested();
    try {
      if (angularVelocity instanceof AngularVelocity) {
        return ((AngularVelocity) angularVelocity).xRotationRate;
      } else {
        RobotLog.e("AngularVelocity.getXRotationRate - angularVelocity is not an AngularVelocity");
      }
    } catch (Exception e) {
      RobotLog.e("AngularVelocity.getXRotationRate - caught " + e);
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = AngularVelocity.class, fieldName = "yRotationRate")
  public float getYRotationRate(Object angularVelocity) {
    checkIfStopRequested();
    try {
      if (angularVelocity instanceof AngularVelocity) {
        return ((AngularVelocity) angularVelocity).yRotationRate;
      } else {
        RobotLog.e("AngularVelocity.getYRotationRate - angularVelocity is not an AngularVelocity");
      }
    } catch (Exception e) {
      RobotLog.e("AngularVelocity.getYRotationRate - caught " + e);
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = AngularVelocity.class, fieldName = "zRotationRate")
  public float getZRotationRate(Object angularVelocity) {
    checkIfStopRequested();
    try {
      if (angularVelocity instanceof AngularVelocity) {
        return ((AngularVelocity) angularVelocity).zRotationRate;
      } else {
        RobotLog.e("AngularVelocity.getZRotationRate - angularVelocity is not an AngularVelocity");
      }
    } catch (Exception e) {
      RobotLog.e("AngularVelocity.getZRotationRate - caught " + e);
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = AngularVelocity.class, fieldName = "acquisitionTime")
  public long getAcquisitionTime(Object angularVelocity) {
    checkIfStopRequested();
    try {
      if (angularVelocity instanceof AngularVelocity) {
        return ((AngularVelocity) angularVelocity).acquisitionTime;
      } else {
        RobotLog.e("AngularVelocity.getAcquisitionTime - angularVelocity is not an AngularVelocity");
      }
    } catch (Exception e) {
      RobotLog.e("AngularVelocity.getAcquisitionTime - caught " + e);
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = AngularVelocity.class, constructor = true)
  public AngularVelocity create() {
    checkIfStopRequested();
    try {
      return new AngularVelocity();
    } catch (Exception e) {
      RobotLog.e("AngularVelocity.create - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = AngularVelocity.class, constructor = true)
  public AngularVelocity create_withArgs(
      String angleUnitString, float xRotationRate, float yRotationRate,
      float zRotationRate, long acquisitionTime) {
    checkIfStopRequested();
    try {
      AngleUnit angleUnit =
          AngleUnit.valueOf(angleUnitString.toUpperCase(Locale.ENGLISH));
      return new AngularVelocity(
          angleUnit, xRotationRate, yRotationRate, zRotationRate, acquisitionTime);
    } catch (Exception e) {
      RobotLog.e("AngularVelocity.create - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = AngularVelocity.class, methodName = "toAngleUnit")
  public AngularVelocity toAngleUnit(Object angularVelocity, String angleUnitString) {
    checkIfStopRequested();
    try {
      if (angularVelocity instanceof AngularVelocity) {
        AngleUnit angleUnit =
            AngleUnit.valueOf(angleUnitString.toUpperCase(Locale.ENGLISH));
        return ((AngularVelocity) angularVelocity).toAngleUnit(angleUnit);
      } else {
        RobotLog.e("AngularVelocity.toAngleUnit - angularVelocity is not an AngularVelocity");
      }
    } catch (Exception e) {
      RobotLog.e("AngularVelocity.toAngleUnit - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = AngularVelocity.class, fieldName = {"xRotationRate", "yRotationRate", "zRotationRate"})
  public float getRotationRate(Object angularVelocity, String axisString) {
    checkIfStopRequested();
    try {
      if (angularVelocity instanceof AngularVelocity) {
        Axis axis = Axis.valueOf(axisString.toUpperCase(Locale.ENGLISH));
        switch (axis) {
          case X:
            return ((AngularVelocity) angularVelocity).xRotationRate;
          case Y:
            return ((AngularVelocity) angularVelocity).yRotationRate;
          case Z:
            return ((AngularVelocity) angularVelocity).zRotationRate;
          case UNKNOWN:
            RobotLog.e("AngularVelocity.getRotationRate - axis is not an Axis");
        }
      } else {
        RobotLog.e("AngularVelocity.getRotationRate - angularVelocity is not an AngularVelocity");
      }
    } catch (Exception e) {
      RobotLog.e("AngularVelocity.getRotationRate - caught " + e);
    }
    return 0;
  }
}
