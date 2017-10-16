// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.RobotLog;

/**
 * A class that provides JavaScript access to a {@link Gamepad}.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
class GamepadAccess extends Access {
  private final Gamepad gamepad;

  GamepadAccess(BlocksOpMode blocksOpMode, String identifier, Gamepad gamepad) {
    super(blocksOpMode, identifier);
    this.gamepad = gamepad;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {Gamepad.class}, fieldName = "left_stick_x")
  public float getLeftStickX() {
    checkIfStopRequested();
    if (gamepad != null) {
      return gamepad.left_stick_x;
    }
    return 0f;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {Gamepad.class}, fieldName = "left_stick_y")
  public float getLeftStickY() {
    checkIfStopRequested();
    if (gamepad != null) {
      return gamepad.left_stick_y;
    }
    return 0f;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {Gamepad.class}, fieldName = "right_stick_x")
  public float getRightStickX() {
    checkIfStopRequested();
    if (gamepad != null) {
      return gamepad.right_stick_x;
    }
    return 0f;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {Gamepad.class}, fieldName = "right_stick_y")
  public float getRightStickY() {
    checkIfStopRequested();
    if (gamepad != null) {
      return gamepad.right_stick_y;
    }
    return 0f;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {Gamepad.class}, fieldName = "dpad_up")
  public boolean getDpadUp() {
    checkIfStopRequested();
    if (gamepad != null) {
      return gamepad.dpad_up;
    }
    return false;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {Gamepad.class}, fieldName = "dpad_down")
  public boolean getDpadDown() {
    checkIfStopRequested();
    if (gamepad != null) {
      return gamepad.dpad_down;
    }
    return false;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {Gamepad.class}, fieldName = "dpad_left")
  public boolean getDpadLeft() {
    checkIfStopRequested();
    if (gamepad != null) {
      return gamepad.dpad_left;
    }
    return false;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {Gamepad.class}, fieldName = "dpad_right")
  public boolean getDpadRight() {
    checkIfStopRequested();
    if (gamepad != null) {
      return gamepad.dpad_right;
    }
    return false;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {Gamepad.class}, fieldName = "a")
  public boolean getA() {
    checkIfStopRequested();
    if (gamepad != null) {
      return gamepad.a;
    }
    return false;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {Gamepad.class}, fieldName = "b")
  public boolean getB() {
    checkIfStopRequested();
    if (gamepad != null) {
      return gamepad.b;
    }
    return false;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {Gamepad.class}, fieldName = "x")
  public boolean getX() {
    checkIfStopRequested();
    if (gamepad != null) {
      return gamepad.x;
    }
    return false;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {Gamepad.class}, fieldName = "y")
  public boolean getY() {
    checkIfStopRequested();
    if (gamepad != null) {
      return gamepad.y;
    }
    return false;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {Gamepad.class}, fieldName = "guide")
  public boolean getGuide() {
    checkIfStopRequested();
    if (gamepad != null) {
      return gamepad.guide;
    }
    return false;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {Gamepad.class}, fieldName = "start")
  public boolean getStart() {
    checkIfStopRequested();
    if (gamepad != null) {
      return gamepad.start;
    }
    return false;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {Gamepad.class}, fieldName = "back")
  public boolean getBack() {
    checkIfStopRequested();
    if (gamepad != null) {
      return gamepad.back;
    }
    return false;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {Gamepad.class}, fieldName = "left_bumper")
  public boolean getLeftBumper() {
    checkIfStopRequested();
    if (gamepad != null) {
      return gamepad.left_bumper;
    }
    return false;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {Gamepad.class}, fieldName = "right_bumper")
  public boolean getRightBumper() {
    checkIfStopRequested();
    if (gamepad != null) {
      return gamepad.right_bumper;
    }
    return false;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {Gamepad.class}, fieldName = "left_stick_button")
  public boolean getLeftStickButton() {
    checkIfStopRequested();
    if (gamepad != null) {
      return gamepad.left_stick_button;
    }
    return false;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {Gamepad.class}, fieldName = "right_stick_button")
  public boolean getRightStickButton() {
    checkIfStopRequested();
    if (gamepad != null) {
      return gamepad.right_stick_button;
    }
    return false;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {Gamepad.class}, fieldName = "left_trigger")
  public float getLeftTrigger() {
    checkIfStopRequested();
    if (gamepad != null) {
      return gamepad.left_trigger;
    }
    return 0f;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {Gamepad.class}, fieldName = "right_trigger")
  public float getRightTrigger() {
    checkIfStopRequested();
    if (gamepad != null) {
      return gamepad.right_trigger;
    }
    return 0f;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  @Block(classes = {Gamepad.class}, methodName = "atRest")
  public boolean getAtRest() {
    checkIfStopRequested();
    if (gamepad != null) {
      return gamepad.atRest();
    }
    return false;
  }
}
