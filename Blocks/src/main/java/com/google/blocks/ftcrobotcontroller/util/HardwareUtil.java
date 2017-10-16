// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * A class that provides utility methods related to hardware.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
public class HardwareUtil {
  private static final String ELAPSED_TIME_DEFAULT_VAR_NAME = "timer";

  private static final String DC_MOTOR_DUAL_CATEGORY_NAME = "Dual";
  private static final String GAMEPAD_CATEGORY_NAME = "Gamepad"; // see toolbox/gamepad.xml
  private static final String LINEAR_OP_MODE_CATEGORY_NAME = "LinearOpMode"; // see toolbox/linear_op_mode.xml
  private static final String COLOR_CATEGORY_NAME = "Color"; // see toolbox/color.xml
  private static final String ELAPSED_TIME_CATEGORY_NAME = "ElapsedTime"; // see toolbox/elapsed_time.xml

  /**
   * A {@link Map} from xmlTag to List of {@link HardwareType}.
   */
  private static final Map<String, List<HardwareType>> XML_TAG_TO_HARDWARE_TYPES =
      new HashMap<String, List<HardwareType>>();
  static {
    for (HardwareType hardwareType : HardwareType.values()) {
      for (String xmlTag : hardwareType.xmlTags) {
        List<HardwareType> list = (List<HardwareType>) XML_TAG_TO_HARDWARE_TYPES.get(xmlTag);
        if (list == null) {
          list = new ArrayList<HardwareType>();
          XML_TAG_TO_HARDWARE_TYPES.put(xmlTag, list);
        }
        list.add(hardwareType);
      }
    }
  }

  // Prevent instantiation of util class.
  private HardwareUtil() {
  }

  /**
   * Returns the corresponding {@link HardwareType}s for the given XML tag.
   */
  // visible for testing
  static Iterable<HardwareType> getHardwareTypes(String xmlTag) {
    return XML_TAG_TO_HARDWARE_TYPES.containsKey(xmlTag)
        ? Collections.<HardwareType>unmodifiableList(XML_TAG_TO_HARDWARE_TYPES.get(xmlTag))
        : Collections.<HardwareType>emptyList();
  }

  /**
   * Returns the corresponding {@link HardwareType}s for the given {@link DeviceConfiguration}.
   */
  static Iterable<HardwareType> getHardwareTypes(DeviceConfiguration deviceConfiguration) {
    return getHardwareTypes(deviceConfiguration.getConfigurationType().getXmlTag());
  }

  /**
   * Returns the JavaScript code related to the hardware in the active configuration.
   */
  public static String fetchJavaScriptForHardware() {
    return fetchJavaScriptForHardware(HardwareItemMap.newHardwareItemMap());
  }

  /**
   * Returns the JavaScript code related to the hardware in the given {@link HardwareItem}.
   */
  // visible for testing
  static String fetchJavaScriptForHardware(HardwareItemMap hardwareItemMap) {
    StringBuilder jsHardware = new StringBuilder().append("\n");

    jsHardware
        .append("function isValidProjectName(projectName) {\n")
        .append("  if (projectName) {\n")
        .append("    return /").append(ProjectsUtil.VALID_PROJECT_REGEX).append("/.test(projectName);\n")
        .append("  }\n")
        .append("  return false;\n")
        .append("}\n\n");

    jsHardware
        .append("function isValidSoundName(soundName) {\n")
        .append("  if (soundName) {\n")
        .append("    return /").append(SoundsUtil.VALID_SOUND_REGEX).append("/.test(soundName);\n")
        .append("  }\n")
        .append("  return false;\n")
        .append("}\n\n");

    for (Identifier identifier : Identifier.values()) {
      if (identifier.blocksVariable != null) {
        jsHardware
            .append("var ").append(identifier.blocksVariable).append(" = '")
            .append(identifier.identifier).append("';\n");
      }
    }

    // Gamepad
    jsHardware
        .append("function createGamepadDropdown() {\n")
        .append("  var CHOICES = [\n")
        .append("      ['gamepad1', '").append(Identifier.GAMEPAD_1.identifier).append("'],\n")
        .append("      ['gamepad2', '").append(Identifier.GAMEPAD_2.identifier).append("'],\n")
        .append("  ];\n")
        .append("  return createFieldDropdown(CHOICES);\n")
        .append("}\n");

    // hardware
    for (HardwareType hardwareType : HardwareType.values()) {
      jsHardware
          .append("function ").append(hardwareType.createDropdownFunctionName).append("() {\n")
          .append("  var CHOICES = [\n");
      if (hardwareItemMap.contains(hardwareType)) {
        for (HardwareItem hardwareItem : hardwareItemMap.getHardwareItems(hardwareType)) {
          jsHardware
              .append("      ['").append(hardwareItem.visibleName).append("', '")
              .append(hardwareItem.identifier).append("'],\n");
        }
      }
      jsHardware.append("  ];\n")
          .append("  return createFieldDropdown(CHOICES);\n")
          .append("}\n");
    }

    jsHardware.append("function addReservedWordsForHardware() {\n");
    for (HardwareItem hardwareItem : hardwareItemMap.getAllHardwareItems()) {
      jsHardware.append("  Blockly.JavaScript.addReservedWords('").append(hardwareItem.identifier)
          .append("');\n");
    }
    for (Identifier identifier : Identifier.values()) {
      jsHardware.append("  Blockly.JavaScript.addReservedWords('")
          .append(identifier.identifier).append("');\n");
    }
    jsHardware.append("}\n");

    jsHardware.append("function getIconClass(categoryName) {\n");
    for (HardwareType hardwareType : HardwareType.values()) {
      jsHardware.append("  if (categoryName == \"").append(hardwareType.toolboxCategoryName).append("\") {\n")
          .append("    return \"").append(hardwareType.toolboxIconClass).append("\";\n")
          .append("  }\n");
    }
    jsHardware.append("  if (categoryName == \"").append(DC_MOTOR_DUAL_CATEGORY_NAME).append("\") {\n")
        .append("    return \"").append("DcMotor-icon").append("\";\n")
        .append("  }\n");
    jsHardware.append("  if (categoryName == \"").append(GAMEPAD_CATEGORY_NAME).append("\") {\n")
        .append("    return \"").append("Gamepad-icon").append("\";\n")
        .append("  }\n");
    jsHardware.append("  if (categoryName == \"").append(LINEAR_OP_MODE_CATEGORY_NAME).append("\") {\n")
        .append("    return \"").append("LinearOpMode-icon").append("\";\n")
        .append("  }\n");
    jsHardware.append("  if (categoryName == \"").append(COLOR_CATEGORY_NAME).append("\") {\n")
        .append("    return \"").append("ColorSensor-icon").append("\";\n")
        .append("  }\n");
    jsHardware.append("  if (categoryName == \"").append(ELAPSED_TIME_CATEGORY_NAME).append("\") {\n")
        .append("    return \"").append("ElapsedTime-icon").append("\";\n")
        .append("  }\n");
    jsHardware.append("  return \"\";\n")
        .append("}\n");

    return jsHardware.toString();
  }

