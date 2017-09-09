package com.snobot.simulator.motor_sim;

public class GravityLoadDcMotorSim extends BaseDcMotorSimulator
{
    protected final static double sGRAVITY = 9.8;

    protected final GravityLoadMotorSimulationConfig mConfig;

    public GravityLoadDcMotorSim(DcMotorModel aModel, GravityLoadMotorSimulationConfig aConfig)
    {
        super(aModel);

        mConfig = aConfig;
    }


    @Override
    public void update(double cycleTime)
    {
        double extraAcceleration = -sGRAVITY;

        mMotorModel.step(mVoltagePercentage * 12, mConfig.mLoad, extraAcceleration, cycleTime);
    }

    public GravityLoadMotorSimulationConfig getConfig()
    {
        return mConfig;
    }
}
