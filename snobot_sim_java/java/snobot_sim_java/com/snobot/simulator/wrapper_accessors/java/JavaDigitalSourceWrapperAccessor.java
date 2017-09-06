package com.snobot.simulator.wrapper_accessors.java;

import java.util.Map;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.module_wrapper.DigitalSourceWrapper;
import com.snobot.simulator.wrapper_accessors.DigitalSourceWrapperAccessor;

public class JavaDigitalSourceWrapperAccessor extends BaseWrapperAccessor<DigitalSourceWrapper> implements DigitalSourceWrapperAccessor
{
    @Override
    protected Map<Integer, DigitalSourceWrapper> getMap()
    {
        return SensorActuatorRegistry.get().getDigitalSources();
    }

    @Override
    public boolean getState(int aPort)
    {
        return getValue(aPort).get();
    }

    @Override
    public void setState(int aPort, boolean aValue)
    {
        getValue(aPort).set(aValue);
    }
}
