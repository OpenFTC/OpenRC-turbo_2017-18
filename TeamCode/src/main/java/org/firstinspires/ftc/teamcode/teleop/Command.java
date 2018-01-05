package org.firstinspires.ftc.teamcode.teleop;

import org.firstinspires.ftc.teamcode.robot.RevbotCommands;

/**
 * Created by josh on 1/1/18.
 */

public interface Command {
    void execute();
}

class ClawOpenCommand implements Command {
    RevbotCommands commands;


    @Override
    public void execute() {
        commands.clawOpen();
    }
}

class ClawCloseCommand implements Command {
    @Override
    public void execute() {
        
    }
}

class LeftClawOpenCommand implements Command {
    @Override
    public void execute() {

    }
}

class RightClawOpenCommand implements Command {
    @Override
    public void execute() {

    }
}

class WinchRaiseCommand implements Command {
    @Override
    public void execute() {

    }
}

class WinchLowerCommand implements Command {
    @Override
    public void execute() {

    }
}

class FondleLeftCommand implements Command {
    @Override
    public void execute() {

    }
}

class FondleCenterCommand implements Command {
    @Override
    public void execute() {

    }
}

class FondleRightCommand implements Command {
    @Override
    public void execute() {

    }
}

class LiftRaiseCommand implements Command {
    @Override
    public void execute() {

    }
}

class LiftLowerCommand implements Command {
    @Override
    public void execute() {
        
    }
}
