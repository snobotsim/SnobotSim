
package com.snobot.simulator.wrapper_accessors.jni;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.snobot.simulator.jni.module_wrapper.AccelerometerWrapperJni;
import com.snobot.simulator.wrapper_accessors.AccelerometerWrapperAccessor;

public class JniAccelerometerWrapperAccessor implements AccelerometerWrapperAccessor
{
    public void register(int aPort, String aName)
    {
        AccelerometerWrapperJni.register(aPort, aName);
    }
    
    public void setName(int aPort, String aName)
    {
        AccelerometerWrapperJni.setName(aPort, aName);
    }
    
    public String getName(int aPort)
    {
        return AccelerometerWrapperJni.getName(aPort);
    }

    public boolean getWantsHidden(int aPort)
    {
        return AccelerometerWrapperJni.getWantsHidden(aPort);
    }
    
    public double getAcceleration(int aPort)
    {
        return AccelerometerWrapperJni.getAcceleration(aPort);
    }
    
    public List<Integer> getPortList()
    {
        return IntStream.of(AccelerometerWrapperJni.getPortList()).boxed().collect(Collectors.toList());
    }
}
