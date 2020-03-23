
package com.snobot.simulator.wrapper_accessors.jni;

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

        private AnalogOutWrapper(int aHandle, String aType)
        {
            AnalogOutWrapperJni.createSimulator(aHandle, aType);
            mHandle = aHandle;
        }

        @Override
        public boolean isInitialized() {
            return AnalogOutWrapperJni.isInitialized(mHandle);
        }

        @Override
        public void setInitialized(boolean aInitialized) {
            // TODO Auto-generated method stub

        }

        @Override
        public String getName() {
            return AnalogOutWrapperJni.getName(mHandle);
        }

        @Override
        public void setName(String aName) {
            AnalogOutWrapperJni.setName(mHandle, aName);
        }

        @Override
        public boolean getWantsHidden() {
            return AnalogOutWrapperJni.getWantsHidden(mHandle);
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
        }

        @Override
        public double getVoltage() {
            return AnalogOutWrapperJni.getVoltage(mHandle);
        }
    }



    // @Override
    // public boolean isInitialized(int aPort)
    // {
    //     return AnalogOutWrapperJni.isInitialized(aPort);
    // }

    // @Override
    // public boolean createSimulator(int aPort, String aType)
    // {
    //     return AnalogOutWrapperJni.createSimulator(aPort, aType);
    // }

    // @Override
    // public void removeSimulator(int aPort)
    // {
    //     AnalogOutWrapperJni.removeSimluator(aPort);
    // }

    // @Override
    // public void setName(int aPort, String aName)
    // {
    //     AnalogOutWrapperJni.setName(aPort, aName);
    // }

    // @Override
    // public String getName(int aPort)
    // {
    //     return AnalogOutWrapperJni.getName(aPort);
    // }

    // @Override
    // public boolean getWantsHidden(int aPort)
    // {
    //     return AnalogOutWrapperJni.getWantsHidden(aPort);
    // }

    // @Override
    // public double getVoltage(int aPort)
    // {
    //     return AnalogOutWrapperJni.getVoltage(aPort);
    // }

    // @Override
    // public void setVoltage(int aPort, double aVoltage)
    // {
    //     // nothing to do
    // }
    
    private Map<Integer, AnalogOutWrapper> mWrappers;

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
    public IAnalogOutWrapper createSimulator(int aPort, String aType) {
        return mWrappers.put(aPort, new AnalogOutWrapper(aPort, aType));
    }

    @Override
    public IAnalogOutWrapper getWrapper(int aHandle) {
        return mWrappers.get(aHandle);
    }
}
