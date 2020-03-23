
package com.snobot.simulator.wrapper_accessors.jni;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.snobot.simulator.jni.module_wrapper.EncoderWrapperJni;
import com.snobot.simulator.module_wrapper.interfaces.IEncoderWrapper;
import com.snobot.simulator.wrapper_accessors.EncoderWrapperAccessor;

public class JniEncoderWrapperAccessor implements EncoderWrapperAccessor
{
    private static class EncoderWrapper implements IEncoderWrapper
    {
        private final int mHandle;

        private EncoderWrapper(int aHandle)
        {
            mHandle = aHandle;
        }

        @Override
        public boolean isInitialized()
        {
            return EncoderWrapperJni.isInitialized(mHandle);
        }

        @Override
        public void setInitialized(boolean aInitialized)
        {
            // TODO Auto-generated method stub

        }

        @Override
        public String getName()
        {
            return EncoderWrapperJni.getName(mHandle);
        }

        @Override
        public void setName(String aName)
        {
            EncoderWrapperJni.setName(mHandle, aName);
        }

        @Override
        public boolean getWantsHidden()
        {
            return EncoderWrapperJni.getWantsHidden(mHandle);
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
        public void setPosition(double aPosition)
        {
            EncoderWrapperJni.setPosition(mHandle, aPosition);
        }

        @Override
        public void setVelocity(double aVelocity)
        {
            EncoderWrapperJni.setVelocity(mHandle, aVelocity);
        }

        @Override
        public double getPosition()
        {
            return EncoderWrapperJni.getDistance(mHandle);
        }

        @Override
        public double getVelocity()
        {
            return EncoderWrapperJni.getVelocity(mHandle);
        }

        @Override
        public void reset()
        {
            EncoderWrapperJni.reset(mHandle);
        }

        @Override
        public boolean isHookedUp()
        {
            return EncoderWrapperJni.isHookedUp(mHandle);
        }

        @Override
        public int getHookedUpId()
        {
            return EncoderWrapperJni.getHookedUpId(mHandle);
        }

        @Override
        public boolean connectSpeedController(int aSpeedControllerHandle)
        {
            return EncoderWrapperJni.connectSpeedController(mHandle, aSpeedControllerHandle);
        }
    }


    private final Map<Integer, EncoderWrapper> mWrappers = new HashMap<>();

    @Override
    public List<Integer> getPortList()
    {
        return IntStream.of(EncoderWrapperJni.getPortList()).boxed().collect(Collectors.toList());
    }

    @Override
    public String getType(int aPort)
    {
        return null;
    }

    @Override
    public IEncoderWrapper createSimulator(int aPort, String aType)
    {
        EncoderWrapperJni.createSimulator(aPort, aType);
        mWrappers.put(aPort, new EncoderWrapper(aPort));
        return mWrappers.get(aPort);
    }

    @Override
    public IEncoderWrapper getWrapper(int aHandle)
    {
        if (!mWrappers.containsKey(aHandle))
        {
            mWrappers.put(aHandle, new EncoderWrapper(aHandle));
        }
        return mWrappers.get(aHandle);
    }
}
