package com.snobot.simulator.jni.navx;

public final class NavxSimulatorJni
{
    private NavxSimulatorJni()
    {

    }

    public enum DataType
    {
        X, Y, Z, Yaw, Pitch, Roll
    }

    public static native long createNavx(String aType, int aPort);

    public static native void deleteNavx(String aType, long aNativePointer);

    public static native double getNavxData(String aType, long aNativePointer, int aDataType);

    public static double getNavxData(String aType, long aNativePointer, DataType aDataType)
    {
        return getNavxData(aType, aNativePointer, aDataType.ordinal());
    }

    public static native void setNavxData(String aType, long aNativePointer, int aDataType, double aValue);

    public static void setNavxData(String aType, long aNativePointer, DataType aDataType, double aValue)
    {
        setNavxData(aType, aNativePointer, aDataType.ordinal(), aValue);
    }

}
