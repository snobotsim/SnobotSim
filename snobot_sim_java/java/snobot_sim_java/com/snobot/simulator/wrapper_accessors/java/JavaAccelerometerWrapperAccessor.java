package com.snobot.simulator.wrapper_accessors.java;

import java.util.Map;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.simulator_components.accelerometer.AccelerometerWrapper;
import com.snobot.simulator.wrapper_accessors.AccelerometerWrapperAccessor;

public class JavaAccelerometerWrapperAccessor extends BaseWrapperAccessor<AccelerometerWrapper> implements AccelerometerWrapperAccessor
{

    @Override
    public void register(int aPort, String aName)
    {

    }

    @Override
    public double getAcceleration(int aPort)
    {
        return getValue(aPort).getAcceleration();
    }

    @Override
    public void setAcceleration(int aPort, double aAcceleration)
    {
        getValue(aPort).setAcceleration(aAcceleration);
    }

    @Override
    protected Map<Integer, AccelerometerWrapper> getMap()
    {
        return SensorActuatorRegistry.get().getAccelerometers();
    }

}
