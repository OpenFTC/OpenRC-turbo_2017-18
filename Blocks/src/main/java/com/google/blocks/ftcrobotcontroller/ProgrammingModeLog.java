// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller;

import org.firstinspires.ftc.robotcore.internal.webserver.PingDetails;

/**
 * An interface for logging messages during programming mode.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
public interface ProgrammingModeLog {
  /**
   * Called when a message should be added to the log.
   */
  void addToLog(String msg);

  /**
   * Called when the {@link ProgrammingWebHandlers} receives a ping request.
   */
  void ping(PingDetails pingDetails);
}
