package com.snobot.simulator.wrapper_accessors.java;

import java.util.Map;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.module_wrapper.factories.DefaultAnalogInWrapperFactory;
import com.snobot.simulator.module_wrapper.interfaces.IAnalogInWrapper;
import com.snobot.simulator.wrapper_accessors.AnalogInWrapperAccessor;

public class JavaAnalogInWrapperAccessor extends BaseWrapperAccessor<IAnalogInWrapper> implements AnalogInWrapperAccessor
{

    private final DefaultAnalogInWrapperFactory mFactory;

    public JavaAnalogInWrapperAccessor()
    {
        mFactory = new DefaultAnalogInWrapperFactory();
    }

    @Override
    public IAnalogInWrapper createSimulator(int aPort, String aType)
    {
        mFactory.create(aPort, aType);
        return getValue(aPort);
    }

    @Override
    protected Map<Integer, IAnalogInWrapper> getMap()
    {
        return SensorActuatorRegistry.get().getAnalogIn();
    }

    @Override
    public IAnalogInWrapper getWrapper(int aPort) {
        return getValue(aPort);
    }
}
