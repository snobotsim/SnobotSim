
package com.snobot.simulator.wrapper_accessors;

import java.util.List;

public interface EncoderWrapperAccessor
{
	
    public int getHandle(int aPortA, int aPortB);
    
    public void setName(int aPort, String aName);
    
    public String getName(int aPort);

    public boolean getWantsHidden(int aPort);
    
    public boolean connectSpeedController(int aEncoderHandle, int aSpeedControllerHandle);

    public boolean isHookedUp(int aPort);
    
    public int getHookedUpId(int aPort);

    public void setDistancePerTick(int aPort, double aDistancePerTick);

    public double getDistancePerTick(int aPort);

    public double getRaw(int aPort);
    
    public double getDistance(int aPort);
    
    public List<Integer> getPortList();
}
