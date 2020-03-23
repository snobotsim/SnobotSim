
package com.snobot.simulator.wrapper_accessors.jni;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.snobot.simulator.jni.module_wrapper.SolenoidWrapperJni;
import com.snobot.simulator.module_wrapper.interfaces.ISolenoidWrapper;
import com.snobot.simulator.wrapper_accessors.SolenoidWrapperAccessor;

public class JniSolenoidWrapperAccessor implements SolenoidWrapperAccessor
{
    private static class SolenoidWrapper implements ISolenoidWrapper
    {
        private final int mHandle;

        private SolenoidWrapper(int aHandle)
        {
            mHandle = aHandle;
        }

        @Override
        public boolean isInitialized()
        {
            return SolenoidWrapperJni.isInitialized(mHandle);
        }

        @Override
        public void setInitialized(boolean aInitialized)
        {
            // TODO Auto-generated method stub

        }

        @Override
        public String getName()
        {
            return SolenoidWrapperJni.getName(mHandle);
        }

        @Override
        public void setName(String aName)
        {
            SolenoidWrapperJni.setName(mHandle, aName);
        }

        @Override
        public boolean getWantsHidden()
        {
            return SolenoidWrapperJni.getWantsHidden(mHandle);
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
        public void set(boolean aState)
        {
            // TODO Auto-generated method stub

        }

        @Override
        public boolean get()
        {
            return SolenoidWrapperJni.get(mHandle);
        }
    }

    private final Map<Integer, SolenoidWrapper> mWrappers = new HashMap<>();

    @Override
    public List<Integer> getPortList()
    {
        return IntStream.of(SolenoidWrapperJni.getPortList()).boxed().collect(Collectors.toList());
    }

    @Override
    public String getType(int aPort)
    {
        return null;
    }

    @Override
    public ISolenoidWrapper createSimulator(int aPort, String aType)
    {
        SolenoidWrapperJni.createSimulator(aPort, aType);
        mWrappers.put(aPort, new SolenoidWrapper(aPort));
        return mWrappers.get(aPort);
    }

    @Override
    public ISolenoidWrapper getWrapper(int aHandle)
    {
        if (!mWrappers.containsKey(aHandle))
        {
            mWrappers.put(aHandle, new SolenoidWrapper(aHandle));
        }
        return mWrappers.get(aHandle);
    }
}
