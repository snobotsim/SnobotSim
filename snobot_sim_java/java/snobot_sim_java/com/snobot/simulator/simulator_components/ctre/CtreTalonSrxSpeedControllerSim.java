package com.snobot.simulator.simulator_components.ctre;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.snobot.simulator.module_wrapper.PwmWrapper;

public class CtreTalonSrxSpeedControllerSim extends PwmWrapper
{
    private static final Logger sLOGGER = Logger.getLogger(CtreTalonSrxSpeedControllerSim.class);

    public enum ControlType
    {
        Raw, Position, Speed, MotionMagic, MotionProfile
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

    public static class MotionProfilePoint
    {
        public final int mIndex;
        public final double mPosition;
        public final double mVelocity;

        public MotionProfilePoint(int aIndex, double aPosition, double aVelocity)
        {
            mIndex = aIndex;
            mPosition = aPosition;
            mVelocity = aVelocity;
        }

        @Override
        public String toString()
        {
            return "MotionProfilePoint [mIndex=" + mIndex + ", mPosition=" + mPosition + ", mVelocity=" + mVelocity + "]";
        }

    }

    protected PIDFConstants mPidConstants;
    protected ControlType mControlType;
    protected double mControlGoal;
    protected double mSumError;
    protected double mLastError;

    protected List<MotionProfilePoint> mMotionProfilePoints;
    protected int mMotionProfileProcessedCounter;
    protected int mMotionProfileCurrentPointIndex;

    protected List<CtreTalonSrxSpeedControllerSim> mFollowers;

    public CtreTalonSrxSpeedControllerSim(int aCanHandle)
    {
        super(aCanHandle + 100, "CAN SC: " + aCanHandle);

        mPidConstants = new PIDFConstants();
        mControlType = ControlType.Raw;

        mMotionProfilePoints = new LinkedList<>();
        mMotionProfileProcessedCounter = 0;

        mFollowers = new ArrayList<>();
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

    public void setMotionMagicGoal(double aSpeed)
    {
        mControlType = ControlType.MotionMagic;
        mControlGoal = aSpeed;
    }

    public void setMotionProfilingCommand(int demand)
    {
        mControlType = ControlType.MotionProfile;
        mControlGoal = demand;
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
        case MotionMagic:
        {
            double output = calculateMotionMagicOutput(getPosition(), getVelocity(), mControlGoal);
            super.set(output);
            break;
        }
        case MotionProfile:
        {
            double output = calculateMotionProfileOutput(getPosition(), getVelocity(), (int) mControlGoal);
            super.set(output);
            break;
        }
        // Just use normal update
        case Raw:
            break;
        default:
            sLOGGER.log(Level.ERROR, "Unsupported control type : " + mControlType);
            break;
        }
        super.update(aWaitTime);
    }

    @Override
    public void set(double aVoltagePercentage)
    {
        super.set(aVoltagePercentage);
        for (CtreTalonSrxSpeedControllerSim follower : mFollowers)
        {
            follower.set(aVoltagePercentage);
        }
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
        
        sLOGGER.log(Level.DEBUG,
                "Updating CAN PID: Error: " + error + ", Output: " + output + 
                " (Cur: " + aCurrent + ", Goal: " + aGoal + ") " + 
                " (P: " + pComp + ", I: " + iComp + ", D: " + dComp + ", F: " + fComp+ ")");

        return output;

    }

    double tmpMaxAccel = 5;
    double tmpMaxVel = 5;

    public void setMotionMagicMaxAcceleration(int aAccel)
    {
        tmpMaxAccel = aAccel;
    }

    public void setMotionMagicMaxVelocity(int aVel)
    {
        tmpMaxVel = aVel;
    }

