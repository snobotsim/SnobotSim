package com.snobot.simulator.motor_sim;

public class StaticLoadDcMotorSim extends BaseDcMotorSimulator
{
    protected double mLoad;
    protected double mConversionFactor;

    public StaticLoadDcMotorSim(DcMotorModel aModel, double aLoad)
    {
        this(aModel, aLoad, 1);
    }

    public StaticLoadDcMotorSim(DcMotorModel aModel, double aLoad, double aConversionFactor)
    {
        super(aModel, aConversionFactor);

        mLoad = aLoad;
    }

    @Override
    public void update(double cycleTime)
    {
        mMotorModel.step(mVoltagePercentage * 12, mLoad, 0, cycleTime);
    }

}
