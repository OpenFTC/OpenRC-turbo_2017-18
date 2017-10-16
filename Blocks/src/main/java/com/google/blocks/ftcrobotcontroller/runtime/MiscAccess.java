// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;

/**
 * A class that provides JavaScript access to miscellaneous functionality.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
class MiscAccess extends Access {

  MiscAccess(BlocksOpMode blocksOpMode, String identifier) {
    super(blocksOpMode, identifier);
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public Object getNull() {
    checkIfStopRequested();
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public boolean isNull(Object value) {
    checkIfStopRequested();
    return (value == null);
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public boolean isNotNull(Object value) {
    checkIfStopRequested();
    return (value != null);
  }
}
