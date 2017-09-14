package com.snobot.simulator.config;

import com.snobot.simulator.motor_sim.IMotorSimulatorConfig;

public class PwmConfig extends BasicModuleConfig
{
    private IMotorSimulatorConfig mMotorSimConfig;

    public PwmConfig()
    {
        this(-1, "Unset", null);
    }

    public PwmConfig(int aHandle, String aName, IMotorSimulatorConfig aMotorSimConfig)
    {
        super(aHandle, aName);

        mMotorSimConfig = aMotorSimConfig;
    }

    public IMotorSimulatorConfig getmMotorSimConfig()
    {
        return mMotorSimConfig;
    }

    public void setmMotorSimConfig(IMotorSimulatorConfig mMotorSimConfig)
    {
        this.mMotorSimConfig = mMotorSimConfig;
    }

}
