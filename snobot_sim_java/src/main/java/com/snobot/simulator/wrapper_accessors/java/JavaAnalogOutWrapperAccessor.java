package com.snobot.simulator.wrapper_accessors.java;

import java.util.Map;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.module_wrapper.factories.DefaultAnalogOutWrapperFactory;
import com.snobot.simulator.module_wrapper.interfaces.IAnalogOutWrapper;
import com.snobot.simulator.wrapper_accessors.AnalogOutWrapperAccessor;

public class JavaAnalogOutWrapperAccessor extends BaseWrapperAccessor<IAnalogOutWrapper> implements AnalogOutWrapperAccessor
{
    private final DefaultAnalogOutWrapperFactory mFactory;

    public JavaAnalogOutWrapperAccessor()
    {
        mFactory = new DefaultAnalogOutWrapperFactory();
    }

    @Override
    public IAnalogOutWrapper createSimulator(int aPort, String aType)
    {
        mFactory.create(aPort, aType);
        return getWrapper(aPort);
    }

    @Override
    public IAnalogOutWrapper getWrapper(int aHandle) {
        return getValue(aHandle);
    }
    @Override
    protected Map<Integer, IAnalogOutWrapper> getMap()
    {
        return SensorActuatorRegistry.get().getAnalogOut();
    }
}
