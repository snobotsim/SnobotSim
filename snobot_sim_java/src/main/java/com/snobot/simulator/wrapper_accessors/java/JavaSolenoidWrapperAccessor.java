package com.snobot.simulator.wrapper_accessors.java;

import java.util.Map;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.module_wrapper.SolenoidWrapper;
import com.snobot.simulator.wrapper_accessors.SolenoidWrapperAccessor;

public class JavaSolenoidWrapperAccessor extends BaseWrapperAccessor<SolenoidWrapper> implements SolenoidWrapperAccessor
{
    @Override
    protected Map<Integer, SolenoidWrapper> getMap()
    {
        return SensorActuatorRegistry.get().getSolenoids();
    }

    @Override
    public boolean get(int aPort)
    {
        return getValue(aPort).get();
    }
}
