// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller.runtime;

import static android.speech.tts.TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE;

import android.app.Activity;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.webkit.JavascriptInterface;

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
        super(blocksOpMode, identifier, "AndroidTextToSpeech");
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
        startBlockExecution(BlockType.FUNCTION, ".initialize");
        textToSpeech = new TextToSpeech(activity, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                onInitStatus = status;
            }
        });
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    public String getStatus() {
        startBlockExecution(BlockType.GETTER, ".Status");
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
        startBlockExecution(BlockType.GETTER, ".LanguageCode");
        if (textToSpeech != null) {
            return textToSpeech.getLanguage().getLanguage();
        } else {
            reportWarning("You forgot to call AndroidTextToSpeech.initialize!");
        }
        return "";
    }

    @SuppressWarnings({"unused", "deprecation"})
    @JavascriptInterface
    public String getCountryCode() {
        startBlockExecution(BlockType.GETTER, ".CountryCode");
        if (textToSpeech != null) {
            return textToSpeech.getLanguage().getCountry();
        } else {
            reportWarning("You forgot to call AndroidTextToSpeech.initialize!");
        }
        return "";
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    public boolean getIsSpeaking() {
        startBlockExecution(BlockType.GETTER, ".IsSpeaking");
        if (textToSpeech != null) {
            return textToSpeech.isSpeaking();
        } else {
            reportWarning("You forgot to call AndroidTextToSpeech.initialize!");
        }
        return false;
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    public void setPitch(float pitch) {
        startBlockExecution(BlockType.SETTER, ".Pitch");
        if (textToSpeech != null) {
            textToSpeech.setPitch(pitch);
        } else {
            reportWarning("You forgot to call AndroidTextToSpeech.initialize!");
        }
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    public void setSpeechRate(float speechRate) {
        startBlockExecution(BlockType.SETTER, ".SpeechRate");
        if (textToSpeech != null) {
            textToSpeech.setSpeechRate(speechRate);
        } else {
            reportWarning("You forgot to call AndroidTextToSpeech.initialize!");
        }
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    public boolean isLanguageAvailable(String languageCode) {
        startBlockExecution(BlockType.FUNCTION, ".isLanguageAvailable");
        if (textToSpeech != null) {
            Locale locale = new Locale(languageCode);
            return textToSpeech.isLanguageAvailable(locale) == LANG_COUNTRY_VAR_AVAILABLE;
        } else {
            reportWarning("You forgot to call AndroidTextToSpeech.initialize!");
        }
        return false;
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    public boolean isLanguageAndCountryAvailable(String languageCode, String countryCode) {
        startBlockExecution(BlockType.FUNCTION, ".isLanguageAndCountryAvailable");
        if (textToSpeech != null) {
            Locale locale = new Locale(languageCode, countryCode);
            return textToSpeech.isLanguageAvailable(locale) == LANG_COUNTRY_VAR_AVAILABLE;
        } else {
            reportWarning("You forgot to call AndroidTextToSpeech.initialize!");
        }
        return false;
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    public void setLanguage(String languageCode) {
        startBlockExecution(BlockType.FUNCTION, ".setLanguage");
        if (textToSpeech != null) {
            textToSpeech.setLanguage(new Locale(languageCode));
        } else {
            reportWarning("You forgot to call AndroidTextToSpeech.initialize!");
        }
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    public void setLanguageAndCountry(String languageCode, String countryCode) {
        startBlockExecution(BlockType.FUNCTION, ".setLanguageAndCountry");
        if (textToSpeech != null) {
            textToSpeech.setLanguage(new Locale(languageCode, countryCode));
        } else {
            reportWarning("You forgot to call AndroidTextToSpeech.initialize!");
        }
    }

    @SuppressWarnings({"unused", "deprecation"})
    @JavascriptInterface
    public void speak(String text) {
        startBlockExecution(BlockType.FUNCTION, ".speak");
        if (textToSpeech != null) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null /* params */);
        } else {
            reportWarning("You forgot to call AndroidTextToSpeech.initialize!");
        }
    }
}
