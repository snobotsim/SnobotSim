package com.snobot.simulator.simulator_components;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.module_wrapper.AnalogWrapper;
import com.snobot.simulator.module_wrapper.PwmWrapper;

public class PotentiometerSimulator implements ISimulatorUpdater
{
    private final AnalogWrapper mAnalogWrapper;
    private final PwmWrapper mSpeedController;

    private final boolean mIsSetup;
    private double mPositionThrow;
    private double mMinVoltage;
    private double mMaxVoltage;

    public PotentiometerSimulator(AnalogWrapper aWrapper, PwmWrapper aSpeedController)
    {
        mAnalogWrapper = aWrapper;
        mSpeedController = aSpeedController;

        mIsSetup = (mAnalogWrapper != null) && (mSpeedController != null);

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
        return mIsSetup;
    }

    @Override
    public Object getConfig()
    {
        return null;
    }
}
