// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;

import com.google.blocks.ftcrobotcontroller.util.HardwareItem;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * A class that provides JavaScript access to a {@link DcMotor}.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
class DcMotorAccess extends HardwareAccess<DcMotor> {
    private final DcMotor dcMotor;

    DcMotorAccess(BlocksOpMode blocksOpMode, HardwareItem hardwareItem, HardwareMap hardwareMap) {
        super(blocksOpMode, hardwareItem, hardwareMap, DcMotor.class);
        this.dcMotor = hardwareDevice;
    }

    // From com.qualcomm.robotcore.hardware.DcMotorSimple

    @SuppressWarnings("unused")
    @JavascriptInterface
    @Block(classes = {DcMotor.class}, methodName = "setDirection")
    public void setDirection(String directionString) {
        startBlockExecution(BlockType.SETTER, ".Direction");
        Direction direction = checkArg(directionString, Direction.class, "");
        if (direction != null) {
            dcMotor.setDirection(direction);
        }
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    @Block(classes = {DcMotor.class}, methodName = "getDirection")
    public String getDirection() {
        startBlockExecution(BlockType.GETTER, ".Direction");
        Direction direction = dcMotor.getDirection();
        if (direction != null) {
            return direction.toString();
        }
        return "";
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    @Block(classes = {DcMotor.class}, methodName = "setPower")
    public void setPower(double power) {
        startBlockExecution(BlockType.SETTER, ".Power");
        dcMotor.setPower(power);
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    @Block(classes = {DcMotor.class}, methodName = "getPower")
    public double getPower() {
        startBlockExecution(BlockType.GETTER, ".Power");
        return dcMotor.getPower();
    }

    // From com.qualcomm.robotcore.hardware.DcMotor

    @SuppressWarnings("unused")
    @JavascriptInterface
    @Deprecated
    public void setMaxSpeed(double maxSpeed) {
        startBlockExecution(BlockType.SETTER, ".MaxSpeed");
        // This method does nothing. MaxSpeed is deprecated.
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    @Deprecated
    public int getMaxSpeed() {
        startBlockExecution(BlockType.GETTER, ".MaxSpeed");
        // This method always returns 0. MaxSpeed is deprecated.
        return 0;
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    @Block(classes = {DcMotor.class}, methodName = "setZeroPowerBehavior")
    public void setZeroPowerBehavior(String zeroPowerBehaviorString) {
        startBlockExecution(BlockType.SETTER, ".ZeroPowerBehavior");
        ZeroPowerBehavior zeroPowerBehavior = checkArg(zeroPowerBehaviorString, ZeroPowerBehavior.class, "");
        if (zeroPowerBehavior != null) {
            dcMotor.setZeroPowerBehavior(zeroPowerBehavior);
        }
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    @Block(classes = {DcMotor.class}, methodName = "getZeroPowerBehavior")
    public String getZeroPowerBehavior() {
        startBlockExecution(BlockType.GETTER, ".ZeroPowerBehavior");
        ZeroPowerBehavior zeroPowerBehavior = dcMotor.getZeroPowerBehavior();
        if (zeroPowerBehavior != null) {
            return zeroPowerBehavior.toString();
        }
        return "";
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    @Block(classes = {DcMotor.class}, methodName = "getPowerFloat")
    public boolean getPowerFloat() {
        startBlockExecution(BlockType.GETTER, ".PowerFloat");
        return dcMotor.getPowerFloat();
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    @Block(classes = {DcMotor.class}, methodName = "setTargetPosition")
    public void setTargetPosition(double position) {
        startBlockExecution(BlockType.SETTER, ".TargetPosition");
        dcMotor.setTargetPosition((int) position);
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    @Block(classes = {DcMotor.class}, methodName = "getTargetPosition")
    public int getTargetPosition() {
        startBlockExecution(BlockType.GETTER, ".TargetPosition");
        return dcMotor.getTargetPosition();
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    @Block(classes = {DcMotor.class}, methodName = "isBusy")
    public boolean isBusy() {
        startBlockExecution(BlockType.FUNCTION, ".isBusy");
        return dcMotor.isBusy();
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    @Block(classes = {DcMotor.class}, methodName = "getCurrentPosition")
    public int getCurrentPosition() {
        startBlockExecution(BlockType.GETTER, ".CurrentPosition");
        return dcMotor.getCurrentPosition();
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    @Block(classes = {DcMotor.class}, methodName = "setMode")
    public void setMode(String runModeString) {
        startBlockExecution(BlockType.SETTER, ".Mode");
        RunMode runMode = checkArg(runModeString, RunMode.class, "");
        if (runMode != null) {
            dcMotor.setMode(runMode);
        }
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    @Block(classes = {DcMotor.class}, methodName = "getMode")
    public String getMode() {
        startBlockExecution(BlockType.GETTER, ".Mode");
        RunMode runMode = dcMotor.getMode();
        if (runMode != null) {
            return runMode.toString();
        }
        return "";
    }

    // Dual set property

    @SuppressWarnings("unused")
    @JavascriptInterface
    @Deprecated
    public void setDualMaxSpeed(double maxSpeed1, Object otherArg, double maxSpeed2) {
        startBlockExecution(BlockType.SETTER, ".MaxSpeed");
        // This method does nothing. MaxSpeed is deprecated.
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    @Block(classes = {DcMotor.class}, methodName = "setMode")
    public void setDualMode(String runMode1String, Object otherArg, String runMode2String) {
        startBlockExecution(BlockType.SETTER, ".Mode");
        RunMode runMode1 = checkArg(runMode1String, RunMode.class, "first");
        RunMode runMode2 = checkArg(runMode2String, RunMode.class, "second");
        if (runMode1 != null && runMode2 != null &&
                otherArg instanceof DcMotorAccess) {
            DcMotorAccess other = (DcMotorAccess) otherArg;
            dcMotor.setMode(runMode1);
            other.dcMotor.setMode(runMode2);
        }
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    @Block(classes = {DcMotor.class}, methodName = "setPower")
    public void setDualPower(double power1, Object otherArg, double power2) {
        startBlockExecution(BlockType.SETTER, ".Power");
        if (otherArg instanceof DcMotorAccess) {
            DcMotorAccess other = (DcMotorAccess) otherArg;
            dcMotor.setPower(power1);
            other.dcMotor.setPower(power2);
        }
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    @Block(classes = {DcMotor.class}, methodName = "setTargetPosition")
    public void setDualTargetPosition(double position1, Object otherArg, double position2) {
        startBlockExecution(BlockType.SETTER, ".TargetPosition");
        if (otherArg instanceof DcMotorAccess) {
            DcMotorAccess other = (DcMotorAccess) otherArg;
            dcMotor.setTargetPosition((int) position1);
            other.dcMotor.setTargetPosition((int) position2);
        }
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    @Block(classes = {DcMotor.class}, methodName = "setZeroPowerBehavior")
    public void setDualZeroPowerBehavior(String zeroPowerBehavior1String,
                                         Object otherArg, String zeroPowerBehavior2String) {
        startBlockExecution(BlockType.SETTER, ".ZeroPowerBehavior");
        ZeroPowerBehavior zeroPowerBehavior1 = checkArg(zeroPowerBehavior1String, ZeroPowerBehavior.class, "first");
        ZeroPowerBehavior zeroPowerBehavior2 = checkArg(zeroPowerBehavior2String, ZeroPowerBehavior.class, "second");
        if (zeroPowerBehavior1 != null && zeroPowerBehavior2 != null &&
                otherArg instanceof DcMotorAccess) {
            DcMotorAccess other = (DcMotorAccess) otherArg;
            dcMotor.setZeroPowerBehavior(zeroPowerBehavior1);
            other.dcMotor.setZeroPowerBehavior(zeroPowerBehavior2);
        }
    }
}
