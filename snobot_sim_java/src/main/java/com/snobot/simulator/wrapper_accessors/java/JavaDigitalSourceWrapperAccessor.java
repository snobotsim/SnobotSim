package com.snobot.simulator.wrapper_accessors.java;

import java.util.Map;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.module_wrapper.factories.DefaultDigitalIoWrapperFactory;
import com.snobot.simulator.module_wrapper.interfaces.IDigitalIoWrapper;
import com.snobot.simulator.wrapper_accessors.DigitalSourceWrapperAccessor;

public class JavaDigitalSourceWrapperAccessor extends BaseWrapperAccessor<IDigitalIoWrapper> implements DigitalSourceWrapperAccessor
{
    private final DefaultDigitalIoWrapperFactory mFactory;

    public JavaDigitalSourceWrapperAccessor()
    {
        mFactory = new DefaultDigitalIoWrapperFactory();
    }

    @Override
    public IDigitalIoWrapper createSimulator(int aPort, String aType)
    {
        mFactory.create(aPort, aType);
        return getWrapper(aPort);
    }

    @Override
    public IDigitalIoWrapper getWrapper(int aHandle) {
        return getValue(aHandle);
    }

    @Override
    protected Map<Integer, IDigitalIoWrapper> getMap()
    {
        return SensorActuatorRegistry.get().getDigitalSources();
    }
}
