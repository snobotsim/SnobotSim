
package com.snobot.simulator.module_wrapper;

public class EncoderWrapperJni {
    
    public static native String getName(int aPort);
    
    public static native double getDistance(int aPort);
    
    public static native int[] getPortList();
}
