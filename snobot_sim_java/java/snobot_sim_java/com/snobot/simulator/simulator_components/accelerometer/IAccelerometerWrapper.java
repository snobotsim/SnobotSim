package com.snobot.simulator.simulator_components.accelerometer;

import com.snobot.simulator.module_wrapper.ISensorWrapper;

public interface IAccelerometerWrapper extends ISensorWrapper
{

    void setAcceleration(double aAcceleration);

    double getAcceleration();

}