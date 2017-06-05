
package com.snobot.simulator.jni.module_wrapper;

public class DigitalSourceWrapperJni {
    
    public static native void setName(int aPort, String aName);
    
    public static native String getName(int aPort);

    public static native boolean getWantsHidden(int aPort);
    
    public static native boolean getState(int aPort);
    
    public static native int[] getPortList();
}
