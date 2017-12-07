package com.google.blocks.ftcrobotcontroller.runtime;

import android.app.Activity;
import android.webkit.WebView;

import org.openftc.turbo.TurboException;

/**
 * This is a dummy class, used in the OpenFTC Turbo build variant
 */

public class BlocksOpMode {
    public static void setActivityAndWebView(Activity activity, WebView webview) {
        throw new TurboException();
    }
}
