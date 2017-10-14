package com.snobot.simulator.simulator_components.gyro;

import com.snobot.simulator.module_wrapper.ISensorWrapper;

public interface IGyroWrapper extends ISensorWrapper
{
    double getAngle();

    void setAngle(double aAngle);
}
