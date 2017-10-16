// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller.runtime;

import android.graphics.Color;
import android.webkit.JavascriptInterface;

/**
 * A class that provides JavaScript access to {@link Color}.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
class ColorAccess extends Access {

  ColorAccess(BlocksOpMode blocksOpMode, String identifier) {
    super(blocksOpMode, identifier);
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public double getRed(int color) {
    checkIfStopRequested();
    return Color.red(color);
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public double getGreen(int color) {
    checkIfStopRequested();
    return Color.green(color);
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public double getBlue(int color) {
    checkIfStopRequested();
    return Color.blue(color);
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public double getAlpha(int color) {
    checkIfStopRequested();
    return Color.alpha(color);
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public float getHue(int color) {
    checkIfStopRequested();
    float[] array = new float[3];
    Color.colorToHSV(color, array);
    return array[0];
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public float getSaturation(int color) {
    checkIfStopRequested();
    float[] array = new float[3];
    Color.colorToHSV(color, array);
    return array[1];
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public float getValue(int color) {
    checkIfStopRequested();
    float[] array = new float[3];
    Color.colorToHSV(color, array);
    return array[2];
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public int rgbToColor(int red, int green, int blue) {
    checkIfStopRequested();
    return Color.rgb(red, green, blue);
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public int argbToColor(int alpha, int red, int green, int blue) {
    checkIfStopRequested();
    return Color.argb(alpha, red, green, blue);
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public int hsvToColor(float hue, float saturation, float value) {
    checkIfStopRequested();
    float[] array = new float[3];
    array[0] = hue;
    array[1] = saturation;
    array[2] = value;
    return Color.HSVToColor(array);
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public int ahsvToColor(int alpha, float hue, float saturation, float value) {
    checkIfStopRequested();
    float[] array = new float[3];
    array[0] = hue;
    array[1] = saturation;
    array[2] = value;
    return Color.HSVToColor(alpha, array);
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public int textToColor(String text) {
    checkIfStopRequested();
    return Color.parseColor(text);
  }
}
