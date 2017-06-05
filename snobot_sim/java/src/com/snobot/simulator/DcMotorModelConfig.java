package com.snobot.simulator;

public class DcMotorModelConfig
{

    // Motor Parameters
    public final double NOMINAL_VOLTAGE;
    public final double FREE_SPEED_RPM;
    public final double FREE_CURRENT;
    public final double STALL_TORQUE;
    public final double STALL_CURRENT;

    // Motor constants
    public double mKT;
    public double mKV;
    public double mResistance;
    public double mMotorInertia;
    public boolean mInverted;

    // Indicates the motor has a brake, i.e. when givin 0 volts it will stay put
    public boolean mHasBrake;

    public DcMotorModelConfig(
            double aNominalVoltage, 
            double aFreeSpeedRpm, 
            double aFreeCurrent, 
            double aStallTorque, 
            double aStallCurrent,
            double aMotorInertia)
    {
        this(
                aNominalVoltage, 
                aFreeSpeedRpm, 
                aFreeCurrent, 
                aStallTorque, 
                aStallCurrent, 
                aMotorInertia, 
                false, 
                false);
    }

    public DcMotorModelConfig(
            double aNominalVoltage, 
            double aFreeSpeedRpm, 
            double aFreeCurrent, 
            double aStallTorque, 
            double aStallCurrent,
            double aMotorInertia,
            boolean aHasBrake,
            boolean aInverted)
    {
        this(
                aNominalVoltage, 
                aFreeSpeedRpm, 
                aFreeCurrent, 
                aStallTorque, 
                aStallCurrent, 
                aMotorInertia, 
                aHasBrake, 
                aInverted,
                aStallTorque / aStallCurrent, 
                (aFreeSpeedRpm / aNominalVoltage) * (Math.PI * 2.0) / 60.0,
                aNominalVoltage / aStallCurrent);
    }

    public DcMotorModelConfig(DcMotorModelConfig aCopyConfig)
    {
        this(
                aCopyConfig.NOMINAL_VOLTAGE,
                aCopyConfig.FREE_SPEED_RPM,
                aCopyConfig.FREE_CURRENT,
                aCopyConfig.STALL_TORQUE,
                aCopyConfig.STALL_CURRENT,
                aCopyConfig.mMotorInertia,

                aCopyConfig.mHasBrake,
                aCopyConfig.mInverted,

                aCopyConfig.mKT,
                aCopyConfig.mKV,
                aCopyConfig.mResistance);
    }

    public DcMotorModelConfig(
            double aNominalVoltage, 
            double aFreeSpeedRpm, 
            double aFreeCurrent, 
            double aStallTorque, 
            double aStallCurrent,
            double aMotorInertia, 
            
            boolean aHasBrake, 
            boolean aInverted, 
            
            double aKT,
            double aKV, 
            double aResistance)
    {
        NOMINAL_VOLTAGE = aNominalVoltage;
        FREE_SPEED_RPM = aFreeSpeedRpm;
        FREE_CURRENT = aFreeCurrent;
        STALL_TORQUE = aStallTorque;
        STALL_CURRENT = aStallCurrent;

        mHasBrake = aHasBrake;
        mInverted = aInverted;

        mKT = aKT;
        mKV = aKV;
        mResistance = aResistance;
        mMotorInertia = aMotorInertia;
    }

    public void setInverted(boolean aInverted)
    {
        mInverted = aInverted;
    }

    public void setHasBrake(boolean aHasBrake)
    {
        mHasBrake = aHasBrake;
    }

    @Override
    public String toString()
    {
        return "DcMotorModelConfig [NOMINAL_VOLTAGE=" + NOMINAL_VOLTAGE + ", FREE_SPEED_RPM=" + FREE_SPEED_RPM + ", FREE_CURRENT=" + FREE_CURRENT
                + ", STALL_TORQUE=" + STALL_TORQUE + ", STALL_CURRENT=" + STALL_CURRENT + ", mKT=" + mKT + ", mKV=" + mKV + ", mResistance="
                + mResistance + ", mMotorInertia=" + mMotorInertia + ", mInverted=" + mInverted + ", mHasBrake=" + mHasBrake + "]";
    }

}