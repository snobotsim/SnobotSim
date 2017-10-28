package com.snobot.simulator.simulator_components.accelerometer;

import com.snobot.simulator.module_wrapper.ASensorWrapper;

public class AnalogAccelerometerWrapper extends ASensorWrapper implements IAccelerometerWrapper
{
    protected double mAcceleration;

    public AnalogAccelerometerWrapper(String aName)
    {
        super(aName);
    }

    @Override
    public void setAcceleration(double aAcceleration)
    {
        mAcceleration = aAcceleration;
    }

    @Override
    public double getAcceleration()
    {
        return mAcceleration;
    }

}
