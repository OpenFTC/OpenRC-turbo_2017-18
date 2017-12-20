// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller.runtime;

import android.content.Context;
import android.webkit.JavascriptInterface;

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
        super(blocksOpMode, identifier, "VuforiaLocalizer.Parameters");
        this.context = context;
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    public Parameters create() {
        startBlockExecution(BlockType.CREATE, "");
        return new Parameters();
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    public void setVuforiaLicenseKey(Object parametersArg, String vuforiaLicenseKey) {
        startBlockExecution(BlockType.FUNCTION, ".setVuforiaLicenseKey");
        Parameters parameters = checkVuforiaLocalizerParameters(parametersArg);
        if (parameters != null) {
            parameters.vuforiaLicenseKey = vuforiaLicenseKey;
        }
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    public String getVuforiaLicenseKey(Object parametersArg) {
        startBlockExecution(BlockType.GETTER, ".VuforiaLicenseKey");
        Parameters parameters = checkVuforiaLocalizerParameters(parametersArg);
        if (parameters != null) {
            return parameters.vuforiaLicenseKey;
        }
        return null;
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    public void setCameraDirection(Object parametersArg, String cameraDirectionString) {
        startBlockExecution(BlockType.FUNCTION, ".setCameraDirection");
        Parameters parameters = checkVuforiaLocalizerParameters(parametersArg);
        CameraDirection cameraDirection = checkVuforiaLocalizerCameraDirection(cameraDirectionString);
        if (parameters != null || cameraDirection != null) {
            parameters.cameraDirection = cameraDirection;
        }
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    public String getCameraDirection(Object parametersArg) {
        startBlockExecution(BlockType.GETTER, ".CameraDirection");
        Parameters parameters = checkVuforiaLocalizerParameters(parametersArg);
        if (parameters != null) {
            CameraDirection cameraDirection = parameters.cameraDirection;
            if (cameraDirection != null) {
                return cameraDirection.toString();
            }
        }
        return "";
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    public void setUseExtendedTracking(Object parametersArg, boolean useExtendedTracking) {
        startBlockExecution(BlockType.FUNCTION, ".setUseExtendedTracking");
        Parameters parameters = checkVuforiaLocalizerParameters(parametersArg);
        if (parameters != null) {
            parameters.useExtendedTracking = useExtendedTracking;
        }
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    public boolean getUseExtendedTracking(Object parametersArg) {
        startBlockExecution(BlockType.GETTER, ".UseExtendedTracking");
        Parameters parameters = checkVuforiaLocalizerParameters(parametersArg);
        if (parameters != null) {
            return parameters.useExtendedTracking;
        }
        return false;
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    public boolean getEnableCameraMonitoring(Object parametersArg) {
        startBlockExecution(BlockType.GETTER, ".EnableCameraMonitoring");
        Parameters parameters = checkVuforiaLocalizerParameters(parametersArg);
        if (parameters != null) {
            return parameters.cameraMonitorViewIdParent != 0;
        }
        return false;
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    public void setCameraMonitorFeedback(Object parametersArg, String cameraMonitorFeedbackString) {
        startBlockExecution(BlockType.FUNCTION, ".setCameraMonitorFeedback");
        Parameters parameters = checkVuforiaLocalizerParameters(parametersArg);
        boolean cameraMonitorFeedbackIsValid;
        CameraMonitorFeedback cameraMonitorFeedback;
        if (cameraMonitorFeedbackString.equalsIgnoreCase("DEFAULT")) {
            cameraMonitorFeedback = null;
            cameraMonitorFeedbackIsValid = true;
        } else {
            cameraMonitorFeedback = checkArg(cameraMonitorFeedbackString, CameraMonitorFeedback.class, "cameraMonitorFeedback");
            cameraMonitorFeedbackIsValid = cameraMonitorFeedback != null;
        }
        if (parameters != null && cameraMonitorFeedbackIsValid) {
            parameters.cameraMonitorFeedback = cameraMonitorFeedback;
        }
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    public String getCameraMonitorFeedback(Object parametersArg) {
        startBlockExecution(BlockType.GETTER, ".CameraMonitorFeedback");
        Parameters parameters = checkVuforiaLocalizerParameters(parametersArg);
        if (parameters != null) {
            CameraMonitorFeedback cameraMonitorFeedback = parameters.cameraMonitorFeedback;
            return (cameraMonitorFeedback == null) ? "DEFAULT" : cameraMonitorFeedback.toString();
        }
        return "";
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    public void setFillCameraMonitorViewParent(Object parametersArg, boolean fillCameraMonitorViewParent) {
        startBlockExecution(BlockType.FUNCTION, ".setFillCameraMonitorViewParent");
        Parameters parameters = checkVuforiaLocalizerParameters(parametersArg);
        if (parameters != null) {
            parameters.fillCameraMonitorViewParent = fillCameraMonitorViewParent;
        }
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    public boolean getFillCameraMonitorViewParent(Object parametersArg) {
        startBlockExecution(BlockType.GETTER, ".FillCameraMonitorViewParent");
        Parameters parameters = checkVuforiaLocalizerParameters(parametersArg);
        if (parameters != null) {
            return parameters.fillCameraMonitorViewParent;
        }
        return false;
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    public void setEnableCameraMonitoring(Object parametersArg, boolean enableCameraMonitoring) {
        startBlockExecution(BlockType.FUNCTION, ".setEnableCameraMonitoring");
        Parameters parameters = checkVuforiaLocalizerParameters(parametersArg);
        if (parameters != null) {
            if (enableCameraMonitoring) {
                parameters.cameraMonitorViewIdParent = context.getResources().getIdentifier(
                        "cameraMonitorViewId", "id", context.getPackageName());
            } else {
                parameters.cameraMonitorViewIdParent = 0;
            }
        }
    }
}
