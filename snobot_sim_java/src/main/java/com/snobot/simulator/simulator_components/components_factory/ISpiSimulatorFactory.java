package com.snobot.simulator.simulator_components.components_factory;

import java.util.Collection;
import java.util.Map;

import com.snobot.simulator.simulator_components.ISpiWrapper;

public interface ISpiSimulatorFactory
{
    public ISpiWrapper createSpiWrapper(int aPort);

    public void setDefaultWrapper(int aPort, String aType);

    public Collection<String> getAvailableTypes();

    public Map<Integer, String> getDefaults();
}
