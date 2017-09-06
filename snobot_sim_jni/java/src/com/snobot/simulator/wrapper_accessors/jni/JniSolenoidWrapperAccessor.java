
package com.snobot.simulator.wrapper_accessors.jni;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.snobot.simulator.jni.module_wrapper.SolenoidWrapperJni;
import com.snobot.simulator.wrapper_accessors.SolenoidWrapperAccessor;

public class JniSolenoidWrapperAccessor implements SolenoidWrapperAccessor
{
    public void setName(int aPort, String aName)
    {
        SolenoidWrapperJni.setName(aPort, aName);
    }
    
    public String getName(int aPort)
    {
        return SolenoidWrapperJni.getName(aPort);
    }

    public boolean getWantsHidden(int aPort)
    {
        return SolenoidWrapperJni.getWantsHidden(aPort);
    }
    
    public boolean get(int aPort)
    {
        return SolenoidWrapperJni.get(aPort);
    }
    
    public List<Integer> getPortList()
    {
        return IntStream.of(SolenoidWrapperJni.getPortList()).boxed().collect(Collectors.toList());
    }
}
