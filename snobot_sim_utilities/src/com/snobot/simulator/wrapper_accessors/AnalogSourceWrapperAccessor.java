
package com.snobot.simulator.wrapper_accessors;

import java.util.List;

public interface AnalogSourceWrapperAccessor
{
    
    public void setName(int aPort, String aName);
    
    public String getName(int aPort);

    public boolean getWantsHidden(int aPort);
    
    public double getVoltage(int aPort);
    
    public List<Integer> getPortList();
}
