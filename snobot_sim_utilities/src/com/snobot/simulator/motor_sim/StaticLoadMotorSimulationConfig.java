package com.snobot.simulator.motor_sim;

public class StaticLoadMotorSimulationConfig
{
    public final double mLoad;
    public final double mConversionFactor;

    public StaticLoadMotorSimulationConfig(double aLoad)
    {
        this(aLoad, 1);
    }

    public StaticLoadMotorSimulationConfig(double aLoad, double aConversionFactor)
    {
        this.mLoad = aLoad;
        this.mConversionFactor = aConversionFactor;
    }
}
