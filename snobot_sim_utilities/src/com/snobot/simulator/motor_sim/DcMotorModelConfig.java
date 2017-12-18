package com.snobot.simulator.motor_sim;

public class DcMotorModelConfig
{
    public static class FactoryParams
    {
        public String mMotorType;
        public int mNumMotors;
        public double mGearReduction;
        public double mGearboxEfficiency;

        // Indicates you want a positive voltage to move the system backwards
        // instead of forwards
        public boolean mInverted;

        // Indicates the motor has a brake, i.e. when givin 0 volts it will stay
        // put
        public boolean mHasBrake;

        @SuppressWarnings("unused")
        private FactoryParams()
        {
            this("", 0, 0, 0, false, false);
        }

        public FactoryParams(String aMotorType, int aNumMotors, double aGearReduction, double aGearboxEfficiency, boolean aInverted,
                boolean aHasBrake)
        {
            mMotorType = aMotorType;
            mNumMotors = aNumMotors;
            mGearReduction = aGearReduction;
            mGearboxEfficiency = aGearboxEfficiency;

            mHasBrake = aHasBrake;
            mInverted = aInverted;
        }

        public String getmMotorType()
        {
            return mMotorType;
        }

        public int getmNumMotors()
        {
            return mNumMotors;
        }

        public double getmGearReduction()
        {
            return mGearReduction;
        }

        public double getmGearboxEfficiency()
        {
            return mGearboxEfficiency;
        }

        public void setmMotorType(String mMotorType)
        {
            this.mMotorType = mMotorType;
        }

        public void setmNumMotors(int mNumMotors)
        {
            this.mNumMotors = mNumMotors;
        }

        public void setmGearReduction(double mGearReduction)
        {
            this.mGearReduction = mGearReduction;
        }

        public void setmGearboxEfficiency(double mGearboxEfficiency)
        {
            this.mGearboxEfficiency = mGearboxEfficiency;
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
            return "FactoryParams [mMotorType=" + mMotorType + ", mNumMotors=" + mNumMotors + ", mGearReduction=" + mGearReduction
                    + ", mGearboxEfficiency=" + mGearboxEfficiency + "]";
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            long temp;
            temp = Double.doubleToLongBits(mGearReduction);
            result = prime * result + (int) (temp ^ (temp >>> 32));
            temp = Double.doubleToLongBits(mGearboxEfficiency);
            result = prime * result + (int) (temp ^ (temp >>> 32));
            result = prime * result + ((mMotorType == null) ? 0 : mMotorType.hashCode());
            result = prime * result + mNumMotors;
            return result;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            FactoryParams other = (FactoryParams) obj;
            if (Double.doubleToLongBits(mGearReduction) != Double.doubleToLongBits(other.mGearReduction))
                return false;
            if (Double.doubleToLongBits(mGearboxEfficiency) != Double.doubleToLongBits(other.mGearboxEfficiency))
                return false;
            if (mMotorType == null)
            {
                if (other.mMotorType != null)
                    return false;
            }
            else if (!mMotorType.equals(other.mMotorType))
                return false;
            if (mNumMotors != other.mNumMotors)
                return false;
            return true;
        }

    }

    public static class MotorParams
    {
        public final double NOMINAL_VOLTAGE;
        public final double FREE_SPEED_RPM;
        public final double FREE_CURRENT;
        public final double STALL_TORQUE;
        public final double STALL_CURRENT;
        public final double MOTOR_INERTIA;
        public final double mKT;
        public final double mKV;
        public final double mResistance;

        public MotorParams(double aNominalVoltage, double aFreeSpeedRpm, double aFreeCurrent, double aStallTorque, double aStallCurrent)
        {
            this(aNominalVoltage, aFreeSpeedRpm, aFreeCurrent, aStallTorque, aStallCurrent, 0);
        }

        public MotorParams(double aNominalVoltage, double aFreeSpeedRpm, double aFreeCurrent, double aStallTorque, double aStallCurrent,
                double aMotorInertia)
        {
            this(aNominalVoltage, aFreeSpeedRpm, aFreeCurrent, aStallTorque, aStallCurrent, aMotorInertia, 1);
        }

        public MotorParams(
                double aNominalVoltage, 
                double aFreeSpeedRpm, 
                double aFreeCurrent, 
                double aStallTorque, 
                double aStallCurrent,
                double aMotorInertia,
                double aKtScaler)
        {
            this(aNominalVoltage, aFreeSpeedRpm, aFreeCurrent, aStallTorque, aStallCurrent, 
                    aMotorInertia, 
                    (aStallTorque / aStallCurrent) * aKtScaler, 
                    (aFreeSpeedRpm / aNominalVoltage) * (Math.PI * 2.0) / 60.0,
                    aNominalVoltage / aStallCurrent);
        }

