package com.snobot.simulator.motor_sim;

//https://github.com/Team254/Sim-FRC-2015/blob/master/src/com/team254/frc2015/sim/DCMotor.java
public class DcMotorModel
{
    protected final DcMotorModelConfig mConfig;

    // Current motor state
    protected double mPosition;
    protected double mVelocity;
    protected double mAcceleration;
    protected double mCurrent;


    public DcMotorModel(DcMotorModelConfig motorConfig)
    {
        mConfig = motorConfig;
    }

    /**
     * Reset the motor to a specified state.
     * 
     * @param position
     *            The new position
     * @param velocity
     *            The new velocity
     * @param current
     *            The new current
     */
    public void reset(double position, double velocity, double current)
    {
        mPosition = position;
        mVelocity = velocity;
        mCurrent = current;
        mAcceleration = 0;
    }

    /**
     * Simulate applying a given voltage and load for a specified period of
     * time.
     * 
     * @param load
     *            Load applied to the motor (kg*m^2)
     * @param timestep
     *            How long the input is applied (s)
     */
    public void step(double applied_voltage, double load, double external_torque, double timestep)
    {
        if (mConfig.mFactoryParams.mInverted)
        {
            applied_voltage *= -1;
        }

        /*
         * Using the 971-style first order system model. V = I * R + Kv * w
         * torque = Kt * I
         * 
         * V = torque / Kt * R + Kv * w torque = J * dw/dt + external_torque
         * 
         * dw/dt = (V - Kv * w) * Kt / (R * J) - external_torque / J
         */

        if (mConfig.mFactoryParams.mHasBrake && applied_voltage == 0)
        {
            mAcceleration = 0;
            mVelocity = 0;
            mCurrent = 0;
        }
        else
        {
            load += mConfig.mMotorParams.MOTOR_INERTIA;
            mAcceleration = (applied_voltage - mVelocity / mConfig.mMotorParams.mKV) * mConfig.mMotorParams.mKT
                    / (mConfig.mMotorParams.mResistance * load) + external_torque / load;
            mVelocity += mAcceleration * timestep;
            mPosition += mVelocity * timestep + .5 * mAcceleration * timestep * timestep;
            mCurrent = load * mAcceleration * Math.signum(applied_voltage) / mConfig.mMotorParams.mKT;
        }
    }

    public double getPosition()
    {
        return mPosition;
    }

    public double getVelocity()
    {
        return mVelocity;
    }

    public double getCurrent()
    {
        return mCurrent;
    }

    public double getAcceleration()
    {
        return mAcceleration;
    }

    public DcMotorModelConfig getMotorConfig()
    {
        return mConfig;
    }
}
