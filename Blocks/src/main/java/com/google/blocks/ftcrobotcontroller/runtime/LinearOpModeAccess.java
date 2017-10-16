// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.RobotLog;

/**
 * A class that provides JavaScript access to a {@link LinearOpMode}.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
class LinearOpModeAccess extends Access {
  private final BlocksOpMode blocksOpMode;

  LinearOpModeAccess(BlocksOpMode blocksOpMode, String identifier) {
    super(blocksOpMode, identifier);
    this.blocksOpMode = blocksOpMode;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void waitForStart() {
    checkIfStopRequested();
    blocksOpMode.waitForStartForBlocks();
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void idle() {
    checkIfStopRequested();
    blocksOpMode.idle();
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void sleep(double millis) {
    checkIfStopRequested();
    blocksOpMode.sleepForBlocks((long) millis);
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public boolean opModeIsActive() {
    checkIfStopRequested();
    return blocksOpMode.opModeIsActive();
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public boolean isStarted() {
    checkIfStopRequested();
    return blocksOpMode.isStartedForBlocks();
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public boolean isStopRequested() {
    checkIfStopRequested();
    return blocksOpMode.isStopRequestedForBlocks();
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public double getRuntime() {
    checkIfStopRequested();
    return blocksOpMode.getRuntime();
  }
}
