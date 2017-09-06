package com.snobot.simulator.motor_sim;

public class GravityLoadDcMotorSim extends BaseDcMotorSimulator
{
    protected final static double sGRAVITY = 9.8;

    protected final double mLoad;

    public GravityLoadDcMotorSim(DcMotorModel aModel, double aLoad)
    {
        super(aModel);

        mLoad = aLoad;
    }


    @Override
    public void update(double cycleTime)
    {
        double extraAcceleration = -sGRAVITY;

        mMotorModel.step(mVoltagePercentage * 12, mLoad, extraAcceleration, cycleTime);
    }
}
