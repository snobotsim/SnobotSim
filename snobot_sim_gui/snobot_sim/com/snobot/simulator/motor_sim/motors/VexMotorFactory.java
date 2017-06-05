package com.snobot.simulator.motor_sim.motors;

import com.snobot.simulator.DcMotorModelConfig;

public class VexMotorFactory
{
    private final static double NOMINAL_VOLTAGE = 12;

    public static DcMotorModelConfig makeCIMMotor()
    {
        final double FREE_SPEED_RPM = 5330;
        final double FREE_CURRENT = 2.7;
        final double STALL_TORQUE = 2.41;
        final double STALL_CURRENT = 131;

        return new DcMotorModelConfig(NOMINAL_VOLTAGE, FREE_SPEED_RPM, FREE_CURRENT, STALL_TORQUE, STALL_CURRENT, 0);
    }

    public static DcMotorModelConfig makeMiniCIMMotor()
    {
        final double FREE_SPEED_RPM = 5840;
        final double FREE_CURRENT = 3;
        final double STALL_TORQUE = 1.41;
        final double STALL_CURRENT = 89;

        return new DcMotorModelConfig(NOMINAL_VOLTAGE, FREE_SPEED_RPM, FREE_CURRENT, STALL_TORQUE, STALL_CURRENT, 0);
    }

    public static DcMotorModelConfig makeBagMotor()
    {
        final double FREE_SPEED_RPM = 13180;
        final double FREE_CURRENT = 1.8;
        final double STALL_TORQUE = .43;
        final double STALL_CURRENT = 53;

        return new DcMotorModelConfig(NOMINAL_VOLTAGE, FREE_SPEED_RPM, FREE_CURRENT, STALL_TORQUE, STALL_CURRENT, 0);
    }

    public static DcMotorModelConfig make775Pro()
    {
        final double FREE_SPEED_RPM = 18730;
        final double FREE_CURRENT = .7;
        final double STALL_TORQUE = .71;
        final double STALL_CURRENT = 134;

        return new DcMotorModelConfig(NOMINAL_VOLTAGE, FREE_SPEED_RPM, FREE_CURRENT, STALL_TORQUE, STALL_CURRENT, 0);
    }

    public static DcMotorModelConfig makeAndyMarkRs775_125()
    {
        final double FREE_SPEED_RPM = 5800;
        final double FREE_CURRENT = 1.6;
        final double STALL_TORQUE = .28;
        final double STALL_CURRENT = 18;

        return new DcMotorModelConfig(NOMINAL_VOLTAGE, FREE_SPEED_RPM, FREE_CURRENT, STALL_TORQUE, STALL_CURRENT, 0);
    }

    public static DcMotorModelConfig makeBaneBotsRS775()
    {
        final double FREE_SPEED_RPM = 13050;
        final double FREE_CURRENT = 2.7;
        final double STALL_TORQUE = .72;
        final double STALL_CURRENT = 97;

        return new DcMotorModelConfig(NOMINAL_VOLTAGE, FREE_SPEED_RPM, FREE_CURRENT, STALL_TORQUE, STALL_CURRENT, 0);
    }

    public static DcMotorModelConfig makeAndyMark9015()
    {
        final double FREE_SPEED_RPM = 14270;
        final double FREE_CURRENT = 3.7;
        final double STALL_TORQUE = .36;
        final double STALL_CURRENT = 71;

        return new DcMotorModelConfig(NOMINAL_VOLTAGE, FREE_SPEED_RPM, FREE_CURRENT, STALL_TORQUE, STALL_CURRENT, 0);
    }

    public static DcMotorModelConfig makeBaneBotsRs550()
    {
        final double FREE_SPEED_RPM = 19000;
        final double FREE_CURRENT = .4;
        final double STALL_TORQUE = .38;
        final double STALL_CURRENT = 84;

        return new DcMotorModelConfig(NOMINAL_VOLTAGE, FREE_SPEED_RPM, FREE_CURRENT, STALL_TORQUE, STALL_CURRENT, 0);
    }
}
