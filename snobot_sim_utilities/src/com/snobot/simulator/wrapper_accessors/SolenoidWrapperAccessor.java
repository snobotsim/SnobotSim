
package com.snobot.simulator.wrapper_accessors;

import java.util.List;

public interface SolenoidWrapperAccessor
{
    
    public void setName(int aPort, String aName);
    
    public String getName(int aPort);

    public boolean getWantsHidden(int aPort);
    
    public boolean get(int aPort);
    
    public List<Integer> getPortList();
}
