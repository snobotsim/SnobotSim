package com.snobot.simulator.wrapper_accessors.java;

import java.util.Map;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.module_wrapper.SpeedControllerWrapper;
import com.snobot.simulator.wrapper_accessors.GyroWrapperAccessor;

public class JavaGyroWrapperAccessor extends BaseWrapperAccessor<SpeedControllerWrapper> implements GyroWrapperAccessor
{

    @Override
    public void register(int aPort, String aName)
    {

    }

    @Override
    public double getAngle(int aPort)
    {
        return 0;
    }

    @Override
    public void reset(int aPort)
    {

    }

    @Override
    protected Map<Integer, SpeedControllerWrapper> getMap()
    {
        return SensorActuatorRegistry.get().getSpeedControllers();
    }

}
