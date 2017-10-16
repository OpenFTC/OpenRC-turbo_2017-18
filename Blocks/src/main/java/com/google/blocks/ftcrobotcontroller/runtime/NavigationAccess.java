// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import java.util.Locale;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.UnnormalizedAngleUnit;

/**
 * A class that provides JavaScript access to various navigation enum methods.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
class NavigationAccess extends Access {

  NavigationAccess(BlocksOpMode blocksOpMode, String identifier) {
    super(blocksOpMode, identifier);
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public double angleUnit_normalize(double angle, String angleUnitString) {
    checkIfStopRequested();
    AngleUnit angleUnit = AngleUnit.valueOf(angleUnitString.toUpperCase(Locale.ENGLISH));
    return angleUnit.normalize(angle);
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public double angleUnit_convert(double angle, String fromAngleUnitString, String toAngleUnitString) {
    checkIfStopRequested();
    AngleUnit fromAngleUnit = AngleUnit.valueOf(fromAngleUnitString.toUpperCase(Locale.ENGLISH));
    AngleUnit toAngleUnit = AngleUnit.valueOf(toAngleUnitString.toUpperCase(Locale.ENGLISH));
    return toAngleUnit.fromUnit(fromAngleUnit, angle);
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public double unnormalizedAngleUnit_convert(double angle, String fromAngleUnitString, String toAngleUnitString) {
    checkIfStopRequested();
    UnnormalizedAngleUnit fromAngleUnit = AngleUnit.valueOf(fromAngleUnitString.toUpperCase(Locale.ENGLISH)).getUnnormalized();
    UnnormalizedAngleUnit toAngleUnit = AngleUnit.valueOf(toAngleUnitString.toUpperCase(Locale.ENGLISH)).getUnnormalized();
    return toAngleUnit.fromUnit(fromAngleUnit, angle);
  }
}
