// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.Locale;
import org.firstinspires.ftc.robotcore.external.navigation.TempUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Temperature;

/**
 * A class that provides JavaScript access to {@link Temperature}.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
class TemperatureAccess extends Access {

  TemperatureAccess(BlocksOpMode blocksOpMode, String identifier) {
    super(blocksOpMode, identifier);
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public String getTempUnit(Object temperature) {
    checkIfStopRequested();
    try {
      if (temperature instanceof Temperature) {
        TempUnit tempUnit = ((Temperature) temperature).unit;
        if (tempUnit != null) {
          return tempUnit.toString();
        }
      } else {
        RobotLog.e("Temperature.getTempUnit - temperature is not a Temperature");
      }
    } catch (Exception e) {
      RobotLog.e("Temperature.getTempUnit - caught " + e);
    }
    return "";
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public double getTemperature(Object temperature) {
    checkIfStopRequested();
    try {
      if (temperature instanceof Temperature) {
        return ((Temperature) temperature).temperature;
      } else {
        RobotLog.e("Temperature.getTemperature - temperature is not a Temperature");
      }
    } catch (Exception e) {
      RobotLog.e("Temperature.getTemperature - caught " + e);
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public long getAcquisitionTime(Object temperature) {
    checkIfStopRequested();
    try {
      if (temperature instanceof Temperature) {
        return ((Temperature) temperature).acquisitionTime;
      } else {
        RobotLog.e("Temperature.getAcquisitionTime - temperature is not a Temperature");
      }
    } catch (Exception e) {
      RobotLog.e("Temperature.getAcquisitionTime - caught " + e);
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public Temperature create() {
    checkIfStopRequested();
    try {
      return new Temperature();
    } catch (Exception e) {
      RobotLog.e("Temperature.create - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public Temperature create_withArgs(
      String tempUnitString, double temperature, long acquisitionTime) {
    checkIfStopRequested();
    try {
      TempUnit tempUnit = TempUnit.valueOf(tempUnitString.toUpperCase(Locale.ENGLISH));
      return new Temperature(tempUnit, temperature, acquisitionTime);
    } catch (Exception e) {
      RobotLog.e("Temperature.create - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public Temperature toTempUnit(Object temperature, String tempUnitString) {
    checkIfStopRequested();
    try {
      if (temperature instanceof Temperature) {
        TempUnit tempUnit =
            TempUnit.valueOf(tempUnitString.toUpperCase(Locale.ENGLISH));
        return ((Temperature) temperature).toUnit(tempUnit);
      } else {
        RobotLog.e("Temperature.toTempUnit - temperature is not a Temperature");
      }
    } catch (Exception e) {
      RobotLog.e("Temperature.toTempUnit - caught " + e);
    }
    return null;
  }
}
