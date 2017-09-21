package com.snobot.simulator.simulator_components.components_factory;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.snobot.simulator.simulator_components.ISpiWrapper;
import com.snobot.simulator.simulator_components.accelerometer.ADXL345_SpiAccelerometer;
import com.snobot.simulator.simulator_components.accelerometer.ADXL362_SpiAccelerometer;
import com.snobot.simulator.simulator_components.gyro.ADXRS450_SpiGyroWrapper;
import com.snobot.simulator.simulator_components.navx.SpiNavxSimulator;

public class DefaultSpiSimulatorFactory implements ISpiSimulatorFactory
{
    private static final Logger sLOGGER = Logger.getLogger(DefaultSpiSimulatorFactory.class);

    protected Map<Integer, String> mDefaults;

    public DefaultSpiSimulatorFactory()
    {
        mDefaults = new HashMap<>();
    }

    @Override
    public ISpiWrapper createSpiWrapper(int aPort)
    {
        ISpiWrapper output = null;

        if (mDefaults.containsKey(aPort))
        {
            output = createSimulator(aPort, mDefaults.get(aPort));
        }

        if (output == null)
        {
            sLOGGER.log(Level.ERROR, "Could not create simulator for SPI on port " + aPort);
        }

        return output;
    }

    @Override
    public void setDefaultWrapper(int aPort, String aType)
    {
        sLOGGER.log(Level.DEBUG, "Setting default on SPI port " + aPort + " to '" + aType + "'");
        mDefaults.put(aPort, aType);
    }

    public Collection<String> getAvailableTypes()
    {
        return Arrays.asList("NavX", "ADXRS450", "ADXL345", "ADXL362");
    }

    protected ISpiWrapper createSimulator(int aPort, String aType)
    {
        if ("NavX".equals(aType))
        {
            return new SpiNavxSimulator(aPort);
        }
        else if ("ADXRS450".equals(aType))
        {
            return new ADXRS450_SpiGyroWrapper(aPort);
        }
        else if ("ADXL345".equals(aType))
        {
            return new ADXL345_SpiAccelerometer(aPort);
        }
        else if ("ADXL362".equals(aType))
        {
            return new ADXL362_SpiAccelerometer(aPort);
        }

        return null;
    }

    @Override
    public Map<Integer, String> getDefaults()
    {
        return mDefaults;
    }

}
