// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.qualcomm.robotcore.util.RobotLog;

/**
 * An abstract class for classes that provides JavaScript access to an object.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
abstract class Access {
  private final BlocksOpMode blocksOpMode;
  private final String identifier;

  protected Access(BlocksOpMode blocksOpMode, String identifier) {
    this.blocksOpMode = blocksOpMode;
    this.identifier = identifier;
  }

  protected final void checkIfStopRequested() {
    blocksOpMode.checkIfStopRequested();
  }

  /**
   * The close method should be overridden in classes that need to clean up when the OpMode has
   * finished running.
   */
  void close() {
  }
}
