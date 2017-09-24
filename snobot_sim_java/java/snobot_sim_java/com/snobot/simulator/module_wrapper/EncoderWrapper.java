package com.snobot.simulator.module_wrapper;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.simulator_components.IMotorFeedbackSensor;

public class EncoderWrapper extends ASensorWrapper implements IMotorFeedbackSensor
{
    public static interface DistanceSetterHelper
    {
        public void setDistance(double aDistance);
    }

    private final DistanceSetterHelper mSetterHelper;
    private double mEncodingFactor;
    private double mPosition;

    public EncoderWrapper(int aIndex, DistanceSetterHelper aSetterHelper)
    {
        this("Encoder " + aIndex, aSetterHelper);
    }

    public EncoderWrapper(String aName, DistanceSetterHelper aSetterHelper)
    {
        super(aName);

        mSetterHelper = aSetterHelper;
        mPosition = 0;
        mEncodingFactor = 4;
    }

    public int getRaw()
    {
        return (int) (getPosition() / (mEncodingFactor));
    }

    @Override
    public void setPosition(double aPosition)
    {
        boolean isUpdate = aPosition != mPosition;
        mPosition = aPosition;

        if (isUpdate)
        {
            mSetterHelper.setDistance(mPosition);
        }
    }

    @Override
    public double getPosition()
    {
        return mPosition;
    }

    public void reset()
    {
        for (PwmWrapper pwmWrapper : SensorActuatorRegistry.get().getSpeedControllers().values())
        {
            if (pwmWrapper.getFeedbackSensor().equals(this))
            {
                pwmWrapper.reset();
                setPosition(0);
            }
        }
    }
}