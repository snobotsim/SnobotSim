package com.snobot.simulator.jni;

import com.snobot.simulator.JniLibraryResourceLoader;

public class BaseSnobotJni
{
    static
    {
        JniLibraryResourceLoader.loadLibrary("wpiutil");
        JniLibraryResourceLoader.loadLibrary("wpiHal");
        JniLibraryResourceLoader.loadLibrary("snobotSimJavaJni");

        RegisterCallbacksJni.reset();
    }
}
