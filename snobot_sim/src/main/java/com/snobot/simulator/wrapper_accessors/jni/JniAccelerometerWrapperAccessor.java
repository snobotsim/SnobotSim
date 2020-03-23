
package com.snobot.simulator.wrapper_accessors.jni;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.snobot.simulator.jni.module_wrapper.AccelerometerWrapperJni;
import com.snobot.simulator.module_wrapper.interfaces.IAccelerometerWrapper;
import com.snobot.simulator.wrapper_accessors.AccelerometerWrapperAccessor;

public class JniAccelerometerWrapperAccessor implements AccelerometerWrapperAccessor
{

    private static class AcelerometerWrapper implements IAccelerometerWrapper
    {
        private final int mHandle;

        private AcelerometerWrapper(int aHandle)
        {
            mHandle = aHandle;
        }

        @Override
        public boolean isInitialized() {
            return AccelerometerWrapperJni.isInitialized(mHandle);
        }

        @Override
        public void setInitialized(boolean aInitialized) {
            // TODO Auto-generated method stub

        }

        @Override
        public String getName() {
            return AccelerometerWrapperJni.getName(mHandle);
        }

        @Override
        public void setName(String aName) {
            AccelerometerWrapperJni.setName(mHandle, aName);
        }

        @Override
        public boolean getWantsHidden() {
            return AccelerometerWrapperJni.getWantsHidden(mHandle);
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
        public void setAcceleration(double aAcceleration) {
            AccelerometerWrapperJni.setAcceleration(mHandle, aAcceleration);
        }

        @Override
        public double getAcceleration() {
            return AccelerometerWrapperJni.getAcceleration(mHandle);
        }
        
    }

    private Map<Integer, AcelerometerWrapper> mWrappers = new HashMap<>();

    @Override
    public List<Integer> getPortList()
    {
        return IntStream.of(AccelerometerWrapperJni.getPortList()).boxed().collect(Collectors.toList());
    }

    @Override
    public String getType(int aPort)
    {
        return null;
    }

    @Override
    public IAccelerometerWrapper createSimulator(int aPort, String aType) {
        return mWrappers.put(aPort, new AcelerometerWrapper(aPort));
    }

    @Override
    public IAccelerometerWrapper getWrapper(int aHandle) {
        if (!mWrappers.containsKey(aHandle))
        {
            mWrappers.put(aHandle, new AcelerometerWrapper(aHandle));
        }
        return mWrappers.get(aHandle);
    }
}
