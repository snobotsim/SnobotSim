
package com.snobot.simulator.jni;

public class SnobotSimulatorJni extends BaseSimulatorJni
{

	public static native void initializeSimulator();

    public static native void reset();

    public static native void shutdown();

    public static native String getVersion();

    public static native void initializeLogging(int aLogLevel);
}
