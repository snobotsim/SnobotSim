package com.snobot.simulator.simulator_components.components_factory;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.snobot.simulator.simulator_components.ISpiWrapper;
import com.snobot.simulator.simulator_components.gyro.SpiGyroWrapper;
import com.snobot.simulator.simulator_components.navx.SpiNavxSimulator;

public class DefaultSpiSimulatorFactory implements ISpiSimulatorFactory
{
    protected Map<Integer, Class<? extends ISpiWrapper>> mDefaults;

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
            try
            {
                Class<? extends ISpiWrapper> clazz = mDefaults.get(aPort);
                Constructor<? extends ISpiWrapper> constr = clazz.getConstructor(int.class);
                output = constr.newInstance(aPort);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        if (output == null)
        {
            System.err.println("Could not create simulator for I2C on port " + aPort);
        }

        return output;
    }

    @Override
    public void setDefaultWrapper(int aPort, Class<? extends ISpiWrapper> aClass)
    {
        mDefaults.put(aPort, aClass);
    }

    public Collection<Class<? extends ISpiWrapper>> getAvailableClassTypes()
    {
        return Arrays.asList(SpiGyroWrapper.class, SpiNavxSimulator.class);
    }

}
