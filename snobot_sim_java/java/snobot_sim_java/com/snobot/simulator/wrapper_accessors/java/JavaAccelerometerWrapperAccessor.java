package com.snobot.simulator.wrapper_accessors.java;

import java.util.Map;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.module_wrapper.SpeedControllerWrapper;
import com.snobot.simulator.wrapper_accessors.AccelerometerWrapperAccessor;

public class JavaAccelerometerWrapperAccessor extends BaseWrapperAccessor<SpeedControllerWrapper> implements AccelerometerWrapperAccessor
{

    @Override
    public void register(int aPort, String aName)
    {

    }

    @Override
    public double getAcceleration(int aPort)
    {
        return 0;
    }

    @Override
    protected Map<Integer, SpeedControllerWrapper> getMap()
    {
        return SensorActuatorRegistry.get().getSpeedControllers();
    }

}
