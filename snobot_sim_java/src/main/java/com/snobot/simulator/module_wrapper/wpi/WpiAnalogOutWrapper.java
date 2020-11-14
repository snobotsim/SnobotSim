package com.snobot.simulator.module_wrapper.wpi;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import com.snobot.simulator.module_wrapper.ASensorWrapper;
import com.snobot.simulator.module_wrapper.interfaces.IAnalogOutWrapper;

import edu.wpi.first.hal.HALValue;
import edu.wpi.first.wpilibj.simulation.AnalogOutputSim;
import edu.wpi.first.hal.simulation.NotifyCallback;

public class WpiAnalogOutWrapper extends ASensorWrapper implements IAnalogOutWrapper, NotifyCallback
{
    private final AnalogOutputSim mWpiSimulator;

    public WpiAnalogOutWrapper(int aPort)
    {
        super("Analog Out " + aPort);
        mWpiSimulator = new AnalogOutputSim(aPort);

        // mWpiSimulator.registerVoltageCallback(this, false);
    }

    @Override
    public void callback(String aCallbackType, HALValue aHalValue)
    {
        LogManager.getLogger(WpiAnalogOutWrapper.class).log(Level.WARN, "Callback " + aCallbackType + " not supported");
    }

    @Override
    public void setVoltage(double aVoltage)
    {
        mWpiSimulator.setVoltage(aVoltage);
    }

    @Override
    public double getVoltage()
    {
        return mWpiSimulator.getVoltage();
    }

}
