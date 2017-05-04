
package com.snobot.simulator.module_wrapper;

public class SolenoidWrapperJni {
    
    public static native String getName(int aPort);
    
    public static native boolean get(int aPort);
    
    public static native int[] getPortList();
}
