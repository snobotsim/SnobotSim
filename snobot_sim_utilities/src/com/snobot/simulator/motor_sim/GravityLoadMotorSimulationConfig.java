package com.snobot.simulator.motor_sim;

public class GravityLoadMotorSimulationConfig implements IMotorSimulatorConfig
{
    public double mLoad;

    public GravityLoadMotorSimulationConfig(double aLoad)
    {
        mLoad = aLoad;
    }

}
