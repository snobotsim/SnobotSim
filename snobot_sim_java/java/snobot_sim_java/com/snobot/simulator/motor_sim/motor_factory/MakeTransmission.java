package com.snobot.simulator.motor_sim.motor_factory;

import com.snobot.simulator.DcMotorModelConfig;

public class MakeTransmission
{
    public static DcMotorModelConfig makeTransmission(DcMotorModelConfig motor, int num_motors, double gear_reduction, double efficiency)
    {
        DcMotorModelConfig.FactoryParams factoryParams = new DcMotorModelConfig.FactoryParams(motor.mFactoryParams.mMotorType, num_motors,
                gear_reduction, efficiency);
        DcMotorModelConfig.MotorParams modifiedMotorParams = new DcMotorModelConfig.MotorParams(
                motor.mMotorParams.NOMINAL_VOLTAGE,
                motor.mMotorParams.FREE_SPEED_RPM / gear_reduction,
                motor.mMotorParams.FREE_CURRENT  * num_motors,
                motor.mMotorParams.STALL_TORQUE  * num_motors,
                motor.mMotorParams.STALL_CURRENT * num_motors,
                motor.mMotorParams.MOTOR_INERTIA * num_motors * gear_reduction * gear_reduction,
                efficiency * num_motors * gear_reduction);

        DcMotorModelConfig output = new DcMotorModelConfig(factoryParams, modifiedMotorParams,
                motor.mHasBrake,
                motor.mInverted
                );

        return output;
    }
}
