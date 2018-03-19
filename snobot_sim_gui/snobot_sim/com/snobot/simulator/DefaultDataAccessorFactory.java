package com.snobot.simulator;

import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;
import com.snobot.simulator.wrapper_accessors.java.JavaDataAccessor;

/**
 * Helper class that sets up the data accessor abstraction layer
 *
 * @author PJ
 *
 */
public class DefaultDataAccessorFactory
{
    private static final boolean sINITIALIZED = false;

    public static void initalize()
    {
        if (!sINITIALIZED)
        {
            DataAccessorFactory.setAccessor(new JavaDataAccessor());
        }
    }
}
