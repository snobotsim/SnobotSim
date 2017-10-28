package com.snobot.simulator.jni;

import com.snobot.simulator.JniLibraryResourceLoader;

public class BaseSnobotJni
{
    static
    {
        JniLibraryResourceLoader.loadLibrary("wpiutil");
        JniLibraryResourceLoader.loadLibrary("wpiHal");
        JniLibraryResourceLoader.loadLibrary("halsim_adx_gyro_accelerometer");
        JniLibraryResourceLoader.loadLibrary("navx_simulator");
        JniLibraryResourceLoader.loadLibrary("snobotSimJavaJni");

        RegisterCallbacksJni.reset();
    }
}
