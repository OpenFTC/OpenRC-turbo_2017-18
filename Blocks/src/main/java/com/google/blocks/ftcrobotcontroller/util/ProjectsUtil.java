// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller.util;

import static com.google.blocks.ftcrobotcontroller.util.FileUtil.BLOCKS_DIR;
import static org.firstinspires.ftc.robotcore.internal.system.AppUtil.BLOCKS_BLK_EXT;
import static org.firstinspires.ftc.robotcore.internal.system.AppUtil.BLOCKS_JS_EXT;

import android.content.res.AssetManager;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.Xml;

import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.Supplier;
import org.firstinspires.ftc.robotcore.external.ThrowingCallable;
import org.firstinspires.ftc.robotcore.internal.files.FileBasedLock;
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeMeta;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.system.Assert;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;


import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * A class that provides utility methods related to blocks projects.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
@SuppressWarnings("WeakerAccess")
public class ProjectsUtil {

  public static final String TAG = "ProjectsUtil";

  private static final File PROJECTS_LOCK = new File(BLOCKS_DIR, "/projectslock/");
  static final String VALID_PROJECT_REGEX =
      "^[a-zA-Z0-9 \\!\\#\\$\\%\\&\\'\\(\\)\\+\\,\\-\\.\\;\\=\\@\\[\\]\\^_\\{\\}\\~]+$";
  private static final String XML_END_TAG = "</xml>";
  private static final String XML_TAG_EXTRA = "Extra";
  private static final String XML_TAG_OP_MODE_META = "OpModeMeta";
  private static final String XML_ATTRIBUTE_FLAVOR = "flavor";
  private static final String XML_ATTRIBUTE_GROUP = "group";
  private static final String XML_TAG_ENABLED = "Enabled";
  private static final String XML_ATTRIBUTE_VALUE = "value";
  private static final String BLOCKS_SAMPLES_PATH = "blocks/samples";
  private static final String DEFAULT_BLOCKS_SAMPLE_NAME = "default";

  private static final OpModeMeta.Flavor DEFAULT_FLAVOR = OpModeMeta.Flavor.TELEOP;

  // Prevent instantiation of utility class.
  private ProjectsUtil() {
  }

  /** prevents the set of project files from changing while lock is held */
  protected static <T> T lockProjectsWhile(Supplier<T> supplier) {
    try {
      return (new FileBasedLock(PROJECTS_LOCK)).lockWhile(supplier);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      return null;
    }
  }

  protected static <T,E extends Throwable> T lockProjectsWhile(final ThrowingCallable<T,E> callable) throws E {
    try {
      return (new FileBasedLock(PROJECTS_LOCK)).lockWhile(callable);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      return null;
    }
  }

  /**
   * Returns the names and last modified time of existing blocks projects that have a blocks file.
   */
  public static String fetchProjectsWithBlocks() {
    return lockProjectsWhile(new Supplier<String>() {
      @Override public String get() {
        File[] files = BLOCKS_DIR.listFiles(new FilenameFilter() {
          @Override
          public boolean accept(File dir, String filename) {
            if (filename.endsWith(BLOCKS_BLK_EXT)) {
              String projectName = filename.substring(0, filename.length() - BLOCKS_BLK_EXT.length());
              return isValidProjectName(projectName);
            }
            return false;
          }
        });
        if (files != null) {
          StringBuilder jsonProjects = new StringBuilder();
          jsonProjects.append("[");
          String delimiter = "";
          for (int i = 0; i < files.length; i++) {
            String filename = files[i].getName();
            String projectName = filename.substring(0, filename.length() - BLOCKS_BLK_EXT.length());
            boolean enabled = isProjectEnabled(projectName);
            jsonProjects.append(delimiter)
                .append("{")
                .append("\"name\":\"").append(projectName).append("\", ")
                .append("\"escapedName\":\"").append(Html.escapeHtml(projectName)).append("\", ")
                .append("\"dateModifiedMillis\":").append(files[i].lastModified()).append(", ")
                .append("\"enabled\":").append(enabled)
                .append("}");
            delimiter = ",";
          }
          jsonProjects.append("]");
          return jsonProjects.toString();
        }
        return "[]";
      }
    });
  }

