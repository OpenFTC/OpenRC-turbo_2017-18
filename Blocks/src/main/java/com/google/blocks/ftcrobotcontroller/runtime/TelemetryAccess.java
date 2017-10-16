// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.qualcomm.robotcore.util.RobotLog;
import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * A class that provides JavaScript access to {@link Telemetry}.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
class TelemetryAccess extends Access {
  private final Telemetry telemetry;

  TelemetryAccess(BlocksOpMode blocksOpMode, String identifier, Telemetry telemetry) {
    super(blocksOpMode, identifier);
    this.telemetry = telemetry;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void addNumericData(String key, double msg) {
    checkIfStopRequested();
    try {
      telemetry.addData(key, msg);
    } catch (Exception e) {
      RobotLog.e("TelemetryAccess.addNumericData - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void addTextData(String key, String msg) {
    checkIfStopRequested();
    try {
      telemetry.addData(key, msg);
    } catch (Exception e) {
      RobotLog.e("TelemetryAccess.addTextData - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void update() {
    checkIfStopRequested();
    try {
      telemetry.update();
    } catch (Exception e) {
      RobotLog.e("TelemetryAccess.update - caught " + e);
    }
  }
}
