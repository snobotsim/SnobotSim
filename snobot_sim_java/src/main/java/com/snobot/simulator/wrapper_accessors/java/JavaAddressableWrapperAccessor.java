package com.snobot.simulator.wrapper_accessors.java;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.module_wrapper.factories.DefaultAddressableWrapperFactory;
import com.snobot.simulator.module_wrapper.interfaces.IAddressableLedWrapper;
import com.snobot.simulator.wrapper_accessors.AddressableLedWrapperAccessor;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import java.util.Map;

public class JavaAddressableWrapperAccessor extends BaseWrapperAccessor<IAddressableLedWrapper> implements AddressableLedWrapperAccessor
{

    private final DefaultAddressableWrapperFactory mFactory;

    public JavaAddressableWrapperAccessor()
    {
        mFactory = new DefaultAddressableWrapperFactory();
    }

    @Override
    public IAddressableLedWrapper createSimulator(int aPort, String aType)
    {
        mFactory.create(aPort, aType);
        return getWrapper(aPort);
    }

    @Override
    public IAddressableLedWrapper getWrapper(int aHandle) {
        return getValue(aHandle);
    }

    @Override
    protected Map<Integer, IAddressableLedWrapper> getMap()
    {
        return SensorActuatorRegistry.get().getLeds();
    }
}
