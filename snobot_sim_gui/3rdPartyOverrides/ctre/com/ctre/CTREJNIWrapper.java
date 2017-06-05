package com.ctre;

public class CTREJNIWrapper
{
    public CTREJNIWrapper()
    {
    }

    public static native int getPortWithModule(byte paramByte1, byte paramByte2);

    public static native int getPort(byte paramByte);
}