    private double calculateMotionMagicOutput(double aCurrentPosition, double aCurrentVelocity, double aControlGoal)
    {
        double error = aControlGoal - aCurrentPosition;
        double dErr = error - mLastError;

        double time_to_stop = aCurrentVelocity / tmpMaxAccel;
        double time_to_destination = error / aCurrentVelocity;
        

        double pComp = mPidConstants.mP * error;
        double iComp = mPidConstants.mI * mSumError;
        double dComp = mPidConstants.mD * dErr;
        double fComp = mPidConstants.mF * tmpMaxVel;

        double output = pComp + iComp + dComp + fComp;

        output = Math.min(output, 1.0);
        output = Math.max(output, -1.0);

        mLastError = error;

//        DecimalFormat df = new DecimalFormat("#.##");
//        System.out.print("Motion Magic... " + 
//                "Goal: " + aControlGoal + ", " + 
//                "CurPos: " + df.format(position) + ", " + 
//                "CurVel: " + df.format(velocity) + ", " + 
//                "TimeToStop: " + df.format(time_to_stop) + ", " + 
//                "TimeToDestination: " + df.format(time_to_destination));
//
//        if (time_to_destination > time_to_stop)
//        {
        // LOGGER.log(Level.DEBUG, " In constant velocity " + output);
//        }
//        else
//        {
        // LOGGER.log(Level.DEBUG, " In decel " + output);
//        }

        return output;
    }

    private double calculateMotionProfileOutput(double aCurrentPosition, double aCurrentVelocity, int aModeType)
    {
        if (aModeType != 1)
        {
            // System.out.println(mMotionProfilePoints);
            return 0;
        }

        if (mMotionProfileCurrentPointIndex >= mMotionProfilePoints.size())
        {
            return 0;
        }

        MotionProfilePoint currentPoint = mMotionProfilePoints.get(mMotionProfileCurrentPointIndex++);

        return calculateMotionProfileOutput(aCurrentPosition, aCurrentVelocity, currentPoint.mPosition, currentPoint.mVelocity);
    }

    private double calculateMotionProfileOutput(double aCurrentPosition, double aCurrentVelocity, double aGoalPosition, double aGoalVelocity)
    {
        double error = aGoalPosition - aCurrentPosition;

        double p_term = error * mPidConstants.mP;
        double d_term = 0;// mPidConstants.mD * ((error - last_error_) /
                          // segment.dt - segment.vel);
        double v_term = mPidConstants.mF * aGoalVelocity;
        double output = p_term + d_term + v_term;

//        DecimalFormat df = new DecimalFormat("#.##");
//        System.out.println(output + "  (" + 
//                "P: " + df.format(aCurrentPosition) + ", " + "V: " + df.format(aCurrentVelocity) + " - " + 
//                "GP: " + aGoalPosition + ", GV: " + aGoalVelocity + ") " + 
//                "P: " + p_term + ", V: " + v_term);

        return output;
    }

    public double getLastClosedLoopError()
    {
        return mLastError;
    }

    public void addMotionProfilePoint(MotionProfilePoint aPoint)
    {
        if (aPoint.mIndex == 0)
        {
            mMotionProfilePoints.clear();
            mMotionProfileProcessedCounter = 0;
            mMotionProfileCurrentPointIndex = 0;
        }
        else
        {
            mMotionProfilePoints.add(aPoint);
            mMotionProfileProcessedCounter++;
        }
    }

    public int getMotionProfileSize()
    {
        return mMotionProfilePoints.size();
    }

    public MotionProfilePoint getMotionProfilePoint()
    {
        if (mMotionProfileProcessedCounter <= mMotionProfilePoints.size() && mMotionProfileProcessedCounter != 0)
        {
            return mMotionProfilePoints.get(mMotionProfileProcessedCounter - 1);
        }
        return null;
    }

    public void addFollower(CtreTalonSrxSpeedControllerSim aWrapper)
    {
        mFollowers.add(aWrapper);
    }

    private static boolean sLOGGED_CANT_OVERRIDE_FWD_LIMIT_SWITCH = false;
    private static boolean sLOGGED_CANT_OVERRIDE_REV_LIMIT_SWITCH = false;

    public void setLimitSwitchOverride(boolean overrideFwdLimitSwitch, boolean overrideRevLimitSwitch)
    {
        if (overrideFwdLimitSwitch && !sLOGGED_CANT_OVERRIDE_FWD_LIMIT_SWITCH)
        {
            sLOGGED_CANT_OVERRIDE_FWD_LIMIT_SWITCH = true;
            sLOGGER.log(Level.WARN, "Cannot override forward limit switches");
        }

        if (overrideRevLimitSwitch && !sLOGGED_CANT_OVERRIDE_REV_LIMIT_SWITCH)
        {
            sLOGGED_CANT_OVERRIDE_REV_LIMIT_SWITCH = true;
            sLOGGER.log(Level.WARN, "Cannot override reverse limit switch");
        }
    }
}