  /**
   * Returns the toolbox xml, with some categories related to the hardware in the active
   * configuration.
   */
  public static String fetchToolbox(AssetManager assetManager)
      throws IOException {
    return fetchToolbox(HardwareItemMap.newHardwareItemMap(), assetManager);
  }

  /**
   * Fetches the toolbox for the blocks editor, excluding the categories for {@link HardwareType}s
   * that do not exist in the given {@link HardwareItemMap}.
   */
  // visible for testing
  static String fetchToolbox(
      HardwareItemMap hardwareItemMap, AssetManager assetManager)
      throws IOException {
    StringBuilder xmlToolbox = new StringBuilder();
    xmlToolbox.append("<xml id=\"toolbox\" style=\"display: none\">\n");

    // assetManager can be null during tests.
    if (assetManager != null) {
      ToolboxUtil.addAssetToToolbox(xmlToolbox, assetManager, "toolbox/linear_op_mode.xml");
      ToolboxUtil.addAssetToToolbox(xmlToolbox, assetManager, "toolbox/gamepad.xml");
    }

    for (ToolboxFolder toolboxFolder : ToolboxFolder.values()) {
      xmlToolbox.append(" <category name=\"").append(toolboxFolder.label)
          .append("\">\n");
      for (HardwareType hardwareType : hardwareItemMap.getHardwareTypes()) {
        if (hardwareType.toolboxFolder == toolboxFolder) {
          addHardwareCategoryToToolbox(
              xmlToolbox, hardwareType, hardwareItemMap.getHardwareItems(hardwareType));
          if (hardwareType == HardwareType.BNO055IMU) {
            if (assetManager != null) {
              ToolboxUtil.addAssetToToolbox(
                  xmlToolbox, assetManager, "toolbox/bno055imu_parameters.xml");
            }
          }
        }
      }
      xmlToolbox.append(" </category>\n");
    }

    addAndroidCategoriesToToolbox(xmlToolbox, assetManager);

    xmlToolbox.append(" <category name=\"Utilities\">\n");
    if (assetManager != null) {
      ToolboxUtil.addAssetToToolbox(xmlToolbox, assetManager, "toolbox/telemetry.xml");
      ToolboxUtil.addAssetToToolbox(xmlToolbox, assetManager, "toolbox/elapsed_time.xml");
      ToolboxUtil.addAssetToToolbox(xmlToolbox, assetManager, "toolbox/color.xml");
      ToolboxUtil.addAssetToToolbox(xmlToolbox, assetManager, "toolbox/dbg_log.xml");
      ToolboxUtil.addAssetToToolbox(xmlToolbox, assetManager, "toolbox/range.xml");
      xmlToolbox.append("<sep></sep>\n");
      ToolboxUtil.addAssetToToolbox(xmlToolbox, assetManager, "toolbox/navigation.xml");
    }
    xmlToolbox.append(" </category>\n");

    if (assetManager != null) {
      ToolboxUtil.addAssetToToolbox(xmlToolbox, assetManager, "toolbox/misc.xml");
    }

    xmlToolbox.append("</xml>\n");
    return xmlToolbox.toString();
  }

  /**
   * Adds the category for Android functionality to the toolbox, iff there is at least one
   * sub-category supported by the Android device.
   */
  private static void addAndroidCategoriesToToolbox(
      StringBuilder xmlToolbox, AssetManager assetManager)
      throws IOException {
    SensorManager sensorManager = (SensorManager) AppUtil.getDefContext().getSystemService(Context.SENSOR_SERVICE);
    boolean hasAccelerometer = !sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).isEmpty();
    boolean hasGyroscope = !sensorManager.getSensorList(Sensor.TYPE_GYROSCOPE).isEmpty();
    boolean hasMagneticField = !sensorManager.getSensorList(Sensor.TYPE_MAGNETIC_FIELD).isEmpty();

    StringBuilder sb = new StringBuilder();
    boolean empty = true;

    if (hasAccelerometer) {
      if (assetManager != null) {
        ToolboxUtil.addAssetToToolbox(sb, assetManager, "toolbox/android_accelerometer.xml");
        empty = false;
      }
    }
    if (hasGyroscope) {
      if (assetManager != null) {
        ToolboxUtil.addAssetToToolbox(sb, assetManager, "toolbox/android_gyroscope.xml");
        empty = false;
      }
    }
    if (hasAccelerometer && hasMagneticField) {
      if (assetManager != null) {
        ToolboxUtil.addAssetToToolbox(sb, assetManager, "toolbox/android_orientation.xml");
        empty = false;
      }
    }
    if (assetManager != null) {
      ToolboxUtil.addAssetToToolbox(sb, assetManager, "toolbox/android_sound_pool.xml");
      ToolboxUtil.addAssetToToolbox(sb, assetManager, "toolbox/android_text_to_speech.xml");
      empty = false;
    }

