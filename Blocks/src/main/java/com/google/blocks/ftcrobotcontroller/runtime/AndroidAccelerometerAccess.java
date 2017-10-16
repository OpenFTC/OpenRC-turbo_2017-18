// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller.runtime;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.webkit.JavascriptInterface;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.Locale;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

/**
 * A class that provides JavaScript access to the Android Accelerometer.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
class AndroidAccelerometerAccess extends Access implements SensorEventListener {
  private final Activity activity;
  private volatile boolean listening;
  private volatile long timestamp;
  private volatile float x; // Acceleration minus Gx on the x-axis, in SI units (m/s^2).
  private volatile float y; // Acceleration minus Gx on the y-axis, in SI units (m/s^2).
  private volatile float z; // Acceleration minus Gx on the z-axis, in SI units (m/s^2).
  private volatile DistanceUnit distanceUnit = DistanceUnit.METER;

  AndroidAccelerometerAccess(BlocksOpMode blocksOpMode, String identifier, Activity activity) {
    super(blocksOpMode, identifier);
    this.activity = activity;
  }

  // Access methods

  @Override
  void close() {
    if (listening) {
      SensorManager sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
      sensorManager.unregisterListener(this);
      listening = false;
      timestamp = 0;
    }
  }

  // SensorEventListener methods

  @Override
  public void onAccuracyChanged(Sensor sensor, int accuracy) {
  }

  @Override
  public void onSensorChanged(SensorEvent sensorEvent) {
    timestamp = sensorEvent.timestamp;
    x = sensorEvent.values[0];
    y = sensorEvent.values[1];
    z = sensorEvent.values[2];
  }

  // Javascript methods

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void setDistanceUnit(String distanceUnitString) {
    checkIfStopRequested();
    try {
      distanceUnit = DistanceUnit.valueOf(distanceUnitString.toUpperCase(Locale.ENGLISH));
    } catch (Exception e) {
      RobotLog.e("AndroidAccelerometer.setAngelUnit - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public double getX() {
    checkIfStopRequested();
    if (timestamp != 0) {
      return distanceUnit.fromMeters(x);
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public double getY() {
    checkIfStopRequested();
    if (timestamp != 0) {
      return distanceUnit.fromMeters(y);
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public double getZ() {
    checkIfStopRequested();
    if (timestamp != 0) {
      return distanceUnit.fromMeters(z);
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public Acceleration getAcceleration() {
    checkIfStopRequested();
    if (timestamp != 0) {
      return new Acceleration(DistanceUnit.METER, x, y, z, timestamp)
          .toUnit(distanceUnit);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public String getDistanceUnit() {
    checkIfStopRequested();
    return distanceUnit.toString();
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public boolean isAvailable() {
    checkIfStopRequested();
    SensorManager sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
    return !sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).isEmpty();
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void startListening() {
    checkIfStopRequested();
    if (!listening) {
      SensorManager sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
      Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
      sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
      listening = true;
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void stopListening() {
    checkIfStopRequested();
    close();
  }
}
