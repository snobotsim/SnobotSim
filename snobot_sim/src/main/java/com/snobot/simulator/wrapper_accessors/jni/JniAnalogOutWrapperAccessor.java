
package com.snobot.simulator.wrapper_accessors.jni;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.snobot.simulator.jni.module_wrapper.AnalogOutWrapperJni;
import com.snobot.simulator.module_wrapper.interfaces.IAnalogOutWrapper;
import com.snobot.simulator.wrapper_accessors.AnalogOutWrapperAccessor;

public class JniAnalogOutWrapperAccessor implements AnalogOutWrapperAccessor
{
    private static class AnalogOutWrapper implements IAnalogOutWrapper
    {
        private final int mHandle;

        private AnalogOutWrapper(int aHandle)
        {
            mHandle = aHandle;
        }

        @Override
        public boolean isInitialized()
        {
            return AnalogOutWrapperJni.isInitialized(mHandle);
        }

        @Override
        public void setInitialized(boolean aInitialized)
        {
            // TODO Auto-generated method stub

        }

        @Override
        public String getName()
        {
            return AnalogOutWrapperJni.getName(mHandle);
        }

        @Override
        public void setName(String aName)
        {
            AnalogOutWrapperJni.setName(mHandle, aName);
        }

        @Override
        public boolean getWantsHidden()
        {
            return AnalogOutWrapperJni.getWantsHidden(mHandle);
        }

        @Override
        public void setWantsHidden(boolean aVisible)
        {
            // TODO Auto-generated method stub

        }

        @Override
        public void close() throws Exception
        {
            // TODO Auto-generated method stub

        }

        @Override
        public void setVoltage(double aVoltage)
        {
        }

        @Override
        public double getVoltage()
        {
            return AnalogOutWrapperJni.getVoltage(mHandle);
        }
    }

    private final Map<Integer, AnalogOutWrapper> mWrappers = new HashMap<>();

    @Override
    public List<Integer> getPortList()
    {
        return IntStream.of(AnalogOutWrapperJni.getPortList()).boxed().collect(Collectors.toList());
    }

    @Override
    public String getType(int aPort)
    {
        return null;
    }

    @Override
    public IAnalogOutWrapper createSimulator(int aPort, String aType)
    {
        AnalogOutWrapperJni.createSimulator(aPort, aType);
        mWrappers.put(aPort, new AnalogOutWrapper(aPort));
        return mWrappers.get(aPort);
    }

    @Override
    public IAnalogOutWrapper getWrapper(int aHandle)
    {
        if (!mWrappers.containsKey(aHandle))
        {
            mWrappers.put(aHandle, new AnalogOutWrapper(aHandle));
        }
        return mWrappers.get(aHandle);
    }
}