  /**
   * Returns the names of blocks samples
   */
  public static String fetchSamples() throws IOException {
    StringBuilder jsonSamples = new StringBuilder();
    jsonSamples.append("[");

    AssetManager assetManager = AppUtil.getDefContext().getAssets();
    List<String> sampleFileNames = Arrays.asList(assetManager.list(BLOCKS_SAMPLES_PATH));
    Collections.sort(sampleFileNames);
    if (sampleFileNames != null) {
      String delimiter = "";
      for (String filename : sampleFileNames) {
        if (filename.endsWith(BLOCKS_BLK_EXT)) {
          String sampleName = filename.substring(0, filename.length() - BLOCKS_BLK_EXT.length());
          if (!sampleName.equals(DEFAULT_BLOCKS_SAMPLE_NAME)) {
            jsonSamples.append(delimiter)
                .append("\"").append(sampleName).append("\"");
            delimiter = ",";
          }
        }
      }
    }
    jsonSamples.append("]");
    return jsonSamples.toString();
  }

  /**
   * Returns the {@link OpModeMeta} for existing blocks projects that have a JavaScript file and
   * are enabled.
   */
  public static List<OpModeMeta> fetchEnabledProjectsWithJavaScript() {
    return lockProjectsWhile(new Supplier<List<OpModeMeta>>() {
      @Override public List<OpModeMeta> get() {
        String[] files = BLOCKS_DIR.list(new FilenameFilter() {
          @Override
          public boolean accept(File dir, String filename) {
            if (filename.endsWith(BLOCKS_JS_EXT)) {
              String projectName = filename.substring(0, filename.length() - BLOCKS_JS_EXT.length());
              return isValidProjectName(projectName);
            }
            return false;
          }
        });
        List<OpModeMeta> projects = new ArrayList<OpModeMeta>();
        if (files != null) {
          for (int i = 0; i < files.length; i++) {
            String projectName = files[i].substring(0, files[i].length() - BLOCKS_JS_EXT.length());
            OpModeMeta opModeMeta = fetchOpModeMeta(projectName);
            if (opModeMeta != null) {
              projects.add(opModeMeta);
            }
          }
        }
        return projects;
      }
    });
  }

  @Nullable
  private static OpModeMeta fetchOpModeMeta(String projectName) {
    if (!isValidProjectName(projectName)) {
      throw new IllegalArgumentException();
    }
    try {
      String blkFileContent = FileUtil.readFile(new File(BLOCKS_DIR, projectName + BLOCKS_BLK_EXT));
      // The extraXml is after the first </xml>.
      int i = blkFileContent.indexOf(XML_END_TAG);
      Assert.assertTrue(i != -1);
      String extraXml = blkFileContent.substring(i + XML_END_TAG.length());
      // Return null if the project is not enabled.
      if (!isProjectEnabled(projectName, extraXml)) {
        return null;
      }
      return createOpModeMeta(projectName, extraXml);
    } catch (IOException e) {
      RobotLog.e("ProjectsUtil.fetchOpModeMeta(\"" + projectName + "\") - failed.");
      RobotLog.logStackTrace(e);
      return null;
    }
  }

  /**
   * Returns true if the given project name is not null and contains only valid characters.
   * This function does not check whether the project exists.
   */
  public static boolean isValidProjectName(String projectName) {
    if (projectName != null) {
      return projectName.matches(VALID_PROJECT_REGEX);
    }
    return false;
  }

  /**
   * Returns the content of the blocks file with the given project name. The returned content
   * may include extra XML after the blocks XML.
   *
   * @param projectName the name of the project
   */
  public static String fetchBlkFileContent(String projectName) throws IOException {
    if (!isValidProjectName(projectName)) {
      throw new IllegalArgumentException();
    }
    String blkFileContent = FileUtil.readFile(new File(BLOCKS_DIR, projectName + BLOCKS_BLK_EXT));

    // Separate the blocksContent from the extraXml, so we can upgrade the blocksContent.
    // The extraXml is after the first </xml>.
    int i = blkFileContent.indexOf(XML_END_TAG);
    Assert.assertTrue(i != -1);
    String blocksContent = blkFileContent.substring(0, i + XML_END_TAG.length());
    String extraXml = blkFileContent.substring(i + XML_END_TAG.length());

    String upgradedBlocksContent = HardwareUtil.upgradeBlocks(blocksContent);
    if (!upgradedBlocksContent.equals(blocksContent)) {
      blkFileContent = upgradedBlocksContent + extraXml;
    }

    return blkFileContent;
  }

