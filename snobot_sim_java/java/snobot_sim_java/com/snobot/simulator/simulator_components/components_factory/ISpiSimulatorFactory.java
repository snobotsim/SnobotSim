package com.snobot.simulator.simulator_components.components_factory;

import com.snobot.simulator.simulator_components.ISpiWrapper;
import com.snobot.simulator.simulator_components.gyro.SpiGyroWrapper;

public interface ISpiSimulatorFactory
{
    public ISpiWrapper createSpiWrapper(int aPort);

    public void setDefaultWrapper(int aPort, SpiGyroWrapper aWrapper);
}
