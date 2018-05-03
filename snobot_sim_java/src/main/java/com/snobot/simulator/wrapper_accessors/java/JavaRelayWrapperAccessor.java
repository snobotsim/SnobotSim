package com.snobot.simulator.wrapper_accessors.java;

import java.util.Map;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.module_wrapper.RelayWrapper;
import com.snobot.simulator.wrapper_accessors.RelayWrapperAccessor;

public class JavaRelayWrapperAccessor extends BaseWrapperAccessor<RelayWrapper> implements RelayWrapperAccessor
{
    @Override
    protected Map<Integer, RelayWrapper> getMap()
    {
        return SensorActuatorRegistry.get().getRelays();
    }

    @Override
    public boolean getFowardValue(int aPort)
    {
        return getValue(aPort).getRelayForwards();
    }

    @Override
    public boolean getReverseValue(int aPort)
    {
        return getValue(aPort).getRelayReverse();
    }
}
