/*
 * Copyright (c) 2017 David Sargent
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of NAME nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior
 * written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESSFOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.google.blocks.ftcrobotcontroller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.blocks.ftcrobotcontroller.util.HardwareItem;
import com.google.blocks.ftcrobotcontroller.util.HardwareItemMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.qualcomm.robotcore.eventloop.opmode.FtcRobotControllerServiceState;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.LegacyModule;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ReadWriteFile;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.Predicate;
import org.firstinspires.ftc.robotcore.internal.collections.SimpleGson;
import org.firstinspires.ftc.robotcore.internal.files.FileModifyObserver;
import org.firstinspires.ftc.robotcore.internal.opmode.OnBotJavaClassLoader;
import org.firstinspires.ftc.robotcore.internal.opmode.OnBotJavaManager;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.webserver.MimeTypesUtil;
import org.firstinspires.ftc.robotcore.internal.webserver.WebHandler;
import org.firstinspires.ftc.robotcore.internal.webserver.WebHandlerManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import fi.iki.elonen.NanoHTTPD;

import static fi.iki.elonen.NanoHTTPD.MIME_PLAINTEXT;
import static fi.iki.elonen.NanoHTTPD.newChunkedResponse;
import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

@SuppressWarnings("ResultOfMethodCallIgnored")
class JavaModeWebHandlers {
    private final String TAG = JavaModeWebHandlers.class.getSimpleName();
    private static final String URI_JAVA_PREFIX = "/java";
    @SuppressWarnings("unused") // required for the OnBotUI
    private static final String URI_JAVA_EDITOR = URI_JAVA_PREFIX + "/editor.html";

    // File Tasks
    private static final String URI_FILE_COPY = URI_JAVA_PREFIX + "/file/copy";
    private static final String URI_FILE_DELETE = URI_JAVA_PREFIX + "/file/delete";
    private static final String URI_FILE_DOWNLOAD = URI_JAVA_PREFIX + "/file/download";
    private static final String URI_FILE_GET = URI_JAVA_PREFIX + "/file/get";
    private static final String URI_FILE_NEW = URI_JAVA_PREFIX + "/file/new";
    private static final String URI_FILE_SAVE = URI_JAVA_PREFIX + "/file/save";
    private static final String URI_FILE_TEMPLATES = URI_JAVA_PREFIX + "/file/templates";
    private static final String URI_FILE_TREE = URI_JAVA_PREFIX + "/file/tree";
    private static final String URI_FILE_UPLOAD = URI_JAVA_PREFIX + "/file/upload";
    private static final String URI_JS_AUTOCOMPLETE = URI_JAVA_PREFIX + "/js/editor/autocomplete";
    // Build tasks
    private static final String URI_BUILD_LAUNCH = URI_JAVA_PREFIX + "/build/start";
    private static final String URI_BUILD_LOG = URI_JAVA_PREFIX + "/build/log";
    private static final String URI_BUILD_STATUS = URI_JAVA_PREFIX + "/build/status";
    private static final String URI_BUILD_WAIT = URI_JAVA_PREFIX + "/build/wait";
    private static final String URI_JS_SETTINGS = URI_JAVA_PREFIX + "/js/settings.js";
    // Admin tasks
    private static final String URI_ADMIN_CLEAN = URI_JAVA_PREFIX + "/admin/clean";
    private static final String URI_ADMIN_REARM = URI_JAVA_PREFIX + "/admin/rearm";
    private static final String URI_ADMIN_SETTINGS = URI_JAVA_PREFIX + "/admin/settings";
    private static final String URI_ADMIN_SETTINGS_RESET = URI_ADMIN_SETTINGS + "/reset";
    private static final String URI_ADMIN_RESET_ONBOTJAVA = URI_JAVA_PREFIX + "/admin/factory_reset";

    private final static String MIME_JSON = "application/json";

    private static final String REQUEST_KEY_FILE = "f";
    private static final String REQUEST_KEY_NEW = "new";
    private static final String REQUEST_KEY_SAVE = "data";
    private static final String REQUEST_KEY_TEMPLATE = "template";
    private static final String REQUEST_KEY_OPMODE_NAME = "opModeName";
    private static final String REQUEST_KEY_TEAM_NAME = "teamName";
    private static final String REQUEST_KEY_SETUP_HARDWARE = "rcSetupHardware";
    private static final String REQUEST_KEY_OPMODE_ANNOTATIONS = "opModeAnnotations";
    private static final String REQUEST_KEY_ID = "id";
    private static final String REQUEST_KEY_COPY_FROM = "origin";
    private static final String REQUEST_KEY_COPY_TO = "dest";
    private static final String REQUEST_KEY_DELETE = "delete";
    private static final String REQUEST_KEY_PRESERVE = "preserve";

    private final String PATH_SEPARATOR = "/";
    private final String EXT_TEMP_FILE = ".tmp";
    private final String EXT_JAVA_FILE = ".java";
    private final String EXT_ZIP_FILE = ".zip";

    private final static String VALID_TEMPLATE_FILE_REGEX =
            "templates/([\\w.\\d/_]+/)*([\\w.\\d_]+\\.(?:java|txt|md|/)|[\\w\\d_]+)$";
    private final static String VALID_SRC_FILE_REGEX =
            "/(?:src|jars)[\\w.\\d/_$]+[\\w.\\d_$]+\\.(?:java|jar|zip|txt|md)$";
    private final static String VALID_SRC_FILE_OR_FOLDER_REGEX =
            "(?:/(?:src|jars)[\\w.\\d/_$]+[\\w.\\d_$]+(?:\\.(?:java|jar|zip|txt|md)|/)|/|)$";

    private static final List<Class<? extends HardwareDevice>> HARDWARE_TYPES_PREVENTED_FROM_HARDWARE_SETUP = Arrays.asList(DcMotorController.class,ServoController.class,VoltageSensor.class,LegacyModule.class);

    private final BuildMonitor buildMonitor;
    private final Gson gson;
    private final SharedPreferences sharedPrefs;

    private EditorSettings editorSettings;
    private final File templatesDir;
    private FtcRobotControllerServiceState serviceState;

    private enum LineEndings {
        WINDOWS("\r\n"), UNIX("\n");
        final String lineEnding;

        LineEndings(String ending) {
            lineEnding = ending;
        }
    }

    JavaModeWebHandlers() {
        gson = new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .create();
        sharedPrefs = AppUtil.getDefContext()
                .getSharedPreferences(JavaModeWebHandlers.class.getName(), Context.MODE_PRIVATE);
        editorSettings = new EditorSettings(sharedPrefs);
        buildMonitor = new BuildMonitor();
        templatesDir = new File(OnBotJavaManager.javaRoot, "templates");
    }

    void setState(FtcRobotControllerServiceState state) {
        this.serviceState = state;
    }

    /**
     * Verifies a file is web-accessible
     *
     * @param fileName          the file name being accessed
     * @param regularExpression a regular expression specifying the file name, extensions, and possible
     *                          locations
     * @return {@code true} if the file is web-accessible, otherwise {@code false}
     */
    private boolean isValidFileLocation(String fileName, String regularExpression) {
        return fileName.matches(regularExpression) &&
                !fileName.matches("\\.{2}");
    }

    private NanoHTTPD.Response getFile(Map<String, List<String>> data) {
        return getFile(data, false, null);
    }

    private NanoHTTPD.Response getFile(Map<String, List<String>> data, boolean folderAsZip, String lineEndings) {
        if (!data.containsKey(REQUEST_KEY_FILE)) return badRequest();
        String trimmedUri = data.get(REQUEST_KEY_FILE).get(0);
        final String filePath = OnBotJavaManager.javaRoot.getAbsolutePath() + trimmedUri;
        if (isValidFileLocation(trimmedUri, VALID_SRC_FILE_REGEX)) { // is a file
            try {
                if (lineEndings == null) {
                    return serveFile(filePath);
                } else {
                    return serveFile(filePath, lineEndings);
                }
            } catch (FileNotFoundException e) {
                return fileNotFound();
            }
        } else if (isValidFileLocation(trimmedUri, VALID_SRC_FILE_OR_FOLDER_REGEX) || trimmedUri.equals("/src/")) { // is a folder
            if (folderAsZip) {
                final File sourceFolder = new File(filePath);
                final File tempFolder;
                try {
                    tempFolder = File.createTempFile("onbotjava", EXT_TEMP_FILE, AppUtil.getDefContext().getCacheDir());
                } catch (IOException e) {
                    RobotLog.ee(TAG, e, "Cannot create temp file for zip");
                    return serverError();
                }
                tempFolder.delete();
                tempFolder.mkdirs();
                final File outputZipFile = new File(tempFolder, filePath.substring(filePath.lastIndexOf(PATH_SEPARATOR) + 1) +  EXT_ZIP_FILE);
                try (FileOutputStream destOutput = new FileOutputStream(outputZipFile)) {
                    try (final ZipOutputStream zipOutputStream = new ZipOutputStream(destOutput)) {
                        zipOutputStream.setMethod(ZipOutputStream.DEFLATED);
                        forEachInFolder(sourceFolder, true, new Predicate<File>() {
                            @Override
                            public boolean test(File file) {
                                try {
                                    String entryName = file.getAbsolutePath();
                                    if (entryName.endsWith(EXT_TEMP_FILE)) return true;

                                    entryName = entryName.substring(sourceFolder.getAbsolutePath().length());
                                    if (entryName.startsWith(PATH_SEPARATOR)) entryName = entryName.substring(1);
                                    if (file.isDirectory() && !entryName.endsWith(PATH_SEPARATOR)) entryName += PATH_SEPARATOR;

                                    ZipEntry entry = new ZipEntry(entryName);
                                    zipOutputStream.putNextEntry(entry);
                                    if (!file.isDirectory()) {
                                        try (FileInputStream inputStream =  new FileInputStream(file)) {
                                            AppUtil.getInstance().copyStream(inputStream, zipOutputStream);
                                        }
                                    }
                                    zipOutputStream.closeEntry();
                                } catch (IOException ex) {
                                    RobotLog.ww(TAG, ex, "Cannot save file \"%s\" in zip", file.getAbsolutePath());
                                }

                                return true; // this result is ignored
                            }
                        });
                    }
                } catch (IOException ex) {
                    RobotLog.ee(TAG, ex, "Cannot create zip file");
                    return serverError();
                }

                // These files should be deleted when the controller's cache is cleared or on exit of the Java VM
                // todo: find a way of clearing these files that doesn't harm NanoHTTPD
                outputZipFile.deleteOnExit();
                tempFolder.deleteOnExit();
                try {
                    return newChunkedResponse(NanoHTTPD.Response.Status.OK, MimeTypesUtil.getMimeType( EXT_ZIP_FILE), new FileInputStream(outputZipFile));
                } catch (FileNotFoundException e) {
                    return serverError();
                }
            } else {
                StringBuilder builder = new StringBuilder("Contents:\n");
                for (String file : new File(filePath).list()) {
                    builder.append(file).append(LineEndings.UNIX.lineEnding);
                }

                return successfulResponse(builder.toString());
            }
        }
        return unauthorizedAccess();
    }

    private void forEachInFolder(File folder, boolean recursive, Predicate<File> action) throws FileNotFoundException {
        if (!folder.isDirectory()) throw new FileNotFoundException("not a directory");
        for (File file : folder.listFiles()) {
            if (recursive && file.isDirectory()) forEachInFolder(file, true, action);
            action.test(file);
        }
    }

    private NanoHTTPD.Response successfulResponse(String message) {
        return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, MIME_PLAINTEXT, message);
    }

    private static NanoHTTPD.Response serverError() {
        return newFixedLengthResponse(
                NanoHTTPD.Response.Status.INTERNAL_ERROR, MIME_PLAINTEXT, "Server Error");
    }

    private NanoHTTPD.Response badRequest(String message) {
        return newFixedLengthResponse(
                NanoHTTPD.Response.Status.BAD_REQUEST, MIME_JSON, message);
    }

    private NanoHTTPD.Response badRequest() {
        return badRequest("{'success':'false'}");
    }

    private NanoHTTPD.Response fileNotFound() {
        return newFixedLengthResponse(
                NanoHTTPD.Response.Status.NOT_FOUND, MIME_PLAINTEXT, "File Not Found!");
    }

    private NanoHTTPD.Response unauthorizedAccess() {
        return newFixedLengthResponse(NanoHTTPD.Response.Status.UNAUTHORIZED, MIME_PLAINTEXT, "Access is not allowed");
    }

    public void close() {
        buildMonitor.close();
    }

    private NanoHTTPD.Response currentBuildLog() {
        try {
            return serveFile(OnBotJavaManager.buildLogFile.getAbsolutePath());
        } catch (FileNotFoundException e) {
            return serverError();
        }
    }

    private boolean copyAsset(String assetPath, File dest, boolean mirror) throws IOException {
        if (assetPath.isEmpty()) throw new IllegalArgumentException("assetPath cannot be empty");
        boolean templatesEnsured = true;
        final AssetManager assetManager = AppUtil.getInstance().getRootActivity().getAssets();
        assetPath = assetPath.endsWith(PATH_SEPARATOR) ? assetPath.substring(0, assetPath.length() - 1) : assetPath;

        String name = assetPath.substring(assetPath.lastIndexOf(PATH_SEPARATOR) + 1);
        final File newDest = new File(dest, name);
        if (mirror && newDest.exists()) {
            AppUtil.getInstance().delete(newDest);
            if (newDest.exists())
                throw new IOException(String.format(Locale.ENGLISH, "Failed to remove %s to in order to create a clean copy", newDest.getAbsolutePath()));
        }

        try {
            final List<String> children = Arrays.asList(assetManager.list(assetPath));

            if (children.isEmpty()) { // asset is a file
                try (InputStream stream = assetManager.open(assetPath)) {
                    try {
                        AppUtil.getInstance().copyStream(stream, newDest);
                    } catch (FileNotFoundException ex) {
                        RobotLog.ww(TAG, ex, "Could not open file %s", newDest.getAbsolutePath());
                        templatesEnsured = false;
                    }
                }
            } else { // asset is a folder
                newDest.mkdirs();

                for (String child : children) {
                    if (!copyAsset(assetPath + PATH_SEPARATOR + child, newDest, mirror)) {
                        templatesEnsured = false;
                    }
                }
            }

        } catch (IOException e) {
            RobotLog.ee(TAG, e, "Cannot copy asset, template data might be invalid");
            templatesEnsured = false;
        }

        return templatesEnsured;
    }

    private boolean ensureTemplates() {
        if (templatesDir.exists() && !templatesDir.isDirectory()) templatesDir.delete();
        final String javaTemplatesDirPath = "java/templates";

        try {
            // Allow the user to keep custom templates, although they are now responsible for maintenance of the templates folder
            return copyAsset(javaTemplatesDirPath, templatesDir.getParentFile(), !(new File(templatesDir, "user").exists()));
        } catch (IOException ex) {
            throw new RuntimeException("ensureTemplates", ex);
        }
    }

    private void searchForFiles(String startPath, File startFile, List<String> results, boolean includeFolders) {
        // fail fast
        if (!startFile.isDirectory())
            throw new IllegalArgumentException("startFile is not a directory");
        if (results == null) throw new NullPointerException("results is null");
        for (File srcFile : startFile.listFiles()) {
            String absolutePath = srcFile.getAbsolutePath();
            absolutePath = absolutePath.startsWith(startPath) ?
                    absolutePath.substring(startPath.length()) : absolutePath;
            // The trailing slash is how clients can tell the result is a folder
            if (srcFile.isDirectory()) absolutePath += PATH_SEPARATOR;
            if (!srcFile.isDirectory() || includeFolders) results.add(absolutePath);
            if (srcFile.isDirectory()) searchForFiles(startPath, srcFile, results, includeFolders);
        }
    }

    private NanoHTTPD.Response serveFile(String uri) throws FileNotFoundException {
        return serveFile(uri, null);
    }

    private NanoHTTPD.Response serveFile(String uri, String lineEnding) throws FileNotFoundException {
        File test = new File(uri);
        uri = test.getAbsolutePath();
        if (!uri.startsWith(AppUtil.FIRST_FOLDER.getAbsolutePath()) && !uri.contains(".."))
            return unauthorizedAccess();
        File file = new File(uri);
        String mime = MimeTypesUtil.determineMimeType(uri);
        if (file.exists() && file.canRead()) {
            FileInputStream reader = new FileInputStream(file);
            if (lineEnding != null) {
                StringBuilder builder = readFile(lineEnding, reader);

                return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, mime, builder.toString());
            } else {
                return newChunkedResponse(NanoHTTPD.Response.Status.OK, mime, reader);
            }
        }
        return fileNotFound();
    }

    @NonNull
    private static StringBuilder readFile(String lineEnding, File file) throws IOException {
        try (FileInputStream inputStream = new FileInputStream(file)) {
            return readFile(lineEnding, inputStream);
        }
    }

    @NonNull
    private static StringBuilder readFile(String lineEnding, FileInputStream reader) {
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader1 = new BufferedReader(new InputStreamReader(reader))) {
            String line;
            while ((line = reader1.readLine()) != null) {
                builder.append(line).append(lineEnding);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return builder;
    }

    private void copyJavaFile(@NonNull File source, @NonNull File dest) throws IOException {
        copyJavaFile(source, dest, null);
    }

    /**
     * Copy a Java source file, correcting the package and class name
     */
    private void copyJavaFile(@NonNull File source, @NonNull File dest, @Nullable String sourcePackageName) throws IOException {
        final String contents = readFile(LineEndings.UNIX.lineEnding, source).toString();

        final String oldPackageName;
        if (sourcePackageName == null) {
            oldPackageName = packageNameFromFile(source);
        } else {
            oldPackageName = sourcePackageName;
        }

        String oldClassName = classNameFromFile(source);

        copyJavaFileFromContents(oldClassName, oldPackageName, contents, dest);
    }

    private void copyJavaFileFromContents(@NonNull String oldClassName, @Nullable String oldPackageName, @NonNull String contents, @NonNull File dest) {
        String newPackageName = packageNameFromFile(dest);

        String newClassName = classNameFromFile(dest);
        String newContents = contents;
        if (oldPackageName != null) {
            newContents = contents.replaceAll("(\\s*)package(\\s+)" + oldPackageName + ";(\\s)",
                    "$1package$2" + newPackageName + ";$3");
        }

        newContents = newContents.replaceAll(
                "(\\s+)(class|interface|@interface)(\\s+)" + oldClassName + "(\\s+)",
                "$1$2$3" + newClassName + "$4");
        ReadWriteFile.writeFile(dest, newContents);
    }

    private String packageNameFromFile(@NonNull File source) {
        String packageName = source.getAbsolutePath();
        packageName = packageName.substring(0, packageName.lastIndexOf(PATH_SEPARATOR));
        if (packageName.indexOf(OnBotJavaManager.srcDir.getAbsolutePath()) == 0) {
            packageName = packageName.substring(OnBotJavaManager.srcDir.getAbsolutePath().length() + 1);
        }
        packageName = packageName.replaceAll(PATH_SEPARATOR, ".");
        return packageName;
    }

    private String classNameFromFile(File source) {
        String oldClassName = source.getName();
        if (oldClassName.endsWith(EXT_JAVA_FILE)) {
            oldClassName = oldClassName.substring(0, oldClassName.length() - EXT_JAVA_FILE.length());
        } else if (oldClassName.contains(".")) {
            oldClassName = oldClassName.substring(0, oldClassName.lastIndexOf("."));
        }

        return oldClassName;
    }

    void register(final WebHandlerManager manager, final ProgrammingWebHandlers.ProgrammingModeWebHandlerDecorator decorator) {
        try {
            // long start = System.currentTimeMillis();
            final Class<?>[] declaredClasses = JavaModeWebHandlers.class.getDeclaredClasses();
            for (Class<?> klazz : declaredClasses) {
                if (!klazz.isAnnotationPresent(RegisterWebHandler.class)) continue;

                WebHandler handler = null;
                try {
                    // Automatically build the WebHandlers instances in this class
                    if (Modifier.isStatic(klazz.getModifiers())) {
                        final Constructor<?> declaredConstructor = klazz.getDeclaredConstructor();
                        declaredConstructor.setAccessible(true);
                        handler = (WebHandler) declaredConstructor.newInstance();
                    } else {
                        final Constructor<?> declaredConstructor = klazz.getDeclaredConstructor(JavaModeWebHandlers.this.getClass());
                        declaredConstructor.setAccessible(true);
                        handler = (WebHandler) declaredConstructor.newInstance(JavaModeWebHandlers.this);
                    }
                } catch (InstantiationException | IllegalAccessException | ClassCastException | NoSuchMethodException e) {
                    RobotLog.ee(TAG, e, "Failed to generate handler '%s'", klazz.getName());
                } catch (InvocationTargetException ex) {
                    RobotLog.ee(TAG, ex.getTargetException(), "Handler threw during construction");
                }

                if (handler == null) continue;
                final RegisterWebHandler registerHandler = klazz.getAnnotation(RegisterWebHandler.class);
                String uri = registerHandler.uri();
                boolean paramGenerator = registerHandler.usesParamGenerator();
                manager.register(uri, decorator.decorate(handler, paramGenerator));
            }

            //RobotLog.dd(TAG, "It took %.3f s. to setup OnBotJava web handlers", (System.currentTimeMillis() - start) / 1000d);
        } catch (Exception ex) {
            RobotLog.ee(TAG, ex, "Failed to register OnBotJava web handlers");
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface RegisterWebHandler {
        String uri();

        boolean usesParamGenerator() default true;
    }

    private static class FileTree {
        final List<String> src;
        final List<String> jars;

        private FileTree(List<String> src, List<String> jars) {
            this.src = src;
            this.jars = jars;
        }
    }

    private static class BuildMonitor {
        final Object buildCompletionNotifier;
        private final Object buildInformationUpdateLock = new Object();

        // Status src
        private final File buildStartedFile;
        private final File buildCompleteFile;

        // Status variables
        private final FileModifyObserver runningObserver;
        private final FileModifyObserver completedObserver;

        /*
         *      <li>status: As the build progresses, several src are written. These src may be
         *                monitored for changes (see {@link FileModifyObserver}) as triggers to take
         *                actions to process the output of the build.
         *                <ol>
         *                      <li>buildStarted.txt:    updated when the build starts</li>
         *                      <li>buildSuccessful.txt: updated when the build has been determined
         *                                               to be successful</li>
         *                      <li>buildComplete.txt:   updated when the build finishes, whether
         *                                               or not it was successful</li>
         *                      <li>buildLog.txt:        contains unstructured output from the compiler
         *                                               and other build tools. Note that after successful
         *                                               builds, this will likely be empty.</li>
         *                </ol>
         *                To this list it might be reasonable to add buildLog.xml that had a structured
         *                version of buildLog.txt, but that has not yet been implemented. Feedback is welcome.
         *                </li>
         */
        // Status variables
        private boolean isRunning = false;
        private boolean lastBuildSuccessful = false;
        private boolean lastBuildCompleted = false;
        private boolean closed;

        BuildMonitor() {
            //final String statusDirPath = OnBotJavaManager.statusDir;
            File statusDir = OnBotJavaManager.statusDir;
            if (!statusDir.isDirectory()) statusDir.mkdirs();
            buildStartedFile = OnBotJavaManager.buildStartedFile;
            buildCompleteFile = OnBotJavaManager.buildCompleteFile;
            runningObserver = new FileModifyObserver(buildStartedFile,
                new FileModifyObserver.Listener() {
                    @Override
                    public void onFileChanged(int event, File file) {
                        synchronized (buildInformationUpdateLock) {
                            isRunning = true;
                            lastBuildCompleted = false;
                            lastBuildSuccessful = false;
                        }
                    }
                }
            );
            completedObserver = new FileModifyObserver(buildCompleteFile, new
                FileModifyObserver.Listener() {
                    @Override
                    public void onFileChanged(int event, File file) {
                        synchronized (buildInformationUpdateLock) {
                            isRunning = false;
                            lastBuildCompleted = true;
                            lastBuildSuccessful = OnBotJavaManager.getBuildStatus() == OnBotJavaManager.BuildStatus.SUCCESSFUL;
                            synchronized (buildCompletionNotifier) {
                                buildCompletionNotifier.notifyAll();
                            }
                        }
                    }
                }
            );

            buildCompletionNotifier = new Object();
        }

        BuildStatus currentBuildStatus() {
            if (closed) throw new IllegalStateException("BuildStatus has been closed!");
            synchronized (buildInformationUpdateLock) {
                return new BuildStatus(lastBuildCompleted, isRunning, lastBuildSuccessful);
            }
        }

        public void close() {
            closed = true;
            runningObserver.close();
            completedObserver.close();
        }

        @Override
        public void finalize() throws Throwable {
            super.finalize();
            if (closed) return;
            RobotLog.ww("OnBotJavaBuildStatus", "Did not call close(), running finalizer");
            close();
        }
    }

    private static class BuildStatus {
        final boolean completed;
        final boolean running;
        final boolean successful;

        BuildStatus(boolean completed, boolean running, boolean successful) {
            this.completed = completed;
            this.running = running;
            this.successful = successful;
        }
    }

    private static class EditorSettings {
        private Map<String, Object> settings;

        enum Setting {
            AUTOCOMPLETE_ENABLED("autocompleteEnabled"),
            AUTOCOMPLETE_FORCE_ENABLE("autocompleteForceEnabled"),
            AUTOIMPORT_ENABLED("autoImportEnabled"),
            DEFAULT_PACKAGE("defaultPackage"),
            FONT("font"),
            FONT_SIZE("fontSize"),
            KEYBINDING("keybinding"),
            SHOW_PRINT_MARGIN("printMargin"),
            SHOW_INVISIBLE_CHARS("invisibleChars"),
            SOFT_WRAP("softWrap"),
            SPACES_TO_TAB("spacesToTab"),
            THEME("theme"),
            WHITESPACE("whitespace");

            final String name;

            Setting(String name) {
                this.name = name;
            }
        }

        EditorSettings() {
            settings = new HashMap<>();
            settings.put(Setting.FONT.name, "Source Code Pro");
            settings.put(Setting.THEME.name, "chrome");
            settings.put(Setting.FONT_SIZE.name, 16);
            settings.put(Setting.WHITESPACE.name, "space");
            settings.put(Setting.SPACES_TO_TAB.name, 4);
            settings.put(Setting.DEFAULT_PACKAGE.name, "org.firstinspires.ftc.teamcode");
            settings.put(Setting.AUTOCOMPLETE_ENABLED.name, true);
            settings.put(Setting.AUTOCOMPLETE_FORCE_ENABLE.name, false);
            settings.put(Setting.AUTOIMPORT_ENABLED.name, true);
            settings.put(Setting.KEYBINDING.name, "OnBotJava");
            settings.put(Setting.SHOW_PRINT_MARGIN.name, true);
            settings.put(Setting.SHOW_INVISIBLE_CHARS.name, false);
            settings.put(Setting.SOFT_WRAP.name, false);
        }

        private EditorSettings(Map<String, Object> map) {
            settings = map;
        }

        EditorSettings(SharedPreferences preferences) {
            this();
            final SharedPreferences.Editor edit = preferences.edit();
            final Map<String, ?> prefMap = preferences.getAll();
            for (String key : settings.keySet()) {
                if (prefMap.containsKey(key)) {
                    settings.put(key, prefMap.get(key));
                } else {
                    updateValue(edit, key);
                }
            }
            edit.apply();
        }

        static EditorSettings parse(String json) {
            Type type = new TypeToken<Map<String, Object>>() {
            }.getType();
            //noinspection unchecked
            return new EditorSettings((Map<String, Object>) SimpleGson.getInstance().fromJson(json, type));
        }

        private void updateValue(SharedPreferences.Editor editor, Setting key) {
            updateValue(editor, key.name);
        }

        private void updateValue(SharedPreferences.Editor edit, String key) {
            Object obj = settings.get(key);
            if (obj instanceof Integer) {
                edit.putInt(key, (Integer) obj);
            } else if (obj instanceof String) {
                edit.putString(key, (String) obj);
            } else if (obj instanceof Boolean) {
                edit.putBoolean(key, (Boolean) obj);
            }
        }

        public void update(EditorSettings settings, SharedPreferences preferences) {
            update(settings.settings, preferences);
        }

        public void update(Map<String, Object> updatedMap, SharedPreferences preferences) {
            final SharedPreferences.Editor editor = preferences.edit();
            for (String key : updatedMap.keySet()) {
                settings.put(key, updatedMap.get(key));
                updateValue(editor, key);
            }
            editor.apply();
        }

        public void trim(SharedPreferences preferences) {
            final Map<String, ?> prefMap = preferences.getAll();
            final SharedPreferences.Editor edit = preferences.edit();
            for (String key : prefMap.keySet()) {
                if (!settings.containsKey(key)) edit.remove(key);
            }
            edit.apply();
        }

        public Object get(Setting key) {
            return settings.get(key.name);
        }

        void resetToDefaults(SharedPreferences preferences) {
            update(new EditorSettings().settings, preferences);
            trim(preferences);
        }

        String stringify() {
            return SimpleGson.getInstance().toJson(this.settings);
        }
    }

    @RegisterWebHandler(uri = URI_ADMIN_CLEAN)
    private class OnBotJavaAdminClean implements WebHandler {
        @Override
        public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession session) throws IOException, NanoHTTPD.ResponseException {
            OnBotJavaClassLoader.fullClean();
            return successfulResponse("Done");
        }
    }

    @RegisterWebHandler(uri = URI_ADMIN_REARM)
    private class OnBotJavaAdminRearm implements WebHandler {

        @Override
        public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession session) throws IOException, NanoHTTPD.ResponseException {
            OnBotJavaManager.initialize();
            return successfulResponse("Done");
        }
    }

    @RegisterWebHandler(uri = URI_ADMIN_SETTINGS)
    private class OnBotJavaAdminSettings implements WebHandler {
        @Override
        public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession session) throws IOException, NanoHTTPD.ResponseException {
            if (NanoHTTPD.Method.GET.equals(session.getMethod())) {
                final String json = editorSettings.stringify();
                return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, json, json);
            } else if (NanoHTTPD.Method.POST.equals(session.getMethod())) {
                final Map<String, List<String>> parameters = session.getParameters();
                if (parameters.containsKey("settings")) {
                    final EditorSettings settings = EditorSettings.parse(parameters.get("settings").get(0));
                    editorSettings.update(settings, sharedPrefs);
                    return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, MIME_PLAINTEXT, "OK");
                }

                return badRequest();
            }
            return badRequest();
        }
    }

    @RegisterWebHandler(uri = URI_JS_SETTINGS)
    private class OnBotJavaJavaScriptSettings implements WebHandler {
        /* Template
        (function(window, $, settings) {
            window.env = typeof window.env !== 'undefined' ? window.env : {},
            env.settings = settings;
            env.settings['_dict'] = env.settings.hasOwnProperty('_dict') ? env.settings._dict : {};
            var dict = env.settings._dict;

            settings.get = function(name) {
                return typeof dict[name] === 'undefined' ? null : dict[name];
            }

            settings.put = function(name, val) {
                if (typeof name === 'undefined' || typeof val === 'undefined') throw new Error('put doesn\' work with name or val undefined');
                if (settings.get(name) === null) { console.warn(name + " is not a valid setting"); return; }
                dict[name] = typeof val === 'function' ? val() : val;
                return $.post(settings._settingsUrl, 'settings=' + window.JSON.stringify(dict));
            }
        })(window, jQuery,
        {
            _dict: window.JSON.parse('%s'),
            _settingsUrl: '%s'
        });
         */
        @Override
        public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession session) throws IOException, NanoHTTPD.ResponseException {
            final String editorSettings = JavaModeWebHandlers.this.editorSettings.stringify();
            HashMap<String, String> onBotJavaUrls = new HashMap<>();

            for (Field field : JavaModeWebHandlers.class.getDeclaredFields()) {
                int modifiers = field.getModifiers();
                if (!Modifier.isStatic(modifiers) || !Modifier.isFinal(modifiers) || !field.getType().equals(String.class)) continue;
                field.setAccessible(true);
                try {
                    onBotJavaUrls.put(field.getName(), (String) field.get(JavaModeWebHandlers.this));
                } catch (IllegalAccessException ignored) { /// should not be thrown

                }
            }

            final String result = String.format(Locale.ENGLISH,
                    "(function(window, $, settings) {\n" +
                    "	window.env = typeof window.env !== 'undefined' ? window.env : {},\n" +
                    "	env.settings = settings;\n" +
                    "	env.settings['_dict'] = env.settings.hasOwnProperty('_dict') ? env.settings._dict : {};\n" +
                    "	var dict = env.settings._dict;\n" +
                    "\n" +
                    "	settings.get = function(name) {\n" +
                    "		return typeof dict[name] === 'undefined' ? null : dict[name];\n" +
                    "	}\n" +
                    "\n" +
                    "	settings.put = function(name, val) {\n" +
                    "		if (typeof name === 'undefined' || typeof val === 'undefined') throw new Error('put doesn\\' work with name or val undefined');\n" +
                    "		if (settings.get(name) === null) { console.warn(name + \" is not a valid setting\"); return; }\n" +
                    "		dict[name] = typeof val === 'function' ? val() : val;\n" +
                    "		return $.post(settings._settingsUrl, 'settings=' + window.JSON.stringify(dict));\n" +
                    "   }\n" +
                    "\n" +
                    "   env.urls = JSON.parse(settings._urls)" +
                    "})(window, jQuery, \n" +
                    "{\n" +
                    "	_dict: window.JSON.parse('%s'),\n" +
                    "	_settingsUrl: '%s',\n" +
                    "   _urls: '%s'\n" +
                    "});",
                    editorSettings, URI_ADMIN_SETTINGS, SimpleGson.getInstance().toJson(onBotJavaUrls).replace("\\", "\\\\"));
            return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, MIME_JSON, result);
        }
    }

    @RegisterWebHandler(uri = URI_ADMIN_SETTINGS_RESET)
    private class OnBotJavaAdminSettingsReset implements WebHandler {
        @Override
        public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession session) throws IOException, NanoHTTPD.ResponseException {
            editorSettings.resetToDefaults(sharedPrefs);
            return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, MIME_PLAINTEXT, "OK");
        }
    }

    @RegisterWebHandler(uri = URI_ADMIN_RESET_ONBOTJAVA)
    private class OnBotJavaAdminResetOnBotJava implements WebHandler {

        private final long INVALID_ID = -1;
        private long handshakeId = -1;

        @Override
        public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession session) throws IOException, NanoHTTPD.ResponseException {
            final Map<String, List<String>> parameters = session.getParameters();
            if (parameters.containsKey(REQUEST_KEY_ID)) {
                final String possibleHandshakeIdString = parameters.get(REQUEST_KEY_ID).get(0);

                long possibleHandshakeId = INVALID_ID;
                try {
                    possibleHandshakeId = Long.parseLong(possibleHandshakeIdString);
                } catch (NumberFormatException ignored) {

                }

                if (handshakeId != INVALID_ID && possibleHandshakeId == handshakeId) {
                    try {
                        if (resetOnBotJava()) {
                            return successfulResponse("done");
                        } else {
                            return newFixedLengthResponse(NanoHTTPD.Response.Status.INTERNAL_ERROR, MIME_PLAINTEXT, "user intervention required");
                        }
                    } catch (RuntimeException ex) {
                        return newFixedLengthResponse(NanoHTTPD.Response.Status.INTERNAL_ERROR, MIME_PLAINTEXT, "see logs");
                    }
                }

                handshakeId = INVALID_ID;
                return badRequest("invalid handshake");
            } else {
                handshakeId = UUID.randomUUID().getMostSignificantBits();
                return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, MIME_PLAINTEXT, Long.toString(handshakeId));
            }
        }

        @SuppressLint({"SetWorldReadable", "SetWorldWritable"})
        private boolean resetOnBotJava() {
            final File javaRoot = OnBotJavaManager.javaRoot;

            // Set global R/W on our files in case permissions got messy
            javaRoot.setReadable(true, false);
            javaRoot.setWritable(true, false);
            javaRoot.setExecutable(true, false);
            for (File file : AppUtil.getInstance().filesIn(javaRoot)) {
                file.setWritable(true, false);
                file.setReadable(true, false);
                if (file.isDirectory()) { // allows us to read the folder listing
                    file.setExecutable(true, false);
                }
            }

            AppUtil.getInstance().delete(javaRoot);
            if (javaRoot.exists()) { // delete failed
                if (javaRoot.isDirectory()) {
                    for (File file : AppUtil.getInstance().filesIn(javaRoot)) {
                        RobotLog.e(TAG, "[RESET] Delete failed for %s", file.getAbsolutePath());
                    }
                } else { // java root is not a directory, for some reason, but we have attempted to delete it, so we need user intervention
                    RobotLog.e(TAG, "[RESET] Delete of javaRoot (\"%s\") failed, not a directory??? User required to delete \"%s\"", javaRoot, javaRoot);
                }

                return false;
            }

            OnBotJavaManager.initialize(); // resetup everything in OnBotJava
            ensureTemplates();
            editorSettings.resetToDefaults(sharedPrefs); // reset editor defaults
            return true;
        }
    }

    @RegisterWebHandler(uri = URI_JS_AUTOCOMPLETE)
    private static class OnBotJavaJavaScriptAutocomplete implements WebHandler {
        static volatile String response = null;
        private static final Object lock = new Object();
        private static final String[] packagesToAutocomplete = new String[] {
                "org/firstinspires/ftc/ftccommon/external",
                "org/firstinspires/ftc/robotcore/external",
                "com/qualcomm/robotcore/eventloop/opmode",
                "com/qualcomm/robotcore/hardware",
                "com/qualcomm/robotcore/robot",
                "com/qualcomm/robotcore/util",
                "com/qualcomm/hardware",
                "java/io",
                "java/lang",
                "java/math",
                "java/text",
                "java/util"
        };

        static {
            (new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        buildResponse();
                    } catch (IOException e) {
                        RobotLog.ee(OnBotJavaJavaScriptAutocomplete.class.getName(), e,
                                "Error with autocomplete response");
                        response = "";
                    }
                }
            })).start();
        }

        @Override
        public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession session) throws IOException, NanoHTTPD.ResponseException {
            // Wait for the build response to complete, it is still running
            synchronized (lock) {
                while (response == null) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return serverError();
                    }
                }
            }
            // Make sure the result is valid...
            if (response.equals("")) return serverError();

            return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, MIME_JSON, response);
        }

        private static void buildResponse() throws IOException {
            final List<File> jarFiles = AppUtil.getInstance().filesIn(OnBotJavaManager.libDir, ".jar");
            final HashMap<String, List<AutoClass>> autoClassList = new HashMap<>();
            for (File jarFile : jarFiles) {
                JarInputStream jarInputStream = new JarInputStream(new FileInputStream(jarFile));
                JarEntry entry;
                while ((entry = jarInputStream.getNextJarEntry()) != null) {
                    final String entryName = entry.getName();
                    // Skip the unimportant classes
                    if (!entryName.endsWith(".class")) continue;
                    if (!packagesToAutoComplete(entryName)) continue;
                    String className = entryName.replaceAll("/", "\\.");
                    String myClass = className.substring(0, className.lastIndexOf('.'));
                    // A "$" denotes an inner class which we will parse as referenced by the classes we scan
                    if (myClass.contains("$")) continue;
                    final Class currentClass;
                    try {
                        currentClass = Class.forName(myClass, false,
                                JavaModeWebHandlers.class.getClassLoader());
                    } catch (ClassNotFoundException ignored) {
                        continue;
                    }

                    parseClassForAutocomplete(autoClassList, currentClass);
                }
            }

            response = SimpleGson.getInstance().toJson(autoClassList);
            synchronized (lock) {
                lock.notifyAll();
            }
        }

        private static void parseClassForAutocomplete(HashMap<String, List<AutoClass>> autoClassList, Class currentClass) {
            final AutoClass autoClass;
            final String currentClassName;

            SecurityModifier classModifier = SecurityModifier.fromModifierInt(currentClass.getModifiers());
            if (classModifier != SecurityModifier.PUBLIC && classModifier != SecurityModifier.PROTECTED)
                return;

            currentClassName = classNameFor(currentClass);
            String packageName = currentClass.getPackage().getName();

            // Check if we have already added this class (to prevent recursion loops), if so do nothing more
            if (autoClassList.containsKey(currentClassName)) {
                for (AutoClass klazz : autoClassList.get(currentClassName)) {
                    if (klazz.packageName.equals(packageName)) return;
                }
            }

            HashMap<String, AutoField> fields = new HashMap<>();
            for (Field field : currentClass.getDeclaredFields()) {
                SecurityModifier fieldSecurityModifier = SecurityModifier.fromModifierInt(field.getModifiers());
                if (fieldSecurityModifier == SecurityModifier.PRIVATE) continue;
                final String name1 = field.getName();
                final String fieldType = field.getType().getName();
                fields.put(name1, new AutoField(fieldSecurityModifier, fieldType));
            }

            HashMap<String, ArrayList<AutoMethod>> methods = new HashMap<>();
            for (Method method : currentClass.getDeclaredMethods()) {
                SecurityModifier fieldSecurityModifier = SecurityModifier.fromModifierInt(method.getModifiers());
                if (fieldSecurityModifier == SecurityModifier.PRIVATE) continue;
                final String methodName = method.getName();
                final String fieldType = method.getReturnType().getName();
                final Class<?>[] parameterTypes = method.getParameterTypes();
                final List<String> paramTypes = new ArrayList<>(parameterTypes.length);
                for (Class<?> paramType : parameterTypes) {
                    paramTypes.add(paramType.getName());
                }

                if (!methods.containsKey(methodName)) methods.put(methodName, new ArrayList<AutoMethod>());
                methods.get(methodName)
                        .add(new AutoMethod(fieldSecurityModifier, fieldType,paramTypes));
            }

            final Class superclass = currentClass.getSuperclass();
            final String superclassName;
            if (superclass != null) {
                superclassName = superclass.getName();
                parseClassForAutocomplete(autoClassList, superclass);
            } else {
                superclassName = "java.lang.Object";
            }

            List<String> interfaces = getInterfacesFor(currentClass, autoClassList, new ArrayList<String>());

            autoClass = new AutoClass(classModifier, methods, fields, currentClassName, packageName, interfaces, superclassName);
            if (!autoClassList.containsKey(currentClassName))
                autoClassList.put(currentClassName, new ArrayList<AutoClass>());
            autoClassList.get(currentClassName).add(autoClass);

            for (Class<?> innerClass : currentClass.getDeclaredClasses()) {
                parseClassForAutocomplete(autoClassList, innerClass);
            }
        }

        private static List<String> getInterfacesFor(Class<?> currentClass, HashMap<String, List<AutoClass>> autoClassMap, List<String> list) {
            if (currentClass == null || currentClass.equals(Object.class)) return list;

            for (Class<?> klazz : currentClass.getInterfaces()) {
                list.add(klazz.getName());
                parseClassForAutocomplete(autoClassMap, klazz);
            }

            return getInterfacesFor(currentClass.getSuperclass(), autoClassMap, list);
        }

        private static boolean packagesToAutoComplete(String entryName) {
            for (String testPackage : packagesToAutocomplete) {
                if (entryName.startsWith(testPackage)) return true;
            }

            return false;
        }

        private static String classNameFor(Class<?> klazz) {
            final String fullClassName = klazz.getName();
            if (fullClassName.indexOf('.') >= 0) {
                return fullClassName.substring(fullClassName.lastIndexOf('.') + 1);
            } else {
                return fullClassName;
            }
        }

        @SuppressWarnings("unused")
        private static class AutoClass {
            private SecurityModifier modifier;
            private HashMap<String, ArrayList<AutoMethod>> methods;
            private HashMap<String, AutoField> fields;
            private String packageName;
            private List<String> interfaces;
            private String parentClass;

            private AutoClass(SecurityModifier modifier, HashMap<String, ArrayList<AutoMethod>> methods, HashMap<String, AutoField> fields,
                              String name, String packageName, List<String> interfaces, String parentClass) {
                this.modifier = modifier;
                this.methods = methods;
                this.fields = fields;
                this.packageName = packageName;
                this.interfaces = interfaces;
                this.parentClass = parentClass;
            }
        }

        @SuppressWarnings("unused")
        private static class AutoField {
            private SecurityModifier modifier;
            private String type;

            private AutoField(SecurityModifier modifier, String type) {
                this.modifier = modifier;
                this.type = type;
            }
        }

        @SuppressWarnings("unused")
        private static class AutoMethod {
            private SecurityModifier modifier;
            private String type;
            private List<String> params;

            private AutoMethod(SecurityModifier modifier, String type, List<String> params) {
                this.modifier = modifier;
                this.type = type;
                this.params = params;
            }
        }

        private enum SecurityModifier {
            PUBLIC, PRIVATE, PROTECTED, PACKAGE_PRIVATE, UNKNOWN;

            static SecurityModifier fromModifierInt(int modifier) {
                if (Modifier.isPublic(modifier)) return PUBLIC;
                if (Modifier.isPrivate(modifier)) return PRIVATE;
                if (Modifier.isProtected(modifier)) return PROTECTED;
                return PACKAGE_PRIVATE;
            }
        }
    }

    @RegisterWebHandler(uri = URI_BUILD_LAUNCH)
    private class OnBotJavaBuildLaunch implements WebHandler {
        @Override
        public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession session) throws IOException, NanoHTTPD.ResponseException {
            return launchBuild();
        }

        /**
         * <p>Called with {@link #URI_BUILD_LAUNCH}</p>
         *
         * @return a response with the current millisecond time if the launch was successful
         */
        private NanoHTTPD.Response launchBuild() {
        /*
        *      <li>control: Whenever the contents of the 'buildRequest.txt' file in this directory is
*                changed, a build is automatically kicked off it's not already running. Note
*                that the contents of the file don't matter, only the act of changing it. This
*                directory also contains a locking mechanism so as to allow clients to examine
*                the state of a successful build without having to worry that a new build will
*                be kicked off while they are doing so. See 'buildLock' below.</li>
         */
            File buildStartFile = OnBotJavaManager.buildRequestFile;
            synchronized (buildMonitor.buildCompletionNotifier) {
                try {
                    while (buildMonitor.isRunning) {
                        buildMonitor.buildCompletionNotifier.wait();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return serverError();
                }
            }
            final String startTime = Long.toString(System.currentTimeMillis());
            ReadWriteFile.writeFile(buildStartFile, startTime + " - begin build");
            return successfulResponse(startTime);
        }
    }

    @RegisterWebHandler(uri = URI_BUILD_LOG)
    private class OnBotJavaBuildLog implements WebHandler {
        /**
         * <p>Called with {@link #URI_BUILD_LOG}</p>
         */
        @Override
        public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession session) throws IOException, NanoHTTPD.ResponseException {
            return currentBuildLog();
        }
    }

    @RegisterWebHandler(uri = URI_BUILD_STATUS)
    private class OnBotJavaBuildStatus implements WebHandler {
        @Override
        public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession session) throws IOException, NanoHTTPD.ResponseException {
            return currentBuildStatus(session.getUri());
        }

        /**
         * <p>Called with {@link #URI_BUILD_STATUS}</p>
         *
         * @param uri trimmed uri, should not be prefixed with {@link #URI_JAVA_PREFIX}
         */
        private NanoHTTPD.Response currentBuildStatus(String uri) {
            uri = uri.substring(URI_BUILD_STATUS.length()).toLowerCase();
            String response;
            if (uri.equals("")) {
                response = gson.toJson(buildMonitor.currentBuildStatus());
                return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, MIME_JSON, response);
            } else {
                return badRequest();
            }
        }
    }

    @RegisterWebHandler(uri = URI_BUILD_WAIT)
    private class OnBotJavaBuildWait implements WebHandler {
        @Override
        public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession session) throws IOException, NanoHTTPD.ResponseException {
            synchronized (buildMonitor.buildCompletionNotifier) {
                try {
                    while (buildMonitor.isRunning) {
                        buildMonitor.buildCompletionNotifier.wait();
                    }
                    return currentBuildLog();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return serverError();
                }
            }
        }
    }

    @RegisterWebHandler(uri = URI_FILE_COPY)
    private class OnBotJavaFileCopy implements WebHandler {
        @Override
        public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession session) throws IOException, NanoHTTPD.ResponseException {
            final Map<String, List<String>> data = session.getParameters();
            if (!data.containsKey(REQUEST_KEY_COPY_TO) || !data.containsKey(REQUEST_KEY_COPY_FROM)) {
                return badRequest();
            }

            final String fromFileName = data.get(REQUEST_KEY_COPY_FROM).get(0);
            final String destFileName = data.get(REQUEST_KEY_COPY_TO).get(0);
            if (!isValidFileLocation(fromFileName, VALID_SRC_FILE_OR_FOLDER_REGEX) ||
                    !isValidFileLocation(destFileName, VALID_SRC_FILE_OR_FOLDER_REGEX))
                return badRequest();

            File origin = new File(OnBotJavaManager.javaRoot, fromFileName);
            File dest = new File(OnBotJavaManager.javaRoot, destFileName);
            try {
                recursiveCopy(origin, dest);
            } catch (IOException ex) {
                return badRequest("cannot copy files");
            }

            return successfulResponse("done");
        }

        private void recursiveCopy(File origin, File dest) throws IOException {
            if (origin.isDirectory()) {
                if (dest.exists() && !dest.isDirectory()) throw new IOException("Cannot merge origin and destination");
                dest.mkdirs();
                final String[] files = origin.list();
                for (String file : files) {
                    File src = new File(origin, file);
                    File destFile = new File(dest, file);

                    // Prevent a file from being copied endlessly, if we are copying the parent folder to the inside of itself
                    if (src.getAbsolutePath().equals(dest.getAbsolutePath())) continue;

                    if (src.isDirectory()) {
                        destFile.mkdirs();
                    }

                    recursiveCopy(src, destFile);
                }
            } else {
                copyFile(origin, dest);
            }
        }

        /**
         * Copies the given source File to the given dest File.
         */
        void copyFile(File source, File dest) throws IOException {
            if (dest.exists()) {
                String originalName = dest.getName();
                String ext = originalName.substring(originalName.lastIndexOf('.'));
                originalName = originalName.substring(0, originalName.lastIndexOf('.'));
                String suffix = "_Copy";
                dest = new File(dest.getParentFile(), originalName + suffix + ext);
                for (int i = 2; i < 1000 && dest.exists(); i++) {
                    dest = new File(dest.getParentFile(), originalName + suffix + i + ext);
                }
            }

            if (source.getPath().endsWith(EXT_JAVA_FILE)) {
                copyJavaFile(source, dest);
            } else {
                try (FileChannel sourceChannel = new FileInputStream(source).getChannel();
                     FileChannel destChannel = new FileOutputStream(dest).getChannel()) {
                    destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
                }
            }
        }
    }

    @RegisterWebHandler(uri = URI_FILE_DELETE)
    private class OnBotJavaFileDelete implements WebHandler {
        /**
         * <li>Delete
         * <p>Requires a "delete" entry in data map. This should be a JSON encoded array</p>
         * </li>
         */
        @Override
        public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession session) throws IOException, NanoHTTPD.ResponseException {
            final Map<String, List<String>> data = session.getParameters();
            if (data.containsKey(REQUEST_KEY_DELETE)) {
                String[] deleteFiles;
                try {
                    deleteFiles = gson.fromJson(data.get(REQUEST_KEY_DELETE).get(0), String[].class);
                } catch (JsonSyntaxException ex) {
                    return badRequest("Invalid delete syntax - bad JSON");
                }
                for (String fileToDeletePath : deleteFiles) {
                    final File fileToDelete = new File(OnBotJavaManager.javaRoot, fileToDeletePath);
                    if (fileToDelete.exists()) {
                        recursiveDelete(fileToDelete);
                    }
                }
                return successfulResponse("done");
            }

            return badRequest();
        }

        /**
         * Recursively delete a file or a folder
         *
         * @param file the starting point
         */
        private void recursiveDelete(File file) {
            if (file.isDirectory()) {
                final File[] files = file.listFiles();
                for (File file1 : files) {
                    recursiveDelete(file1);
                }
            }
            file.delete();
        }
    }

    @RegisterWebHandler(uri = URI_FILE_DOWNLOAD)
    private class OnBotJavaFileDownload implements WebHandler {
        @Override
        public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession session) throws IOException, NanoHTTPD.ResponseException {
            final NanoHTTPD.Response file = getFile(session.getParameters(), true, LineEndings.WINDOWS.lineEnding);
            if (file.getStatus() == NanoHTTPD.Response.Status.OK) {
                String fileName = session.getParameters().get(REQUEST_KEY_FILE).get(0);
                if (fileName.equals(PATH_SEPARATOR + OnBotJavaManager.srcDir.getName() + PATH_SEPARATOR)) {
                    fileName = "OnBotJava-" + AppUtil.getInstance().getIso8601DateFormat().format(new Date()) + EXT_ZIP_FILE;
                } else {
                    // Check to see if this is a folder, if so add a ".zip" extension
                    fileName = fileName.substring(fileName.lastIndexOf(PATH_SEPARATOR) + 1);
                    if (fileName.endsWith(PATH_SEPARATOR)) fileName = fileName.substring(0, fileName.length() - 1) +  EXT_ZIP_FILE;
                }

                file.addHeader("Content-Disposition", "attachment; filename=" + fileName);
                file.addHeader("Pragma",  "no-cache");
            }

            return file;
        }
    }

    @RegisterWebHandler(uri = URI_FILE_GET)
    private class OnBotJavaFileGetSource implements WebHandler {
        @Override
        public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession session) throws IOException, NanoHTTPD.ResponseException {
            return getFile(session.getParameters());
        }
    }

    /**
     * <li>New
     * <p>
     * Requires a "new" entry in data map. If url ends in "/", a new folder will be created,
     * otherwise a new file will be created.
     * <p>
     * If a file will be created, the user can use a template provided in the "template"
     * entry in the data map, which should be inside the {@link #templatesDir} folder. If
     * the user specifies no template, a default template will be used based on the file
     * extension.
     * </p>
     * <p>
     * See {@link #buildTemplateKeyMap(String, Map, File)} for details regarding additional
     * data entries for template use
     * </p>
     * </li>
     */
    @RegisterWebHandler(uri = URI_FILE_NEW)
    private class OnBotJavaFileNew implements WebHandler {


        @Override
        public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession session) throws IOException, NanoHTTPD.ResponseException {
            final Map<String, List<String>> data = session.getParameters();
            if (data.containsKey(REQUEST_KEY_NEW) && data.containsKey(REQUEST_KEY_FILE)) {
                final String fileNameUri = data.get(REQUEST_KEY_FILE).get(0);
                File file = new File(OnBotJavaManager.javaRoot, fileNameUri);
                if (fileNameUri.endsWith(PATH_SEPARATOR)) {
                    file.mkdirs();
                    return successfulResponse("true");
                } else {
                    Map<String, String> templateKeyMap = buildTemplateKeyMap(fileNameUri, data, file);
                    if (!isValidFileLocation(fileNameUri, VALID_SRC_FILE_REGEX)) return badRequest();
                    if (data.containsKey(REQUEST_KEY_TEMPLATE)) {
                        String template = data.get(REQUEST_KEY_TEMPLATE).get(0);
                        if (isValidFileLocation(template, VALID_TEMPLATE_FILE_REGEX)) {
                            File templateFile = new File(OnBotJavaManager.javaRoot, template);
                            if (templateFile.exists() && !templateFile.isDirectory()) {
                                String templateData = ReadWriteFile.readFile(templateFile);
                                newFileFromTemplate(file, templateKeyMap, templateData, templateFile, data.containsKey(REQUEST_KEY_PRESERVE));
                                return successfulResponse("true");
                            }
                        }

                        return badRequest();
                    } else { // use default extension based template if any
                        String newFileData = defaultTemplateForFile(fileNameUri);
                        newFileFromTemplate(file, templateKeyMap, newFileData, null, false);
                        return successfulResponse("true");
                    }
                }
            }

            return badRequest();
        }

        private Map<String, String> buildTemplateKeyMap(String uri, Map<String, List<String>> data, File file) {
            String name = file.getName();
            String packageName = packageNameFromUri(uri, name);
            name = name.lastIndexOf('.') == -1 ? name : name.substring(0, name.lastIndexOf('.'));
            String opModeName = data.containsKey(REQUEST_KEY_OPMODE_NAME) ?
                    data.get(REQUEST_KEY_OPMODE_NAME).get(0) : name;

            String opModeAnnotations = data.containsKey(REQUEST_KEY_OPMODE_ANNOTATIONS) ?
                    data.get(REQUEST_KEY_OPMODE_ANNOTATIONS).get(0) : "";
            opModeAnnotations = opModeAnnotations
                    .replaceAll("\\{\\{ \\+opModeName \\}\\}", opModeName);

            HashMap<String, String> results = new HashMap<>();
            results.put("packageName", packageName);
            results.put("opModeAnnotations", opModeAnnotations);
            results.put("name", name);
            results.put("opModeName", opModeName);
            results.put("year", new SimpleDateFormat("yyyy", Locale.US).format(new Date()));
            if (data.containsKey(REQUEST_KEY_TEAM_NAME)) {
                results.put("teamName", data.get("teamName").get(0));
            }

            if (data.containsKey(REQUEST_KEY_SETUP_HARDWARE) && data.get(REQUEST_KEY_SETUP_HARDWARE).get(0).equals("1")) {
                final HardwareItemMap hardwareItemMap = HardwareItemMap.newHardwareItemMap();
                StringBuilder rcHardwareFieldsBuilder = new StringBuilder();
                StringBuilder rcHardwareSetupBuilder = new StringBuilder();
                Set<String> knownDeviceNames = new HashSet<>();
                for (HardwareItem device : hardwareItemMap.getAllHardwareItems()) {
                    String deviceName = device.deviceName;
                    char[] sanitizedDeviceNameChars = deviceName.toCharArray();
                    if (sanitizedDeviceNameChars.length > 0) {
                        // make the device name the right case
                        char sanitizedDeviceNameChar = Character.toLowerCase(sanitizedDeviceNameChars[0]);
                        sanitizedDeviceNameChars[0] = Character.isJavaIdentifierStart(sanitizedDeviceNameChar) ? sanitizedDeviceNameChar : '_';
                    }

                    for (int i = 1; i < sanitizedDeviceNameChars.length; i++) {
                        char sanitizedDeviceNameChar = sanitizedDeviceNameChars[i];
                        sanitizedDeviceNameChars[i] = Character.isJavaIdentifierPart(sanitizedDeviceNameChar) ? sanitizedDeviceNameChar : '_';
                    }

                    final String sanitizedDeviceName = new String(sanitizedDeviceNameChars);

                    // Prevent fields with the same name from being created
                    if (knownDeviceNames.contains(sanitizedDeviceName)) continue;
                    knownDeviceNames.add(sanitizedDeviceName);

                    Class typeClass = getHardwareTypeName(device.hardwareType.deviceType);
                    if (typeClass == null)
                        typeClass = device.hardwareType.deviceType;
                    if (HARDWARE_TYPES_PREVENTED_FROM_HARDWARE_SETUP.indexOf(typeClass) >= 0)
                        continue;

                    String typeName = typeClass.getSimpleName();

                    if (editorSettings.get(EditorSettings.Setting.WHITESPACE).equals("tab")) {
                        rcHardwareFieldsBuilder.append('\t');
                        rcHardwareSetupBuilder.append("\t\t");
                    } else {
                        for (int i = 0; i < (Integer) editorSettings.get(EditorSettings.Setting.SPACES_TO_TAB); i++) {
                            rcHardwareFieldsBuilder.append(' ');
                            // two spaces since hardware setup is two indents deep
                            rcHardwareSetupBuilder.append("  ");
                        }
                    }

                    rcHardwareFieldsBuilder.append(String.format(Locale.ENGLISH, "private %s %s;\n", typeName, sanitizedDeviceName));
                    rcHardwareSetupBuilder.append(String.format(Locale.ENGLISH, "%s = hardwareMap.get(%s.class, \"%s\");\n", sanitizedDeviceName, typeName, deviceName));
                }

                results.put("rcHardwareFields", rcHardwareFieldsBuilder.toString());
                results.put("rcHardwareSetup", rcHardwareSetupBuilder.toString());
            }

            return results;
        }

        Class getHardwareTypeName(Class typeClass) {
            if (typeClass.equals(Object.class)) return null;

            final String packageName = "com.qualcomm.robotcore.hardware";
            if (typeClass.getPackage().getName().equals(packageName)) {
                return typeClass;
            }

            for (Class<?> klazz : typeClass.getInterfaces()) {
                if (klazz.getPackage().getName().equals(packageName)) {
                    return klazz;
                }
            }

            if (typeClass.getSuperclass().getPackage().getName().equals(packageName))  {
                return typeClass.getSuperclass();
            }

            return getHardwareTypeName(typeClass.getSuperclass());
        }

        @NonNull
        private String packageNameFromUri(String uri, String name) {
            String packageName = uri;
            if (packageName.indexOf("/jars") == 0) {
                packageName = packageName.substring("/jars".length());
            } else if (packageName.indexOf("/src") == 0) {
                packageName = packageName.substring("/src".length());
            }
            packageName = packageName.substring(0, packageName.lastIndexOf(name));
            if (!packageName.isEmpty()) {
                if (packageName.equals(PATH_SEPARATOR)) return "";
                packageName = packageName.indexOf(PATH_SEPARATOR) == 0 ? packageName.substring(1) : packageName;
                final int packageNameLength = packageName.length();
                packageName = packageName.lastIndexOf(PATH_SEPARATOR) == packageNameLength - 1 ?
                        packageName.substring(0, packageNameLength - 1) : packageName;
                packageName = packageName.replace(PATH_SEPARATOR, ".");
            }
            return packageName;
        }

        private void newFileFromTemplate(File file, Map<String, String> templateKeyMap, String templateData, @Nullable File templateSource, boolean preserveAnnotations) {
            templateData = parseTemplate(templateData, templateKeyMap, preserveAnnotations);

            // Since a template could possibly be just a raw sample, we need to take measures to ensure that
            // everything appears to be right to the user
            if (templateSource != null) {
                String oldClassName = classNameFromFile(templateSource);
                String oldPackageName = extractPackageInformationFromJavaFileContents(templateData);
                copyJavaFileFromContents(oldClassName, oldPackageName, templateData, file);
            } else {
                ReadWriteFile.writeFile(file, templateData);
            }
        }

        @NonNull
        private String defaultTemplateForFile(String uri) {
            String newFileData;
            if (uri.endsWith(EXT_JAVA_FILE)) {
                newFileData = "package {{ +packageName }};\n" +
                        "\n" +
                        "{{ +opModeAnnotations }}\n" +
                        "public class {{ +name }} {\n" +
                        "{{ +rcHardwareFields }}\n" +
                        "\t// todo: write your code here\n" +
                        "}";
            } else {
                newFileData = "";
            }
            return newFileData;
        }

        private String parseTemplate(String templateData, Map<String, String> valueMap, boolean preserveAnnotations) {
            for (Map.Entry<String, String> entry : valueMap.entrySet()) {
                RobotLog.dd(TAG, "Processing template tag '%s'", entry.getKey());
                templateData = templateData.replaceAll("\\{\\{ \\+" + entry.getKey() + " \\}\\}", Matcher.quoteReplacement(entry.getValue()));
            }

            if (!preserveAnnotations && valueMap.containsKey("opModeAnnotations")) {
                String opModeAnnotations = valueMap.get("opModeAnnotations");
                if (!opModeAnnotations.contains("@Disabled")) {
                    Pattern pattern = Pattern.compile("^\\s*@Disabled.*$", Pattern.MULTILINE);
                    final Matcher matcher = pattern.matcher(templateData);
                    if (matcher.find()) {
                        templateData = matcher.replaceAll("");
                    }
                }

                if (!opModeAnnotations.contains("@Autonomous")) {
                    Pattern pattern = Pattern.compile("^\\s*@Autonomous(|\\([^)]+\\))(.*)$", Pattern.MULTILINE);
                    if (opModeAnnotations.contains("@TeleOp")) {
                        templateData = pattern.matcher(templateData).replaceAll("@TeleOp$1$2");
                    } else {
                        templateData = pattern.matcher(templateData).replaceAll("");
                    }
                }

                if (!opModeAnnotations.contains("@TeleOp")) {
                    Pattern pattern = Pattern.compile("^\\s@TeleOp(|\\([^)]+\\))(.*)$", Pattern.MULTILINE);
                    if (opModeAnnotations.contains("@Autonomous")) {
                        templateData = pattern.matcher(templateData).replaceAll("@Autonomous$1$2");
                    } else {
                        templateData = pattern.matcher(templateData).replaceAll("");
                    }
                }
            }

            templateData = templateData.replaceAll("\\{\\{ \\+\\w+ \\}\\}", "");
            return templateData;
        }
    }

    @RegisterWebHandler(uri = URI_FILE_SAVE)
    private class OnBotJavaFileSave implements WebHandler {
        /**
         * <p>Called with {@link #URI_FILE_SAVE}</p>
         * <p>
         * <p>Handle a file operation (not limited to saving), however only one major operation
         * per request is supported.</p>
         * Currently supported operations:
         * <ul>
         * <li>Save
         * <p>Requires a "code" entry in data map</p>
         * </li>
         * <p>
         * </ul>
         *
         * @param method HTTP request method
         * @param data   POSTed data, represented in a map form
         */
        private NanoHTTPD.Response saveFile(
                NanoHTTPD.Method method, Map<String, List<String>> data) {
            if (!data.containsKey(REQUEST_KEY_FILE)) return badRequest();
            String uri = data.get(REQUEST_KEY_FILE).get(0);
            // language=RegExp
            if (isValidFileLocation(uri, VALID_SRC_FILE_OR_FOLDER_REGEX) &&
                    NanoHTTPD.Method.POST.equals(method)) {
                // language=text
                final String KEY_SAVE = REQUEST_KEY_SAVE;
                if (data.containsKey(KEY_SAVE)) {
                    String code;
                    code = data.get(KEY_SAVE).get(0);
                    if (code == null) return badRequest();
                    File codeFile = new File(OnBotJavaManager.javaRoot, uri);
                    ReadWriteFile.writeFile(codeFile, code);
                    String responseData = gson.toJson(data);
                    return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, MIME_JSON, responseData);
                }
            }
            // default response
            return badRequest();
        }

        @Override
        public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession session) throws IOException, NanoHTTPD.ResponseException {
            return saveFile(session.getMethod(), session.getParameters());
        }
    }

    @RegisterWebHandler(uri = URI_FILE_TEMPLATES)
    private class OnBotJavaFileTemplates implements WebHandler {
        private String response;
        private boolean templatesEnsured = true;

        /**
         * <p>Called with {@link #URI_FILE_TEMPLATES}</p>
         * <p>Makes a listing of the available project templates for use in JSON format</p>
         */
        private NanoHTTPD.Response projectTemplates() {
            if (response == null || !templatesEnsured) {
                List<String> templates = new ArrayList<>();
                //final File templatesFolder = templatesDir;
                templatesEnsured = ensureTemplates();
                String templatePath = templatesDir.getAbsolutePath();
                searchForFiles(templatePath, templatesDir, templates, false);
                for (int i = 0; i < templates.size(); i++) {
                    String template = templates.get(i);

                    // remove temp files and plain old directories
                    if (template.endsWith(EXT_TEMP_FILE)) {
                        templates.remove(template);
                        // Preserve current loop index by subtracting 1 then continuing the loop, causing the loop to re-run the same index
                        // which now should be a different object
                        --i;
                        continue;
                    }

                    if (template.startsWith(PATH_SEPARATOR)) {
                        template = template.substring(1);
                        templates.set(i, template);
                    }
                }

                Collections.sort(templates);
                response = gson.toJson(templates);
            }

            return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, MIME_JSON, response);
        }

        @Override
        public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession session) throws IOException, NanoHTTPD.ResponseException {
            return projectTemplates();
        }
    }

    @RegisterWebHandler(uri = URI_FILE_TREE)
    private class OnBotJavaFileTree implements WebHandler {
        /**
         * <p>Called with {@link #URI_FILE_TREE}</p>
         * <p>
         * <p>Generates a listing of files in the {@link OnBotJavaManager#srcDir} and
         * {@link OnBotJavaManager#jarDir}. </p>
         * {@link OnBotJavaManager#jarDir}
         *
         * @return This returns the file tree in JSON, with the src and jar directories separate fields
         */
        private NanoHTTPD.Response projectTree() {
        /*
        <li>src:  .java source code is placed here in package-appropriate subdirectories, in the
            *                 usual Java style</li>
         */
            final String srcPath = OnBotJavaManager.srcDir.getAbsolutePath();
            File srcDir = OnBotJavaManager.srcDir;
            if (!srcDir.isDirectory()) srcDir.mkdirs();
            ArrayList<String> srcList = new ArrayList<>();
        /*
        *      <li>jars: (optional) Any externally-compiled jar src can be placed in this
        *                directory. They will be installed in the system, much as the .java source
        *                src are after they have been compiled.</li>
         */
            final String jarPath = OnBotJavaManager.jarDir.getAbsolutePath();
            File jarDir = OnBotJavaManager.jarDir.getAbsoluteFile();
            if (!jarDir.isDirectory()) jarDir.mkdirs();
            ArrayList<String> jarList = new ArrayList<>();
            searchForFiles(srcPath, srcDir, srcList, true);
            searchForFiles(jarPath, jarDir, jarList, true);

            String results = gson.toJson(new FileTree(srcList, jarList));
            return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, MIME_JSON, results);
        }

        @Override
        public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession session) throws IOException, NanoHTTPD.ResponseException {
            return projectTree();
        }
    }

    @RegisterWebHandler(uri = URI_FILE_UPLOAD, usesParamGenerator = false)
    private class OnBotJavaFileUpload implements WebHandler {
        @Override
        public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession session) throws IOException, NanoHTTPD.ResponseException {
            Map<String, String> files = new HashMap<>();
            session.parseBody(files);
            return uploadFile(session.getParameters(), files);
        }

        private NanoHTTPD.Response uploadFile(Map<String, List<String>> data, Map<String, String> files) {
            if (!data.containsKey("file")) return badRequest();
            final String newFileName = data.get("file").get(0).replaceAll("(?:\\.\\.|\\\\|/)", "");
            String newFolderName = null;
            if (data.containsKey("folder")) {
                newFolderName = data.get("folder").get(0);
                if (newFolderName.contains(".."))
                    newFolderName = null; // keep things secure
            }
            File src = new File(files.get("file"));
            File dest;
            if (newFileName.endsWith(EXT_JAVA_FILE)) {
                dest = OnBotJavaManager.srcDir;
                if (newFolderName == null) {
                    final String code = ReadWriteFile.readFile(src);
                    String group = extractPackageInformationFromJavaFileContents(code);

                    if (group != null) {
                        String folder = group.replaceAll("\\.", PATH_SEPARATOR);
                        dest = new File(dest, folder);
                    } else {
                        dest = OnBotJavaManager.srcDir;
                    }
                }
            } else if (newFileName.endsWith( EXT_ZIP_FILE) || newFileName.endsWith(".jar")) {
                dest = OnBotJavaManager.jarDir;
            } else {
                dest = OnBotJavaManager.srcDir;
            }

            dest = new File(dest, newFileName);
            dest.getParentFile().mkdirs();
            try (InputStream in = new FileInputStream(src)) {
                try (OutputStream out = new FileOutputStream(dest)) {
                    // Transfer bytes from in to out
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                }
            } catch (IOException e) {
                RobotLog.ee(TAG, e, "Error copying file \"%s\"", newFileName);
                return serverError();
            }

            return successfulResponse(newFileName);
        }
    }

    @Nullable
    private String extractPackageInformationFromJavaFileContents(String code) {
        final Pattern packagePattern = Pattern.compile("(?:\\n|)package\\s+(.+);(?:\\n|\\s*)");
        final Matcher matcher = packagePattern.matcher(code);
        if (matcher.find()) { // has package statement
            return matcher.group(1);

        }

        return null;
    }
}

