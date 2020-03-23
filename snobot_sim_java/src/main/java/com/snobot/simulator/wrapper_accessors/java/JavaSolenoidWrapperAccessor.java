package com.snobot.simulator.wrapper_accessors.java;

import java.util.Map;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.module_wrapper.factories.DefaultSolenoidWrapperFactory;
import com.snobot.simulator.module_wrapper.interfaces.ISolenoidWrapper;
import com.snobot.simulator.wrapper_accessors.SolenoidWrapperAccessor;

public class JavaSolenoidWrapperAccessor extends BaseWrapperAccessor<ISolenoidWrapper> implements SolenoidWrapperAccessor
{
    private final DefaultSolenoidWrapperFactory mFactory;

    public JavaSolenoidWrapperAccessor()
    {
        mFactory = new DefaultSolenoidWrapperFactory();
    }


    @Override
    public ISolenoidWrapper createSimulator(int aPort, String aType)
    {
        mFactory.create(aPort, aType);
        return getWrapper(aPort);
    }

    @Override
    public ISolenoidWrapper getWrapper(int aHandle)
    {
        return getValue(aHandle);
    }

    @Override
    protected Map<Integer, ISolenoidWrapper> getMap()
    {
        return SensorActuatorRegistry.get().getSolenoids();
    }
}
