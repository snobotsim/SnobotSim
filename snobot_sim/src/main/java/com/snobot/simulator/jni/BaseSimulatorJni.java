package com.snobot.simulator.jni;

import com.snobot.simulator.JniLibraryResourceLoader;

public abstract class BaseSimulatorJni // NOPMD
{
    static
    {
        initilaize();
    }

    public static void initilaize()
    {
        JniLibraryResourceLoader.loadLibrary("wpiutil");
        JniLibraryResourceLoader.loadLibrary("wpiHal");
        JniLibraryResourceLoader.loadLibrary("snobotSimCppBase");
        JniLibraryResourceLoader.loadLibrary("snobotSimCppjni");
        JniLibraryResourceLoader.loadLibrary("snobotSimCppGuiBase");
        JniLibraryResourceLoader.loadLibrary("snobotSimCppGuijni");
        SnobotSimulatorJni.initializeSimulator();
    }
}
