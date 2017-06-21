
package com.snobot.simulator.jni.module_wrapper;

import com.snobot.simulator.DcMotorModelConfig;
import com.snobot.simulator.jni.BaseSimulatorJni;

public class SpeedControllerWrapperJni extends BaseSimulatorJni
{
    public enum MotorSimType
    {
        None("None"), Simple("Simple"), StaticLoad("Static Load"), RotationalLoad("Rotational Load"), GravitationalLoad("Gravitational Load");

        private String mDisplayName;

        MotorSimType(String aDisplayName)
        {
            mDisplayName = aDisplayName;
        }

        public String toString()
        {
            return mDisplayName;
        }
    }
    
    public static native void setName(int aPort, String aName);
    
    public static native String getName(int aPort);

    public static native boolean getWantsHidden(int aPort);
    
    public static native double getVoltagePercentage(int aPort);
    
    public static native void updateAllSpeedControllers(double aUpdatePeriod);

    public static MotorSimType getMotorSimType(int aPort)
    {
        int rawType = getMotorSimTypeNative(aPort);
        return MotorSimType.values()[rawType];
    }

    public static native DcMotorModelConfig getMotorConfig(int aPort);

    private static native int getMotorSimTypeNative(int aPort);
    
    public static native int[] getPortList();
}
