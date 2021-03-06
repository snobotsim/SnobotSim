
package com.snobot.simulator.wrapper_accessors.jni;

import com.snobot.simulator.jni.module_wrapper.DigitalSourceWrapperJni;
import com.snobot.simulator.module_wrapper.interfaces.IDigitalIoWrapper;
import com.snobot.simulator.module_wrappers.DigitalSourceWrapper;
import com.snobot.simulator.wrapper_accessors.DigitalSourceWrapperAccessor;

public class JniDigitalSourceWrapperAccessor extends BaseWrapperAccessor<IDigitalIoWrapper> implements DigitalSourceWrapperAccessor
{
    @Override
    public IDigitalIoWrapper createSimulator(int aPort, String aType)
    {
        DigitalSourceWrapper wrapper = new DigitalSourceWrapper(aPort, aType);
        register(aPort, wrapper);
        return wrapper;
    }

    @Override
    protected DigitalSourceWrapper createWrapperForExistingType(int aHandle)
    {
        return new DigitalSourceWrapper(aHandle);
    }

    @Override
    public int[] getPortList()
    {
        return DigitalSourceWrapperJni.getPortList();
    }
}
