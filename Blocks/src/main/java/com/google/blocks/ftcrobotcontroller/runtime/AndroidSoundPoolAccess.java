// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller.runtime;

import android.media.AudioManager;
import android.media.SoundPool;
import android.webkit.JavascriptInterface;

import com.google.blocks.ftcrobotcontroller.util.SoundsUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * A class that provides JavaScript access to the Android SoundPool.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
class AndroidSoundPoolAccess extends Access {
    private volatile SoundPool soundPool;
    private final Map<String, Integer> soundMap = new HashMap<String, Integer>();
    private volatile float volume = 1.0f;
    private volatile float rate = 1.0f;
    private volatile int loop = 0;
    private volatile Integer streamId;

    AndroidSoundPoolAccess(BlocksOpMode blocksOpMode, String identifier) {
        super(blocksOpMode, identifier, "AndroidSoundPool");
    }

    // Access methods

    @Override
    void close() {
        if (soundPool != null) {
            if (streamId != null) {
                soundPool.stop(streamId.intValue());
                streamId = null;
            }

            for (Integer soundId : soundMap.values()) {
                soundPool.unload(soundId);
            }
            soundMap.clear();

            soundPool.release();
            soundPool = null;
        }
    }

    // Javascript methods

    @SuppressWarnings({"unused", "deprecation"})
    @JavascriptInterface
    public void initialize() {
        startBlockExecution(BlockType.FUNCTION, ".initialize");
        soundPool = new SoundPool(1 /* maxStreams */, AudioManager.STREAM_MUSIC, 0);
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    public boolean preloadSound(String soundName) {
        startBlockExecution(BlockType.FUNCTION, ".preloadSound");
        if (soundPool != null) {
            int soundId = getSoundId(soundName);
            if (soundId != 0) {
                return true;
            } else {
                reportWarning("Failed to preload " + soundName);
            }
        } else {
            reportWarning("You forgot to call AndroidSoundPool.initialize!");
        }
        return false;
    }

    private int getSoundId(String soundName) {
        Integer preloadedSoundId = soundMap.get(soundName);
        if (preloadedSoundId != null) {
            return preloadedSoundId.intValue();
        }
        int soundId = soundPool.load(SoundsUtil.getPathForSound(soundName), 1);
        if (soundId != 0) {
            soundMap.put(soundName, soundId);
        }
        return soundId;
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    public void play(String soundName) {
        startBlockExecution(BlockType.FUNCTION, ".play");
        if (soundPool != null) {
            int soundId = getSoundId(soundName);
            if (soundId != 0) {
                streamId = soundPool.play(soundId, volume, volume, 0 /* priority */, loop, rate);
            } else {
                reportWarning("Failed to load " + soundName);
            }
        } else {
            reportWarning("You forgot to call AndroidSoundPool.initialize!");
        }
    }

    @SuppressWarnings({"unused"})
    @JavascriptInterface
    public void stop() {
        startBlockExecution(BlockType.FUNCTION, ".stop");
        if (streamId != null) {
            soundPool.stop(streamId.intValue());
            streamId = null;
        }
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    public float getVolume() {
        startBlockExecution(BlockType.GETTER, ".Volume");
        return volume;
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    public void setVolume(float volume) {
        startBlockExecution(BlockType.SETTER, ".Volume");
        if (volume >= 0.0f && volume <= 1.0f) {
            this.volume = volume;
        } else {
            reportInvalidArg("", "a number between 0.0 and 1.0");
        }
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    public float getRate() {
        startBlockExecution(BlockType.GETTER, ".Rate");
        return rate;
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    public void setRate(float rate) {
        startBlockExecution(BlockType.SETTER, ".Rate");
        if (rate >= 0.5f && rate <= 2.0f) {
            this.rate = rate;
        } else {
            reportInvalidArg("", "a number between 0.5 and 2.0");
        }
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    public int getLoop() {
        startBlockExecution(BlockType.GETTER, ".Loop");
        return loop;
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    public void setLoop(int loop) {
        startBlockExecution(BlockType.SETTER, ".Loop");
        if (loop >= -1) {
            this.loop = loop;
        } else {
            reportInvalidArg("", "a number greater than or equal to -1");
        }
    }
}
