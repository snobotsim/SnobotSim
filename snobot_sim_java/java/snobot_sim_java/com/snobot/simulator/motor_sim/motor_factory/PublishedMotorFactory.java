package com.snobot.simulator.motor_sim.motor_factory;

import com.snobot.simulator.DcMotorModelConfig;

public class PublishedMotorFactory
{
    private static final double OZIN_TO_NM = 0.00706155183333;

    protected static final DcMotorModelConfig.MotorParams MOTOR_PARAMS_RS775 = new DcMotorModelConfig.MotorParams(18, 19500, 2.7, 166.65 * OZIN_TO_NM, 130, 1.20348237E-5);

    public static DcMotorModelConfig makeRS775()
    {
        DcMotorModelConfig.FactoryParams factoryParams = new DcMotorModelConfig.FactoryParams("RS775", 1, 1, 1);
        DcMotorModelConfig.MotorParams motorParams = MOTOR_PARAMS_RS775;

        return new DcMotorModelConfig(factoryParams, motorParams, false, false);
    }
}