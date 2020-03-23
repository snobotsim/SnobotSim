package com.snobot.simulator.wrapper_accessors.java;

import java.util.Map;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.module_wrapper.factories.DefaultEncoderWrapperFactory;
import com.snobot.simulator.module_wrapper.interfaces.IEncoderWrapper;
import com.snobot.simulator.wrapper_accessors.EncoderWrapperAccessor;

public class JavaEncoderWrapperAccessor extends BaseWrapperAccessor<IEncoderWrapper> implements EncoderWrapperAccessor
{
    private final DefaultEncoderWrapperFactory mFactory;

    public JavaEncoderWrapperAccessor()
    {
        mFactory = new DefaultEncoderWrapperFactory();
    }


    @Override
    public IEncoderWrapper createSimulator(int aPort, String aType)
    {
        mFactory.create(aPort, aType);
        return getWrapper(aPort);
    }

    @Override
    public IEncoderWrapper getWrapper(int aHandle) 
    {
        return getValue(aHandle);
    }

    @Override
    protected Map<Integer, IEncoderWrapper> getMap()
    {
        return SensorActuatorRegistry.get().getEncoders();
    }
}
