
package com.snobot.simulator.jni.module_wrapper;

import com.snobot.simulator.jni.BaseSimulatorJni;

public class SpeedControllerWrapperJni extends BaseSimulatorJni
{
    
    public static native void setName(int aPort, String aName);
    
    public static native String getName(int aPort);

    public static native boolean getWantsHidden(int aPort);
    
    public static native double getVoltagePercentage(int aPort);
    
    public static native void updateAllSpeedControllers(double aUpdatePeriod);
    
    public static native int[] getPortList();
}
