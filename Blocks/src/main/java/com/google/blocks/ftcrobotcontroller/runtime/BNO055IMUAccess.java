// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.google.blocks.ftcrobotcontroller.util.HardwareItem;
import com.qualcomm.hardware.adafruit.AdafruitBNO055IMU;
import com.qualcomm.hardware.bosch.BNO055IMU.CalibrationStatus;
import com.qualcomm.hardware.bosch.BNO055IMU.Parameters;
import com.qualcomm.hardware.bosch.BNO055IMU.SystemError;
import com.qualcomm.hardware.bosch.BNO055IMU.SystemStatus;
import com.qualcomm.hardware.bosch.BNO055IMUImpl;
import com.qualcomm.hardware.lynx.LynxEmbeddedIMU;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.util.ReadWriteFile;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.Locale;
import java.util.Set;
import junit.framework.Assert;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Axis;
import org.firstinspires.ftc.robotcore.external.navigation.MagneticFlux;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Quaternion;
import org.firstinspires.ftc.robotcore.external.navigation.Temperature;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

/**
 * A class that provides JavaScript access to {@link BNO055IMUImpl}.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
class BNO055IMUAccess extends HardwareAccess<BNO055IMUImpl> {
  private final BNO055IMUImpl imu;

  BNO055IMUAccess(BlocksOpMode blocksOpMode,
      HardwareItem hardwareItem, HardwareMap hardwareMap, Class<? extends HardwareDevice> deviceType) {
    super(blocksOpMode, hardwareItem, hardwareMap, BNO055IMUImpl.class);
    Assert.assertTrue(deviceType == BNO055IMUImpl.class);
    this.imu = hardwareDevice;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = "getAcceleration")
  public Acceleration getAcceleration() {
    checkIfStopRequested();
    try {
      if (imu != null) {
        return imu.getAcceleration();
      }
    } catch (Exception e) {
      RobotLog.e("BNO055IMU.getAcceleration - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = "getAngularOrientation")
  public Orientation getAngularOrientation() {
    checkIfStopRequested();
    try {
      if (imu != null) {
        return imu.getAngularOrientation();
      }
    } catch (Exception e) {
      RobotLog.e("BNO055IMU.getAngularOrientation - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = "getAngularVelocity")
  public AngularVelocity getAngularVelocity() {
    checkIfStopRequested();
    try {
      if (imu != null) {
        return imu.getAngularVelocity();
      }
    } catch (Exception e) {
      RobotLog.e("BNO055IMU.getAngularVelocity - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = "getCalibrationStatus")
  public String getCalibrationStatus() {
    checkIfStopRequested();
    try {
      if (imu != null) {
        CalibrationStatus calibrationStatus = imu.getCalibrationStatus();
        if (calibrationStatus != null) {
          return calibrationStatus.toString();
        }
      }
    } catch (Exception e) {
      RobotLog.e("BNO055IMU.getCalibrationStatus - caught " + e);
    }
    return "";
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = "getGravity")
  public Acceleration getGravity() {
    checkIfStopRequested();
    try {
      if (imu != null) {
        return imu.getGravity();
      }
    } catch (Exception e) {
      RobotLog.e("BNO055IMU.getGravity - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = "getLinearAcceleration")
  public Acceleration getLinearAcceleration() {
    checkIfStopRequested();
    try {
      if (imu != null) {
        return imu.getLinearAcceleration();
      }
    } catch (Exception e) {
      RobotLog.e("BNO055IMU.getLinearAcceleration - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = "getMagneticFieldStrength")
  public MagneticFlux getMagneticFieldStrength() {
    checkIfStopRequested();
    try {
      if (imu != null) {
        return imu.getMagneticFieldStrength();
      }
    } catch (Exception e) {
      RobotLog.e("BNO055IMU.getMagneticFieldStrength - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = "getOverallAcceleration")
  public Acceleration getOverallAcceleration() {
    checkIfStopRequested();
    try {
      if (imu != null) {
        return imu.getOverallAcceleration();
      }
    } catch (Exception e) {
      RobotLog.e("BNO055IMU.getOverallAcceleration - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = "getParameters")
  public Parameters getParameters() {
    checkIfStopRequested();
    try {
      if (imu != null) {
        return imu.getParameters();
      }
    } catch (Exception e) {
      RobotLog.e("BNO055IMU.getParameters - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = "getPosition")
  public Position getPosition() {
    checkIfStopRequested();
    try {
      if (imu != null) {
        return imu.getPosition();
      }
    } catch (Exception e) {
      RobotLog.e("BNO055IMU.getPosition - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = "getQuaternionOrientation")
  public Quaternion getQuaternionOrientation() {
    checkIfStopRequested();
    try {
      if (imu != null) {
        return imu.getQuaternionOrientation();
      }
    } catch (Exception e) {
      RobotLog.e("BNO055IMU.getQuaternionOrientation - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = "getSystemError")
  public String getSystemError() {
    checkIfStopRequested();
    try {
      if (imu != null) {
        SystemError systemError = imu.getSystemError();
        if (systemError != null) {
          return systemError.toString();
        }
      }
    } catch (Exception e) {
      RobotLog.e("BNO055IMU.getSystemError - caught " + e);
    }
    return "";
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = "getSystemStatus")
  public String getSystemStatus() {
    checkIfStopRequested();
    try {
      if (imu != null) {
        SystemStatus systemStatus = imu.getSystemStatus();
        if (systemStatus != null) {
          return systemStatus.toString();
        }
      }
    } catch (Exception e) {
      RobotLog.e("BNO055IMU.getSystemStatus - caught " + e);
    }
    return "";
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = "getTemperature")
  public Temperature getTemperature() {
    checkIfStopRequested();
    try {
      if (imu != null) {
        return imu.getTemperature();
      }
    } catch (Exception e) {
      RobotLog.e("BNO055IMU.getTemperature - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = "getVelocity")
  public Velocity getVelocity() {
    checkIfStopRequested();
    try {
      if (imu != null) {
        return imu.getVelocity();
      }
    } catch (Exception e) {
      RobotLog.e("BNO055IMU.getVelocity - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = "initialize")
  public void initialize(Object parameters) {
    checkIfStopRequested();
    try {
      if (imu != null) {
        if (parameters instanceof Parameters) {
          imu.initialize(((Parameters) parameters));
        } else {
          RobotLog.e("BNO055IMU.initialize - parameters is not a BNO055IMU.Parameters");
        }
      }
    } catch (Exception e) {
      RobotLog.e("BNO055IMU.initialize - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = "startAccelerationIntegration")
  public void startAccelerationIntegration_with1(int msPollInterval) {
    checkIfStopRequested();
    try {
      if (imu != null) {
        imu.startAccelerationIntegration(
            null /* initialPosition */, null /* initialVelocity*/, msPollInterval);
      }
    } catch (Exception e) {
      RobotLog.e("BNO055IMU.startAccelerationIntegration - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = "startAccelerationIntegration")
  public void startAccelerationIntegration_with3(Object initialPosition, Object initialVelocity, int msPollInterval) {
    checkIfStopRequested();
    try {
      if (imu != null) {
        if (initialPosition == null || initialPosition instanceof Position) {
          if (initialVelocity == null || initialVelocity instanceof Velocity) {
            imu.startAccelerationIntegration(
                (Position) initialPosition, (Velocity) initialVelocity, msPollInterval);
          } else {
            RobotLog.e(
                "BNO055IMU.startAccelerationIntegration - initialVelocity is not a Velocity");
          }
        } else {
          RobotLog.e(
              "BNO055IMU.startAccelerationIntegration - initialPosition is not a Position");
        }
      }
    } catch (Exception e) {
      RobotLog.e("BNO055IMU.startAccelerationIntegration - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = "stopAccelerationIntegration")
  public void stopAccelerationIntegration() {
    checkIfStopRequested();
    try {
      if (imu != null) {
        imu.stopAccelerationIntegration();
      }
    } catch (Exception e) {
      RobotLog.e("BNO055IMU.stopAccelerationIntegration - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = "isSystemCalibrated")
  public boolean isSystemCalibrated() {
    checkIfStopRequested();
    try {
      if (imu != null) {
        return imu.isSystemCalibrated();
      }
    } catch (Exception e) {
      RobotLog.e("BNO055IMU.isSystemCalibrated - caught " + e);
    }
    return false;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = "isGyroCalibrated")
  public boolean isGyroCalibrated() {
    checkIfStopRequested();
    try {
      if (imu != null) {
        return imu.isGyroCalibrated();
      }
    } catch (Exception e) {
      RobotLog.e("BNO055IMU.isGyroCalibrated - caught " + e);
    }
    return false;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = "isAccelerometerCalibrated")
  public boolean isAccelerometerCalibrated() {
    checkIfStopRequested();
    try {
      if (imu != null) {
        return imu.isAccelerometerCalibrated();
      }
    } catch (Exception e) {
      RobotLog.e("BNO055IMU.isAccelerometerCalibrated - caught " + e);
    }
    return false;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = "isMagnetometerCalibrated")
  public boolean isMagnetometerCalibrated() {
    checkIfStopRequested();
    try {
      if (imu != null) {
        return imu.isMagnetometerCalibrated();
      }
    } catch (Exception e) {
      RobotLog.e("BNO055IMU.isMagnetometerCalibrated - caught " + e);
    }
    return false;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = "readCalibrationData")
  public void saveCalibrationData(String absoluteFileName) {
    checkIfStopRequested();
    try {
      if (imu != null) {
        ReadWriteFile.writeFile(
            AppUtil.getInstance().getSettingsFile(absoluteFileName),
            imu.readCalibrationData().serialize());
      }
    } catch (Exception e) {
      RobotLog.e("BNO055IMU.saveCalibrationData - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = "setI2cAddress")
  public void setI2cAddress7Bit(int i2cAddr7Bit) {
    checkIfStopRequested();
    try {
      if (imu != null) {
        imu.setI2cAddress(I2cAddr.create7bit(i2cAddr7Bit));
      }
    } catch (Exception e) {
      RobotLog.e("BNO055IMU.setI2cAddress7Bit - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = "getI2cAddress")
  public int getI2cAddress7Bit() {
    checkIfStopRequested();
    try {
      if (imu != null) {
        I2cAddr i2cAddr = imu.getI2cAddress();
        if (i2cAddr != null) {
          return i2cAddr.get7Bit();
        }
      }
    } catch (Exception e) {
      RobotLog.e("BNO055IMU.getI2cAddress7Bit - caught " + e);
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = "setI2cAddress")
  public void setI2cAddress8Bit(int i2cAddr8Bit) {
    checkIfStopRequested();
    try {
      if (imu != null) {
        imu.setI2cAddress(I2cAddr.create8bit(i2cAddr8Bit));
      }
    } catch (Exception e) {
      RobotLog.e("BNO055IMU.setI2cAddress8Bit - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = "getI2cAddress")
  public int getI2cAddress8Bit() {
    checkIfStopRequested();
    try {
      if (imu != null) {
        I2cAddr i2cAddr = imu.getI2cAddress();
        if (i2cAddr != null) {
          return i2cAddr.get8Bit();
        }
      }
    } catch (Exception e) {
      RobotLog.e("BNO055IMU.getI2cAddress8Bit - caught " + e);
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = "getAngularVelocityAxes")
  public String getAngularVelocityAxes() {
    checkIfStopRequested();
    try {
      if (imu != null) {
        Set<Axis> axes = imu.getAngularVelocityAxes();
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        String delimiter = "";
        for (Axis axis : axes) {
          sb.append(delimiter).append("\"").append(axis.toString()).append("\"");
          delimiter = ",";
        }
        sb.append("]");
        return sb.toString();
      }
    } catch (Exception e) {
      RobotLog.e("BNO055IMU.getAngularVelocityAxes - caught " + e);
    }
    return "[]";
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = "getAngularVelocity")
  public AngularVelocity getAngularVelocity(String angleUnitString) {
    checkIfStopRequested();
    try {
      if (imu != null) {
        AngleUnit angleUnit =
            AngleUnit.valueOf(angleUnitString.toUpperCase(Locale.ENGLISH));
        return imu.getAngularVelocity(angleUnit);
      }
    } catch (Exception e) {
      RobotLog.e("BNO055IMU.getAngularVelocity - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = "getAngularOrientationAxes")
  public String getAngularOrientationAxes() {
    checkIfStopRequested();
    try {
      if (imu != null) {
        Set<Axis> axes = imu.getAngularOrientationAxes();
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        String delimiter = "";
        for (Axis axis : axes) {
          sb.append(delimiter).append("\"").append(axis.toString()).append("\"");
          delimiter = ",";
        }
        sb.append("]");
        return sb.toString();
      }
    } catch (Exception e) {
      RobotLog.e("BNO055IMU.getAngularOrientationAxes - caught " + e);
    }
    return "[]";
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = "getAngularOrientation")
  public Orientation getAngularOrientation(String axesReferenceString, String axesOrderString, String angleUnitString) {
    checkIfStopRequested();
    try {
      if (imu != null) {
        AxesReference axesReference = AxesReference.valueOf(axesReferenceString.toUpperCase(Locale.ENGLISH));
        AxesOrder axesOrder = AxesOrder.valueOf(axesOrderString.toUpperCase(Locale.ENGLISH));
        AngleUnit angleUnit = AngleUnit.valueOf(angleUnitString.toUpperCase(Locale.ENGLISH));
        return imu.getAngularOrientation(axesReference, axesOrder, angleUnit);
      }
    } catch (Exception e) {
      RobotLog.e("BNO055IMU.getAngularOrientation - caught " + e);
    }
    return null;
  }
}
