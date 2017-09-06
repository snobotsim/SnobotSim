package com.snobot.simulator.module_wrapper;

import com.snobot.simulator.motor_sim.IMotorSimulator;

public class SpeedControllerWrapper extends ASensorWrapper
{
    private IMotorSimulator mMotorSimulator;

    public SpeedControllerWrapper(int index)
    {
        super("Speed Controller " + index);

        mMotorSimulator = new IMotorSimulator.NullMotorSimulator();
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
    }

    public double getPosition()
    {
        return mMotorSimulator.getPosition();
    }

    public double getVelocity()
    {
        return mMotorSimulator.getVelocity();
    }

    public void reset()
    {
        mMotorSimulator.reset();
    }

    public void reset(double aPosition, double aVelocity, double aCurrent)
    {
        mMotorSimulator.reset(aPosition, aVelocity, aCurrent);
    }
}
