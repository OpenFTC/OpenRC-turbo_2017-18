// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.Locale;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;

/**
 * A class that provides JavaScript access to {@link Position}.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
class PositionAccess extends Access {

  PositionAccess(BlocksOpMode blocksOpMode, String identifier) {
    super(blocksOpMode, identifier);
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public String getDistanceUnit(Object position) {
    checkIfStopRequested();
    try {
      if (position instanceof Position) {
        DistanceUnit distanceUnit = ((Position) position).unit;
        if (distanceUnit != null) {
          return distanceUnit.toString();
        }
      } else {
        RobotLog.e("Position.getDistanceUnit - position is not a Position");
      }
    } catch (Exception e) {
      RobotLog.e("Position.getDistanceUnit - caught " + e);
    }
    return "";
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public double getX(Object position) {
    checkIfStopRequested();
    try {
      if (position instanceof Position) {
        return ((Position) position).x;
      } else {
        RobotLog.e("Position.getX - position is not a Position");
      }
    } catch (Exception e) {
      RobotLog.e("Position.getX - caught " + e);
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public double getY(Object position) {
    checkIfStopRequested();
    try {
      if (position instanceof Position) {
        return ((Position) position).y;
      } else {
        RobotLog.e("Position.getY - position is not a Position");
      }
    } catch (Exception e) {
      RobotLog.e("Position.getY - caught " + e);
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public double getZ(Object position) {
    checkIfStopRequested();
    try {
      if (position instanceof Position) {
        return ((Position) position).z;
      } else {
        RobotLog.e("Position.getZ - position is not a Position");
      }
    } catch (Exception e) {
      RobotLog.e("Position.getZ - caught " + e);
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public long getAcquisitionTime(Object position) {
    checkIfStopRequested();
    try {
      if (position instanceof Position) {
        return ((Position) position).acquisitionTime;
      } else {
        RobotLog.e("Position.getAcquisitionTime - position is not a Position");
      }
    } catch (Exception e) {
      RobotLog.e("Position.getAcquisitionTime - caught " + e);
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public Position create() {
    checkIfStopRequested();
    try {
      return new Position();
    } catch (Exception e) {
      RobotLog.e("Position.create - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public Position create_withArgs(
      String distanceUnitString, double x, double y, double z, long acquisitionTime) {
    checkIfStopRequested();
    try {
      DistanceUnit distanceUnit =
          DistanceUnit.valueOf(distanceUnitString.toUpperCase(Locale.ENGLISH));
      return new Position(distanceUnit, x, y, z, acquisitionTime);
    } catch (Exception e) {
      RobotLog.e("Position.create - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public Position toDistanceUnit(Object position, String distanceUnitString) {
    checkIfStopRequested();
    try {
      if (position instanceof Position) {
        DistanceUnit distanceUnit =
            DistanceUnit.valueOf(distanceUnitString.toUpperCase(Locale.ENGLISH));
        return ((Position) position).toUnit(distanceUnit);
      } else {
        RobotLog.e("Position.toDistanceUnit - position is not a Position");
      }
    } catch (Exception e) {
      RobotLog.e("Position.toDistanceUnit - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public String toText(Object position) {
    checkIfStopRequested();
    try {
      if (position instanceof Position) {
        return ((Position) position).toString();
      } else {
        RobotLog.e("Position.toText - position is not a Position");
      }
    } catch (Exception e) {
      RobotLog.e("Position.toText - caught " + e);
    }
    return "";
  }
}
