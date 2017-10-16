// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller.util;

import android.support.annotation.Nullable;

/**
 * An enum to represent the various identifiers that are used in the generate javascript code.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
public enum Identifier {
  ACCELERATION("accelerationAccess", "accelerationIdentifier"),
  ANDROID_ACCELEROMETER("androidAccelerometerAccess", "androidAccelerometerIdentifier"),
  ANDROID_GYROSCOPE("androidGyroscopeAccess", "androidGyroscopeIdentifier"),
  ANDROID_ORIENTATION("androidOrientationAccess", "androidOrientationIdentifier"),
  ANDROID_SOUND_POOL("androidSoundPoolAccess", "androidSoundPoolIdentifier"),
  ANDROID_TEXT_TO_SPEECH("androidTextToSpeechAccess", "androidTextToSpeechIdentifier"),
  ANGULAR_VELOCITY("angularVelocityAccess", "angularVelocityIdentifier"),
  BLOCKS_OP_MODE("blocksOpMode", null),
  BNO055IMU_PARAMETERS("bno055imuParametersAccess", "bno055imuParametersIdentifier"),
  COLOR("colorAccess", "colorIdentifier"),
  DBG_LOG("dbgLogAccess", "dbgLogIdentifier"),
  ELAPSED_TIME("elapsedTimeAccess", "elapsedTimeIdentifier"),
  GAMEPAD_1("gamepad1", null),
  GAMEPAD_2("gamepad2", null),
  LINEAR_OP_MODE("linearOpMode", "linearOpModeIdentifier"),
  MAGNETIC_FLUX("magneticFluxAccess", "magneticFluxIdentifier"),
  MATRIX_F("matrixFAccess", "matrixFIdentifier"),
  MISC("miscAccess", "miscIdentifier"),
  NAVIGATION("navigationAccess", "navigationIdentifier"),
  OPEN_GL_MATRIX("openGLMatrixAccess", "openGLMatrixIdentifier"),
  ORIENTATION("orientationAccess", "orientationIdentifier"),
  POSITION("positionAccess", "positionIdentifier"),
  QUATERNION("quaternionAccess", "quaternionIdentifier"),
  RANGE("rangeAccess", "rangeIdentifier"),
  SYSTEM("systemAccess", "systemIdentifier"),
  TELEMETRY("telemetry", "telemetryIdentifier"),
  TEMPERATURE("temperatureAccess", "temperatureIdentifier"),
  VECTOR_F("vectorFAccess", "vectorFIdentifier"),
  VELOCITY("velocityAccess", "velocityIdentifier"),
  VUFORIA("vuforiaAccess", "vuforiaIdentifier"),
  VUFORIA_LOCALIZER("vuforiaLocalizerAccess", "vuforiaLocalizerIdentifier"),
  VUFORIA_LOCALIZER_PARAMETERS("vuforiaLocalizerParametersAccess", "vuforiaLocalizerParametersIdentifier"),
  VUFORIA_TRACKABLE("vuforiaTrackableAccess", "vuforiaTrackableIdentifier"),
  VUFORIA_TRACKABLE_DEFAULT_LISTENER("vuforiaTrackableDefaultListenerAccess", "vuforiaTrackableDefaultListenerIdentifier"),
  VUFORIA_TRACKABLES("vuforiaTrackablesAccess", "vuforiaTrackablesIdentifier");

  /**
   * The identifier used in the generated javascript code.
   */
  public final String identifier;
  /**
   * The variable used in the javascript in the blocks editor (AKA programming mode).
   */
  @Nullable
  public final String blocksVariable;

  Identifier(String identifier, String blocksVariable) {
    this.identifier = identifier;
    this.blocksVariable = blocksVariable;
  }
}
