package com.snobot.simulator.jni;

import java.nio.ByteBuffer;

public class SensorFeedbackJni extends BaseSnobotJni
{

    public static native void setAnalogGyroAngle(int aHandle, double aAngle);

    public static native void setEncoderDistance(int aHandle, double aAngle);

    public static native void setDigitalInput(int aHandle, boolean aState);

    public static native void setAnalogVoltage(int aHandle, double aVoltage);

    public static native void setSpiAccumulatorValue(int aHandle, long aValue);

    public static native void setSpiValueForRead(int aHandle, ByteBuffer aReadArray, int aCount);

    public static native void getSpiLastWrite(int aHandle, ByteBuffer aOutputBuffer, int aCopyCount);

    public static native void setI2CValueForRead(int aHandle, ByteBuffer aReadArray, int aCount);

    public static native void getI2CLastWrite(int aHandle, ByteBuffer aOutputBuffer, int aCopyCount);

    public static native void setEnabled(boolean aEnabled);

    public static native void setAutonomous(boolean aAuton);

    public static native void waitForProgramToStart();

    public static native void delayForNextUpdateLoop(double aWaitPeriod);

    public static native double getMatchTime();

    public static native void setJoystickInformation(int aJoystickHandle, float[] aAxesArray, short[] aPovsArray, int aButtonCount, int aButtonMask);


}
