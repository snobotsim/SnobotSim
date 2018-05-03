package com.snobot.simulator.wrapper_accessors.java;

import java.util.Map;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.module_wrapper.AnalogWrapper;
import com.snobot.simulator.wrapper_accessors.AnalogSourceWrapperAccessor;

public class JavaAnalogSourceWrapperAccessor extends BaseWrapperAccessor<AnalogWrapper> implements AnalogSourceWrapperAccessor
{
    @Override
    protected Map<Integer, AnalogWrapper> getMap()
    {
        return SensorActuatorRegistry.get().getAnalog();
    }

    @Override
    public double getVoltage(int aPort)
    {
        return getValue(aPort).getVoltage();
    }
}
