
package com.snobot.simulator.wrapper_accessors.jni;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.snobot.simulator.jni.module_wrapper.GyroWrapperJni;
import com.snobot.simulator.wrapper_accessors.GyroWrapperAccessor;

public class JniGyroWrapperAccessor implements GyroWrapperAccessor
{
    
    public void register(int aPort, String aName)
    {
        GyroWrapperJni.register(aPort, aName);
    }
    
    public void setName(int aPort, String aName)
    {
        GyroWrapperJni.setName(aPort, aName);
    }
    
    public String getName(int aPort)
    {
        return GyroWrapperJni.getName(aPort);
    }

    public boolean getWantsHidden(int aPort)
    {
        return GyroWrapperJni.getWantsHidden(aPort);
    }
    
    public double getAngle(int aPort)
    {
        return GyroWrapperJni.getAngle(aPort);
    }
    
    public void reset(int aPort)
    {
        GyroWrapperJni.reset(aPort);
    }
    
    public List<Integer> getPortList()
    {
        return IntStream.of(GyroWrapperJni.getPortList()).boxed().collect(Collectors.toList());
    }
}
