
package com.snobot.simulator.wrapper_accessors.jni;

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

        private EncoderWrapper(int aHandle, String aType)
        {
            EncoderWrapperJni.createSimulator(aHandle, aType);
            mHandle = aHandle;
        }

        @Override
        public boolean isInitialized() {
            return EncoderWrapperJni.isInitialized(mHandle);
        }

        @Override
        public void setInitialized(boolean aInitialized) {
            // TODO Auto-generated method stub

        }

        @Override
        public String getName() {
            return EncoderWrapperJni.getName(mHandle);
        }

        @Override
        public void setName(String aName) {
            EncoderWrapperJni.setName(mHandle, aName);
        }

        @Override
        public boolean getWantsHidden() {
            return EncoderWrapperJni.getWantsHidden(mHandle);
        }

        @Override
        public void setWantsHidden(boolean aVisible) {
            // TODO Auto-generated method stub

        }

        @Override
        public void close() throws Exception {
            // TODO Auto-generated method stub

        }

        @Override
        public void setPosition(double aPosition) {
            // TODO Auto-generated method stub

        }

        @Override
        public void setVelocity(double aVelocity) {
            // TODO Auto-generated method stub

        }

        @Override
        public double getPosition() {
            return EncoderWrapperJni.getDistance(mHandle);
        }

        @Override
        public double getVelocity() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public void reset() {
            // TODO Auto-generated method stub

        }

        @Override
        public boolean isHookedUp() {
            return EncoderWrapperJni.isHookedUp(mHandle);
        }

        @Override
        public int getHookedUpId() {
            return EncoderWrapperJni.getHookedUpId(mHandle);
        }

        @Override
        public boolean connectSpeedController(int aSpeedControllerHandle) {
            return EncoderWrapperJni.connectSpeedController(mHandle, aSpeedControllerHandle);
        }
    }


    private Map<Integer, EncoderWrapper> mWrappers;

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
    public IEncoderWrapper createSimulator(int aPort, String aType) {
        return mWrappers.put(aPort, new EncoderWrapper(aPort, aType));
    }

    @Override
    public IEncoderWrapper getWrapper(int aHandle) {
        return mWrappers.get(aHandle);
    }
}