  /**
   * Returns the content of the JavaScript file with the given project name.
   *
   * @param projectName the name of the project
   */
  public static String fetchJsFileContent(String projectName) throws IOException {
    if (!isValidProjectName(projectName)) {
      throw new IllegalArgumentException();
    }
    return FileUtil.readFile(new File(BLOCKS_DIR, projectName + BLOCKS_JS_EXT));
  }

  /**
   * Returns the content of the blocks file for a new project. Note that this method does not save
   * the project. It just creates the content.
   *
   * @param projectName the name of the project
   * @param sampleName the name of the sample to copy.
   * @return the content of the blocks file
   */
  public static String newProject(String projectName, String sampleName) throws IOException {
    if (!isValidProjectName(projectName)) {
      throw new IllegalArgumentException();
    }

    if (sampleName == null || sampleName.isEmpty()) {
      sampleName = DEFAULT_BLOCKS_SAMPLE_NAME;
    }

    StringBuilder blkFileContent = new StringBuilder();
    AssetManager assetManager = AppUtil.getDefContext().getAssets();
    String assetName = BLOCKS_SAMPLES_PATH + "/" + sampleName + BLOCKS_BLK_EXT;
    BufferedReader reader = new BufferedReader(new InputStreamReader(assetManager.open(assetName)));
    try {
      String line = null;
      while ((line = reader.readLine()) != null) {
        blkFileContent.append(line).append("\n");
      }
    } finally {
      reader.close();
    }
    return blkFileContent.toString();
  }

  /**
   * Save the blocks file and JavaScript file with the given project name.
   *
   * @param projectName the name of the project
   * @param blkContent the content to write to the blocks file.
   * @param jsFileContent the content to write to the JavaScript file.
   */
  public static void saveProject(final String projectName, final String blkContent, final String jsFileContent,
      @Nullable final String flavor, @Nullable final String group, final boolean enable)
      throws IOException {

    if (!isValidProjectName(projectName)) {
      throw new IllegalArgumentException();
    }

    lockProjectsWhile(new ThrowingCallable<Void, IOException>() {
      @Override public Void call() throws IOException {
        AppUtil.getInstance().ensureDirectoryExists(BLOCKS_DIR, false);

        String extraXml = "";
        // If flavor and group are not null, then blkContent does not contain the extra xml to
        // specify OpModeMeta flavor and group. We need to generate and add the extra xml now.
        if (flavor != null && group != null) {
          try {
            // Convert flavor from string to OpModeMeta.Flavor to ensure its validity.
            extraXml = formatExtraXml(
                OpModeMeta.Flavor.valueOf(flavor.toUpperCase(Locale.ENGLISH)), group, enable);
          } catch (IOException e) {
            RobotLog.ee(TAG, e, "ProjectsUtil.saveProject(\"" + projectName + "\") - failed to format extra xml.");
          }
        }
        FileUtil.writeFile(new File(BLOCKS_DIR, projectName + BLOCKS_BLK_EXT), blkContent + extraXml);
        FileUtil.writeFile(new File(BLOCKS_DIR, projectName + BLOCKS_JS_EXT), jsFileContent);
        return null;
      }
    });
  }

  /**
   * Renames the blocks file and JavaScript file with the given project name.
   *
   * @param oldProjectName the old name of the project
   * @param newProjectName the new name of the project
   */
  public static void renameProject(final String oldProjectName, final String newProjectName)
      throws IOException {
    if (!isValidProjectName(oldProjectName) || !isValidProjectName(newProjectName)) {
      throw new IllegalArgumentException();
    }
    lockProjectsWhile(new ThrowingCallable<Void, IOException>() {
      @Override public Void call() throws IOException {
        AppUtil.getInstance().ensureDirectoryExists(BLOCKS_DIR, false);

        File oldBlk = new File(BLOCKS_DIR, oldProjectName + BLOCKS_BLK_EXT);
        File newBlk = new File(BLOCKS_DIR, newProjectName + BLOCKS_BLK_EXT);
        if (oldBlk.renameTo(newBlk)) {
          File oldJs = new File(BLOCKS_DIR, oldProjectName + BLOCKS_JS_EXT);
          File newJs = new File(BLOCKS_DIR, newProjectName + BLOCKS_JS_EXT);
          oldJs.renameTo(newJs);
        }
        return null;
      }
    });

  }

