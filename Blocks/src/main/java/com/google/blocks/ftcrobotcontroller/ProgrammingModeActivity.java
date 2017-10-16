// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller;

import android.os.Bundle;

import com.google.blocks.AbstractProgrammingModeActivity;
import com.qualcomm.ftccommon.LaunchActivityConstantsList;

import org.firstinspires.ftc.robotcore.internal.system.Assert;
import org.firstinspires.ftc.robotcore.internal.ui.LocalByRefIntentExtraHolder;
import org.firstinspires.ftc.robotcore.internal.webserver.PingDetails;

/**
 * Activity class for programming mode.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
public class ProgrammingModeActivity extends AbstractProgrammingModeActivity {

  @Override public String getTag() { return this.getClass().getSimpleName(); }

  protected ProgrammingWebHandlers programmingWebHandlers;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    LocalByRefIntentExtraHolder holder = getIntent().getParcelableExtra(LaunchActivityConstantsList.PROGRAMMING_MODE_ACTIVITY_PROGRAMMING_WEB_HANDLERS);
    programmingWebHandlers = (ProgrammingWebHandlers)holder.getTargetAndForget();
    Assert.assertNotNull(programmingWebHandlers);
  }

  @Override
  public void onResume() {
    super.onResume();

    ProgrammingModeLog programmingModeLog = new ProgrammingModeLog() {
      @Override
      public void addToLog(String msg) {
        addMessageToTextViewLog(msg);
      }

      @Override
      public void ping(PingDetails pingDetails) {
        addPing(pingDetails);
      }
    };

    programmingWebHandlers.setProgrammingModeLog(programmingModeLog);

    // Update the display.
    updateDisplay(programmingWebHandlers.getWebServer().getConnectionInformation());
  }
}
