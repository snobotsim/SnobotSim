package com.snobot.simulator.jni;

import com.snobot.simulator.JniLibraryResourceLoader;

public class BaseSimulatorJni
{

    static
    {
        initilaize();
    }

    public static void initilaize()
    {
        JniLibraryResourceLoader.loadLibrary("snobotSimHal");
        JniLibraryResourceLoader.loadLibrary("HALAthena");
    }
}
