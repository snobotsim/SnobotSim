package com.snobot.simulator.module_wrapper;

import com.snobot.simulator.motor_sim.IMotorSimulator;
import com.snobot.simulator.simulator_components.IMotorFeedbackSensor;

public class PwmWrapper extends ASensorWrapper
{
    private IMotorSimulator mMotorSimulator;
    private IMotorFeedbackSensor mFeedbackSensor;

    public PwmWrapper(int index)
    {
        super("Speed Controller " + index);

        mMotorSimulator = new IMotorSimulator.NullMotorSimulator();
        mFeedbackSensor = new IMotorFeedbackSensor.NullFeedbackSensor();
    }

    public void setMotorSimulator(IMotorSimulator aSimulator)
    {
        mMotorSimulator = aSimulator;
    }

    public double get()
    {
        return mMotorSimulator.getVoltagePercentage();
    }

    public void set(double speed)
    {
        mMotorSimulator.setVoltagePercentage(speed);
    }

    public void update(double aWaitTime)
    {
        mMotorSimulator.update(aWaitTime);
        mFeedbackSensor.setPosition(mMotorSimulator.getPosition());
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

    public void reset()
    {
        mMotorSimulator.reset();
    }

    public void reset(double aPosition, double aVelocity, double aCurrent)
    {
        mMotorSimulator.reset(aPosition, aVelocity, aCurrent);
    }

    public boolean hasFeedbackSensor()
    {
        return mFeedbackSensor != null;
    }

    public void setFeedbackSensor(EncoderWrapper aFeedbackSensor)
    {
        mFeedbackSensor = aFeedbackSensor;
    }
}
