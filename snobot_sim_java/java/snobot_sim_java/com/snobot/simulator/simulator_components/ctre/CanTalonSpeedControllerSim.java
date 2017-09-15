package com.snobot.simulator.simulator_components.ctre;

import com.snobot.simulator.module_wrapper.PwmWrapper;

public class CanTalonSpeedControllerSim extends PwmWrapper
{
    protected class PIDFConstants
    {
        public double mP;
        public double mI;
        public double mD;
        public double mF;

        public PIDFConstants()
        {
            mP = 0;
            mI = 0;
            mD = 0;
            mF = 0;
        }
    }

    protected PIDFConstants mPidConstants;

    public CanTalonSpeedControllerSim(int aCanHandle)
    {
        super(aCanHandle + 100, "CAN SC: " + aCanHandle);

        mPidConstants = new PIDFConstants();
    }

    public void setPGain(double aP)
    {
        mPidConstants.mP = aP;
    }

    public void setIGain(double aI)
    {
        mPidConstants.mI = aI;
    }

    public void setDGain(double aD)
    {
        mPidConstants.mD = aD;
    }

    public void setFGain(double aF)
    {
        mPidConstants.mF = aF;
    }

    public PIDFConstants getPidConstants()
    {
        return mPidConstants;
    }


}
