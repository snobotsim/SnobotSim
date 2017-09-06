package com.snobot.simulator.module_wrapper;

public class EncoderWrapper extends ASensorWrapper
{
    private SpeedControllerWrapper mMotorWrapper;
    private double mEncodingFactor;
    private double mDistancePerTick;

    public EncoderWrapper(int aIndex)
    {
        this("Encoder " + aIndex);
    }

    public EncoderWrapper(int aIndexA, int aIndexB)
    {
        this("Encoder (" + aIndexA + ", " + aIndexB + ")");
    }

    public EncoderWrapper(String aName)
    {
        super(aName);

        // setEncodingFactor(CounterBase.EncodingType.k4X);
        mDistancePerTick = 1;
    }

    // private void setEncodingFactor(EncodingType aFactor)
    // {
    // switch (aFactor)
    // {
    // case k1X:
    // mEncodingFactor = 1.0;
    // break;
    // case k2X:
    // mEncodingFactor = 0.5;
    // break;
    // case k4X:
    // mEncodingFactor = 0.25;
    // break;
    // default:
    // // This is never reached, EncodingType enum limits values
    // mEncodingFactor = 0.0;
    // break;
    // }
    // }

    public void reset()
    {
        if (mMotorWrapper != null)
        {
            mMotorWrapper.reset();
        }
    }

    public int getRaw()
    {
        return (int) (getDistance() / (mEncodingFactor * mDistancePerTick));
    }

    public double getDistance()
    {
        if (mMotorWrapper == null)
        {
            return 0;
        }
        else
        {
            return mMotorWrapper.getPosition();
        }
    }

    public boolean isHookedUp()
    {
        return mMotorWrapper != null;
    }

    public void setSpeedController(SpeedControllerWrapper aMotorWrapper)
    {
        mMotorWrapper = aMotorWrapper;
    }

    public void setDistancePerTick(double aDistancePerTick)
    {
        mDistancePerTick = aDistancePerTick;
    }
}