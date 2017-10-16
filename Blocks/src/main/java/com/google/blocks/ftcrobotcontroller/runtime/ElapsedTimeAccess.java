// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.ElapsedTime.Resolution;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.Locale;

/**
 * A class that provides JavaScript access to {@link ElapsedTime}.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
class ElapsedTimeAccess extends Access {

  ElapsedTimeAccess(BlocksOpMode blocksOpMode, String identifier) {
    super(blocksOpMode, identifier);
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public ElapsedTime create() {
    checkIfStopRequested();
    try {
      return new ElapsedTime();
    } catch (Exception e) {
      RobotLog.e("ElapsedTime.create - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public ElapsedTime create_withStartTime(long startTime) {
    checkIfStopRequested();
    try {
      return new ElapsedTime(startTime);
    } catch (Exception e) {
      RobotLog.e("ElapsedTime.create - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public ElapsedTime create_withResolution(String resolutionString) {
    checkIfStopRequested();
    try {
      Resolution resolution = Resolution.valueOf(resolutionString.toUpperCase(Locale.ENGLISH));
      return new ElapsedTime(resolution);
    } catch (Exception e) {
      RobotLog.e("ElapsedTime.create - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public double getStartTime(Object elapsedTime) {
    checkIfStopRequested();
    try {
      if (elapsedTime instanceof ElapsedTime) {
        return ((ElapsedTime) elapsedTime).startTime();
      } else {
        RobotLog.e("ElapsedTime.getStartTime - parameter is not an ElapsedTime");
      }
    } catch (Exception e) {
      RobotLog.e("ElapsedTime.getStartTime - caught " + e);
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public double getTime(Object elapsedTime) {
    checkIfStopRequested();
    try {
      if (elapsedTime instanceof ElapsedTime) {
        return ((ElapsedTime) elapsedTime).time();
      } else {
        RobotLog.e("ElapsedTime.getTime - parameter is not an ElapsedTime");
      }
    } catch (Exception e) {
      RobotLog.e("ElapsedTime.getTime - caught " + e);
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public double getSeconds(Object elapsedTime) {
    checkIfStopRequested();
    try {
      if (elapsedTime instanceof ElapsedTime) {
        return ((ElapsedTime) elapsedTime).seconds();
      } else {
        RobotLog.e("ElapsedTime.getSeconds - parameter is not an ElapsedTime");
      }
    } catch (Exception e) {
      RobotLog.e("ElapsedTime.getSeconds - caught " + e);
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public double getMilliseconds(Object elapsedTime) {
    checkIfStopRequested();
    try {
      if (elapsedTime instanceof ElapsedTime) {
        return ((ElapsedTime) elapsedTime).milliseconds();
      } else {
        RobotLog.e("ElapsedTime.getMilliseconds - parameter is not an ElapsedTime");
      }
    } catch (Exception e) {
      RobotLog.e("ElapsedTime.getMilliseconds - caught " + e);
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public String getResolution(Object elapsedTime) {
    checkIfStopRequested();
    try {
      if (elapsedTime instanceof ElapsedTime) {
        Resolution resolution = ((ElapsedTime) elapsedTime).getResolution();
        if (resolution != null) {
          return resolution.toString();
        }
      } else {
        RobotLog.e("ElapsedTime.getResolution - parameter is not an ElapsedTime");
      }
    } catch (Exception e) {
      RobotLog.e("ElapsedTime.getResolution - caught " + e);
    }
    return "";
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public String getAsText(Object elapsedTime) {
    checkIfStopRequested();
    try {
      if (elapsedTime instanceof ElapsedTime) {
        return ((ElapsedTime) elapsedTime).toString();
      } else {
        RobotLog.e("ElapsedTime.getAsText - parameter is not an ElapsedTime");
      }
    } catch (Exception e) {
      RobotLog.e("ElapsedTime.getAsText - caught " + e);
    }
    return "";
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void reset(Object elapsedTime) {
    checkIfStopRequested();
    try {
      if (elapsedTime instanceof ElapsedTime) {
        ((ElapsedTime) elapsedTime).reset();
      } else {
        RobotLog.e("ElapsedTime.reset - parameter is not an ElapsedTime");
      }
    } catch (Exception e) {
      RobotLog.e("ElapsedTime.reset - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void log(Object elapsedTime, String label) {
    checkIfStopRequested();
    try {
      if (elapsedTime instanceof ElapsedTime) {
        ((ElapsedTime) elapsedTime).log(label);
      } else {
        RobotLog.e("ElapsedTime.log - parameter is not an ElapsedTime");
      }
    } catch (Exception e) {
      RobotLog.e("ElapsedTime.log - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public String toText(Object elapsedTime) {
    checkIfStopRequested();
    try {
      if (elapsedTime instanceof ElapsedTime) {
        return ((ElapsedTime) elapsedTime).toString();
      } else {
        RobotLog.e("ElapsedTime.toText - elapsedTime is not an ElapsedTime");
      }
    } catch (Exception e) {
      RobotLog.e("ElapsedTime.toText - caught " + e);
    }
    return "";
  }
}
