package com.snobot.simulator.jni;

public class SensorFeedbackJni extends BaseSnobotJni
{

    public static native void setAnalogGyroAngle(int aHandle, double aAngle);

    public static native void setEncoderDistance(int aHandle, double aAngle);

    public static native void setDigitalInput(int aHandle, boolean aState);

    public static native void setAnalogVoltage(int aHandle, double aVoltage);
}
