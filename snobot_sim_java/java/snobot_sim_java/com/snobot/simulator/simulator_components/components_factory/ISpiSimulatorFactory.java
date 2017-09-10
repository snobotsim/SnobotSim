package com.snobot.simulator.simulator_components.components_factory;

import java.util.Collection;

import com.snobot.simulator.simulator_components.ISpiWrapper;

public interface ISpiSimulatorFactory
{
    public ISpiWrapper createSpiWrapper(int aPort);

    public void setDefaultWrapper(int aPort, Class<? extends ISpiWrapper> aClass);

    public Collection<Class<? extends ISpiWrapper>> getAvailableClassTypes();
}