  /**
   * Copies the blocks file and JavaScript file with the given project name.
   *
   * @param oldProjectName the old name of the project
   * @param newProjectName the new name of the project
   */
  public static void copyProject(final String oldProjectName, final String newProjectName)
      throws IOException {
    if (!isValidProjectName(oldProjectName) || !isValidProjectName(newProjectName)) {
      throw new IllegalArgumentException();
    }
    lockProjectsWhile(new ThrowingCallable<Void, IOException>() {
      @Override public Void call() throws IOException {
        AppUtil.getInstance().ensureDirectoryExists(BLOCKS_DIR, false);

        File oldBlk = new File(BLOCKS_DIR, oldProjectName + BLOCKS_BLK_EXT);
        File newBlk = new File(BLOCKS_DIR, newProjectName + BLOCKS_BLK_EXT);
        FileUtil.copyFile(oldBlk, newBlk);
        File oldJs = new File(BLOCKS_DIR, oldProjectName + BLOCKS_JS_EXT);
        File newJs = new File(BLOCKS_DIR, newProjectName + BLOCKS_JS_EXT);
        FileUtil.copyFile(oldJs, newJs);
        return null;
      }
    });

  }

  /**
   * Enables (or disables) the project with the given name.
   *
   * @param projectName the name of the project
   * @param enable whether to enable (or disable) the project
   */
  public static void enableProject(final String projectName, final boolean enable)
      throws IOException {

    if (!isValidProjectName(projectName)) {
      throw new IllegalArgumentException();
    }

    lockProjectsWhile(new ThrowingCallable<Void, IOException>() {
      @Override public Void call() throws IOException {
        File blkFile = new File(BLOCKS_DIR, projectName + BLOCKS_BLK_EXT);
        String blkFileContent = FileUtil.readFile(blkFile);

        // Separate the blocksContent from the extraXml, so we can extract the OpModeMeta from the extraXml.
        // The extraXml is after the first </xml>.
        int i = blkFileContent.indexOf(XML_END_TAG);
        Assert.assertTrue(i != -1);
        String blocksContent = blkFileContent.substring(0, i + XML_END_TAG.length());
        String extraXml = blkFileContent.substring(i + XML_END_TAG.length());
        OpModeMeta opModeMeta = createOpModeMeta(projectName, extraXml);

        // Regenerate the extra xml with the enable argument.
        final String newBlkFileContent = blocksContent +
            formatExtraXml(opModeMeta.flavor, opModeMeta.group, enable);
        FileUtil.writeFile(blkFile, newBlkFileContent);
        return null;
      }
    });
  }

  /**
   * Delete the blocks and JavaScript files for the given project names.
   *
   * @param projectNames the names of the projects to delete
   */
  public static Boolean deleteProjects(final String[] projectNames) {

    return lockProjectsWhile(new Supplier<Boolean>() {
      @Override public Boolean get() {
        for (String projectName : projectNames) {
          if (!isValidProjectName(projectName)) {
            throw new IllegalArgumentException();
          }
        }
        boolean success = true;
        for (String projectName : projectNames) {
          File jsFile = new File(BLOCKS_DIR, projectName + BLOCKS_JS_EXT);
          if (jsFile.exists()) {
            if (!jsFile.delete()) {
              success = false;
            }
          }
          if (success) {
            File blkFile = new File(BLOCKS_DIR, projectName + BLOCKS_BLK_EXT);
            if (blkFile.exists()) {
              if (!blkFile.delete()) {
                success = false;
              }
            }
          }
        }
        return success;
      }
    });
  }

