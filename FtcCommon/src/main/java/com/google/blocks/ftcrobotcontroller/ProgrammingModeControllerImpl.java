package com.google.blocks.ftcrobotcontroller;

import android.app.Activity;
import android.widget.TextView;

import com.qualcomm.ftccommon.FtcEventLoopHandler;
import com.qualcomm.ftccommon.ProgrammingModeController;

import org.openftc.turbo.TurboException;

/**
 * This is a dummy class, used in the OpenFTC Turbo build variant.
 * It really belongs in the Turbo module, but putting it there would result in a circular dependency :/
 */

public class ProgrammingModeControllerImpl implements ProgrammingModeController {
    public ProgrammingModeControllerImpl(Activity activity, TextView textView, ProgrammingWebHandlers programmingWebHandlers) {

    }

    @Override
    public boolean isActive() {
        throw new TurboException();
    }

    @Override
    public void startProgrammingMode(FtcEventLoopHandler ftcEventLoopHandler) {
        throw new TurboException();
    }

    @Override
    public void stopProgrammingMode() {
        throw new TurboException();
    }
}
