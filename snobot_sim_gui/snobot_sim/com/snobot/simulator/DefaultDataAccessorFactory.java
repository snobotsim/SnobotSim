package com.snobot.simulator;

import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;
import com.snobot.simulator.wrapper_accessors.java.JavaDataAccessor;

public class DefaultDataAccessorFactory
{
    public static void initalize()
    {
        DataAccessorFactory.setAccessor(new JavaDataAccessor());
    }
}
