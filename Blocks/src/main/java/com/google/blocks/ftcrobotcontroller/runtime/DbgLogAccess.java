// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;

import com.qualcomm.robotcore.util.RobotLog;

/**
 * A class that provides JavaScript access to DbgLog.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
class DbgLogAccess extends Access {

  public static final String TAG = "DbgLog";

  DbgLogAccess(BlocksOpMode blocksOpMode, String identifier) {
    super(blocksOpMode, identifier);
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void msg(String message) {
    checkIfStopRequested();
    RobotLog.ii(TAG, message);
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void error(String message) {
    checkIfStopRequested();
    RobotLog.ee(TAG, message);
  }
}
