package com.snobot.simulator.simulator_components.gyro;

import com.snobot.simulator.jni.standard_components.AnalogGyroCallbackJni;
import com.snobot.simulator.module_wrapper.ASensorWrapper;

public class AnalogGyroWrapper extends ASensorWrapper implements IGyroWrapper
{
    protected final int mPort;
    protected double mAngle;

    public AnalogGyroWrapper(int aPort, String aName)
    {
        super(aName);
        mPort = aPort;
    }

    public void setAngle(double aAngle)
    {
        boolean isUpdate = mAngle != aAngle;
        mAngle = aAngle;
        if (isUpdate)
        {
            AnalogGyroCallbackJni.setAnalogGyroAngle(mPort, aAngle);
        }
    }

    public double getAngle()
    {
        return mAngle;
    }
}
