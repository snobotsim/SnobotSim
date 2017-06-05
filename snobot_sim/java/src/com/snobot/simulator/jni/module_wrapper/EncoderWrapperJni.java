
package com.snobot.simulator.jni.module_wrapper;

public class EncoderWrapperJni {
	
    public static native int getHandle(int aPortA, int aPortB);
    
    public static native void setName(int aPort, String aName);
    
    public static native String getName(int aPort);

    public static native boolean getWantsHidden(int aPort);
    
    public static native boolean isHookedUp(int aPort);
    
    public static native double getRaw(int aPort);
    
    public static native double getDistance(int aPort);
    
    public static native int[] getPortList();
}
