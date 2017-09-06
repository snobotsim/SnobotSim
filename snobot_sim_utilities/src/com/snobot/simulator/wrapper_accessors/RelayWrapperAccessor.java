
package com.snobot.simulator.wrapper_accessors;

import java.util.List;

public interface RelayWrapperAccessor
{
    
    public void setName(int aPort, String aName);
    
    public String getName(int aPort);

    public boolean getWantsHidden(int aPort);

    public boolean getFowardValue(int aPort);
    
    public boolean getReverseValue(int aPort);
    
    public List<Integer> getPortList();
}
