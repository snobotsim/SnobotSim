package com.snobot.simulator;

import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;
import com.snobot.simulator.wrapper_accessors.java.JavaDataAccessor;

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
