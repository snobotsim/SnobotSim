package com.snobot.simulator.simulator_components;

import com.snobot.simulator.module_wrapper.AnalogWrapper;
import com.snobot.simulator.module_wrapper.SpeedControllerWrapper;

public class PotentiometerSimulator implements ISimulatorUpdater
{
    private AnalogWrapper mWrapper;
    private SpeedControllerWrapper mSpeedController;

    private double mPositionThrow;
    private double mMinVoltage;
    private double mMaxVoltage;

    public PotentiometerSimulator(AnalogWrapper aWrapper, SpeedControllerWrapper aSpeedController)
    {
        mWrapper = aWrapper;
        mSpeedController = aSpeedController;

        mPositionThrow = mMaxVoltage = mMinVoltage = 1;
    }

    public void setParameters(double aThrow, double aMinVoltage, double aMaxVoltage)
    {
        mPositionThrow = aThrow;
        mMaxVoltage = aMaxVoltage;
        mMinVoltage = aMinVoltage;
    }

    public void update()
    {
        double voltage = mMinVoltage + (mMaxVoltage - mMinVoltage) / mPositionThrow * mSpeedController.getPosition();
        mWrapper.setVoltage(voltage);
    }

}