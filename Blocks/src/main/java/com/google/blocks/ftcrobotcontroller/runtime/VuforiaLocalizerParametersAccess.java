// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller.runtime;

import android.content.Context;
import android.webkit.JavascriptInterface;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.Locale;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.Parameters;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.Parameters.CameraMonitorFeedback;

/**
 * A class that provides JavaScript access to {@link VuforiaLocalizer#Parameters}.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
class VuforiaLocalizerParametersAccess extends Access {
  private final Context context;

  VuforiaLocalizerParametersAccess(BlocksOpMode blocksOpMode, String identifier, Context context) {
    super(blocksOpMode, identifier);
    this.context = context;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public Parameters create() {
    checkIfStopRequested();
    try {
      return new Parameters();
    } catch (Exception e) {
      RobotLog.e("VuforiaLocalizerParameters.create - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void setVuforiaLicenseKey(Object vuforiaLocalizerParameters, String vuforiaLicenseKey) {
    checkIfStopRequested();
    try {
      if (vuforiaLocalizerParameters instanceof Parameters) {
        ((Parameters) vuforiaLocalizerParameters).vuforiaLicenseKey = vuforiaLicenseKey;
      } else {
        RobotLog.e("VuforiaLocalizerParameters.setVuforiaLicenseKey - " +
            "vuforiaLocalizerParameters is not a VuforiaLocalizer.Parameters");
      }
    } catch (Exception e) {
      RobotLog.e("VuforiaLocalizerParameters.setVuforiaLicenseKey - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public String getVuforiaLicenseKey(Object vuforiaLocalizerParameters) {
    checkIfStopRequested();
    try {
      if (vuforiaLocalizerParameters instanceof Parameters) {
        return ((Parameters) vuforiaLocalizerParameters).vuforiaLicenseKey;
      } else {
        RobotLog.e("VuforiaLocalizerParameters.getVuforiaLicenseKey - " +
            "vuforiaLocalizerParameters is not a VuforiaLocalizer.Parameters");
      }
    } catch (Exception e) {
      RobotLog.e("VuforiaLocalizerParameters.getVuforiaLicenseKey - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void setCameraDirection(Object vuforiaLocalizerParameters, String cameraDirectionString) {
    checkIfStopRequested();
    try {
      if (vuforiaLocalizerParameters instanceof Parameters) {
        CameraDirection cameraDirection =
            CameraDirection.valueOf(cameraDirectionString.toUpperCase(Locale.ENGLISH));
        ((Parameters) vuforiaLocalizerParameters).cameraDirection = cameraDirection;
      } else {
        RobotLog.e("VuforiaLocalizerParameters.setCameraDirection - " +
            "vuforiaLocalizerParameters is not a VuforiaLocalizer.Parameters");
      }
    } catch (Exception e) {
      RobotLog.e("VuforiaLocalizerParameters.setCameraDirection - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public String getCameraDirection(Object vuforiaLocalizerParameters) {
    checkIfStopRequested();
    try {
      if (vuforiaLocalizerParameters instanceof Parameters) {
        CameraDirection cameraDirection = ((Parameters) vuforiaLocalizerParameters).cameraDirection;
        if (cameraDirection != null) {
          return cameraDirection.toString();
        }
      } else {
        RobotLog.e("VuforiaLocalizerParameters.getCameraDirection - " +
            "vuforiaLocalizerParameters is not a VuforiaLocalizer.Parameters");
      }
    } catch (Exception e) {
      RobotLog.e("VuforiaLocalizerParameters.getCameraDirection - caught " + e);
    }
    return "";
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void setUseExtendedTracking(
      Object vuforiaLocalizerParameters, boolean useExtendedTracking) {
    checkIfStopRequested();
    try {
      if (vuforiaLocalizerParameters instanceof Parameters) {
        ((Parameters) vuforiaLocalizerParameters).useExtendedTracking = useExtendedTracking;
      } else {
        RobotLog.e("VuforiaLocalizerParameters.setUseExtendedTracking - " +
            "vuforiaLocalizerParameters is not a VuforiaLocalizer.Parameters");
      }
    } catch (Exception e) {
      RobotLog.e("VuforiaLocalizerParameters.setUseExtendedTracking - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public boolean getUseExtendedTracking(Object vuforiaLocalizerParameters) {
    checkIfStopRequested();
    try {
      if (vuforiaLocalizerParameters instanceof Parameters) {
        return ((Parameters) vuforiaLocalizerParameters).useExtendedTracking;
      } else {
        RobotLog.e("VuforiaLocalizerParameters.getUseExtendedTracking - " +
            "vuforiaLocalizerParameters is not a VuforiaLocalizer.Parameters");
      }
    } catch (Exception e) {
      RobotLog.e("VuforiaLocalizerParameters.getUseExtendedTracking - caught " + e);
    }
    return false;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public boolean getEnableCameraMonitoring(Object vuforiaLocalizerParameters) {
    checkIfStopRequested();
    try {
      if (vuforiaLocalizerParameters instanceof Parameters) {
        return ((Parameters) vuforiaLocalizerParameters).cameraMonitorViewIdParent != 0;
      } else {
        RobotLog.e("VuforiaLocalizerParameters.getEnableCameraMonitoring - " +
            "vuforiaLocalizerParameters is not a VuforiaLocalizer.Parameters");
      }
    } catch (Exception e) {
      RobotLog.e("VuforiaLocalizerParameters.getEnableCameraMonitoring - caught " + e);
    }
    return false;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void setCameraMonitorFeedback(
      Object vuforiaLocalizerParameters, String cameraMonitorFeedbackString) {
    checkIfStopRequested();
    try {
      if (vuforiaLocalizerParameters instanceof Parameters) {
        cameraMonitorFeedbackString = cameraMonitorFeedbackString.toUpperCase(Locale.ENGLISH);
        CameraMonitorFeedback cameraMonitorFeedback = cameraMonitorFeedbackString.equals("DEFAULT")
            ? null : CameraMonitorFeedback.valueOf(cameraMonitorFeedbackString);
        ((Parameters) vuforiaLocalizerParameters).cameraMonitorFeedback = cameraMonitorFeedback;
      } else {
        RobotLog.e("VuforiaLocalizerParameters.setCameraMonitorFeedback - " +
            "vuforiaLocalizerParameters is not a VuforiaLocalizer.Parameters");
      }
    } catch (Exception e) {
      RobotLog.e("VuforiaLocalizerParameters.setCameraMonitorFeedback - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public String getCameraMonitorFeedback(Object vuforiaLocalizerParameters) {
    checkIfStopRequested();
    try {
      if (vuforiaLocalizerParameters instanceof Parameters) {
        CameraMonitorFeedback cameraMonitorFeedback =
            ((Parameters) vuforiaLocalizerParameters).cameraMonitorFeedback;
        return (cameraMonitorFeedback == null) ? "DEFAULT" : cameraMonitorFeedback.toString();
      } else {
        RobotLog.e("VuforiaLocalizerParameters.getCameraMonitorFeedback - " +
            "vuforiaLocalizerParameters is not a VuforiaLocalizer.Parameters");
      }
    } catch (Exception e) {
      RobotLog.e("VuforiaLocalizerParameters.getCameraMonitorFeedback - caught " + e);
    }
    return "";
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void setFillCameraMonitorViewParent(
      Object vuforiaLocalizerParameters, boolean fillCameraMonitorViewParent) {
    checkIfStopRequested();
    try {
      if (vuforiaLocalizerParameters instanceof Parameters) {
        ((Parameters) vuforiaLocalizerParameters).fillCameraMonitorViewParent =
            fillCameraMonitorViewParent;
      } else {
        RobotLog.e("VuforiaLocalizerParameters.setFillCameraMonitorViewParent - " +
            "vuforiaLocalizerParameters is not a VuforiaLocalizer.Parameters");
      }
    } catch (Exception e) {
      RobotLog.e("VuforiaLocalizerParameters.setFillCameraMonitorViewParent - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public boolean getFillCameraMonitorViewParent(Object vuforiaLocalizerParameters) {
    checkIfStopRequested();
    try {
      if (vuforiaLocalizerParameters instanceof Parameters) {
        return ((Parameters) vuforiaLocalizerParameters).fillCameraMonitorViewParent;
      } else {
        RobotLog.e("VuforiaLocalizerParameters.getFillCameraMonitorViewParent - " +
            "vuforiaLocalizerParameters is not a VuforiaLocalizer.Parameters");
      }
    } catch (Exception e) {
      RobotLog.e("VuforiaLocalizerParameters.getFillCameraMonitorViewParent - caught " + e);
    }
    return false;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void setEnableCameraMonitoring(
      Object vuforiaLocalizerParameters, boolean enableCameraMonitoring) {
    checkIfStopRequested();
    try {
      if (vuforiaLocalizerParameters instanceof Parameters) {
        if (enableCameraMonitoring) {
          ((Parameters) vuforiaLocalizerParameters).cameraMonitorViewIdParent =
              context.getResources().getIdentifier(
                  "cameraMonitorViewId", "id", context.getPackageName());
        } else {
          ((Parameters) vuforiaLocalizerParameters).cameraMonitorViewIdParent = 0;
        }
      } else {
        RobotLog.e("VuforiaLocalizerParameters.setEnableCameraMonitoring - " +
            "vuforiaLocalizerParameters is not a VuforiaLocalizer.Parameters");
      }
    } catch (Exception e) {
      RobotLog.e("VuforiaLocalizerParameters.setEnableCameraMonitoring - caught " + e);
    }
  }
}
