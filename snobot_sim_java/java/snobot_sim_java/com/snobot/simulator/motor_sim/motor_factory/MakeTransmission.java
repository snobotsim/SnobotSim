package com.snobot.simulator.motor_sim.motor_factory;

import com.snobot.simulator.motor_sim.DcMotorModel;

public class MakeTransmission
{
    public static DcMotorModel makeTransmission(DcMotorModel motor, int num_motors, double gear_reduction, double efficiency)
    {
        DcMotorModel output = new DcMotorModel(
                motor.NOMINAL_VOLTAGE,
                motor.FREE_SPEED_RPM / gear_reduction,
                motor.FREE_CURRENT  * num_motors,
                motor.STALL_TORQUE  * num_motors,
                motor.STALL_CURRENT * num_motors,
                motor.mMotorInertia * num_motors * gear_reduction * gear_reduction,
                motor.mHasBrake,
                motor.mInverted
                );


        output.mKT *= efficiency * num_motors * gear_reduction;

        return output;
    }
}
