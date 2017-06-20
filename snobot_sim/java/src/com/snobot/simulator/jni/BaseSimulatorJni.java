package com.snobot.simulator.jni;

import com.snobot.simulator.JniLibraryResourceLoader;

public class BaseSimulatorJni
{

    static
    {
        JniLibraryResourceLoader.loadLibrary("snobotSimHal");
    }
}
