// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.Locale;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

/**
 * A class that provides JavaScript access to {@link Acceleration}.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
class AccelerationAccess extends Access {

  AccelerationAccess(BlocksOpMode blocksOpMode, String identifier) {
    super(blocksOpMode, identifier);
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = Acceleration.class, fieldName = "unit")
  public String getDistanceUnit(Object acceleration) {
    checkIfStopRequested();
    try {
      if (acceleration instanceof Acceleration) {
        DistanceUnit distanceUnit = ((Acceleration) acceleration).unit;
        if (distanceUnit != null) {
          return distanceUnit.toString();
        }
      } else {
        RobotLog.e("Acceleration.getDistanceUnit - acceleration is not an Acceleration");
      }
    } catch (Exception e) {
      RobotLog.e("Acceleration.getDistanceUnit - caught " + e);
    }
    return "";
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = Acceleration.class, fieldName = "xAccel")
  public double getXAccel(Object acceleration) {
    checkIfStopRequested();
    try {
      if (acceleration instanceof Acceleration) {
        return ((Acceleration) acceleration).xAccel;
      } else {
        RobotLog.e("Acceleration.getXAccel - acceleration is not an Acceleration");
      }
    } catch (Exception e) {
      RobotLog.e("Acceleration.getXAccel - caught " + e);
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = Acceleration.class, fieldName = "yAccel")
  public double getYAccel(Object acceleration) {
    checkIfStopRequested();
    try {
      if (acceleration instanceof Acceleration) {
        return ((Acceleration) acceleration).yAccel;
      } else {
        RobotLog.e("Acceleration.getYAccel - acceleration is not an Acceleration");
      }
    } catch (Exception e) {
      RobotLog.e("Acceleration.getYAccel - caught " + e);
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = Acceleration.class, fieldName = "zAccel")
  public double getZAccel(Object acceleration) {
    checkIfStopRequested();
    try {
      if (acceleration instanceof Acceleration) {
        return ((Acceleration) acceleration).zAccel;
      } else {
        RobotLog.e("Acceleration.getZAccel - acceleration is not an Acceleration");
      }
    } catch (Exception e) {
      RobotLog.e("Acceleration.getZAccel - caught " + e);
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = Acceleration.class, fieldName = "acquisitionTime")
  public long getAcquisitionTime(Object acceleration) {
    checkIfStopRequested();
    try {
      if (acceleration instanceof Acceleration) {
        return ((Acceleration) acceleration).acquisitionTime;
      } else {
        RobotLog.e("Acceleration.getAcquisitionTime - acceleration is not an Acceleration");
      }
    } catch (Exception e) {
      RobotLog.e("Acceleration.getAcquisitionTime - caught " + e);
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = Acceleration.class, constructor = true)
  public Acceleration create() {
    checkIfStopRequested();
    try {
      return new Acceleration();
    } catch (Exception e) {
      RobotLog.e("Acceleration.create - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = Acceleration.class, constructor = true)
  public Acceleration create_withArgs(
      String distanceUnitString, double xAccel, double yAccel, double zAccel, long acquisitionTime) {
    checkIfStopRequested();
    try {
      DistanceUnit distanceUnit =
          DistanceUnit.valueOf(distanceUnitString.toUpperCase(Locale.ENGLISH));
      return new Acceleration(distanceUnit, xAccel, yAccel, zAccel, acquisitionTime);
    } catch (Exception e) {
      RobotLog.e("Acceleration.create - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = Acceleration.class, methodName = "fromGravity")
  public Acceleration fromGravity(double gx, double gy, double gz, long acquisitionTime) {
    checkIfStopRequested();
    try {
      return Acceleration.fromGravity(gx, gy, gz, acquisitionTime);
    } catch (Exception e) {
      RobotLog.e("Acceleration.fromGravity - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = Acceleration.class, methodName = "toUnit")
  public Acceleration toDistanceUnit(Object acceleration, String distanceUnitString) {
    checkIfStopRequested();
    try {
      if (acceleration instanceof Acceleration) {
        DistanceUnit distanceUnit =
            DistanceUnit.valueOf(distanceUnitString.toUpperCase(Locale.ENGLISH));
        return ((Acceleration) acceleration).toUnit(distanceUnit);
      } else {
        RobotLog.e("Acceleration.toDistanceUnit - acceleration is not an Acceleration");
      }
    } catch (Exception e) {
      RobotLog.e("Acceleration.toDistanceUnit - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = Acceleration.class, methodName = "toString")
  public String toText(Object acceleration) {
    checkIfStopRequested();
    try {
      if (acceleration instanceof Acceleration) {
        return ((Acceleration) acceleration).toString();
      } else {
        RobotLog.e("Acceleration.toText - acceleration is not an Acceleration");
      }
    } catch (Exception e) {
      RobotLog.e("Acceleration.toText - caught " + e);
    }
    return "";
  }
}
