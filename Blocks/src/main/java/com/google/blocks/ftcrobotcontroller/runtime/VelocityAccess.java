// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.Locale;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

/**
 * A class that provides JavaScript access to {@link Velocity}.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
class VelocityAccess extends Access {

  VelocityAccess(BlocksOpMode blocksOpMode, String identifier) {
    super(blocksOpMode, identifier);
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public String getDistanceUnit(Object velocity) {
    checkIfStopRequested();
    try {
      if (velocity instanceof Velocity) {
        DistanceUnit distanceUnit = ((Velocity) velocity).unit;
        if (distanceUnit != null) {
          return distanceUnit.toString();
        }
      } else {
        RobotLog.e("Velocity.getDistanceUnit - velocity is not a Velocity");
      }
    } catch (Exception e) {
      RobotLog.e("Velocity.getDistanceUnit - caught " + e);
    }
    return "";
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public double getXVeloc(Object velocity) {
    checkIfStopRequested();
    try {
      if (velocity instanceof Velocity) {
        return ((Velocity) velocity).xVeloc;
      } else {
        RobotLog.e("Velocity.getXVeloc - velocity is not a Velocity");
      }
    } catch (Exception e) {
      RobotLog.e("Velocity.getXVeloc - caught " + e);
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public double getYVeloc(Object velocity) {
    checkIfStopRequested();
    try {
      if (velocity instanceof Velocity) {
        return ((Velocity) velocity).yVeloc;
      } else {
        RobotLog.e("Velocity.getYVeloc - velocity is not a Velocity");
      }
    } catch (Exception e) {
      RobotLog.e("Velocity.getYVeloc - caught " + e);
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public double getZVeloc(Object velocity) {
    checkIfStopRequested();
    try {
      if (velocity instanceof Velocity) {
        return ((Velocity) velocity).zVeloc;
      } else {
        RobotLog.e("Velocity.getZVeloc - velocity is not a Velocity");
      }
    } catch (Exception e) {
      RobotLog.e("Velocity.getZVeloc - caught " + e);
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public long getAcquisitionTime(Object velocity) {
    checkIfStopRequested();
    try {
      if (velocity instanceof Velocity) {
        return ((Velocity) velocity).acquisitionTime;
      } else {
        RobotLog.e("Velocity.getAcquisitionTime - velocity is not a Velocity");
      }
    } catch (Exception e) {
      RobotLog.e("Velocity.getAcquisitionTime - caught " + e);
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public Velocity create() {
    checkIfStopRequested();
    try {
      return new Velocity();
    } catch (Exception e) {
      RobotLog.e("Velocity.create - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public Velocity create_withArgs(
      String distanceUnitString, double xVeloc, double yVeloc, double zVeloc,
      long acquisitionTime) {
    checkIfStopRequested();
    try {
      DistanceUnit distanceUnit =
          DistanceUnit.valueOf(distanceUnitString.toUpperCase(Locale.ENGLISH));
      return new Velocity(distanceUnit, xVeloc, yVeloc, zVeloc, acquisitionTime);
    } catch (Exception e) {
      RobotLog.e("Velocity.create - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public Velocity toDistanceUnit(Object velocity, String distanceUnitString) {
    checkIfStopRequested();
    try {
      if (velocity instanceof Velocity) {
        DistanceUnit distanceUnit =
            DistanceUnit.valueOf(distanceUnitString.toUpperCase(Locale.ENGLISH));
        return ((Velocity) velocity).toUnit(distanceUnit);
      } else {
        RobotLog.e("Velocity.toDistanceUnit - velocity is not a Velocity");
      }
    } catch (Exception e) {
      RobotLog.e("Velocity.toDistanceUnit - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public String toText(Object velocity) {
    checkIfStopRequested();
    try {
      if (velocity instanceof Velocity) {
        return ((Velocity) velocity).toString();
      } else {
        RobotLog.e("Velocity.toText - velocity is not a Velocity");
      }
    } catch (Exception e) {
      RobotLog.e("Velocity.toText - caught " + e);
    }
    return "";
  }
}
