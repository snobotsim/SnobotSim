
package com.snobot.simulator.module_wrapper;

public class DigitalSourceWrapperJni {
    
    public static native String getName(int aPort);

    public static native boolean getWantsHidden(int aPort);
    
    public static native boolean getState(int aPort);
    
    public static native int[] getPortList();
}
