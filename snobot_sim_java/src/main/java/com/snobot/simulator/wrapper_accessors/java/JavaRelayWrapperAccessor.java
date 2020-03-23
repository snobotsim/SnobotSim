package com.snobot.simulator.wrapper_accessors.java;

import java.util.Map;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.module_wrapper.factories.DefaultRelayWrapperFactory;
import com.snobot.simulator.module_wrapper.interfaces.IRelayWrapper;
import com.snobot.simulator.wrapper_accessors.RelayWrapperAccessor;

public class JavaRelayWrapperAccessor extends BaseWrapperAccessor<IRelayWrapper> implements RelayWrapperAccessor
{
    private final DefaultRelayWrapperFactory mFactory;

    public JavaRelayWrapperAccessor()
    {
        mFactory = new DefaultRelayWrapperFactory();
    }

    @Override
    public IRelayWrapper createSimulator(int aPort, String aType)
    {
        mFactory.create(aPort, aType);
        return getWrapper(aPort);
    }

    @Override
    public IRelayWrapper getWrapper(int aHandle) 
    {
        return getValue(aHandle);
    }

    @Override
    protected Map<Integer, IRelayWrapper> getMap()
    {
        return SensorActuatorRegistry.get().getRelays();
    }
}
