// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller.util;

import android.content.res.AssetManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Set;

/**
 * A class that provides utility methods related to the toolbox.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
class ToolboxUtil {
  // Prevent instantiation of util class.
  private ToolboxUtil() {
  }

  /**
   * Appends the text content of an asset to the toolbox.
   */
  static void addAssetToToolbox(
      StringBuilder xmlToolbox, AssetManager assetManager, String assetName) throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(assetManager.open(assetName)));
    try {
      String line = null;
      while ((line = reader.readLine()) != null) {
        xmlToolbox.append(line).append("\n");
      }
    } finally {
      reader.close();
    }
  }

  /**
   * Creates a shadow number block with the given number.
   */
  static String makeNumberShadow(int n) {
    return "<shadow type=\"math_number\"><field name=\"NUM\">" + n + "</field></shadow>\n";
  }

  /**
   * Creates a shadow number block with the given number.
   */
  static String makeNumberShadow(double n) {
    return "<shadow type=\"math_number\"><field name=\"NUM\">" + n + "</field></shadow>\n";
  }

  /**
   * Creates a shadow boolean block with the given value.
   */
  static String makeBooleanShadow(boolean value) {
    String b = value ? "TRUE" : "FALSE";
    return "<shadow type=\"logic_boolean\"><field name=\"BOOL\">" + b + "</field></shadow>\n";
  }

  /**
   * Creates a shadow text block with the given value.
   */
  static String makeTextShadow(String text) {
    return "<shadow type=\"text\"><field name=\"TEXT\">" + text + "</field></shadow>\n";
  }

  /**
   * Creates a shadow enum block with the given {@link HardwareType} and enum type.
   */
  static String makeTypedEnumShadow(HardwareType hardwareType, String enumType) {
    return makeTypedEnumShadow(hardwareType.blockTypePrefix, enumType);
  }

  /**
   * Creates a shadow enum block with the given blockTypePrefix and enum type.
   */
  static String makeTypedEnumShadow(String blockTypePrefix, String enumType) {
    return "<shadow type=\"" + blockTypePrefix + "_typedEnum_" + enumType + "\">\n"
        + "</shadow>\n";
  }

  /**
   * Creates a variable get block.
   */
  static String makeVariableGetBlock(String t) {
    return "<block type=\"variables_get\"><field name=\"VAR\">{" + t + "Variable}</field></block>\n";
  }

  /**
   * Appends a property setter block to the toolbox.
   */
  private static void addPropertySetter(
      StringBuilder xmlToolbox, HardwareType hardwareType, HardwareItem hardwareItem,
      String propertyName, String propertyType, String setterValue) {
    xmlToolbox
        .append("<block type=\"").append(hardwareType.blockTypePrefix).append("_setProperty_").append(propertyType).append("\">\n")
        .append("<field name=\"IDENTIFIER\">").append(hardwareItem.identifier).append("</field>\n")
        .append("<field name=\"PROP\">").append(propertyName).append("</field>\n")
        .append("<value name=\"VALUE\">\n").append(setterValue).append("</value>\n")
        .append("</block>\n");
  }

  /**
   * Appends dual property setter blocks to the toolbox.
   */
  static void addDualPropertySetters(
      StringBuilder xmlToolbox, HardwareType hardwareType, String propertyName, String propertyType,
      HardwareItem hardwareItem1, String setterValue1,
      HardwareItem hardwareItem2, String setterValue2) {
    xmlToolbox
        .append("<block type=\"").append(hardwareType.blockTypePrefix).append("_setDualProperty_").append(propertyType).append("\">\n")
        .append("<field name=\"PROP\">").append(propertyName).append("</field>\n")
        .append("<field name=\"IDENTIFIER1\">").append(hardwareItem1.identifier).append("</field>\n")
        .append("<field name=\"IDENTIFIER2\">").append(hardwareItem2.identifier).append("</field>\n")
        .append("<value name=\"VALUE1\">\n").append(setterValue1).append("</value>\n")
        .append("<value name=\"VALUE2\">\n").append(setterValue2).append("</value>\n")
        .append("</block>\n");
  }

  /**
   * Appends a property getter block to the toolbox.
   */
  private static void addPropertyGetter(
      StringBuilder xmlToolbox, HardwareType hardwareType, HardwareItem hardwareItem,
      String propertyName, String propertyType) {
    xmlToolbox
        .append("<block type=\"").append(hardwareType.blockTypePrefix).append("_getProperty_").append(propertyType).append("\">\n")
        .append("<field name=\"IDENTIFIER\">").append(hardwareItem.identifier).append("</field>\n")
        .append("<field name=\"PROP\">").append(propertyName).append("</field>\n")
        .append("</block>\n");
  }

  /**
   * Appends the property blocks for the given {@link HardwareType} to the toolbox.
   */
  static void addProperties(
      StringBuilder xmlToolbox, HardwareType hardwareType, HardwareItem hardwareItem,
      Map<String, String> properties, Map<String, String[]> setterValues) {

    for (Map.Entry<String, String> property : properties.entrySet()) {
      String propertyName = property.getKey();
      String propertyType = property.getValue();
      if (setterValues != null && setterValues.containsKey(propertyName)) {
        for (String setterValue : setterValues.get(propertyName)) {
          addPropertySetter(
              xmlToolbox, hardwareType, hardwareItem, propertyName, propertyType, setterValue);
        }
      }
      addPropertyGetter(xmlToolbox, hardwareType, hardwareItem, propertyName, propertyType);
    }
  }

  /**
   * Appends the function blocks for the given {@link HardwareType} to the toolbox.
   */
  static void addFunctions(
      StringBuilder xmlToolbox, HardwareType hardwareType, HardwareItem hardwareItem,
      Map<String, Map<String, String>> functions) {
    for (Map.Entry<String, Map<String, String>> functionEntry : functions.entrySet()) {
      String functionName = functionEntry.getKey();
      Map<String, String> args = functionEntry.getValue();

      xmlToolbox
          .append("<block type=\"").append(hardwareType.blockTypePrefix).append("_")
          .append(functionName).append("\">\n")
          .append("<field name=\"IDENTIFIER\">").append(hardwareItem.identifier)
          .append("</field>\n");
      if (args != null) {
        for (Map.Entry<String, String> argEntry : args.entrySet()) {
          String argName = argEntry.getKey();
          String value = argEntry.getValue();
          xmlToolbox
              .append("<value name=\"" + argName + "\">\n")
              .append(value)
              .append("</value>\n");
        }
      }
      xmlToolbox
          .append("</block>\n");
    }
  }
}
