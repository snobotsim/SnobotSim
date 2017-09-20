package com.snobot.simulator.simulator_components.ctre;

import com.snobot.simulator.module_wrapper.PwmWrapper;

public class CanTalonSpeedControllerSim extends PwmWrapper
{
    public enum ControlType
    {
        Raw, Position, Speed
    }

    protected class PIDFConstants
    {
        public double mP;
        public double mI;
        public double mD;
        public double mF;
        public double mIZone;

        public PIDFConstants()
        {
            mP = 0;
            mI = 0;
            mD = 0;
            mF = 0;
            mIZone = 0;
        }
    }

    protected PIDFConstants mPidConstants;
    protected ControlType mControlType;
    protected double mControlGoal;
    protected double mSumError;
    protected double mLastError;

    public CanTalonSpeedControllerSim(int aCanHandle)
    {
        super(aCanHandle + 100, "CAN SC: " + aCanHandle);

        mPidConstants = new PIDFConstants();
        mControlType = ControlType.Raw;
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

    public void setIZone(double aIzone)
    {
        mPidConstants.mIZone = aIzone;
    }

    public PIDFConstants getPidConstants()
    {
        return mPidConstants;
    }

    public void setPositionGoal(double aPosition)
    {
        mControlType = ControlType.Position;
        mControlGoal = aPosition;
    }

    public void setSpeedGoal(double aSpeed)
    {
        mControlType = ControlType.Speed;
        mControlGoal = aSpeed;
    }

    @Override
    public void update(double aWaitTime)
    {
        switch (mControlType)
        {
        case Position:
        {
            double output = calculateFeedbackOutput(getPosition(), mControlGoal);
            super.set(output);
            break;
        }
        case Speed:
        {
            double output = calculateFeedbackOutput(getVelocity(), mControlGoal);
            super.set(output);
            break;
        }
        // Just use normal update
        case Raw:
        default:
            break;
        }
        super.update(aWaitTime);
    }

    protected double calculateFeedbackOutput(double aCurrent, double aGoal)
    {
        double error = aGoal - aCurrent;
        double dErr = error - mLastError;
        
        mSumError += error;
        if (error > mPidConstants.mIZone)
        {
            mSumError = 0;
        }

        double pComp = mPidConstants.mP * error;
        double iComp = mPidConstants.mI * mSumError;
        double dComp = mPidConstants.mD * dErr;
        double fComp = mPidConstants.mF * aGoal;
        
        double output = pComp + iComp + dComp + fComp;

        output = Math.min(output, 1.0);
        output = Math.max(output, -1.0);

        mLastError = error;
        
//        System.out.println(
//                "Updating CAN PID: Error: " + error + ", Output: " + output + 
//                " (Cur: " + aCurrent + ", Goal: " + aGoal + ") " + 
//                " (P: " + pComp + ", I: " + iComp + ", D: " + dComp + ", F: " + fComp+ ")");

        return output;

    }


}
