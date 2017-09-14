package com.snobot.simulator.wrapper_accessors;

import java.util.List;

public interface IBasicSensorActuatorWrapperAccessor
{
    void setName(int aPort, String aName);

    String getName(int aPort);

    boolean getWantsHidden(int aPort);

    public List<Integer> getPortList();
}
