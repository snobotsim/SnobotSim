
package com.snobot.simulator.module_wrapper;

public class SpeedControllerWrapperJni {
    
    public static native String getName(int aPort);
    
    public static native double getVoltagePercentage(int aPort);
}
