
package com.snobot.simulator.jni;

public class JoystickJni {

    public static native void setJoystickInformation(int aJoystickHandle, float[] aAxesArray, short[] aPovsArray, int aButtonCount, int aButtonMask);
}
