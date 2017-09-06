package com.snobot.simulator.jni;

import com.snobot.simulator.DcMotorModelConfig;

public class MotorConfigFactoryJni extends BaseSimulatorJni
{

    public static DcMotorModelConfig createMotor(String aName)
    {
        return createMotor(aName, 1, 1, 1);
    }

    public static native DcMotorModelConfig createMotor(String aName, int aNumMotors, double aGearReduction, double aTransmissionEfficiency);
}
