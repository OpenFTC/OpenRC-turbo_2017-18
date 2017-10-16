// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.Locale;
import org.firstinspires.ftc.robotcore.external.navigation.Quaternion;

/**
 * A class that provides JavaScript access to {@link Quaternion}.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
class QuaternionAccess extends Access {

  QuaternionAccess(BlocksOpMode blocksOpMode, String identifier) {
    super(blocksOpMode, identifier);
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public float getW(Object quaternion) {
    checkIfStopRequested();
    try {
      if (quaternion instanceof Quaternion) {
        return ((Quaternion) quaternion).w;
      } else {
        RobotLog.e("Quaternion.getW - quaternion is not a Quaternion");
      }
    } catch (Exception e) {
      RobotLog.e("Quaternion.getW - caught " + e);
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public float getX(Object quaternion) {
    checkIfStopRequested();
    try {
      if (quaternion instanceof Quaternion) {
        return ((Quaternion) quaternion).x;
      } else {
        RobotLog.e("Quaternion.getX - quaternion is not a Quaternion");
      }
    } catch (Exception e) {
      RobotLog.e("Quaternion.getX - caught " + e);
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public float getY(Object quaternion) {
    checkIfStopRequested();
    try {
      if (quaternion instanceof Quaternion) {
        return ((Quaternion) quaternion).y;
      } else {
        RobotLog.e("Quaternion.getY - quaternion is not a Quaternion");
      }
    } catch (Exception e) {
      RobotLog.e("Quaternion.getY - caught " + e);
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public float getZ(Object quaternion) {
    checkIfStopRequested();
    try {
      if (quaternion instanceof Quaternion) {
        return ((Quaternion) quaternion).z;
      } else {
        RobotLog.e("Quaternion.getZ - quaternion is not a Quaternion");
      }
    } catch (Exception e) {
      RobotLog.e("Quaternion.getZ - caught " + e);
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public long getAcquisitionTime(Object quaternion) {
    checkIfStopRequested();
    try {
      if (quaternion instanceof Quaternion) {
        return ((Quaternion) quaternion).acquisitionTime;
      } else {
        RobotLog.e("Quaternion.getAcquisitionTime - quaternion is not a Quaternion");
      }
    } catch (Exception e) {
      RobotLog.e("Quaternion.getAcquisitionTime - caught " + e);
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public float getMagnitude(Object quaternion) {
    checkIfStopRequested();
    try {
      if (quaternion instanceof Quaternion) {
        return ((Quaternion) quaternion).magnitude();
      } else {
        RobotLog.e("Quaternion.getMagnitude - quaternion is not a Quaternion");
      }
    } catch (Exception e) {
      RobotLog.e("Quaternion.getMagnitude - caught " + e);
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public Quaternion create() {
    checkIfStopRequested();
    try {
      return new Quaternion();
    } catch (Exception e) {
      RobotLog.e("Quaternion.create - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public Quaternion create_withArgs(float w, float x, float y, float z, long acquisitionTime) {
    checkIfStopRequested();
    try {
      return new Quaternion(w, x, y, z, acquisitionTime);
    } catch (Exception e) {
      RobotLog.e("Quaternion.create - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public Quaternion normalized(Object quaternion) {
    checkIfStopRequested();
    try {
      if (quaternion instanceof Quaternion) {
        return ((Quaternion) quaternion).normalized();
      } else {
        RobotLog.e("Quaternion.normalized - quaternion is not a Quaternion");
      }
    } catch (Exception e) {
      RobotLog.e("Quaternion.normalized - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public Quaternion congugate(Object quaternion) {
    checkIfStopRequested();
    try {
      if (quaternion instanceof Quaternion) {
        return ((Quaternion) quaternion).congugate();
      } else {
        RobotLog.e("Quaternion.congugate - quaternion is not a Quaternion");
      }
    } catch (Exception e) {
      RobotLog.e("Quaternion.congugate - caught " + e);
    }
    return null;
  }
}
