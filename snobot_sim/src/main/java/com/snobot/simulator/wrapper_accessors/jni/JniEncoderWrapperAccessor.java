
package com.snobot.simulator.wrapper_accessors.jni;

import com.snobot.simulator.jni.module_wrapper.EncoderWrapperJni;
import com.snobot.simulator.module_wrapper.interfaces.IEncoderWrapper;
import com.snobot.simulator.module_wrappers.EncoderWrapper;
import com.snobot.simulator.wrapper_accessors.EncoderWrapperAccessor;

public class JniEncoderWrapperAccessor extends BaseWrapperAccessor<IEncoderWrapper> implements EncoderWrapperAccessor
{
    @Override
    public IEncoderWrapper createSimulator(int aPort, String aType)
    {
        EncoderWrapper wrapper = new EncoderWrapper(aPort, aType);
        register(aPort, wrapper);
        return wrapper;
    }

    @Override
    protected EncoderWrapper createWrapperForExistingType(int aHandle)
    {
        return new EncoderWrapper(aHandle);
    }

    @Override
    public int[] getPortList()
    {
        return EncoderWrapperJni.getPortList();
    }
}
