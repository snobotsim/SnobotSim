package com.ctre.phoenix.MotorControl;

import com.snobot.simulator.JniLibraryResourceLoader;

public class CTREJNIWrapper
{
    static
    {
        // JniLibraryResourceLoader.loadLibrary("ntcore");
        // JniLibraryResourceLoader.loadLibrary("wpiutil");
        // JniLibraryResourceLoader.loadLibrary("wpilibc");
        JniLibraryResourceLoader.loadLibrary("CTRLibDriver");
    }

    public static native int getPortWithModule(byte paramByte1, byte paramByte2);

    public static native int getPort(byte paramByte);
}
