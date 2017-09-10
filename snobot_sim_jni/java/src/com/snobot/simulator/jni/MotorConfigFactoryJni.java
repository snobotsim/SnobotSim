package com.snobot.simulator.jni;

public class MotorConfigFactoryJni extends BaseSimulatorJni
{

    public static LocalDcMotorModelConfig createMotor(String aName)
    {
        return createMotor(aName, 1, 1, 1);
    }

    public static native LocalDcMotorModelConfig createMotor(String aName, int aNumMotors, double aGearReduction, double aTransmissionEfficiency);
}
