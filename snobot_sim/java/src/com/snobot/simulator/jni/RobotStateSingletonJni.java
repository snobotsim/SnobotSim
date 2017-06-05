
package com.snobot.simulator.jni;

public class RobotStateSingletonJni {

    public static native void setDisabled(boolean aDisabled);

    public static native void setAutonomous(boolean aAuto);

    public static native void setTest(boolean aTest);
    
    public static native double getMatchTime();
    
    public static native void waitForProgramToStart();
    
    public static native void waitForNextUpdateLoop();
    
    public static native double getCycleTime();
}
