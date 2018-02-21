package me.joshlin.a3565lib;

import com.qualcomm.robotcore.hardware.Gamepad;

/**
 * Created by josh on 2/17/18.
 */

public abstract class InputHandler {
    protected Gamepad gamepad1;
    protected Gamepad gamepad2;

    public InputHandler(Gamepad gamepad1, Gamepad gamepad2) {
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
    }

    public abstract void init(RobotMap robotMap);

    public abstract void handleInput();
}