    if (!empty) {
      xmlToolbox.append("<category name=\"Android\">\n")
          .append(sb)
          .append("</category>\n");
    }
  }

  /**
   * Adds the category for the given {@link HardwareType} to the toolbox, iff there is at least one
   * {@link HardwareItem} of that HardwareType.
   */
  private static void addHardwareCategoryToToolbox(
      StringBuilder xmlToolbox, HardwareType hardwareType,
      List<HardwareItem> hardwareItems) {
    if (hardwareItems != null && hardwareItems.size() > 0) {
      xmlToolbox.append("  <category name=\"").append(hardwareType.toolboxCategoryName)
          .append("\">\n");

      switch (hardwareType) {
        case ACCELERATION_SENSOR:
          addAccelerationSensorCategoryToToolbox(xmlToolbox, hardwareType, hardwareItems);
          break;
        case ANALOG_INPUT:
          addAnalogInputCategoryToToolbox(xmlToolbox, hardwareType, hardwareItems);
          break;
        case ANALOG_OUTPUT:
          addAnalogOutputCategoryToToolbox(xmlToolbox, hardwareType, hardwareItems);
          break;
        case BNO055IMU:
          addBNO055IMUCategoryToToolbox(xmlToolbox, hardwareType, hardwareItems);
          break;
        case COLOR_SENSOR:
          addColorSensorCategoryToToolbox(xmlToolbox, hardwareType, hardwareItems);
          break;
        case COMPASS_SENSOR:
          addCompassSensorCategoryToToolbox(xmlToolbox, hardwareType, hardwareItems);
          break;
        case CR_SERVO:
          addCRServoCategoryToToolbox(xmlToolbox, hardwareType, hardwareItems);
          break;
        case DC_MOTOR:
          addDcMotorCategoryToToolbox(xmlToolbox, hardwareType, hardwareItems);
          break;
        case DIGITAL_CHANNEL:
          addDigitalChannelCategoryToToolbox(xmlToolbox, hardwareType, hardwareItems);
          break;
        case GYRO_SENSOR:
          addGyroSensorCategoryToToolbox(xmlToolbox, hardwareType, hardwareItems);
          break;
        case IR_SEEKER_SENSOR:
          addIrSeekerSensorCategoryToToolbox(xmlToolbox, hardwareType, hardwareItems);
          break;
        case LED:
          addLedCategoryToToolbox(xmlToolbox, hardwareType, hardwareItems);
          break;
        case LIGHT_SENSOR:
          addLightSensorCategoryToToolbox(xmlToolbox, hardwareType, hardwareItems);
          break;
        case LYNX_I2C_COLOR_RANGE_SENSOR:
          addLynxI2cColorRangeSensorCategoryToToolbox(xmlToolbox, hardwareType, hardwareItems);
          break;
        case MR_I2C_COMPASS_SENSOR:
          addMrI2cCompassSensorCategoryToToolbox(xmlToolbox, hardwareType, hardwareItems);;
          break;
        case MR_I2C_RANGE_SENSOR:
          addMrI2cRangeSensorCategoryToToolbox(xmlToolbox, hardwareType, hardwareItems);;
          break;
        case OPTICAL_DISTANCE_SENSOR:
          addOpticalDistanceSensorCategoryToToolbox(xmlToolbox, hardwareType, hardwareItems);
          break;
        case SERVO:
          addServoCategoryToToolbox(xmlToolbox, hardwareType, hardwareItems);
          break;
        case SERVO_CONTROLLER:
          addServoControllerCategoryToToolbox(xmlToolbox, hardwareType, hardwareItems);
          break;
        case TOUCH_SENSOR:
          addTouchSensorCategoryToToolbox(xmlToolbox, hardwareType, hardwareItems);
          break;
        case ULTRASONIC_SENSOR:
          addUltrasonicSensorCategoryToToolbox(xmlToolbox, hardwareType, hardwareItems);
          break;
        case VOLTAGE_SENSOR:
          addVoltageSensorCategoryToToolbox(xmlToolbox, hardwareType, hardwareItems);
          break;
        default:
          throw new IllegalArgumentException("Unknown hardware type " + hardwareType);
      }

      xmlToolbox.append("  </category>\n");
    }
  }

  /**
   * Adds the category for acceleration sensor to the toolbox.
   */
  private static void addAccelerationSensorCategoryToToolbox(
      StringBuilder xmlToolbox, HardwareType hardwareType, List<HardwareItem> hardwareItems) {
    HardwareItem hardwareItem = hardwareItems.get(0);

    // Properties
    SortedMap<String, String> properties = new TreeMap<String, String>();
    properties.put("Acceleration", "Acceleration");
    properties.put("XAccel", "Number");
    properties.put("YAccel", "Number");
    properties.put("ZAccel", "Number");
    ToolboxUtil.addProperties(xmlToolbox, hardwareType, hardwareItem, properties,
        null /* setterValues */);

    // Functions
    Map<String, Map<String, String>> functions = new TreeMap<String, Map<String, String>>();
    functions.put("toText", null);
    ToolboxUtil.addFunctions(xmlToolbox, hardwareType, hardwareItem, functions);
  }

  /**
   * Adds the category for BNO055IMU to the toolbox.
   */
  private static void addBNO055IMUCategoryToToolbox(
      StringBuilder xmlToolbox, HardwareType hardwareType, List<HardwareItem> hardwareItems) {
    HardwareItem hardwareItem = hardwareItems.get(0);

    // Properties
    SortedMap<String, String> properties = new TreeMap<String, String>();
    properties.put("Acceleration", "Acceleration");
    properties.put("AngularOrientation", "Orientation");
    properties.put("AngularOrientationAxes", "Array");
    properties.put("AngularVelocity", "AngularVelocity");
    properties.put("AngularVelocityAxes", "Array");
    properties.put("CalibrationStatus", "String");
    properties.put("Gravity", "Acceleration");
    properties.put("I2cAddress7Bit", "Number");
    properties.put("I2cAddress8Bit", "Number");
    properties.put("LinearAcceleration", "Acceleration");
    properties.put("MagneticFieldStrength", "MagneticFlux");
    properties.put("OverallAcceleration", "Acceleration");
    properties.put("Position", "Position");
    properties.put("QuaternionOrientation", "Quaternion");
    properties.put("SystemError", "String");
    properties.put("SystemStatus", "String");
    properties.put("Temperature", "Temperature");
    properties.put("Velocity", "Velocity");
    Map<String, String[]> setterValues = new HashMap<String, String[]>();
    setterValues.put("I2cAddress7Bit", new String[] { ToolboxUtil.makeNumberShadow(8) });
    setterValues.put("I2cAddress8Bit", new String[] { ToolboxUtil.makeNumberShadow(16) });
    ToolboxUtil.addProperties(xmlToolbox, hardwareType, hardwareItem, properties, setterValues);

    // Functions
    Map<String, Map<String, String>> functions = new TreeMap<String, Map<String, String>>();
    Map<String, String> initializeArgs = new HashMap<String, String>();
    initializeArgs.put("PARAMETERS", ToolboxUtil.makeVariableGetBlock("parameters"));
    functions.put("initialize", initializeArgs);
    Map<String, String> startAccelerationIntegration_with1Args = new HashMap<String, String>();
    startAccelerationIntegration_with1Args.put("MS_POLL_INTERVAL", ToolboxUtil.makeNumberShadow(1000));
    functions.put("startAccelerationIntegration_with1", startAccelerationIntegration_with1Args);
    Map<String, String> startAccelerationIntegration_with3Args = new HashMap<String, String>();
    startAccelerationIntegration_with3Args.put("INITIAL_POSITION", ToolboxUtil.makeVariableGetBlock("position"));
    startAccelerationIntegration_with3Args.put("INITIAL_VELOCITY", ToolboxUtil.makeVariableGetBlock("velocity"));
    startAccelerationIntegration_with3Args.put("MS_POLL_INTERVAL", ToolboxUtil.makeNumberShadow(1000));
    functions.put("startAccelerationIntegration_with3", startAccelerationIntegration_with3Args);
    functions.put("stopAccelerationIntegration", null);
    functions.put("isSystemCalibrated", null);
    functions.put("isGyroCalibrated", null);
    functions.put("isAccelerometerCalibrated", null);
    functions.put("isMagnetometerCalibrated", null);
    Map<String, String> saveCalibrationDataArgs = new HashMap<String, String>();
    saveCalibrationDataArgs.put("FILE_NAME", ToolboxUtil.makeTextShadow("IMUCalibration.json"));
    functions.put("saveCalibrationData", saveCalibrationDataArgs);
    Map<String, String> getAngularVelocityArgs = new HashMap<String, String>();
    getAngularVelocityArgs.put("ANGLE_UNIT", ToolboxUtil.makeTypedEnumShadow("navigation", "angleUnit"));
    functions.put("getAngularVelocity", getAngularVelocityArgs);
    Map<String, String> getAngularOrientationArgs = new HashMap<String, String>();
    getAngularOrientationArgs.put("AXES_REFERENCE", ToolboxUtil.makeTypedEnumShadow("navigation", "axesReference"));
    getAngularOrientationArgs.put("AXES_ORDER", ToolboxUtil.makeTypedEnumShadow("navigation", "axesOrder"));
    getAngularOrientationArgs.put("ANGLE_UNIT", ToolboxUtil.makeTypedEnumShadow("navigation", "angleUnit"));
    functions.put("getAngularOrientation", getAngularOrientationArgs);
    ToolboxUtil.addFunctions(xmlToolbox, hardwareType, hardwareItem, functions);
  }

  /**
   * Adds the category for analog input to the toolbox.
   */
  private static void addAnalogInputCategoryToToolbox(
      StringBuilder xmlToolbox, HardwareType hardwareType, List<HardwareItem> hardwareItems) {
    HardwareItem hardwareItem = hardwareItems.get(0);

    // Properties
    SortedMap<String, String> properties = new TreeMap<String, String>();
    properties.put("Voltage", "Number");
    properties.put("MaxVoltage", "Number");
    ToolboxUtil.addProperties(xmlToolbox, hardwareType, hardwareItem, properties,
        null /* setterValues */);
  }

  /**
   * Adds the category for analog output to the toolbox.
   */
  private static void addAnalogOutputCategoryToToolbox(
      StringBuilder xmlToolbox, HardwareType hardwareType, List<HardwareItem> hardwareItems) {
    HardwareItem hardwareItem = hardwareItems.get(0);

    // Functions
    Map<String, Map<String, String>> functions = new TreeMap<String, Map<String, String>>();
    Map<String, String> setAnalogOutputVoltageArgs = new HashMap<String, String>();
    setAnalogOutputVoltageArgs.put("VOLTAGE", ToolboxUtil.makeNumberShadow(512));
    functions.put("setAnalogOutputVoltage_Number", setAnalogOutputVoltageArgs);
    Map<String, String> setAnalogOutputFrequencyArgs = new HashMap<String, String>();
    setAnalogOutputFrequencyArgs.put("FREQUENCY", ToolboxUtil.makeNumberShadow(100));
    functions.put("setAnalogOutputFrequency_Number", setAnalogOutputFrequencyArgs);
    Map<String, String> setAnalogOutputModeArgs = new HashMap<String, String>();
    setAnalogOutputModeArgs.put("MODE", ToolboxUtil.makeNumberShadow(0));
    functions.put("setAnalogOutputMode_Number", setAnalogOutputModeArgs);
    ToolboxUtil.addFunctions(xmlToolbox, hardwareType, hardwareItem, functions);
  }

  /**
   * Adds the category for color sensor to the toolbox.
   */
  private static void addColorSensorCategoryToToolbox(
      StringBuilder xmlToolbox, HardwareType hardwareType, List<HardwareItem> hardwareItems) {
    HardwareItem hardwareItem = hardwareItems.get(0);

    // Properties
    SortedMap<String, String> properties = new TreeMap<String, String>();
    properties.put("Red", "Number");
    properties.put("Green", "Number");
    properties.put("Blue", "Number");
    properties.put("Alpha", "Number");
    properties.put("Argb", "Number");
    properties.put("I2cAddress7Bit", "Number");
    properties.put("I2cAddress8Bit", "Number");
    Map<String, String[]> setterValues = new HashMap<String, String[]>();
    setterValues.put("I2cAddress7Bit", new String[] { ToolboxUtil.makeNumberShadow(8) });
    setterValues.put("I2cAddress8Bit", new String[] { ToolboxUtil.makeNumberShadow(16) });
    ToolboxUtil.addProperties(xmlToolbox, hardwareType, hardwareItem, properties, setterValues);

    // Functions
    Map<String, Map<String, String>> functions = new TreeMap<String, Map<String, String>>();
    Map<String, String> enableLedArgs = new HashMap<String, String>();
    enableLedArgs.put("ENABLE", ToolboxUtil.makeBooleanShadow(true));
    functions.put("enableLed_Boolean", enableLedArgs);
    functions.put("isLightOn", null);
    functions.put("getNormalizedColors", null);
    functions.put("toText", null);
    ToolboxUtil.addFunctions(xmlToolbox, hardwareType, hardwareItem, functions);
  }

  /**
   * Adds the category for compass sensor to the toolbox.
   */
  private static void addCompassSensorCategoryToToolbox(
      StringBuilder xmlToolbox, HardwareType hardwareType, List<HardwareItem> hardwareItems) {
    HardwareItem hardwareItem = hardwareItems.get(0);

    // Properties
    SortedMap<String, String> properties = new TreeMap<String, String>();
    properties.put("Direction", "Number");
    properties.put("CalibrationFailed", "Boolean");
    ToolboxUtil.addProperties(xmlToolbox, hardwareType, hardwareItem, properties,
        null /* setterValues */);

    // Functions
    Map<String, Map<String, String>> functions = new TreeMap<String, Map<String, String>>();
    Map<String, String> setModeArgs = new HashMap<String, String>();
    setModeArgs.put("COMPASS_MODE", ToolboxUtil.makeTypedEnumShadow(hardwareType, "compassMode"));
    functions.put("setMode_CompassMode", setModeArgs);
    ToolboxUtil.addFunctions(xmlToolbox, hardwareType, hardwareItem, functions);
  }

  /**
   * Adds the category for cr servo to the toolbox.
   */
  private static void addCRServoCategoryToToolbox(
      StringBuilder xmlToolbox, HardwareType hardwareType, List<HardwareItem> hardwareItems) {
    HardwareItem hardwareItem = hardwareItems.get(0);

    // Properties
    SortedMap<String, String> properties = new TreeMap<String, String>();
    properties.put("Direction", "Direction");
    properties.put("Power", "Number");
    Map<String, String[]> setterValues = new HashMap<String, String[]>();
    setterValues.put(
        "Direction", new String[] { ToolboxUtil.makeTypedEnumShadow(hardwareType, "direction") });
    setterValues.put("Power", new String[] {
      ToolboxUtil.makeNumberShadow(1),
      ToolboxUtil.makeNumberShadow(0)
    });
    ToolboxUtil.addProperties(xmlToolbox, hardwareType, hardwareItem, properties, setterValues);
  }

  /**
   * Adds the category for dc motor to the toolbox.
   */
  private static void addDcMotorCategoryToToolbox(
      StringBuilder xmlToolbox, HardwareType hardwareType, List<HardwareItem> hardwareItems) {
    HardwareItem hardwareItem = hardwareItems.get(0);
    String zero = ToolboxUtil.makeNumberShadow(0);
    String one = ToolboxUtil.makeNumberShadow(1);
    String ten = ToolboxUtil.makeNumberShadow(10);
    String runMode = ToolboxUtil.makeTypedEnumShadow(hardwareType, "runMode");
    String zeroPowerBehavior = ToolboxUtil.makeTypedEnumShadow(hardwareType, "zeroPowerBehavior");
    String direction = ToolboxUtil.makeTypedEnumShadow(hardwareType, "direction");

    // Properties
    SortedMap<String, String> properties = new TreeMap<String, String>();
    properties.put("CurrentPosition", "Number");
    properties.put("Direction", "Direction");
    properties.put("Mode", "RunMode");
    properties.put("Power", "Number");
    properties.put("PowerFloat", "Boolean");
    properties.put("TargetPosition", "Number");
    properties.put("ZeroPowerBehavior", "ZeroPowerBehavior");
    Map<String, String[]> setterValues = new HashMap<String, String[]>();
    setterValues.put("Direction", new String[] { direction });
    setterValues.put("Mode", new String[] { runMode });
    setterValues.put("Power", new String[] { one, zero });
    setterValues.put("TargetPosition", new String[] { zero });
    setterValues.put("ZeroPowerBehavior", new String[] { zeroPowerBehavior });
    ToolboxUtil.addProperties(xmlToolbox, hardwareType, hardwareItem, properties, setterValues);

    // Functions
    Map<String, Map<String, String>> functions = new TreeMap<String, Map<String, String>>();
    functions.put("isBusy", null);
    ToolboxUtil.addFunctions(xmlToolbox, hardwareType, hardwareItem, functions);

    if (hardwareItems.size() > 1) {
      HardwareItem hardwareItem1 = hardwareItems.get(1);

      xmlToolbox.append("    <category name=\"" + DC_MOTOR_DUAL_CATEGORY_NAME + "\">\n");
      // Set power for both motors to 1.
      ToolboxUtil.addDualPropertySetters(xmlToolbox, hardwareType, "Power", "Number",
          hardwareItem, one,
          hardwareItem1, one);
      // Set power for both motors to 0.
      ToolboxUtil.addDualPropertySetters(xmlToolbox, hardwareType, "Power", "Number",
          hardwareItem, zero,
          hardwareItem1, zero);
      // Set run mode for both motors.
      ToolboxUtil.addDualPropertySetters(xmlToolbox, hardwareType, "Mode", "RunMode",
          hardwareItem, runMode,
          hardwareItem1, runMode);
      // Set target position for both motors.
      ToolboxUtil.addDualPropertySetters(xmlToolbox, hardwareType, "TargetPosition", "Number",
          hardwareItem, zero,
          hardwareItem1, zero);
      // Set zero power behavior for both motors.
      ToolboxUtil.addDualPropertySetters(xmlToolbox, hardwareType, "ZeroPowerBehavior", "ZeroPowerBehavior",
          hardwareItem, zeroPowerBehavior,
          hardwareItem1, zeroPowerBehavior);
      xmlToolbox
          .append("    </category>\n");
    }
  }

  /**
   * Adds the category for digital channel to the toolbox.
   */
  private static void addDigitalChannelCategoryToToolbox(
      StringBuilder xmlToolbox, HardwareType hardwareType, List<HardwareItem> hardwareItems) {
    HardwareItem hardwareItem = hardwareItems.get(0);
    String mode = ToolboxUtil.makeTypedEnumShadow(hardwareType, "mode");

    // Properties
    SortedMap<String, String> properties = new TreeMap<String, String>();
    properties.put("Mode", "DigitalChannelMode");
    properties.put("State", "Boolean");
    Map<String, String[]> setterValues = new HashMap<String, String[]>();
    setterValues.put("Mode", new String[] { mode });
    setterValues.put("State", new String[] { ToolboxUtil.makeBooleanShadow(true) });
    ToolboxUtil.addProperties(xmlToolbox, hardwareType, hardwareItem, properties, setterValues);
  }

  /**
   * Adds the category for gyro sensor to the toolbox.
   */
  private static void addGyroSensorCategoryToToolbox(
      StringBuilder xmlToolbox, HardwareType hardwareType, List<HardwareItem> hardwareItems) {
    HardwareItem hardwareItem = hardwareItems.get(0);
    String headingMode = ToolboxUtil.makeTypedEnumShadow(hardwareType, "headingMode");

    // Properties
    SortedMap<String, String> properties = new TreeMap<String, String>();
    properties.put("Heading", "Number");
    properties.put("HeadingMode", "HeadingMode");
    properties.put("I2cAddress7Bit", "Number");
    properties.put("I2cAddress8Bit", "Number");
    properties.put("IntegratedZValue", "Number");
    properties.put("RawX", "Number");
    properties.put("RawY", "Number");
    properties.put("RawZ", "Number");
    properties.put("RotationFraction", "Number");
    properties.put("AngularVelocityAxes", "Array");
    properties.put("AngularOrientationAxes", "Array");
    Map<String, String[]> setterValues = new HashMap<String, String[]>();
    setterValues.put("HeadingMode", new String[] { headingMode });
    setterValues.put("I2cAddress7Bit", new String[] { ToolboxUtil.makeNumberShadow(8) });
    setterValues.put("I2cAddress8Bit", new String[] { ToolboxUtil.makeNumberShadow(16) });
    ToolboxUtil.addProperties(xmlToolbox, hardwareType, hardwareItem, properties, setterValues);

    // Functions
    Map<String, Map<String, String>> functions = new TreeMap<String, Map<String, String>>();
    functions.put("calibrate", null);
    functions.put("isCalibrating", null);
    functions.put("resetZAxisIntegrator", null);
    Map<String, String> getAngularVelocityArgs = new HashMap<String, String>();
    getAngularVelocityArgs.put("ANGLE_UNIT", ToolboxUtil.makeTypedEnumShadow("navigation", "angleUnit"));
    functions.put("getAngularVelocity", getAngularVelocityArgs);
    Map<String, String> getAngularOrientationArgs = new HashMap<String, String>();
    getAngularOrientationArgs.put("AXES_REFERENCE", ToolboxUtil.makeTypedEnumShadow("navigation", "axesReference"));
    getAngularOrientationArgs.put("AXES_ORDER", ToolboxUtil.makeTypedEnumShadow("navigation", "axesOrder"));
    getAngularOrientationArgs.put("ANGLE_UNIT", ToolboxUtil.makeTypedEnumShadow("navigation", "angleUnit"));
    functions.put("getAngularOrientation", getAngularOrientationArgs);
    ToolboxUtil.addFunctions(xmlToolbox, hardwareType, hardwareItem, functions);
  }

  /**
   * Adds the category for IR seeker sensor to the toolbox.
   */
  private static void addIrSeekerSensorCategoryToToolbox(
      StringBuilder xmlToolbox, HardwareType hardwareType, List<HardwareItem> hardwareItems) {
    HardwareItem hardwareItem = hardwareItems.get(0);
    String mode = ToolboxUtil.makeTypedEnumShadow(hardwareType, "mode");
    String threshold = ToolboxUtil.makeNumberShadow(0.003);

    // Properties
    SortedMap<String, String> properties = new TreeMap<String, String>();
    properties.put("SignalDetectedThreshold", "Number");
    properties.put("Mode", "IrSeekerSensorMode");
    properties.put("IsSignalDetected", "Boolean");
    properties.put("Angle", "Number");
    properties.put("Strength", "Number");
    properties.put("I2cAddress7Bit", "Number");
    properties.put("I2cAddress8Bit", "Number");
    Map<String, String[]> setterValues = new HashMap<String, String[]>();
    setterValues.put("SignalDetectedThreshold", new String[] { threshold });
    setterValues.put("Mode", new String[] { mode });
    setterValues.put("I2cAddress7Bit", new String[] { ToolboxUtil.makeNumberShadow(8) });
    setterValues.put("I2cAddress8Bit", new String[] { ToolboxUtil.makeNumberShadow(16) });
    ToolboxUtil.addProperties(xmlToolbox, hardwareType, hardwareItem, properties, setterValues);
  }

  /**
   * Adds the category for LED to the toolbox.
   */
  private static void addLedCategoryToToolbox(
      StringBuilder xmlToolbox, HardwareType hardwareType, List<HardwareItem> hardwareItems) {
    HardwareItem hardwareItem = hardwareItems.get(0);

    // Functions
    Map<String, Map<String, String>> functions = new TreeMap<String, Map<String, String>>();
    Map<String, String> enableLedArgs = new HashMap<String, String>();
    enableLedArgs.put("ENABLE", ToolboxUtil.makeBooleanShadow(true));
    functions.put("enableLed_Boolean", enableLedArgs);
    functions.put("isLightOn", null);
    ToolboxUtil.addFunctions(xmlToolbox, hardwareType, hardwareItem, functions);
  }

  /**
   * Adds the category for light sensor to the toolbox.
   */
  private static void addLightSensorCategoryToToolbox(
      StringBuilder xmlToolbox, HardwareType hardwareType, List<HardwareItem> hardwareItems) {
    HardwareItem hardwareItem = hardwareItems.get(0);

    // Properties
    SortedMap<String, String> properties = new TreeMap<String, String>();
    properties.put("LightDetected", "Number");
    properties.put("RawLightDetected", "Number");
    properties.put("RawLightDetectedMax", "Number");
    ToolboxUtil.addProperties(xmlToolbox, hardwareType, hardwareItem, properties,
        null /* setterValues */);

    // Functions
    Map<String, Map<String, String>> functions = new TreeMap<String, Map<String, String>>();
    Map<String, String> enableLedArgs = new HashMap<String, String>();
    enableLedArgs.put("ENABLE", ToolboxUtil.makeBooleanShadow(true));
    functions.put("enableLed_Boolean", enableLedArgs);
    ToolboxUtil.addFunctions(xmlToolbox, hardwareType, hardwareItem, functions);
  }

  /**
   * Adds the category for LynxI2cColorRangeSensor to the toolbox.
   */
  private static void addLynxI2cColorRangeSensorCategoryToToolbox(
      StringBuilder xmlToolbox, HardwareType hardwareType, List<HardwareItem> hardwareItems) {
    HardwareItem hardwareItem = hardwareItems.get(0);

    // Properties
    SortedMap<String, String> properties = new TreeMap<String, String>();
    properties.put("Red", "Number");
    properties.put("Green", "Number");
    properties.put("Blue", "Number");
    properties.put("Alpha", "Number");
    properties.put("Argb", "Number");
    properties.put("I2cAddress7Bit", "Number");
    properties.put("I2cAddress8Bit", "Number");
    properties.put("LightDetected", "Number");
    properties.put("RawLightDetected", "Number");
    properties.put("RawLightDetectedMax", "Number");
    Map<String, String[]> setterValues = new HashMap<String, String[]>();
    setterValues.put("I2cAddress7Bit", new String[] { ToolboxUtil.makeNumberShadow(8) });
    setterValues.put("I2cAddress8Bit", new String[] { ToolboxUtil.makeNumberShadow(16) });
    ToolboxUtil.addProperties(xmlToolbox, hardwareType, hardwareItem, properties, setterValues);

    // Functions
    Map<String, Map<String, String>> functions = new TreeMap<String, Map<String, String>>();
    Map<String, String> getDistanceArgs = new HashMap<String, String>();
    getDistanceArgs.put("UNIT", ToolboxUtil.makeTypedEnumShadow("navigation", "distanceUnit"));
    functions.put("getDistance_Number", getDistanceArgs);
    functions.put("getNormalizedColors", null);
    ToolboxUtil.addFunctions(xmlToolbox, hardwareType, hardwareItem, functions);
  }

  /**
   * Adds the category for ModernRoboticsI2cCompassSensor to the toolbox.
   */
  private static void addMrI2cCompassSensorCategoryToToolbox(
      StringBuilder xmlToolbox, HardwareType hardwareType, List<HardwareItem> hardwareItems) {
    HardwareItem hardwareItem = hardwareItems.get(0);

    // Properties
    SortedMap<String, String> properties = new TreeMap<String, String>();
    properties.put("Direction", "Number");
    properties.put("XAccel", "Number");
    properties.put("YAccel", "Number");
    properties.put("ZAccel", "Number");
    properties.put("XMagneticFlux", "Number");
    properties.put("YMagneticFlux", "Number");
    properties.put("ZMagneticFlux", "Number");
    properties.put("I2cAddress7Bit", "Number");
    properties.put("I2cAddress8Bit", "Number");
    Map<String, String[]> setterValues = new HashMap<String, String[]>();
    setterValues.put("I2cAddress7Bit", new String[] { ToolboxUtil.makeNumberShadow(8) });
    setterValues.put("I2cAddress8Bit", new String[] { ToolboxUtil.makeNumberShadow(16) });
    ToolboxUtil.addProperties(xmlToolbox, hardwareType, hardwareItem, properties, setterValues);

    // Functions
    Map<String, Map<String, String>> functions = new TreeMap<String, Map<String, String>>();
    Map<String, String> setModeArgs = new HashMap<String, String>();
    setModeArgs.put("COMPASS_MODE", ToolboxUtil.makeTypedEnumShadow(hardwareType, "compassMode"));
    functions.put("setMode_CompassMode", setModeArgs);
    functions.put("isCalibrating", null);
    functions.put("calibrationFailed", null);
    ToolboxUtil.addFunctions(xmlToolbox, hardwareType, hardwareItem, functions);
  }

  /**
   * Adds the category for ModernRoboticsI2cRangeSensor to the toolbox.
   */
  private static void addMrI2cRangeSensorCategoryToToolbox(
      StringBuilder xmlToolbox, HardwareType hardwareType, List<HardwareItem> hardwareItems) {
    HardwareItem hardwareItem = hardwareItems.get(0);

    // Properties
    SortedMap<String, String> properties = new TreeMap<String, String>();
    properties.put("LightDetected", "Number");
    properties.put("RawLightDetected", "Number");
    properties.put("RawLightDetectedMax", "Number");
    properties.put("RawUltrasonic", "Number");
    properties.put("RawOptical", "Number");
    properties.put("CmUltrasonic", "Number");
    properties.put("CmOptical", "Number");
    properties.put("I2cAddress7Bit", "Number");
    properties.put("I2cAddress8Bit", "Number");
    Map<String, String[]> setterValues = new HashMap<String, String[]>();
    setterValues.put("I2cAddress7Bit", new String[] { ToolboxUtil.makeNumberShadow(8) });
    setterValues.put("I2cAddress8Bit", new String[] { ToolboxUtil.makeNumberShadow(16) });
    ToolboxUtil.addProperties(xmlToolbox, hardwareType, hardwareItem, properties, setterValues);

    // Functions
    Map<String, Map<String, String>> functions = new TreeMap<String, Map<String, String>>();
    Map<String, String> getDistanceArgs = new HashMap<String, String>();
    getDistanceArgs.put("UNIT", ToolboxUtil.makeTypedEnumShadow(hardwareType, "distanceUnit"));
    functions.put("getDistance_Number", getDistanceArgs);
    ToolboxUtil.addFunctions(xmlToolbox, hardwareType, hardwareItem, functions);
  }

  /**
   * Adds the category for optical distance sensor to the toolbox.
   */
  private static void addOpticalDistanceSensorCategoryToToolbox(
      StringBuilder xmlToolbox, HardwareType hardwareType, List<HardwareItem> hardwareItems) {
    HardwareItem hardwareItem = hardwareItems.get(0);

    // Properties
    SortedMap<String, String> properties = new TreeMap<String, String>();
    properties.put("LightDetected", "Number");
    properties.put("RawLightDetected", "Number");
    properties.put("RawLightDetectedMax", "Number");
    ToolboxUtil.addProperties(xmlToolbox, hardwareType, hardwareItem, properties,
        null /* setterValues */);

    // Functions
    Map<String, Map<String, String>> functions = new TreeMap<String, Map<String, String>>();
    Map<String, String> enableLedArgs = new HashMap<String, String>();
    enableLedArgs.put("ENABLE", ToolboxUtil.makeBooleanShadow(true));
    functions.put("enableLed_Boolean", enableLedArgs);
    ToolboxUtil.addFunctions(xmlToolbox, hardwareType, hardwareItem, functions);
  }

  /**
   * Adds the category for servo to the toolbox.
   */
  private static void addServoCategoryToToolbox(
      StringBuilder xmlToolbox, HardwareType hardwareType, List<HardwareItem> hardwareItems) {
    HardwareItem hardwareItem = hardwareItems.get(0);

    // Properties
    SortedMap<String, String> properties = new TreeMap<String, String>();
    properties.put("Direction", "Direction");
    properties.put("Position", "Number");
    Map<String, String[]> setterValues = new HashMap<String, String[]>();
    setterValues.put(
        "Direction", new String[] { ToolboxUtil.makeTypedEnumShadow(hardwareType, "direction") });
    setterValues.put("Position", new String[] { ToolboxUtil.makeNumberShadow(0) });
    ToolboxUtil.addProperties(xmlToolbox, hardwareType, hardwareItem, properties, setterValues);

    // Functions
    Map<String, Map<String, String>> functions = new TreeMap<String, Map<String, String>>();
    Map<String, String> scaleRangeArgs = new HashMap<String, String>();
    scaleRangeArgs.put("MIN", ToolboxUtil.makeNumberShadow(0.2));
    scaleRangeArgs.put("MAX", ToolboxUtil.makeNumberShadow(0.8));
    functions.put("scaleRange_Number", scaleRangeArgs);
    ToolboxUtil.addFunctions(xmlToolbox, hardwareType, hardwareItem, functions);
  }

  /**
   * Adds the category for servo controller to the toolbox.
   */
  private static void addServoControllerCategoryToToolbox(
      StringBuilder xmlToolbox, HardwareType hardwareType, List<HardwareItem> hardwareItems) {
    HardwareItem hardwareItem = hardwareItems.get(0);

    // Properties
    SortedMap<String, String> properties = new TreeMap<String, String>();
    properties.put("PwmStatus", "PwmStatus");
    ToolboxUtil.addProperties(xmlToolbox, hardwareType, hardwareItem, properties,
        null /* setterValues */);

    // Functions
    Map<String, Map<String, String>> functions = new TreeMap<String, Map<String, String>>();
    functions.put("pwmEnable", null);
    functions.put("pwmDisable", null);
    ToolboxUtil.addFunctions(xmlToolbox, hardwareType, hardwareItem, functions);
  }

  /**
   * Adds the category for touch sensor to the toolbox.
   */
  private static void addTouchSensorCategoryToToolbox(
      StringBuilder xmlToolbox, HardwareType hardwareType, List<HardwareItem> hardwareItems) {
    HardwareItem hardwareItem = hardwareItems.get(0);

    // Properties
    SortedMap<String, String> properties = new TreeMap<String, String>();
    properties.put("IsPressed", "Boolean");
    ToolboxUtil.addProperties(xmlToolbox, hardwareType, hardwareItem, properties,
        null /* setterValues */);
  }

  /**
   * Adds the category for ultrasonic sensor to the toolbox.
   */
  private static void addUltrasonicSensorCategoryToToolbox(
      StringBuilder xmlToolbox, HardwareType hardwareType, List<HardwareItem> hardwareItems) {
    HardwareItem hardwareItem = hardwareItems.get(0);

    // Properties
    SortedMap<String, String> properties = new TreeMap<String, String>();
    properties.put("UltrasonicLevel", "Number");
    ToolboxUtil.addProperties(xmlToolbox, hardwareType, hardwareItem, properties,
        null /* setterValues */);
  }

  /**
   * Adds the category for voltage sensor to the toolbox.
   */
  private static void addVoltageSensorCategoryToToolbox(
      StringBuilder xmlToolbox, HardwareType hardwareType, List<HardwareItem> hardwareItems) {
    HardwareItem hardwareItem = hardwareItems.get(0);

    // Properties
    SortedMap<String, String> properties = new TreeMap<String, String>();
    properties.put("Voltage", "Number");
    ToolboxUtil.addProperties(xmlToolbox, hardwareType, hardwareItem, properties,
        null /* setterValues */);
  }

  /**
   * Upgrades the given blocks content based on the active configuration.
   */
  static String upgradeBlocks(String blkContent) {
    return upgradeBlocks(blkContent, HardwareItemMap.newHardwareItemMap());
  }

  /**
   * Upgrades the given blocks content based on the given {@link HardwareItemMap}.
   */
  private static String upgradeBlocks(String blkContent, HardwareItemMap hardwareItemMap) {
    // In previous versions, block type prefix bno055imu_ was adafruitBNO055IMU_.
    blkContent = blkContent.replace(
        "<block type=\"adafruitBNO055IMU_",
        "<block type=\"bno055imu_");
    // In previous versions, identifier suffix AsBNO055IMU was AsAdafruitBNO055IMU.
    blkContent = replaceIdentifierSuffixInBlocks(blkContent,
        hardwareItemMap.getHardwareItems(HardwareType.BNO055IMU),
        "AsAdafruitBNO055IMU", "AsBNO055IMU");
    // In previous versions, block type prefix bno055imuParameters_ was adafruitBNO055IMUParameters_.
    blkContent = blkContent.replace(
        "<block type=\"adafruitBNO055IMUParameters_",
        "<block type=\"bno055imuParameters_");
    // In previous versions, shadow type prefix bno055imuParameters_ was adafruitBNO055IMUParameters_.
    blkContent = blkContent.replace(
        "<shadow type=\"adafruitBNO055IMUParameters_",
        "<shadow type=\"bno055imuParameters_");
    // In previous version, value name BNO055IMU_PARAMETERS was ADAFRUIT_BNO055IMU_PARAMETERS.
    blkContent = blkContent.replace(
        "<value name=\"ADAFRUIT_BNO055IMU_PARAMETERS\">",
        "<value name=\"BNO055IMU_PARAMETERS\">");
    return blkContent;
  }

  /**
   * Replaces an identifier suffix in blocks.
   */
  private static String replaceIdentifierSuffixInBlocks(
      String blkContent, List<HardwareItem> hardwareItemList,
      String oldIdentifierSuffix, String newIdentifierSuffix) {
    if (hardwareItemList != null) {
      for (HardwareItem hardwareItem : hardwareItemList) {
        String newIdentifier = hardwareItem.identifier;
        if (newIdentifier.endsWith(newIdentifierSuffix)) {
          String oldIdentifier = newIdentifier.substring(0, newIdentifier.length() - newIdentifierSuffix.length())
              + oldIdentifierSuffix;
          String oldTag = "<field name=\"IDENTIFIER\">" + oldIdentifier + "</field>";
          String newTag = "<field name=\"IDENTIFIER\">" + newIdentifier + "</field>";
          blkContent = blkContent.replace(oldTag, newTag);
        }
      }
    }
    return blkContent;
  }

  /**
   * Upgrades the given js content based on the given {@link HardwareItemMap}.
   */
  public static String upgradeJs(String jsContent, HardwareItemMap hardwareItemMap) {
    // In previous versions, identifier suffix AsBNO055IMU was AsAdafruitBNO055IMU.
    jsContent = replaceIdentifierSuffixInJs(jsContent,
        hardwareItemMap.getHardwareItems(HardwareType.BNO055IMU),
        "AsAdafruitBNO055IMU", "AsBNO055IMU");
    // In previous versions, identifier bno055imuParametersAccess was adafruitBNO055IMUParametersAccess.
    jsContent = replaceIdentifierInJs(jsContent,
        "adafruitBNO055IMUParametersAccess", "bno055imuParametersAccess");
    return jsContent;
  }

  /**
   * Replaces an identifier suffix in js.
   */
  private static String replaceIdentifierSuffixInJs(
      String jsContent, List<HardwareItem> hardwareItemList,
      String oldIdentifierSuffix, String newIdentifierSuffix) {
    if (hardwareItemList != null) {
      for (HardwareItem hardwareItem : hardwareItemList) {
        String newIdentifier = hardwareItem.identifier;
        if (newIdentifier.endsWith(newIdentifierSuffix)) {
          String oldIdentifier = newIdentifier.substring(0, newIdentifier.length() - newIdentifierSuffix.length())
              + oldIdentifierSuffix;
          String oldCode = oldIdentifier + ".";
          String newCode = newIdentifier + ".";
          jsContent = jsContent.replace(oldCode, newCode);
        }
      }
    }
    return jsContent;
  }

  /**
   * Replaces an identifier in js.
   */
  private static String replaceIdentifierInJs(
      String jsContent, String oldIdentifier, String newIdentifier) {
    String oldCode = oldIdentifier + ".";
    String newCode = newIdentifier + ".";
    return jsContent.replace(oldCode, newCode);
  }
}
