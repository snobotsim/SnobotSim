package com.snobot.simulator.jni.adx_family;

public class SpiI2CSimulatorJni
{
    public enum DataType
    {
        X, Y, Z
    }

    public static native void setSpiI2cAccelerometerData(String aType, long aNativePointer, int aDataType, double aAccel);

    public static native double getSpiI2cAccelerometerData(String aType, long aNativePointer, int aDataType);

    public static void setSpiI2cAccelerometerData(String aType, long aNativePointer, DataType aDataType, double aAccel)
    {
        setSpiI2cAccelerometerData(aType, aNativePointer, aDataType.ordinal(), aAccel);
    }

    public static double getSpiI2cAccelerometerData(String aType, long aNativePointer, DataType aDataType)
    {
        return getSpiI2cAccelerometerData(aType, aNativePointer, aDataType.ordinal());
    }

    public static native long createSpiI2cAccelerometer(String aType, int aPort);

    public static native void deleteAccelerometer(String mType, long aNativePointer);

    public static native long createSpiGyro(String aType, int aPort);

    public static native void deleteSpiGyro(String aType, long aNativePointer);

    public static native void setSpiGyroAngle(String aType, long aNativePointer, double aAngle);

    public static native double getSpiGyroAngle(String aType, long aNativePointer);
}
