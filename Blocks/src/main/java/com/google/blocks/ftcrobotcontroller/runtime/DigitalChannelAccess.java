// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.google.blocks.ftcrobotcontroller.util.HardwareItem;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DigitalChannel.Mode;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.Locale;

/**
 * A class that provides JavaScript access to a {@link DigitalChannel}.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
class DigitalChannelAccess extends HardwareAccess<DigitalChannel> {
  private final DigitalChannel digitalChannel;

  DigitalChannelAccess(BlocksOpMode blocksOpMode, HardwareItem hardwareItem, HardwareMap hardwareMap, Class<? extends HardwareDevice> deviceType) {
    super(blocksOpMode, hardwareItem, hardwareMap, DigitalChannel.class);
    this.digitalChannel = hardwareDevice;
  }

  // Properties

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {DigitalChannel.class}, methodName = "setMode")
  public void setMode(String modeString) {
    checkIfStopRequested();
    try {
      if (digitalChannel != null) {
          Mode mode = Mode.valueOf(modeString.toUpperCase(Locale.ENGLISH));
          digitalChannel.setMode(mode);
      }
    } catch (Exception e) {
      RobotLog.e("DigitalChannel.setMode - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {DigitalChannel.class}, methodName = "getMode")
  public String getMode() {
    checkIfStopRequested();
    if (digitalChannel != null) {
      Mode mode = digitalChannel.getMode();
      if (mode != null) {
        return mode.toString();
      }
    }
    return "";
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {DigitalChannel.class}, methodName = "setState")
  public void setState(boolean state) {
    checkIfStopRequested();
    if (digitalChannel != null) {
      digitalChannel.setState(state);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {DigitalChannel.class}, methodName = "getState")
  public boolean getState() {
    checkIfStopRequested();
    if (digitalChannel != null) {
      return digitalChannel.getState();
    }
    return false;
  }
}
