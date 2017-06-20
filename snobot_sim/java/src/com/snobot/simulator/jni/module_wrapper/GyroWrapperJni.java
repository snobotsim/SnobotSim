
package com.snobot.simulator.jni.module_wrapper;

import com.snobot.simulator.jni.BaseSimulatorJni;

public class GyroWrapperJni extends BaseSimulatorJni
{
    
    public static void register(int aPort)
    {
        register(aPort, "Gyro " + aPort);
    }
    
    public static native void register(int aPort, String aName);
    
    public static native void setName(int aPort, String aName);
    
    public static native String getName(int aPort);

    public static native boolean getWantsHidden(int aPort);
    
    public static native double getAngle(int aPort);
    
    public static native void reset(int aPort);
    
    public static native int[] getPortList();
}
