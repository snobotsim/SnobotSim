
package com.snobot.simulator.wrapper_accessors;

import java.util.List;

public interface AccelerometerWrapperAccessor
{
    public void register(int aPort, String aName);
    
    public void setName(int aPort, String aName);
    
    public String getName(int aPort);

    public boolean getWantsHidden(int aPort);
    
    public double getAcceleration(int aPort);
    
    public List<Integer> getPortList();
}
