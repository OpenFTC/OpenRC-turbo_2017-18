// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.google.blocks.ftcrobotcontroller.util.HardwareItem;
import com.qualcomm.hardware.hitechnic.HiTechnicNxtGyroSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro.HeadingMode;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.OrientationSensor;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.Locale;
import java.util.Set;
import junit.framework.Assert;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Axis;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

/**
 * A class that provides JavaScript access to a {@link GyroSensor}.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
class GyroSensorAccess extends HardwareAccess<GyroSensor> {
  private final GyroSensor gyroSensor;

  GyroSensorAccess(BlocksOpMode blocksOpMode, HardwareItem hardwareItem, HardwareMap hardwareMap,
      Class<? extends HardwareDevice> deviceType) {
    super(blocksOpMode, hardwareItem, hardwareMap, GyroSensor.class);
    Assert.assertTrue(deviceType == GyroSensor.class);
    this.gyroSensor = hardwareDevice;
  }

  // Properties

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtGyroSensor.class, ModernRoboticsI2cGyro.class}, methodName = "getHeading")
  public int getHeading() {
    checkIfStopRequested();
    try {
      if (gyroSensor != null) {
        return gyroSensor.getHeading();
      }
    } catch (Exception e) {
      RobotLog.e("GyroSensor.getHeading - caught " + e);
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = ModernRoboticsI2cGyro.class, methodName = "setHeadingMode")
  public void setHeadingMode(String headingModeString) {
    checkIfStopRequested();
    try {
      if (gyroSensor != null) {
        if (gyroSensor instanceof ModernRoboticsI2cGyro) {
          HeadingMode headingMode = HeadingMode.valueOf(headingModeString.toUpperCase(Locale.ENGLISH));
          ((ModernRoboticsI2cGyro) gyroSensor).setHeadingMode(headingMode);
        } else {
          RobotLog.e("GyroSensor.setHeadingMode - gyro sensor is not a ModernRoboticsI2cGyro");
        }
      }
    } catch (Exception e) {
      RobotLog.e("GyroSensor.setHeadingMode - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = ModernRoboticsI2cGyro.class, methodName = "getHeadingMode")
  public String getHeadingMode() {
    checkIfStopRequested();
    try {
      if (gyroSensor != null) {
        if (gyroSensor instanceof ModernRoboticsI2cGyro) {
          HeadingMode headingMode = ((ModernRoboticsI2cGyro) gyroSensor).getHeadingMode();
          if (headingMode != null) {
            return headingMode.toString();
          }
        } else {
          RobotLog.e("GyroSensor.getHeadingMode - gyro sensor is not a ModernRoboticsI2cGyro");
        }
      }
    } catch (Exception e) {
      RobotLog.e("GyroSensor.getHeadingMode - caught " + e);
    }
    return "";
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = ModernRoboticsI2cGyro.class, methodName = "setI2cAddress")
  public void setI2cAddress7Bit(int i2cAddr7Bit) {
    checkIfStopRequested();
    try {
      if (gyroSensor != null) {
        if (gyroSensor instanceof ModernRoboticsI2cGyro) {
          ((ModernRoboticsI2cGyro) gyroSensor).setI2cAddress(I2cAddr.create7bit(i2cAddr7Bit));
        } else {
          RobotLog.e("GyroSensor.setI2cAddress7Bit - gyro sensor is not a ModernRoboticsI2cGyro");
        }
      }
    } catch (Exception e) {
      RobotLog.e("GyroSensor.setI2cAddress7Bit - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = ModernRoboticsI2cGyro.class, methodName = "getI2cAddress")
  public int getI2cAddress7Bit() {
    checkIfStopRequested();
    try {
      if (gyroSensor != null) {
        if (gyroSensor instanceof ModernRoboticsI2cGyro) {
          I2cAddr i2cAddr = ((ModernRoboticsI2cGyro) gyroSensor).getI2cAddress();
          if (i2cAddr != null) {
            return i2cAddr.get7Bit();
          }
        } else {
          RobotLog.e("GyroSensor.getI2cAddress7Bit - gyro sensor is not a ModernRoboticsI2cGyro");
        }
      }
    } catch (Exception e) {
      RobotLog.e("GyroSensor.getI2cAddress7Bit - caught " + e);
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = ModernRoboticsI2cGyro.class, methodName = "setI2cAddress")
  public void setI2cAddress8Bit(int i2cAddr8Bit) {
    checkIfStopRequested();
    try {
      if (gyroSensor != null) {
        if (gyroSensor instanceof ModernRoboticsI2cGyro) {
          ((ModernRoboticsI2cGyro) gyroSensor).setI2cAddress(I2cAddr.create8bit(i2cAddr8Bit));
        } else {
          RobotLog.e("GyroSensor.setI2cAddress8Bit - gyro sensor is not a ModernRoboticsI2cGyro");
        }
      }
    } catch (Exception e) {
      RobotLog.e("GyroSensor.setI2cAddress8Bit - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = ModernRoboticsI2cGyro.class, methodName = "getI2cAddress")
  public int getI2cAddress8Bit() {
    checkIfStopRequested();
    try {
      if (gyroSensor != null) {
        if (gyroSensor instanceof ModernRoboticsI2cGyro) {
          I2cAddr i2cAddr = ((ModernRoboticsI2cGyro) gyroSensor).getI2cAddress();
          if (i2cAddr != null) {
            return i2cAddr.get8Bit();
          }
        } else {
          RobotLog.e("GyroSensor.getI2cAddress8Bit - gyro sensor is not a ModernRoboticsI2cGyro");
        }
      }
    } catch (Exception e) {
      RobotLog.e("GyroSensor.getI2cAddress8Bit - caught " + e);
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = ModernRoboticsI2cGyro.class, methodName = "getIntegratedZValue")
  public int getIntegratedZValue() {
    checkIfStopRequested();
    try {
      if (gyroSensor != null) {
        if (gyroSensor instanceof ModernRoboticsI2cGyro) {
          return ((ModernRoboticsI2cGyro) gyroSensor).getIntegratedZValue();
        } else {
          RobotLog.e("GyroSensor.getIntegratedZValue - gyro sensor is not a ModernRoboticsI2cGyro");
        }
      }
    } catch (Exception e) {
      RobotLog.e("GyroSensor.getIntegratedZValue - caught " + e);
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtGyroSensor.class, ModernRoboticsI2cGyro.class}, methodName = "rawX")
  public int getRawX() {
    checkIfStopRequested();
    try {
      if (gyroSensor != null) {
        return gyroSensor.rawX();
      }
    } catch (Exception e) {
      RobotLog.e("GyroSensor.getRawX - caught " + e);
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtGyroSensor.class, ModernRoboticsI2cGyro.class}, methodName = "rawY")
  public int getRawY() {
    checkIfStopRequested();
    try {
      if (gyroSensor != null) {
        return gyroSensor.rawY();
      }
    } catch (Exception e) {
      RobotLog.e("GyroSensor.getRawY - caught " + e);
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtGyroSensor.class, ModernRoboticsI2cGyro.class}, methodName = "rawZ")
  public int getRawZ() {
    checkIfStopRequested();
    try {
      if (gyroSensor != null) {
        return gyroSensor.rawZ();
      }
    } catch (Exception e) {
      RobotLog.e("GyroSensor.getRawZ - caught " + e);
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtGyroSensor.class}, methodName = "getRotationFraction")
  public double getRotationFraction() {
    checkIfStopRequested();
    try {
      if (gyroSensor != null) {
        return gyroSensor.getRotationFraction();
      }
    } catch (Exception e) {
      RobotLog.e("GyroSensor.getRotationFraction - caught " + e);
    }
    return 0.0;
  }

  // Functions

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtGyroSensor.class, ModernRoboticsI2cGyro.class}, methodName = "calibrate")
  public void calibrate() {
    checkIfStopRequested();
    try {
      if (gyroSensor != null) {
        gyroSensor.calibrate();
      }
    } catch (Exception e) {
      RobotLog.e("GyroSensor.calibrate - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtGyroSensor.class, ModernRoboticsI2cGyro.class}, methodName = "isCalibrating")
  public boolean isCalibrating() {
    checkIfStopRequested();
    try {
      if (gyroSensor != null) {
        return gyroSensor.isCalibrating();
      }
    } catch (Exception e) {
      RobotLog.e("GyroSensor.isCalibrating - caught " + e);
    }
    return false;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtGyroSensor.class, ModernRoboticsI2cGyro.class}, methodName = "resetZAxisIntegrator")
  public void resetZAxisIntegrator() {
    checkIfStopRequested();
    try {
      if (gyroSensor != null) {
        gyroSensor.resetZAxisIntegrator();
      }
    } catch (Exception e) {
      RobotLog.e("GyroSensor.resetZAxisIntegrator - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtGyroSensor.class, ModernRoboticsI2cGyro.class}, methodName = "getAngularVelocityAxes")
  public String getAngularVelocityAxes() {
    checkIfStopRequested();
    try {
      if (gyroSensor != null) {
        if (gyroSensor instanceof Gyroscope) {
          Set<Axis> axes = ((Gyroscope) gyroSensor).getAngularVelocityAxes();
          StringBuilder sb = new StringBuilder();
          sb.append("[");
          String delimiter = "";
          for (Axis axis : axes) {
            sb.append(delimiter).append("\"").append(axis.toString()).append("\"");
            delimiter = ",";
          }
          sb.append("]");
          return sb.toString();
        } else {
          RobotLog.e("GyroSensor.getAngularVelocityAxes - gyro sensor is not a Gyroscope");
        }
      }
    } catch (Exception e) {
      RobotLog.e("GyroSensor.getAngularVelocityAxes - caught " + e);
    }
    return "[]";
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtGyroSensor.class, ModernRoboticsI2cGyro.class}, methodName = "getAngularVelocity")
  public AngularVelocity getAngularVelocity(String angleUnitString) {
    checkIfStopRequested();
    try {
      if (gyroSensor != null) {
        AngleUnit angleUnit =
            AngleUnit.valueOf(angleUnitString.toUpperCase(Locale.ENGLISH));
        if (gyroSensor instanceof Gyroscope) {
          return ((Gyroscope) gyroSensor).getAngularVelocity(angleUnit);
        } else {
          RobotLog.e("GyroSensor.getAngularVelocity - gyro sensor is not a Gyroscope");
          return new AngularVelocity(angleUnit, 0, 0, 0, 0L);
        }
      }
    } catch (Exception e) {
      RobotLog.e("GyroSensor.getAngularVelocity - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {ModernRoboticsI2cGyro.class}, methodName = "getAngularOrientationAxes")
  public String getAngularOrientationAxes() {
    checkIfStopRequested();
    try {
      if (gyroSensor != null) {
        if (gyroSensor instanceof OrientationSensor) {
          Set<Axis> axes = ((OrientationSensor) gyroSensor).getAngularOrientationAxes();
          StringBuilder sb = new StringBuilder();
          sb.append("[");
          String delimiter = "";
          for (Axis axis : axes) {
            sb.append(delimiter).append("\"").append(axis.toString()).append("\"");
            delimiter = ",";
          }
          sb.append("]");
          return sb.toString();
        } else {
          RobotLog.e("GyroSensor.getAngularOrientationAxes - gyro sensor is not a OrientationSensor");
        }
      }
    } catch (Exception e) {
      RobotLog.e("GyroSensor.getAngularOrientationAxes - caught " + e);
    }
    return "[]";
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {ModernRoboticsI2cGyro.class}, methodName = "getAngularOrientation")
  public Orientation getAngularOrientation(String axesReferenceString, String axesOrderString, String angleUnitString) {
    checkIfStopRequested();
    try {
      if (gyroSensor != null) {
        AxesReference axesReference = AxesReference.valueOf(axesReferenceString.toUpperCase(Locale.ENGLISH));
        AxesOrder axesOrder = AxesOrder.valueOf(axesOrderString.toUpperCase(Locale.ENGLISH));
        AngleUnit angleUnit = AngleUnit.valueOf(angleUnitString.toUpperCase(Locale.ENGLISH));
        if (gyroSensor instanceof OrientationSensor) {
          return ((OrientationSensor) gyroSensor).getAngularOrientation(axesReference, axesOrder, angleUnit);
        } else {
          RobotLog.e("GyroSensor.getAngularOrientation - gyro sensor is not a OrientationSensor");
          return new Orientation(axesReference, axesOrder, angleUnit, 0, 0, 0, 0L);
        }
      }
    } catch (Exception e) {
      RobotLog.e("GyroSensor.getAngularOrientation - caught " + e);
    }
    return null;
  }
}
