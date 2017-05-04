
package com.snobot.simulator.module_wrapper;

public class AnalogSourceWrapperJni {
    
    public static native String getName(int aPort);
    
    public static native double getVoltage(int aPort);
    
    public static native int[] getPortList();
}
