
package com.snobot.simulator.wrapper_accessors;

import java.util.List;

public interface DigitalSourceWrapperAccessor
{
    
    public void setName(int aPort, String aName);
    
    public String getName(int aPort);

    public boolean getWantsHidden(int aPort);
    
    public boolean getState(int aPort);
    
    public void setState(int aPort, boolean aValue);
    
    public List<Integer> getPortList();
}