        public MotorParams(
                double aNominalVoltage, 
                double aFreeSpeedRpm, 
                double aFreeCurrent, 
                double aStallTorque, 
                double aStallCurrent,
                double aMotorInertia,
                double aKt,
                double aKv,
                double aResistance)
        {
            NOMINAL_VOLTAGE = aNominalVoltage;
            FREE_SPEED_RPM = aFreeSpeedRpm;
            FREE_CURRENT = aFreeCurrent;
            STALL_TORQUE = aStallTorque;
            STALL_CURRENT = aStallCurrent;
            MOTOR_INERTIA = aMotorInertia;

            mKT = aKt;
            mKV = aKv;
            mResistance = aResistance;
        }

        public double calculateKt()
        {
            return STALL_TORQUE / STALL_CURRENT;
        }

        public double calculateKv()
        {
            return (FREE_SPEED_RPM / NOMINAL_VOLTAGE) * (Math.PI * 2.0) / 60.0;
        }

        public double calculateResistance()
        {
            return NOMINAL_VOLTAGE / STALL_CURRENT;
        }

        @Override
        public String toString()
        {
            return "MotorParams [NOMINAL_VOLTAGE=" + NOMINAL_VOLTAGE + ", FREE_SPEED_RPM=" + FREE_SPEED_RPM + ", FREE_CURRENT=" + FREE_CURRENT
                    + ", STALL_TORQUE=" + STALL_TORQUE + ", STALL_CURRENT=" + STALL_CURRENT + ", MOTOR_INERTIA=" + MOTOR_INERTIA + ", mKT=" + mKT
                    + ", mKV=" + mKV + ", mResistance=" + mResistance + "]";
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            long temp;
            temp = Double.doubleToLongBits(FREE_CURRENT);
            result = prime * result + (int) (temp ^ (temp >>> 32));
            temp = Double.doubleToLongBits(FREE_SPEED_RPM);
            result = prime * result + (int) (temp ^ (temp >>> 32));
            temp = Double.doubleToLongBits(MOTOR_INERTIA);
            result = prime * result + (int) (temp ^ (temp >>> 32));
            temp = Double.doubleToLongBits(NOMINAL_VOLTAGE);
            result = prime * result + (int) (temp ^ (temp >>> 32));
            temp = Double.doubleToLongBits(STALL_CURRENT);
            result = prime * result + (int) (temp ^ (temp >>> 32));
            temp = Double.doubleToLongBits(STALL_TORQUE);
            result = prime * result + (int) (temp ^ (temp >>> 32));
            temp = Double.doubleToLongBits(mKT);
            result = prime * result + (int) (temp ^ (temp >>> 32));
            temp = Double.doubleToLongBits(mKV);
            result = prime * result + (int) (temp ^ (temp >>> 32));
            temp = Double.doubleToLongBits(mResistance);
            result = prime * result + (int) (temp ^ (temp >>> 32));
            return result;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            MotorParams other = (MotorParams) obj;
            if (Double.doubleToLongBits(FREE_CURRENT) != Double.doubleToLongBits(other.FREE_CURRENT))
                return false;
            if (Double.doubleToLongBits(FREE_SPEED_RPM) != Double.doubleToLongBits(other.FREE_SPEED_RPM))
                return false;
            if (Double.doubleToLongBits(MOTOR_INERTIA) != Double.doubleToLongBits(other.MOTOR_INERTIA))
                return false;
            if (Double.doubleToLongBits(NOMINAL_VOLTAGE) != Double.doubleToLongBits(other.NOMINAL_VOLTAGE))
                return false;
            if (Double.doubleToLongBits(STALL_CURRENT) != Double.doubleToLongBits(other.STALL_CURRENT))
                return false;
            if (Double.doubleToLongBits(STALL_TORQUE) != Double.doubleToLongBits(other.STALL_TORQUE))
                return false;
            if (Double.doubleToLongBits(mKT) != Double.doubleToLongBits(other.mKT))
                return false;
            if (Double.doubleToLongBits(mKV) != Double.doubleToLongBits(other.mKV))
                return false;
            if (Double.doubleToLongBits(mResistance) != Double.doubleToLongBits(other.mResistance))
                return false;
            return true;
        }

    }

    public final FactoryParams mFactoryParams;
    public final MotorParams mMotorParams;

    public DcMotorModelConfig(
            FactoryParams aFactoryParams,
            MotorParams aMotorParams)
    {
        mFactoryParams = aFactoryParams;
        mMotorParams = aMotorParams;
    }

    @Override
    public String toString()
    {
        return "DcMotorModelConfig [mFactoryParams=" + mFactoryParams + ", mMotorParams=" + mMotorParams + "]";
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mFactoryParams == null) ? 0 : mFactoryParams.hashCode());
        result = prime * result + ((mMotorParams == null) ? 0 : mMotorParams.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DcMotorModelConfig other = (DcMotorModelConfig) obj;
        if (mFactoryParams == null)
        {
            if (other.mFactoryParams != null)
                return false;
        }
        else if (!mFactoryParams.equals(other.mFactoryParams))
            return false;
        if (mMotorParams == null)
        {
            if (other.mMotorParams != null)
                return false;
        }
        else if (!mMotorParams.equals(other.mMotorParams))
            return false;
        return true;
    }


}