  /**
   * Formats the extra XML.
   */
  private static String formatExtraXml(OpModeMeta.Flavor flavor, String group, boolean enabled) throws IOException {
    XmlSerializer serializer = Xml.newSerializer();
    StringWriter writer = new StringWriter();
    serializer.setOutput(writer);
    serializer.startDocument("UTF-8", true);
    serializer.startTag("", XML_TAG_EXTRA);
    serializer.startTag("", XML_TAG_OP_MODE_META);
    serializer.attribute("", XML_ATTRIBUTE_FLAVOR, flavor.toString());
    serializer.attribute("", XML_ATTRIBUTE_GROUP, group);
    serializer.endTag("", XML_TAG_OP_MODE_META);
    serializer.startTag("", XML_TAG_ENABLED);
    serializer.attribute("", XML_ATTRIBUTE_VALUE, Boolean.toString(enabled));
    serializer.endTag("", XML_TAG_ENABLED);
    serializer.endTag("", XML_TAG_EXTRA);
    serializer.endDocument();
    return writer.toString();
  }

  /**
   * Creates an {@link OpModeMeta} instance with the given name, extracting the flavor and group
   * from the given extraXml.
   */
  private static OpModeMeta createOpModeMeta(String projectName, String extraXml) {
    OpModeMeta.Flavor flavor = DEFAULT_FLAVOR;
    String group = "";

    try {
      XmlPullParser parser = Xml.newPullParser();
      parser.setInput(new StringReader(extraXml));
      int eventType = parser.getEventType();
      while (eventType != XmlPullParser.END_DOCUMENT) {
        if (eventType == XmlPullParser.START_TAG) {
          if (parser.getName().equals(XML_TAG_OP_MODE_META)) {
            for (int i = 0; i < parser.getAttributeCount(); i++) {
              String name = parser.getAttributeName(i);
              String value = parser.getAttributeValue(i);
              if (name.equals(XML_ATTRIBUTE_FLAVOR)) {
                flavor = OpModeMeta.Flavor.valueOf(value.toUpperCase(Locale.ENGLISH));
              } else if (name.equals(XML_ATTRIBUTE_GROUP)) {
                if (!value.isEmpty() && !value.equals(OpModeMeta.DefaultGroup)) {
                  group = value;
                }
              }
            }
          }
        }
        eventType = parser.next();
      }
    } catch (XmlPullParserException|IOException e) {
      RobotLog.e("ProjectsUtil.createOpmodeMeta(\"" + projectName + "\", ...) - failed to parse xml.");
      RobotLog.logStackTrace(e);
    }

    return new OpModeMeta(projectName, flavor, group);
  }

  private static boolean isProjectEnabled(String projectName) {
    if (!isValidProjectName(projectName)) {
      throw new IllegalArgumentException();
    }
    try {
      String blkFileContent = FileUtil.readFile(new File(BLOCKS_DIR, projectName + BLOCKS_BLK_EXT));
      // The extraXml is after the first </xml>.
      int i = blkFileContent.indexOf(XML_END_TAG);
      Assert.assertTrue(i != -1);
      String extraXml = blkFileContent.substring(i + XML_END_TAG.length());
      return isProjectEnabled(projectName, extraXml);
    } catch (IOException e) {
      RobotLog.e("ProjectsUtil.isProjectEnabled(\"" + projectName + "\") - failed.");
      RobotLog.logStackTrace(e);
      return true;
    }
  }

  /**
   * Returns false if the given extraXml contains the tag/attribute for enabling a project and the
   * value of attribute is false. Otherwise it returns true.
   */
  private static boolean isProjectEnabled(String projectName, String extraXml) {
    boolean enabled = true;

    try {
      XmlPullParser parser = Xml.newPullParser();
      parser.setInput(new StringReader(extraXml));
      int eventType = parser.getEventType();
      while (eventType != XmlPullParser.END_DOCUMENT) {
        if (eventType == XmlPullParser.START_TAG) {
          if (parser.getName().equals(XML_TAG_ENABLED)) {
            for (int i = 0; i < parser.getAttributeCount(); i++) {
              String name = parser.getAttributeName(i);
              String value = parser.getAttributeValue(i);
              if (name.equals(XML_ATTRIBUTE_VALUE)) {
                enabled = Boolean.parseBoolean(value);
              }
            }
          }
        }
        eventType = parser.next();
      }
    } catch (XmlPullParserException|IOException e) {
      RobotLog.e("ProjectsUtil.isProjectEnabled(\"" + projectName + "\", ...) - failed to parse xml.");
      RobotLog.logStackTrace(e);
    }

    return enabled;
  }
}
