package com.snobot.simulator.simulator_components.gyro;

import com.snobot.simulator.module_wrapper.ASensorWrapper;

public class GyroWrapper extends ASensorWrapper
{
    public static interface AngleSetterHelper
    {
        public void updateAngle(double aAngle);
    }

    public static final GyroWrapper.AngleSetterHelper NULL_ANGLE_SETTER = new AngleSetterHelper()
    {

        @Override
        public void updateAngle(double aAngle)
        {

        }
    };

    protected final AngleSetterHelper mAngleSetterHelper;
    protected double mAngle;

    public GyroWrapper(String name, AngleSetterHelper aSetterHelper)
    {
        super(name);

        mAngleSetterHelper = aSetterHelper;
    }

    public void setAngle(double aAngle)
    {
        boolean isUpdate = mAngle != aAngle;
        mAngle = aAngle;
        if (isUpdate)
        {
            mAngleSetterHelper.updateAngle(aAngle);
        }
    }

    public double getAngle()
    {
        return mAngle;
    }
}
