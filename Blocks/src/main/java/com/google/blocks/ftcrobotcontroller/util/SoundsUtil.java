// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller.util;

import android.text.Html;
import android.util.Base64;
import java.io.File;
import java.io.IOException;

/**
 * A class that provides utility methods related to sound files.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
public class SoundsUtil {
  public static final File SOUNDS_DIR = new File(FileUtil.BLOCKS_DIR, "sounds");
  static final String VALID_SOUND_REGEX =
      "^[a-zA-Z0-9 \\!\\#\\$\\%\\&\\'\\(\\)\\+\\,\\-\\.\\;\\=\\@\\[\\]\\^_\\{\\}\\~]+$";

  // Prevent instantiation of utility class.
  private SoundsUtil() {
  }

  /**
   * Returns the names of existing sound files.
   */
  public static String fetchSounds() throws IOException {
    File[] files = SOUNDS_DIR.listFiles();
    if (files != null) {
      StringBuilder jsonSounds = new StringBuilder();
      jsonSounds.append("[");
      String delimiter = "";
      for (int i = 0; i < files.length; i++) {
        String name = files[i].getName();
        jsonSounds.append(delimiter)
            .append("{")
            .append("\"name\":\"").append(name).append("\", ")
            .append("\"escapedName\":\"").append(Html.escapeHtml(name)).append("\", ")
            .append("\"dateModifiedMillis\":").append(files[i].lastModified())
            .append("}");
        delimiter = ",";
      }
      jsonSounds.append("]");
      return jsonSounds.toString();
    }
    return "[]";
  }

  /**
   * Returns true if the given sound name is not null and contains only valid characters.
   * This function does not check whether the sound exists.
   */
  public static boolean isValidSoundName(String soundName) {
    if (soundName != null) {
      return soundName.matches(VALID_SOUND_REGEX);
    }
    return false;
  }

  /**
   * Returns the content of the sound file with the given name.
   *
   * @param soundName the name of the sound
   */
  public static String fetchSoundFileContent(String soundName) throws IOException {
    if (!isValidSoundName(soundName)) {
      throw new IllegalArgumentException();
    }
    byte[] content = FileUtil.readBinaryFile(new File(SOUNDS_DIR, soundName));
    return Base64.encodeToString(content, Base64.DEFAULT);
  }

  /**
   * Save a sound file.
   *
   * @param soundName the name of the sound
   * @param content the content to write to the sound file.
   */
  public static void saveSoundFile(String soundName, String base64Content) throws IOException {
    if (!isValidSoundName(soundName)) {
      throw new IllegalArgumentException();
    }
    if (!SOUNDS_DIR.exists()) {
      SOUNDS_DIR.mkdirs();
    }
    byte[] content = Base64.decode(base64Content, Base64.DEFAULT);
    FileUtil.writeBinaryFile(new File(SOUNDS_DIR, soundName), content);
  }

  /**
   * Renames the sound file with the given name.
   *
   * @param oldSoundName the old name of the sound
   * @param newSoundName the new name of the sound
   */
  public static void renameSound(String oldSoundName, String newSoundName)
      throws IOException {
    if (!isValidSoundName(oldSoundName) || !isValidSoundName(newSoundName)) {
      throw new IllegalArgumentException();
    }
    if (!SOUNDS_DIR.exists()) {
      SOUNDS_DIR.mkdirs();
    }
    File oldFile = new File(SOUNDS_DIR, oldSoundName);
    File newFile = new File(SOUNDS_DIR, newSoundName);
    oldFile.renameTo(newFile);
  }

  /**
   * Copies the sound file with the given name.
   *
   * @param oldSoundName the old name of the sound
   * @param newSoundName the new name of the sound
   */
  public static void copySound(String oldSoundName, String newSoundName)
      throws IOException {
    if (!isValidSoundName(oldSoundName) || !isValidSoundName(newSoundName)) {
      throw new IllegalArgumentException();
    }
    if (!SOUNDS_DIR.exists()) {
      SOUNDS_DIR.mkdirs();
    }

    File oldFile = new File(SOUNDS_DIR, oldSoundName);
    File newFile = new File(SOUNDS_DIR, newSoundName);
    FileUtil.copyFile(oldFile, newFile);
  }

  /**
   * Delete the sound files with the given names.
   *
   * @param soundNames the names of the sounds to delete
   */
  public static boolean deleteSounds(String[] soundNames) {
    for (String soundName : soundNames) {
      if (!isValidSoundName(soundName)) {
        throw new IllegalArgumentException();
      }
    }
    boolean success = true;
    for (String soundName : soundNames) {
      File soundFile = new File(SOUNDS_DIR, soundName);
      if (soundFile.exists()) {
        if (!soundFile.delete()) {
          success = false;
        }
      }
    }
    return success;
  }

  public static String getPathForSound(String soundName) {
    return new File(SOUNDS_DIR, soundName).getAbsolutePath();
  }
}
