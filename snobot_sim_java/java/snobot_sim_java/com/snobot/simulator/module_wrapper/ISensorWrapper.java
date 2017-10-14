package com.snobot.simulator.module_wrapper;

public interface ISensorWrapper
{

    String getName();

    void setName(String aName);

    boolean getWantsHidden();

    void setWantsHidden(boolean aVisible);

}