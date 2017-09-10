package com.snobot.simulator.simulator_components.components_factory;

import java.util.Map;

import com.snobot.simulator.simulator_components.ISpiWrapper;
import com.snobot.simulator.simulator_components.gyro.SpiGyroWrapper;
import com.snobot.simulator.simulator_components.navx.SpiNavxSimulator;

public class DefaultSpiSimulatorFactory implements ISpiSimulatorFactory
{
    protected Map<Integer, Class<?>> mDefaults;

    public static final int SPI_GYRO_OFFSET = 100;

    @Override
    public ISpiWrapper createSpiWrapper(int aPort)
    {
        if (aPort == 0)
        {
            return new SpiGyroWrapper(aPort, aPort + SPI_GYRO_OFFSET);
        }
        else if (aPort == 1)
        {
            return new SpiNavxSimulator(aPort);
        }
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setDefaultWrapper(int aPort, SpiGyroWrapper aWrapper)
    {
        // TODO Auto-generated method stub

    }

}
