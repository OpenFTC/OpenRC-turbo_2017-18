// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller.util;

import static com.google.blocks.ftcrobotcontroller.util.FileUtil.BLOCKS_DIR;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

import java.io.File;
import java.io.IOException;

/**
 * A class that provides utility methods related to blocks clipboard.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
public class ClipboardUtil {
  private static final File CLIPBOARD_FILE = new File(BLOCKS_DIR, "clipboard.xml");

  // Prevent instantiation of utility class.
  private ClipboardUtil() {
  }

  /**
   * Saves the clipboard content.
   *
   * @param clipboardContent the clipboard content to write.
   */
  public static void saveClipboardContent(String clipboardContent) throws IOException {
    AppUtil.getInstance().ensureDirectoryExists(BLOCKS_DIR);
    FileUtil.writeFile(CLIPBOARD_FILE, clipboardContent);
  }

  /**
   * Reads the clipboard content.
   */
  public static String fetchClipboardContent() throws IOException {
    return FileUtil.readFile(CLIPBOARD_FILE);
  }
}
