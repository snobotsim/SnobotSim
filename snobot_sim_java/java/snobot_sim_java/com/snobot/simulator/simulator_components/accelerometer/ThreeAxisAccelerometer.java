package com.snobot.simulator.simulator_components.accelerometer;

import com.snobot.simulator.SensorActuatorRegistry;

public class ThreeAxisAccelerometer
{
    protected final AccelerometerWrapper mXWrapper;
    protected final AccelerometerWrapper mYWrapper;
    protected final AccelerometerWrapper mZWrapper;

    public ThreeAxisAccelerometer(int aBasePort, String aBaseName)
    {
        mXWrapper = new AccelerometerWrapper(aBaseName + "X");
        mYWrapper = new AccelerometerWrapper(aBaseName + "Y");
        mZWrapper = new AccelerometerWrapper(aBaseName + "Z");

        SensorActuatorRegistry.get().register(mXWrapper, aBasePort + 0);
        SensorActuatorRegistry.get().register(mYWrapper, aBasePort + 1);
        SensorActuatorRegistry.get().register(mZWrapper, aBasePort + 2);
    }

    public double getX()
    {
        return mXWrapper.getAcceleration();
    }

    public double getY()
    {
        return mYWrapper.getAcceleration();
    }

    public double getZ()
    {
        return mZWrapper.getAcceleration();
    }

}
