package com.snobot.simulator.simulator_components;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.module_wrapper.AnalogWrapper;
import com.snobot.simulator.module_wrapper.PwmWrapper;

public class PotentiometerSimulator implements ISimulatorUpdater
{
    private AnalogWrapper mAnalogWrapper;
    private PwmWrapper mSpeedController;

    private final boolean isSetup;
    private double mPositionThrow;
    private double mMinVoltage;
    private double mMaxVoltage;

    public PotentiometerSimulator(AnalogWrapper aWrapper, PwmWrapper aSpeedController)
    {
        mAnalogWrapper = aWrapper;
        mSpeedController = aSpeedController;

        isSetup = (mAnalogWrapper != null) && (mSpeedController != null);

        mPositionThrow = mMaxVoltage = mMinVoltage = 1;

        SensorActuatorRegistry.get().register(this);
    }

    public void setParameters(double aThrow, double aMinVoltage, double aMaxVoltage)
    {
        mPositionThrow = aThrow;
        mMaxVoltage = aMaxVoltage;
        mMinVoltage = aMinVoltage;
    }

    @Override
    public void update()
    {
        double voltage = mMinVoltage + (mMaxVoltage - mMinVoltage) / mPositionThrow * mSpeedController.getPosition();
        mAnalogWrapper.setVoltage(voltage);
    }

    public boolean isSetup()
    {
        return isSetup;
    }
}