package com.snobot.simulator.motor_sim.motors;

import com.snobot.simulator.DcMotorModelConfig;

public class PublishedMotorFactory
{
    private static final double OZIN_TO_NM = 0.00706155183333;

    public static DcMotorModelConfig makeRS775()
    {
        final double NOMINAL_VOLTAGE = 18;
        final double FREE_SPEED_RPM = 19500;
        final double FREE_CURRENT = 2.7;
        final double STALL_TORQUE = 166.65 * OZIN_TO_NM;
        final double STALL_CURRENT = 130;
        final double MOTOR_INERTIA = 1.20348237E-5;

        return new DcMotorModelConfig(NOMINAL_VOLTAGE, FREE_SPEED_RPM, FREE_CURRENT, STALL_TORQUE, STALL_CURRENT, MOTOR_INERTIA);
    }
}
