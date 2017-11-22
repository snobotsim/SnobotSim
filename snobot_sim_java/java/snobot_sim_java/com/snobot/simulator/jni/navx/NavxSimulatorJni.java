package com.snobot.simulator.jni.navx;

public class NavxSimulatorJni
{
    public enum DataType
    {
        X, Y, Z, Yaw, Pitch, Roll
    }

    public static native long createNavx(String aType, int aPort);

    public static native double getNavxData(String aType, long aNativePointer, int dataType);

    public static native void setNavxData(String aType, long aNativePointer, int dataType, double aValue);

    public static double getNavxData(String aType, long aNativePointer, DataType dataType)
    {
        return getNavxData(aType, aNativePointer, dataType.ordinal());
    }

    public static void setNavxData(String aType, long aNativePointer, DataType dataType, double aValue)
    {
        setNavxData(aType, aNativePointer, dataType.ordinal(), aValue);
    }
}
