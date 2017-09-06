
package com.snobot.simulator.wrapper_accessors.jni;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.snobot.simulator.jni.module_wrapper.EncoderWrapperJni;
import com.snobot.simulator.wrapper_accessors.EncoderWrapperAccessor;

public class JniEncoderWrapperAccessor implements EncoderWrapperAccessor
{
	
    public int getHandle(int aPortA, int aPortB)
    {
        return -1;
    }
    
    public void setName(int aPort, String aName)
    {
        EncoderWrapperJni.setName(aPort, aName);
    }
    
    public String getName(int aPort)
    {
        return EncoderWrapperJni.getName(aPort);
    }

    public boolean getWantsHidden(int aPort)
    {
        return EncoderWrapperJni.getWantsHidden(aPort);
    }
    
    public void connectSpeedController(int aEncoderHandle, int aSpeedControllerHandle)
    {
        EncoderWrapperJni.connectSpeedController(aEncoderHandle, aSpeedControllerHandle);
    }

    public boolean isHookedUp(int aPort)
    {
        return EncoderWrapperJni.isHookedUp(aPort);
    }
    
    public int getHookedUpId(int aPort)
    {
        return EncoderWrapperJni.getHookedUpId(aPort);
    }

    public void setDistancePerTick(int aPort, double aDistancePerTick)
    {
        EncoderWrapperJni.setDistancePerTick(aPort, aDistancePerTick);
    }

    public double getDistancePerTick(int aPort)
    {
        return EncoderWrapperJni.getDistancePerTick(aPort);
    }

    public double getRaw(int aPort)
    {
        return EncoderWrapperJni.getRaw(aPort);
    }
    
    public double getDistance(int aPort)
    {
        return EncoderWrapperJni.getDistance(aPort);
    }
    
    public List<Integer> getPortList()
    {
        return IntStream.of(EncoderWrapperJni.getPortList()).boxed().collect(Collectors.toList());
    }
}
