
package com.snobot.simulator.module_wrapper;

public class RelayWrapperJni {
    
    public static native String getName(int aPort);
    
    public static native int[] getPortList();
}
