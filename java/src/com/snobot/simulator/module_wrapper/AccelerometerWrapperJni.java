
package com.snobot.simulator.module_wrapper;

public class AccelerometerWrapperJni {
    
    public static void register(int aPort)
    {
        register(aPort, "Accelerometer " + aPort);
    }
    
    public static native void register(int aPort, String aName);
    
    public static native void setName(int aPort, String aName);
    
    public static native String getName(int aPort);

    public static native boolean getWantsHidden(int aPort);
    
    public static native double getAcceleration(int aPort);
    
    public static native int[] getPortList();
}
