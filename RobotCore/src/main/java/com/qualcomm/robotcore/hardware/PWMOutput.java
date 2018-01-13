package com.qualcomm.robotcore.hardware;

/**
 * Created by bob on 2016-03-12.
 */
public interface PWMOutput extends HardwareDevice {
    int getPulseWidthOutputTime();

    void setPulseWidthOutputTime(int usDuration);

    int getPulseWidthPeriod();

    void setPulseWidthPeriod(int usFrame);
}
