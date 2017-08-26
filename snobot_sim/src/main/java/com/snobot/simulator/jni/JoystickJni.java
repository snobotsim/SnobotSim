
package com.snobot.simulator.jni;

public class JoystickJni extends BaseSimulatorJni
{

    public static native void setJoystickInformation(int aJoystickHandle, float[] aAxesArray, short[] aPovsArray, int aButtonCount, int aButtonMask);
}
