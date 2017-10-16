// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller.runtime;

import android.media.AudioManager;
import android.media.SoundPool;
import android.webkit.JavascriptInterface;
import com.google.blocks.ftcrobotcontroller.util.SoundsUtil;
import com.qualcomm.robotcore.util.RobotLog;
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
    super(blocksOpMode, identifier);
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
    checkIfStopRequested();
    try {
      soundPool = new SoundPool(1 /* maxStreams */, AudioManager.STREAM_MUSIC, 0);
    } catch (Exception e) {
      RobotLog.e("AndroidSoundPool.initialize - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public boolean preloadSound(String soundName) {
    checkIfStopRequested();
    try {
      if (soundPool != null) {
        int soundId = getSoundId(soundName);
        if (soundId != 0) {
          return true;
        } else {
          RobotLog.e("AndroidSoundPool.preloadSound - failed to preload " + soundName);
        }
      } else {
        RobotLog.e("AndroidSoundPool.preloadSound - you forgot to call AndroidSoundPool.initialize!");
      }
    } catch (Exception e) {
      RobotLog.e("AndroidSoundPool.preloadSound - caught " + e);
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
    checkIfStopRequested();
    try {
      if (soundPool != null) {
        int soundId = getSoundId(soundName);
        if (soundId != 0) {
          streamId = soundPool.play(soundId, volume, volume, 0 /* priority */, loop, rate);
        } else {
          RobotLog.e("AndroidSoundPool.preloadSound - failed to load " + soundName);
        }
      } else {
        RobotLog.e("AndroidSoundPool.play - you forgot to call AndroidSoundPool.initialize!");
      }
    } catch (Exception e) {
      RobotLog.e("AndroidSoundPool.play - caught " + e);
    }
  }

  @SuppressWarnings({"unused"})
  @JavascriptInterface
  public void stop() {
    checkIfStopRequested();
    try {
      if (streamId != null) {
        soundPool.stop(streamId.intValue());
        streamId = null;
      }
    } catch (Exception e) {
      RobotLog.e("AndroidSoundPool.stop - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public float getVolume() {
    checkIfStopRequested();
    return volume;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void setVolume(float volume) {
    checkIfStopRequested();
    if (volume >= 0.0f && volume <= 1.0f) {
      this.volume = volume;
    } else {
      RobotLog.e("AndroidSoundPool.setVolume - volume range is 0.0 to 1.0.");
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public float getRate() {
    checkIfStopRequested();
    return rate;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void setRate(float rate) {
    checkIfStopRequested();
    if (rate >= 0.5f && rate <= 2.0f) {
      this.rate = rate;
    } else {
      RobotLog.e("AndroidSoundPool.setRate - rate range is 0.5 to 2.0.");
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public int getLoop() {
    checkIfStopRequested();
    return loop;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void setLoop(int loop) {
    checkIfStopRequested();
    if (loop >= -1) {
      this.loop = loop;
    } else {
      RobotLog.e("AndroidSoundPool.setRate - loop value is invalid.");
    }
  }
}
