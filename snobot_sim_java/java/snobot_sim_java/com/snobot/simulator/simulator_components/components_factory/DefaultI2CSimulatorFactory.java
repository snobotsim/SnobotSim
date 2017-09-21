package com.snobot.simulator.simulator_components.components_factory;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.snobot.simulator.simulator_components.II2CWrapper;
import com.snobot.simulator.simulator_components.accelerometer.ADXL345_I2CAcceleratometer;
import com.snobot.simulator.simulator_components.navx.I2CNavxSimulator;

public class DefaultI2CSimulatorFactory implements II2cSimulatorFactory
{
    private static final Logger sLOGGER = Logger.getLogger(DefaultI2CSimulatorFactory.class);
    protected Map<Integer, String> mDefaults;

    public DefaultI2CSimulatorFactory()
    {
        mDefaults = new HashMap<>();
    }

    @Override
    public II2CWrapper createI2CWrapper(int aPort)
    {
        II2CWrapper output = null;

        if (mDefaults.containsKey(aPort))
        {
            output = createWrapper(aPort, mDefaults.get(aPort));
        }

        if (output == null)
        {
            sLOGGER.log(Level.ERROR, "Could not create simulator for I2C on port " + aPort);
        }

        return output;
    }

    @Override
    public void setDefaultWrapper(int aPort, String aType)
    {
        sLOGGER.log(Level.DEBUG, "Setting I2C default for port " + aPort + " to " + aType);
        mDefaults.put(aPort, aType);
    }

    @Override
    public Collection<String> getAvailableTypes()
    {
        return Arrays.asList("NavX", "ADXL345");
    }

    private II2CWrapper createWrapper(int aPort, String aType)
    {
        if ("NavX".equals(aType))
        {
            return new I2CNavxSimulator(aPort);
        }
        if ("ADXL345".equals(aType))
        {
            return new ADXL345_I2CAcceleratometer(aPort);
        }

        return null;
    }

    @Override
    public Map<Integer, String> getDefaults()
    {
        return mDefaults;
    }

}
