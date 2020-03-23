
package com.snobot.simulator.wrapper_accessors.jni;

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

        private SolenoidWrapper(int aHandle, String aType)
        {
            SolenoidWrapperJni.createSimulator(aHandle, aType);
            mHandle = aHandle;
        }

        @Override
        public boolean isInitialized() {
            return SolenoidWrapperJni.isInitialized(mHandle);
        }

        @Override
        public void setInitialized(boolean aInitialized) {
            // TODO Auto-generated method stub

        }

        @Override
        public String getName() {
            return SolenoidWrapperJni.getName(mHandle);
        }

        @Override
        public void setName(String aName) {
            SolenoidWrapperJni.setName(mHandle, aName);
        }

        @Override
        public boolean getWantsHidden() {
            return SolenoidWrapperJni.getWantsHidden(mHandle);
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
        public void set(boolean aState) {
            // TODO Auto-generated method stub

        }

        @Override
        public boolean get() {
            return SolenoidWrapperJni.get(mHandle);
        }
    }
    
    private Map<Integer, SolenoidWrapper> mWrappers;

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
    public ISolenoidWrapper createSimulator(int aPort, String aType) {
        return mWrappers.put(aPort, new SolenoidWrapper(aPort, aType));
    }

    @Override
    public ISolenoidWrapper getWrapper(int aHandle) {
        return mWrappers.get(aHandle);
    }
}
