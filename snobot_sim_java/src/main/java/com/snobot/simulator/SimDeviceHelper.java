package com.snobot.simulator;

import edu.wpi.first.hal.SimDouble;
import edu.wpi.first.hal.sim.SimDeviceSim;

public final class SimDeviceHelper
{
    private SimDeviceHelper()
    {

    }

    public static SimDouble getSimDouble(SimDeviceSim aDevice, String aName)
    {
        SimDouble output = aDevice.getDouble(aName);
        if (output == null)
        {
            throw new IllegalArgumentException("Sim device '" + aName + "' does not exist");
        }

        return output;
    }
}
