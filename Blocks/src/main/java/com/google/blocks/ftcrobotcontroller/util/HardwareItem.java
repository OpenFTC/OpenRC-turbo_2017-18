// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller.util;

import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.HashMap;
import java.util.Map;

/**
 * A class that identifies a single hardware item.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
public class HardwareItem {
  /**
   * The HardwareType of this hardware item.
   */
  public final HardwareType hardwareType;
  /**
   * The deviceName is the value of the name attribute in the configuration xml file and can be
   * used to get a {@link HardwareDevice} from the {@link HardwareMap}.
   */
  public final String deviceName;
  /**
   * The identifier used in generated javascript.
   */
  public final String identifier;
  /**
   * The name shown on the blocks.
   */
  public final String visibleName;

  /**
   * Constructs a {@link HardwareItem} with the given {@link HardwareType} and device name.
   */
  public HardwareItem(HardwareType hardwareType, String deviceName) {
    if (hardwareType == null || deviceName == null) {
      throw new NullPointerException();
    }
    this.hardwareType = hardwareType;
    this.deviceName = deviceName;
    identifier = makeIdentifier(hardwareType, deviceName);
    visibleName = makeVisibleName(deviceName);
  }

  /**
   * Generates the valid Java identifier for the hardware item.
   */
  private static String makeIdentifier(HardwareType hardwareType, String deviceName) {
    int length = deviceName.length();
    StringBuilder identifier = new StringBuilder();

    char ch = deviceName.charAt(0);
    if (Character.isJavaIdentifierStart(ch)) {
      identifier.append(ch);
    } else if (Character.isJavaIdentifierPart(ch)) {
      identifier.append('_').append(ch);
    }
    for (int i = 1; i < length; i++) {
      ch = deviceName.charAt(i);
      if (Character.isJavaIdentifierPart(ch)) {
        identifier.append(ch);
      }
    }

    identifier.append(hardwareType.identifierSuffix);
    return identifier.toString();
  }

  /**
   * Generates the visible name for the hardware item.
   */
  private static String makeVisibleName(String deviceName) {
    int length = deviceName.length();
    StringBuilder visibleName = new StringBuilder();

    for (int i = 0; i < length; i++) {
      char ch = deviceName.charAt(i);
      if (ch == ' ') {
        visibleName.append('\u00A0');
      } else {
        visibleName.append(ch);
      }
    }
    return visibleName.toString();
  }

  // java.lang.Object methods

  @Override
  public boolean equals(Object o) {
    if (o instanceof HardwareItem) {
      HardwareItem that = (HardwareItem) o;
      return this.hardwareType.equals(that.hardwareType)
          && this.deviceName.equals(that.deviceName)
          && this.identifier.equals(that.identifier)
          && this.visibleName.equals(that.visibleName);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return hardwareType.hashCode()
        + deviceName.hashCode()
        + identifier.hashCode()
        + visibleName.hashCode();
  }
}
