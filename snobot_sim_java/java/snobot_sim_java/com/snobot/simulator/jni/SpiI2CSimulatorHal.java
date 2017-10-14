package com.snobot.simulator.jni;

public class SpiI2CSimulatorHal
{

    public static native void setSpiI2cAccelerometerX(String aType, long aNativePointer, double aAccel);

    public static native double getSpiI2cAccelerometerX(String aType, long aNativePointer);

    public static native void setSpiI2cAccelerometerY(String aType, long aNativePointer, double aAccel);

    public static native double getSpiI2cAccelerometerY(String aType, long aNativePointer);

    public static native void setSpiI2cAccelerometerZ(String aType, long aNativePointer, double aAccel);

    public static native double getSpiI2cAccelerometerZ(String aType, long aNativePointer);

    public static native long createSpiI2cAccelerometer(String aType, int aPort);

    public static native long createSpiGyro(String aType, int aPort);

    public static native void setSpiGyroAngle(String aType, long aNativePointer, double aAngle);

    public static native double getSpiGyroAngle(String aType, long aNativePointer);
}
