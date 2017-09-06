
package com.snobot.simulator.wrapper_accessors.jni;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.snobot.simulator.jni.module_wrapper.RelayWrapperJni;
import com.snobot.simulator.wrapper_accessors.RelayWrapperAccessor;

public class JniRelayWrapperAccessor implements RelayWrapperAccessor
{
    public void setName(int aPort, String aName)
    {
        RelayWrapperJni.setName(aPort, aName);
    }
    
    public String getName(int aPort)
    {
        return RelayWrapperJni.getName(aPort);
    }

    public boolean getWantsHidden(int aPort)
    {
        return RelayWrapperJni.getWantsHidden(aPort);
    }

    public boolean getFowardValue(int aPort)
    {
        return RelayWrapperJni.getFowardValue(aPort);
    }
    
    public boolean getReverseValue(int aPort)
    {
        return RelayWrapperJni.getReverseValue(aPort);
    }
    
    public List<Integer> getPortList()
    {
        return IntStream.of(RelayWrapperJni.getPortList()).boxed().collect(Collectors.toList());
    }
}
