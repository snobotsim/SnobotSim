package com.snobot.simulator.simulator_components.gyro;

import com.snobot.simulator.module_wrapper.ASensorWrapper;

public class GyroWrapper extends ASensorWrapper
{
    protected double mAngle;

    public GyroWrapper(String name)
    {
        super(name);
    }

    public void setAngle(double aAngle)
    {
        mAngle = aAngle;
    }

    public double getAngle()
    {
        return mAngle;
    }
}
