
package com.snobot.simulator.wrapper_accessors.jni;

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

        private AnalogInWrapper(int aHandle, String aType)
        {
            AnalogInWrapperJni.createSimulator(aHandle, aType);
            mHandle = aHandle;
        }

        @Override
        public boolean isInitialized() {
            return AnalogInWrapperJni.isInitialized(mHandle);
        }

        @Override
        public void setInitialized(boolean aInitialized) {
            // TODO Auto-generated method stub

        }

        @Override
        public String getName() {
            return AnalogInWrapperJni.getName(mHandle);
        }

        @Override
        public void setName(String aName) {
            AnalogInWrapperJni.setName(mHandle, aName);
        }

        @Override
        public boolean getWantsHidden() {
            return AnalogInWrapperJni.getWantsHidden(mHandle);
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
        public void setVoltage(double aVoltage) {
            // TODO Auto-generated method stub

        }

        @Override
        public double getVoltage() {
            return AnalogInWrapperJni.getVoltage(mHandle);
        }
    }

    // @Override
    // public boolean isInitialized(int aPort)
    // {
    //     return AnalogInWrapperJni.isInitialized(aPort);
    // }

    // @Override
    // public boolean createSimulator(int aPort, String aType)
    // {
    //     return AnalogInWrapperJni.createSimulator(aPort, aType);
    // }

    // @Override
    // public void removeSimulator(int aPort)
    // {
    //     AnalogInWrapperJni.removeSimluator(aPort);
    // }

    // @Override
    // public void setName(int aPort, String aName)
    // {
    //     AnalogInWrapperJni.setName(aPort, aName);
    // }

    // @Override
    // public String getName(int aPort)
    // {
    //     return AnalogInWrapperJni.getName(aPort);
    // }

    // @Override
    // public boolean getWantsHidden(int aPort)
    // {
    //     return AnalogInWrapperJni.getWantsHidden(aPort);
    // }

    // @Override
    // public double getVoltage(int aPort)
    // {
    //     return AnalogInWrapperJni.getVoltage(aPort);
    // }

    // @Override
    // public void setVoltage(int aPort, double aVoltage)
    // {
    //     // nothing to do
    // }

    private Map<Integer, AnalogInWrapper> mWrappers;

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
    public IAnalogInWrapper createSimulator(int aPort, String aType) {
        return mWrappers.put(aPort, new AnalogInWrapper(aPort, aType));
    }

    @Override
    public IAnalogInWrapper getWrapper(int aHandle) {
        return mWrappers.get(aHandle);
    }
}
