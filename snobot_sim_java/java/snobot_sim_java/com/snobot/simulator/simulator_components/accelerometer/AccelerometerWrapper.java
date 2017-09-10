package com.snobot.simulator.simulator_components.accelerometer;

import com.snobot.simulator.module_wrapper.ASensorWrapper;

public class AccelerometerWrapper extends ASensorWrapper
{
    protected double mAcceleration;

    public AccelerometerWrapper(String aName)
    {
        super(aName);
    }

    public void setAcceleration(double aAcceleration)
    {
        mAcceleration = aAcceleration;
    }

    public double getAcceleration()
    {
        return mAcceleration;
    }

}
