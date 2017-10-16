// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.qualcomm.hardware.bosch.BNO055IMU.AccelUnit;
import com.qualcomm.hardware.bosch.BNO055IMU.AccelerationIntegrator;
import com.qualcomm.hardware.bosch.BNO055IMU.AngleUnit;
import com.qualcomm.hardware.bosch.BNO055IMU.Parameters;
import com.qualcomm.hardware.bosch.BNO055IMU.SensorMode;
import com.qualcomm.hardware.bosch.BNO055IMU.TempUnit;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.hardware.bosch.NaiveAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.Locale;

/**
 * A class that provides JavaScript access to {@link BNO055IMU#Parameters}.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
class BNO055IMUParametersAccess extends Access {

  BNO055IMUParametersAccess(BlocksOpMode blocksOpMode, String identifier) {
    super(blocksOpMode, identifier);
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = Parameters.class, constructor = true)
  public Parameters create() {
    checkIfStopRequested();
    try {
      return new Parameters();
    } catch (Exception e) {
      RobotLog.e("BNO055IMUParameters.create - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = Parameters.class, fieldName = "accelUnit")
  public void setAccelUnit(Object bno055imuParameters, String accelUnitString) {
    checkIfStopRequested();
    try {
      if (bno055imuParameters instanceof Parameters) {
        AccelUnit accelUnit = AccelUnit.valueOf(accelUnitString.toUpperCase(Locale.ENGLISH));
        ((Parameters) bno055imuParameters).accelUnit = accelUnit;
      } else {
        RobotLog.e("BNO055IMUParameters.setAccelUnit - " +
            "bno055imuParameters is not a BNO055IMU.Parameters");
      }
    } catch (Exception e) {
      RobotLog.e("BNO055IMUParameters.setAccelUnit - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = Parameters.class, fieldName = "accelUnit")
  public String getAccelUnit(Object bno055imuParameters) {
    checkIfStopRequested();
    try {
      if (bno055imuParameters instanceof Parameters) {
        AccelUnit accelUnit = ((Parameters) bno055imuParameters).accelUnit;
        if (accelUnit != null) {
          return accelUnit.toString();
        }
      } else {
        RobotLog.e("BNO055IMUParameters.getAccelUnit - " +
            "bno055imuParameters is not a BNO055IMU.Parameters");
      }
    } catch (Exception e) {
      RobotLog.e("BNO055IMUParameters.getAccelUnit - caught " + e);
    }
    return "";
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = Parameters.class, fieldName = "accelerationIntegrationAlgorithm")
  public void setAccelerationIntegrationAlgorithm(
      Object bno055imuParameters, String accelerationIntegrationAlgorithm) {
    checkIfStopRequested();
    try {
      if (bno055imuParameters instanceof Parameters) {
        accelerationIntegrationAlgorithm =
            accelerationIntegrationAlgorithm.toUpperCase(Locale.ENGLISH);
        if (accelerationIntegrationAlgorithm.equals("NAIVE")) {
          ((Parameters) bno055imuParameters).accelerationIntegrationAlgorithm = null;
        } else if (accelerationIntegrationAlgorithm.equals("JUST_LOGGING")) {
          ((Parameters) bno055imuParameters).accelerationIntegrationAlgorithm =
              new JustLoggingAccelerationIntegrator();
        }
      } else {
        RobotLog.e("BNO055IMUParameters.setAccelerationIntegrationAlgorithm - " +
            "bno055imuParameters is not a BNO055IMU.Parameters");
      }
    } catch (Exception e) {
      RobotLog.e("BNO055IMUParameters.setAccelerationIntegrationAlgorithm - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = Parameters.class, fieldName = "accelerationIntegrationAlgorithm")
  public String getAccelerationIntegrationAlgorithm(Object bno055imuParameters) {
    checkIfStopRequested();
    try {
      if (bno055imuParameters instanceof Parameters) {
        AccelerationIntegrator accelerationIntegrationAlgorithm =
            ((Parameters) bno055imuParameters).accelerationIntegrationAlgorithm;
        if (accelerationIntegrationAlgorithm == null ||
            accelerationIntegrationAlgorithm instanceof NaiveAccelerationIntegrator) {
          return "NAIVE";
        }
        if (accelerationIntegrationAlgorithm instanceof JustLoggingAccelerationIntegrator) {
          return "JUST_LOGGING";
        }
      } else {
        RobotLog.e("BNO055IMUParameters.getAccelerationIntegrationAlgorithm - " +
            "bno055imuParameters is not a BNO055IMU.Parameters");
      }
    } catch (Exception e) {
      RobotLog.e("BNO055IMUParameters.getAccelerationIntegrationAlgorithm - caught " + e);
    }
    return "";
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = Parameters.class, fieldName = "angleUnit")
  public void setAngleUnit(Object bno055imuParameters, String angleUnitString) {
    checkIfStopRequested();
    try {
      if (bno055imuParameters instanceof Parameters) {
        AngleUnit angleUnit =
            AngleUnit.valueOf(angleUnitString.toUpperCase(Locale.ENGLISH));
        ((Parameters) bno055imuParameters).angleUnit = angleUnit;
      } else {
        RobotLog.e("BNO055IMUParameters.setAngleUnit - " +
            "bno055imuParameters is not a BNO055IMU.Parameters");
      }
    } catch (Exception e) {
      RobotLog.e("BNO055IMUParameters.setAngleUnit - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = Parameters.class, fieldName = "angleUnit")
  public String getAngleUnit(Object bno055imuParameters) {
    checkIfStopRequested();
    try {
      if (bno055imuParameters instanceof Parameters) {
        AngleUnit angleUnit = ((Parameters) bno055imuParameters).angleUnit;
        if (angleUnit != null) {
          return angleUnit.toString();
        }
      } else {
        RobotLog.e("BNO055IMUParameters.getAngleUnit - " +
            "bno055imuParameters is not a BNO055IMU.Parameters");
      }
    } catch (Exception e) {
      RobotLog.e("BNO055IMUParameters.getAngleUnit - caught " + e);
    }
    return "";
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = Parameters.class, fieldName = "calibrationDataFile")
  public void setCalibrationDataFile(Object bno055imuParameters, String calibrationDataFile) {
    checkIfStopRequested();
    try {
      if (bno055imuParameters instanceof Parameters) {
        ((Parameters) bno055imuParameters).calibrationDataFile = calibrationDataFile;
      } else {
        RobotLog.e("BNO055IMUParameters.setCalibrationDataFile - " +
            "bno055imuParameters is not a BNO055IMU.Parameters");
      }
    } catch (Exception e) {
      RobotLog.e("BNO055IMUParameters.setCalibrationDataFile - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = Parameters.class, fieldName = "calibrationDataFile")
  public String getCalibrationDataFile(Object bno055imuParameters) {
    checkIfStopRequested();
    try {
      if (bno055imuParameters instanceof Parameters) {
        String calibrationDataFile = ((Parameters) bno055imuParameters).calibrationDataFile;
        if (calibrationDataFile != null) {
          return calibrationDataFile;
        }
      } else {
        RobotLog.e("BNO055IMUParameters.getCalibrationDataFile - " +
            "bno055imuParameters is not a BNO055IMU.Parameters");
      }
    } catch (Exception e) {
      RobotLog.e("BNO055IMUParameters.getCalibrationDataFile - caught " + e);
    }
    return "";
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = Parameters.class, fieldName = "i2cAddr")
  public void setI2cAddress7Bit(Object bno055imuParameters, int i2cAddr7Bit) {
    checkIfStopRequested();
    try {
      if (bno055imuParameters instanceof Parameters) {
        ((Parameters) bno055imuParameters).i2cAddr = I2cAddr.create7bit(i2cAddr7Bit);
      } else {
        RobotLog.e("BNO055IMUParameters.setI2cAddress7Bit - " +
            "bno055imuParameters is not a BNO055IMU.Parameters");
      }
    } catch (Exception e) {
      RobotLog.e("BNO055IMUParameters.setI2cAddress7Bit - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = Parameters.class, fieldName = "i2cAddr")
  public int getI2cAddress7Bit(Object bno055imuParameters) {
    checkIfStopRequested();
    try {
      if (bno055imuParameters instanceof Parameters) {
        I2cAddr i2cAddr = ((Parameters) bno055imuParameters).i2cAddr;
        if (i2cAddr != null) {
          return i2cAddr.get7Bit();
        }
      } else {
        RobotLog.e("BNO055IMUParameters.getI2cAddress7Bit - " +
            "bno055imuParameters is not a BNO055IMU.Parameters");
      }
    } catch (Exception e) {
      RobotLog.e("BNO055IMUParameters.getI2cAddress7Bit - caught " + e);
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = Parameters.class, fieldName = "i2cAddr")
  public void setI2cAddress8Bit(Object bno055imuParameters, int i2cAddr8Bit) {
    checkIfStopRequested();
    try {
      if (bno055imuParameters instanceof Parameters) {
        ((Parameters) bno055imuParameters).i2cAddr = I2cAddr.create8bit(i2cAddr8Bit);
      } else {
        RobotLog.e("BNO055IMUParameters.setI2cAddress8Bit - " +
            "bno055imuParameters is not a BNO055IMU.Parameters");
      }
    } catch (Exception e) {
      RobotLog.e("BNO055IMUParameters.setI2cAddress8Bit - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = Parameters.class, fieldName = "i2cAddr")
  public int getI2cAddress8Bit(Object bno055imuParameters) {
    checkIfStopRequested();
    try {
      if (bno055imuParameters instanceof Parameters) {
        I2cAddr i2cAddr = ((Parameters) bno055imuParameters).i2cAddr;
        if (i2cAddr != null) {
          return i2cAddr.get8Bit();
        }
      } else {
        RobotLog.e("BNO055IMUParameters.getI2cAddress8Bit - " +
            "bno055imuParameters is not a BNO055IMU.Parameters");
      }
    } catch (Exception e) {
      RobotLog.e("BNO055IMUParameters.getI2cAddress8Bit - caught " + e);
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = Parameters.class, fieldName = "loggingEnabled")
  public void setLoggingEnabled(Object bno055imuParameters, boolean loggingEnabled) {
    checkIfStopRequested();
    try {
      if (bno055imuParameters instanceof Parameters) {
        ((Parameters) bno055imuParameters).loggingEnabled = loggingEnabled;
      } else {
        RobotLog.e("BNO055IMUParameters.setLoggingEnabled - " +
            "bno055imuParameters is not a BNO055IMU.Parameters");
      }
    } catch (Exception e) {
      RobotLog.e("BNO055IMUParameters.setLoggingEnabled - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = Parameters.class, fieldName = "loggingEnabled")
  public boolean getLoggingEnabled(Object bno055imuParameters) {
    checkIfStopRequested();
    try {
      if (bno055imuParameters instanceof Parameters) {
        return ((Parameters) bno055imuParameters).loggingEnabled;
      } else {
        RobotLog.e("BNO055IMUParameters.getLoggingEnabled - " +
            "bno055imuParameters is not a BNO055IMU.Parameters");
      }
    } catch (Exception e) {
      RobotLog.e("BNO055IMUParameters.getLoggingEnabled - caught " + e);
    }
    return false;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = Parameters.class, fieldName = "loggingTag")
  public void setLoggingTag(Object bno055imuParameters, String loggingTag) {
    checkIfStopRequested();
    try {
      if (bno055imuParameters instanceof Parameters) {
        ((Parameters) bno055imuParameters).loggingTag = loggingTag;
      } else {
        RobotLog.e("BNO055IMUParameters.setLoggingTag - " +
            "bno055imuParameters is not a BNO055IMU.Parameters");
      }
    } catch (Exception e) {
      RobotLog.e("BNO055IMUParameters.setLoggingTag - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = Parameters.class, fieldName = "loggingTag")
  public String getLoggingTag(Object bno055imuParameters) {
    checkIfStopRequested();
    try {
      if (bno055imuParameters instanceof Parameters) {
        String loggingTag = ((Parameters) bno055imuParameters).loggingTag;
        if (loggingTag != null) {
          return loggingTag;
        }
      } else {
        RobotLog.e("BNO055IMUParameters.getLoggingTag - " +
            "bno055imuParameters is not a BNO055IMU.Parameters");
      }
    } catch (Exception e) {
      RobotLog.e("BNO055IMUParameters.getLoggingTag - caught " + e);
    }
    return "";
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = Parameters.class, fieldName = "mode")
  public void setSensorMode(Object bno055imuParameters, String sensorModeString) {
    checkIfStopRequested();
    try {
      if (bno055imuParameters instanceof Parameters) {
        SensorMode sensorMode =
            SensorMode.valueOf(sensorModeString.toUpperCase(Locale.ENGLISH));
        ((Parameters) bno055imuParameters).mode = sensorMode;
      } else {
        RobotLog.e("BNO055IMUParameters.setSensorMode - " +
            "bno055imuParameters is not a BNO055IMU.Parameters");
      }
    } catch (Exception e) {
      RobotLog.e("BNO055IMUParameters.setSensorMode - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = Parameters.class, fieldName = "mode")
  public String getSensorMode(Object bno055imuParameters) {
    checkIfStopRequested();
    try {
      if (bno055imuParameters instanceof Parameters) {
        SensorMode sensorMode = ((Parameters) bno055imuParameters).mode;
        if (sensorMode != null) {
          return sensorMode.toString();
        }
      } else {
        RobotLog.e("BNO055IMUParameters.getSensorMode - " +
            "bno055imuParameters is not a BNO055IMU.Parameters");
      }
    } catch (Exception e) {
      RobotLog.e("BNO055IMUParameters.getSensorMode - caught " + e);
    }
    return "";
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = Parameters.class, fieldName = "temperatureUnit")
  public void setTempUnit(Object bno055imuParameters, String temperatureUnitString) {
    checkIfStopRequested();
    try {
      if (bno055imuParameters instanceof Parameters) {
        TempUnit temperatureUnit =
            TempUnit.valueOf(temperatureUnitString.toUpperCase(Locale.ENGLISH));
        ((Parameters) bno055imuParameters).temperatureUnit = temperatureUnit;
      } else {
        RobotLog.e("BNO055IMUParameters.setTempUnit - " +
            "bno055imuParameters is not a BNO055IMU.Parameters");
      }
    } catch (Exception e) {
      RobotLog.e("BNO055IMUParameters.setTempUnit - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = Parameters.class, fieldName = "temperatureUnit")
  public String getTempUnit(Object bno055imuParameters) {
    checkIfStopRequested();
    try {
      if (bno055imuParameters instanceof Parameters) {
        TempUnit temperatureUnit = ((Parameters) bno055imuParameters).temperatureUnit;
        if (temperatureUnit != null) {
          return temperatureUnit.toString();
        }
      } else {
        RobotLog.e("BNO055IMUParameters.getTempUnit - " +
            "bno055imuParameters is not a BNO055IMU.Parameters");
      }
    } catch (Exception e) {
      RobotLog.e("BNO055IMUParameters.getTempUnit - caught " + e);
    }
    return "";
  }
}
