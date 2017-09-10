
package com.snobot.simulator.wrapper_accessors;

import java.util.List;

public interface GyroWrapperAccessor
{
    
    public void register(int aPort, String aName);
    
    public void setName(int aPort, String aName);
    
    public String getName(int aPort);

    public boolean getWantsHidden(int aPort);
    
    public double getAngle(int aPort);

    public void setAngle(int aPort, double aAngle);
    
    public void reset(int aPort);
    
    public List<Integer> getPortList();
}
