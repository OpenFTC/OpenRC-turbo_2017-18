package com.google.blocks.ftcrobotcontroller;

import org.firstinspires.ftc.robotcore.internal.webserver.WebServer;
import org.openftc.turbo.TurboException;

/**
 * This is a dummy class, used in the OpenFTC Turbo build variant
 */

public class ProgrammingWebHandlers {
    public WebServer getWebServer() {
        throw new TurboException();
    }

    public void setState(Object ftcRobotControllerServiceState) {
        throw new TurboException();
    }
}
