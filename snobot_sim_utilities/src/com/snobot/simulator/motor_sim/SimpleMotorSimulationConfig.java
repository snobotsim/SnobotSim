package com.snobot.simulator.motor_sim;

public class SimpleMotorSimulationConfig implements IMotorSimulatorConfig
{
    public final double mMaxSpeed;

    private SimpleMotorSimulationConfig()
    {
        this(0);
    }

    public SimpleMotorSimulationConfig(double aMaxSpeed)
    {
        mMaxSpeed = aMaxSpeed;
    }
}
