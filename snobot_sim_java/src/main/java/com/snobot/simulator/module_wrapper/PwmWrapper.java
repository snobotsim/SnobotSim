package com.snobot.simulator.module_wrapper;

import com.snobot.simulator.motor_sim.IMotorSimulator;
import com.snobot.simulator.simulator_components.IMotorFeedbackSensor;

public class PwmWrapper extends ASensorWrapper
{
    private final int mHandle;
    private IMotorSimulator mMotorSimulator;
    private IMotorFeedbackSensor mFeedbackSensor;

    public PwmWrapper(int aIndex)
    {
        this(aIndex, "Speed Controller " + aIndex);
    }

    public PwmWrapper(int aIndex, String aName)
    {
        super(aName);

        mHandle = aIndex;
        mMotorSimulator = new IMotorSimulator.NullMotorSimulator();
        mFeedbackSensor = new IMotorFeedbackSensor.NullFeedbackSensor();
    }

    public void setMotorSimulator(IMotorSimulator aSimulator)
    {
        mMotorSimulator = aSimulator;
    }

    public IMotorSimulator getMotorSimulator()
    {
        return mMotorSimulator;
    }

    public double get()
    {
        return mMotorSimulator.getVoltagePercentage();
    }

    public void set(double aSpeed)
    {
        mMotorSimulator.setVoltagePercentage(aSpeed);
    }

    public void update(double aWaitTime)
    {
        mMotorSimulator.update(aWaitTime);
        mFeedbackSensor.setPosition(mMotorSimulator.getPosition());
        mFeedbackSensor.setVelocity(mMotorSimulator.getVelocity());
    }

    public double getPosition()
    {
        return mMotorSimulator.getPosition();
    }

    public double getVelocity()
    {
        return mMotorSimulator.getVelocity();
    }

    public double getCurrent()
    {
        return mMotorSimulator.getCurrent();
    }

    public double getAcceleration()
    {
        return mMotorSimulator.getAcceleration();
    }

    public void reset()
    {
        mMotorSimulator.reset();
    }

    public void reset(double aPosition, double aVelocity, double aCurrent)
    {
        mMotorSimulator.reset(aPosition, aVelocity, aCurrent);
    }

    public void setFeedbackSensor(IMotorFeedbackSensor aFeedbackSensor)
    {
        mFeedbackSensor = aFeedbackSensor;
    }

    public IMotorFeedbackSensor getFeedbackSensor()
    {
        return mFeedbackSensor;
    }

    public int getHandle()
    {
        return mHandle;
    }
}
