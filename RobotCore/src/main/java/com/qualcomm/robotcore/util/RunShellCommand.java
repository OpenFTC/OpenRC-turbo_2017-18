/*
 * Copyright (c) 2014, 2015 Qualcomm Technologies Inc
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * (subject to the limitations in the disclaimer below) provided that the following conditions are
 * met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions
 * and the following disclaimer in the documentation and/or other materials provided with the
 * distribution.
 *
 * Neither the name of Qualcomm Technologies Inc nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS LICENSE. THIS
 * SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
 * THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.qualcomm.robotcore.util;

import com.qualcomm.robotcore.exception.RobotCoreException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Run shell commands
 * <p>
 * Light weight utility class for running shell commands.
 */
public class RunShellCommand {

  private final static int BUFFER_SIZE = 512 * 1024;

  boolean logging = false;

  /**
   * Constructor
   */
  public RunShellCommand() {
    // empty constructor
  }

  /**
   * If logging is enabled, all command will be logged
   * @param enable true to enable
   */
  public void enableLogging(boolean enable) {
    logging = enable;
  }

  /**
   * Run the given command
   * @param cmd command to run
   * @return the commands output
   */
  public String run(String cmd) {
    if (logging) RobotLog.v("running command: " + cmd);
    String output = runCommand(cmd, false);
    if (logging) RobotLog.v("         output: " + output);
    return output;
  }

  /**
   * Run the given command, as root
   * @param cmd command to run
   * @return the commands output
   */
  public String runAsRoot(String cmd) {
    if (logging) RobotLog.v("running command: " + cmd);
    String output = runCommand(cmd, true);
    if (logging) RobotLog.v("         output: " + output);
    return output;
  }

  private String runCommand(String cmd, boolean asRoot) {

    byte[] buffer = new byte[BUFFER_SIZE];
    int length = 0;
    String output = "";
    ProcessBuilder processBuilder = new ProcessBuilder();
    Process process = null;

    try {
      if (asRoot) {
        processBuilder.command("su", "-c", cmd).redirectErrorStream(true);
      } else {
        processBuilder.command("sh", "-c", cmd).redirectErrorStream(true);
      }
      process = processBuilder.start();
      process.waitFor();

      InputStream in = process.getInputStream();
      //OutputStreamWriter out = new OutputStreamWriter(process.getOutputStream());

      length = in.read(buffer);
      if (length > 0) output = new String(buffer, 0, length);

    } catch (IOException e) {
      RobotLog.logStacktrace(e);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    } finally {
      if (process != null) process.destroy();
    }
    return output;
  }

  /**
   * Kill any spawn processes matching a given process name
   * @param processName name of process to kill
   * @param packageName name of this package
   * @param shell an instance of RunShellCommand
   * @throws RobotCoreException if unable to kill process
   */
  public static void killSpawnedProcess(String processName, String packageName, RunShellCommand shell) throws RobotCoreException {
    try {
      int pid = getSpawnedProcessPid(processName, packageName, shell);
      while (pid != -1) {
        RobotLog.v("Killing PID " + pid);
        shell.run(String.format("kill %d", pid));
        pid = getSpawnedProcessPid(processName, packageName, shell);
      }
    } catch (Exception e) {
      throw new RobotCoreException(String.format("Failed to kill %s instances started by this app", processName));
    }
  }

  /**
   * return the PID of a given process name started a given package name
   * @param processName name of process to search for
   * @param packageName name of this package
   * @param shell an instance of RunShellCommand
   * @return PID, or -1 if none found
   */
  public static int getSpawnedProcessPid(String processName, String packageName, RunShellCommand shell) {
    // This method has a heavy dependency on the Android version of 'ps'.

    // run ps
    String psOutput = shell.run("ps");
    String username = "invalid";

    // determine the username of this app
    for (String line : psOutput.split("\n")) {
      if (line.contains(packageName)) {
        String[] tokens = line.split("\\s+");
        username = tokens[0];
        break;
      }
    }

    // find an instance of logcat started by this app, if any
    for (String line : psOutput.split("\n")) {
      if (line.contains(processName) && line.contains(username)) {
        String[] tokens = line.split("\\s+");
        return Integer.parseInt(tokens[1]); // if 'ps' changes format this call will fail
      }
    }

    return -1;
  }
}
