
package com.snobot.simulator.wrapper_accessors.jni;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.snobot.simulator.jni.module_wrapper.GyroWrapperJni;
import com.snobot.simulator.module_wrapper.interfaces.IGyroWrapper;
import com.snobot.simulator.wrapper_accessors.GyroWrapperAccessor;

public class JniGyroWrapperAccessor implements GyroWrapperAccessor
{
    private static class GyroWrapper implements IGyroWrapper
    {
        private final int mHandle;

        private GyroWrapper(int aHandle)
        {
            mHandle = aHandle;
        }

        @Override
        public boolean isInitialized()
        {
            return GyroWrapperJni.isInitialized(mHandle);
        }

        @Override
        public void setInitialized(boolean aInitialized)
        {
            // TODO Auto-generated method stub

        }

        @Override
        public String getName()
        {
            return GyroWrapperJni.getName(mHandle);
        }

        @Override
        public void setName(String aName)
        {
            GyroWrapperJni.setName(mHandle, aName);
        }

        @Override
        public boolean getWantsHidden()
        {
            return GyroWrapperJni.getWantsHidden(mHandle);
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
        public double getAngle()
        {
            return GyroWrapperJni.getAngle(mHandle);
        }

        @Override
        public void setAngle(double aAngle)
        {
            GyroWrapperJni.setAngle(mHandle, aAngle);
        }
    }

    private final Map<Integer, GyroWrapper> mWrappers = new HashMap<>();

    @Override
    public List<Integer> getPortList()
    {
        return IntStream.of(GyroWrapperJni.getPortList()).boxed().collect(Collectors.toList());
    }

    @Override
    public String getType(int aPort)
    {
        return null;
    }

    @Override
    public IGyroWrapper createSimulator(int aPort, String aType)
    {
        GyroWrapperJni.createSimulator(aPort, aType);
        mWrappers.put(aPort, new GyroWrapper(aPort));
        return mWrappers.get(aPort);
    }

    @Override
    public IGyroWrapper getWrapper(int aHandle)
    {
        if (!mWrappers.containsKey(aHandle))
        {
            mWrappers.put(aHandle, new GyroWrapper(aHandle));
        }
        return mWrappers.get(aHandle);
    }
}
