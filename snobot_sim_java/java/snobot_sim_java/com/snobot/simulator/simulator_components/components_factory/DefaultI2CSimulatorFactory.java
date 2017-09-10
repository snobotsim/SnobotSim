package com.snobot.simulator.simulator_components.components_factory;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.snobot.simulator.simulator_components.II2CWrapper;

public class DefaultI2CSimulatorFactory implements II2cSimulatorFactory
{
    protected Map<Integer, Class<? extends II2CWrapper>> mDefaults;

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
            try
            {
                Class<? extends II2CWrapper> clazz = mDefaults.get(aPort);
                Constructor<? extends II2CWrapper> constr = clazz.getConstructor(int.class);
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
    public void setDefaultWrapper(int aPort, Class<? extends II2CWrapper> aClass)
    {
        mDefaults.put(aPort, aClass);
    }

    @Override
    public Collection<Class<? extends II2CWrapper>> getAvailableClassTypes()
    {
        return Arrays.asList();
    }

}
