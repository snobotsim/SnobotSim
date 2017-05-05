
package com.snobot.simulator;

public class RobotStateSingletonJni {

    public static native void setDisabled(boolean aDisabled);

    public static native void setAutonomous(boolean aAuto);

    public static native void setTest(boolean aTest);
}
