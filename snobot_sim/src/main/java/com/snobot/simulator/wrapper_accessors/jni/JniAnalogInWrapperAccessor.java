
package com.snobot.simulator.wrapper_accessors.jni;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.snobot.simulator.jni.module_wrapper.AnalogInWrapperJni;
import com.snobot.simulator.module_wrapper.interfaces.IAnalogInWrapper;
import com.snobot.simulator.wrapper_accessors.AnalogInWrapperAccessor;

public class JniAnalogInWrapperAccessor implements AnalogInWrapperAccessor
{
    private static class AnalogInWrapper implements IAnalogInWrapper
    {
        private final int mHandle;

        private AnalogInWrapper(int aHandle)
        {
            mHandle = aHandle;
        }

        @Override
        public boolean isInitialized()
        {
            return AnalogInWrapperJni.isInitialized(mHandle);
        }

        @Override
        public void setInitialized(boolean aInitialized)
        {
            // TODO Auto-generated method stub

        }

        @Override
        public String getName()
        {
            return AnalogInWrapperJni.getName(mHandle);
        }

        @Override
        public void setName(String aName)
        {
            AnalogInWrapperJni.setName(mHandle, aName);
        }

        @Override
        public boolean getWantsHidden()
        {
            return AnalogInWrapperJni.getWantsHidden(mHandle);
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
            // TODO Auto-generated method stub

        }

        @Override
        public double getVoltage()
        {
            return AnalogInWrapperJni.getVoltage(mHandle);
        }
    }

    private final Map<Integer, AnalogInWrapper> mWrappers = new HashMap<>();

    @Override
    public List<Integer> getPortList()
    {
        return IntStream.of(AnalogInWrapperJni.getPortList()).boxed().collect(Collectors.toList());
    }

    @Override
    public String getType(int aPort)
    {
        return null;
    }

    @Override
    public IAnalogInWrapper createSimulator(int aPort, String aType)
    {
        AnalogInWrapperJni.createSimulator(aPort, aType);
        System.out.println("Creating..." + aPort + " " + aType);
        System.out.println(mWrappers.containsKey(aPort));
        mWrappers.put(aPort, new AnalogInWrapper(aPort));
        return mWrappers.get(aPort);
    }

    @Override
    public IAnalogInWrapper getWrapper(int aHandle)
    {
        if (!mWrappers.containsKey(aHandle))
        {
            mWrappers.put(aHandle, new AnalogInWrapper(aHandle));
        }
        return mWrappers.get(aHandle);
    }
}
