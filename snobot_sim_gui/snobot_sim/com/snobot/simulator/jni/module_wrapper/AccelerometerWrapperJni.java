
package com.snobot.simulator.jni.module_wrapper;

import com.snobot.simulator.jni.BaseSimulatorJni;

public class AccelerometerWrapperJni extends BaseSimulatorJni
{
    
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
