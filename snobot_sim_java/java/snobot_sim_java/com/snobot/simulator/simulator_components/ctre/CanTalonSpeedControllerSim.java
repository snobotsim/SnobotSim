package com.snobot.simulator.simulator_components.ctre;

import com.snobot.simulator.module_wrapper.PwmWrapper;

public class CanTalonSpeedControllerSim extends PwmWrapper
{
    public CanTalonSpeedControllerSim(int aCanHandle)
    {
        super(aCanHandle + 100, "CAN SC: " + aCanHandle);
    }

}
