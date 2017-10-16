// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller.runtime;

import android.app.Activity;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.webkit.JavascriptInterface;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.Locale;

/**
 * A class that provides JavaScript access to the Android TextToSpeech.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
class AndroidTextToSpeechAccess extends Access {
  private final Activity activity;
  private volatile TextToSpeech textToSpeech;
  private volatile Integer onInitStatus;

  AndroidTextToSpeechAccess(BlocksOpMode blocksOpMode, String identifier, Activity activity) {
    super(blocksOpMode, identifier);
    this.activity = activity;
  }

  // Access methods

  @Override
  void close() {
    if (textToSpeech != null) {
      textToSpeech.shutdown();
      onInitStatus = null;
      textToSpeech = null;
    }
  }

  // Javascript methods

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void initialize() {
    checkIfStopRequested();
    try {
      textToSpeech = new TextToSpeech(activity, new TextToSpeech.OnInitListener() {
        @Override
        public void onInit(int status) {
          onInitStatus = status;
        }
      });
    } catch (Exception e) {
      RobotLog.e("AndroidTextToSpeech.initialize - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public String getStatus() {
    checkIfStopRequested();
    if (onInitStatus == null) {
      return "Not initialized";
    }
    int status = onInitStatus.intValue();
    if (status == TextToSpeech.SUCCESS) {
      return "Success";
    }
    return "Error code " + status;
  }

  @SuppressWarnings({"unused", "deprecation"})
  @JavascriptInterface
  public String getLanguageCode() {
    checkIfStopRequested();
    try {
      if (textToSpeech != null) {
        return textToSpeech.getLanguage().getLanguage();
      } else {
        RobotLog.e("AndroidTextToSpeech.getLanguageCode - you forgot to call AndroidTextToSpeech.initialize!");
      }
    } catch (Exception e) {
      RobotLog.e("AndroidTextToSpeech.getLanguageCode - caught " + e);
    }
    return "";
  }

  @SuppressWarnings({"unused", "deprecation"})
  @JavascriptInterface
  public String getCountryCode() {
    checkIfStopRequested();
    try {
      if (textToSpeech != null) {
        return textToSpeech.getLanguage().getCountry();
      } else {
        RobotLog.e("AndroidTextToSpeech.getCountryCode - you forgot to call AndroidTextToSpeech.initialize!");
      }
    } catch (Exception e) {
      RobotLog.e("AndroidTextToSpeech.getCountryCode - caught " + e);
    }
    return "";
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public boolean getIsSpeaking() {
    checkIfStopRequested();
    try {
      if (textToSpeech != null) {
        return textToSpeech.isSpeaking();
      } else {
        RobotLog.e("AndroidTextToSpeech.getIsSpeaking - you forgot to call AndroidTextToSpeech.initialize!");
      }
    } catch (Exception e) {
      RobotLog.e("AndroidTextToSpeech.getIsSpeaking - caught " + e);
    }
    return false;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void setPitch(float pitch) {
    checkIfStopRequested();
    try {
      if (textToSpeech != null) {
        textToSpeech.setPitch(pitch);
      } else {
        RobotLog.e("AndroidTextToSpeech.setPitch - you forgot to call AndroidTextToSpeech.initialize!");
      }
    } catch (Exception e) {
      RobotLog.e("AndroidTextToSpeech.setPitch - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void setSpeechRate(float speechRate) {
    checkIfStopRequested();
    try {
      if (textToSpeech != null) {
        textToSpeech.setSpeechRate(speechRate);
      } else {
        RobotLog.e("AndroidTextToSpeech.setSpeechRate - you forgot to call AndroidTextToSpeech.initialize!");
      }
    } catch (Exception e) {
      RobotLog.e("AndroidTextToSpeech.setSpeechRate - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public boolean isLanguageAvailable(String languageCode) {
    checkIfStopRequested();
    try {
      if (textToSpeech != null) {
        return textToSpeech.isLanguageAvailable(new Locale(languageCode)) ==
            TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE;
      } else {
        RobotLog.e("AndroidTextToSpeech.isLanguageAvailable - you forgot to call AndroidTextToSpeech.initialize!");
      }
    } catch (Exception e) {
      RobotLog.e("AndroidTextToSpeech.isLanguageAvailable - caught " + e);
    }
    return false;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public boolean isLanguageAndCountryAvailable(String languageCode, String countryCode) {
    checkIfStopRequested();
    try {
      if (textToSpeech != null) {
        return textToSpeech.isLanguageAvailable(new Locale(languageCode, countryCode)) ==
            TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE;
      } else {
        RobotLog.e("AndroidTextToSpeech.isLanguageAndCountryAvailable - you forgot to call AndroidTextToSpeech.initialize!");
      }
    } catch (Exception e) {
      RobotLog.e("AndroidTextToSpeech.isLanguageAndCountryAvailable - caught " + e);
    }
    return false;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void setLanguage(String languageCode) {
    checkIfStopRequested();
    try {
      if (textToSpeech != null) {
        textToSpeech.setLanguage(new Locale(languageCode));
      } else {
        RobotLog.e("AndroidTextToSpeech.setLanguage - you forgot to call AndroidTextToSpeech.initialize!");
      }
    } catch (Exception e) {
      RobotLog.e("AndroidTextToSpeech.setLanguage - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void setLanguageAndCountry(String languageCode, String countryCode) {
    checkIfStopRequested();
    try {
      if (textToSpeech != null) {
        textToSpeech.setLanguage(new Locale(languageCode, countryCode));
      } else {
        RobotLog.e("AndroidTextToSpeech.setLanguageAndCountry - you forgot to call AndroidTextToSpeech.initialize!");
      }
    } catch (Exception e) {
      RobotLog.e("AndroidTextToSpeech.setLanguageAndCountry - caught " + e);
    }
  }

  @SuppressWarnings({"unused", "deprecation"})
  @JavascriptInterface
  public void speak(String text) {
    checkIfStopRequested();
    try {
      if (textToSpeech != null) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null /* params */);
      } else {
        RobotLog.e("AndroidTextToSpeech.speak - you forgot to call AndroidTextToSpeech.initialize!");
      }
    } catch (Exception e) {
      RobotLog.e("AndroidTextToSpeech.speak - caught " + e);
    }
  }
}
