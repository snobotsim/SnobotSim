package com.snobot.simulator.motor_sim.motor_factory;

import com.snobot.simulator.motor_sim.DcMotorModelConfig;

public final class MakeTransmission
{
    private MakeTransmission()
    {

    }

    public static DcMotorModelConfig makeTransmission(DcMotorModelConfig aMotor, int aNumMotors, double aGearReduction, double aEfficiency)
    {
        DcMotorModelConfig.FactoryParams factoryParams = new DcMotorModelConfig.FactoryParams(aMotor.mFactoryParams.mMotorType, aNumMotors,
                aGearReduction, aEfficiency, aMotor.mFactoryParams.mInverted, aMotor.mFactoryParams.mHasBrake);
        DcMotorModelConfig.MotorParams modifiedMotorParams = new DcMotorModelConfig.MotorParams(
                aMotor.mMotorParams.NOMINAL_VOLTAGE,
                aMotor.mMotorParams.FREE_SPEED_RPM / aGearReduction,
                aMotor.mMotorParams.FREE_CURRENT  * aNumMotors,
                aMotor.mMotorParams.STALL_TORQUE  * aNumMotors,
                aMotor.mMotorParams.STALL_CURRENT * aNumMotors,
                aMotor.mMotorParams.MOTOR_INERTIA * aNumMotors * aGearReduction * aGearReduction,
                aEfficiency * aNumMotors * aGearReduction);

        return new DcMotorModelConfig(factoryParams, modifiedMotorParams);
    }
}
