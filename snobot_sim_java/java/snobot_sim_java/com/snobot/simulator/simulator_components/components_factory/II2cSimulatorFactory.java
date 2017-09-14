package com.snobot.simulator.simulator_components.components_factory;

import java.util.Collection;
import java.util.Map;

import com.snobot.simulator.simulator_components.II2CWrapper;

public interface II2cSimulatorFactory
{
    public II2CWrapper createI2CWrapper(int aPort);

    public void setDefaultWrapper(int aPort, String aType);

    public Collection<String> getAvailableTypes();

    public Map<Integer, String> getDefaults();
}
