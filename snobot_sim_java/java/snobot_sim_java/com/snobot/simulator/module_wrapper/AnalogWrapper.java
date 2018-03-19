package com.snobot.simulator.module_wrapper;

public class AnalogWrapper extends ASensorWrapper
{
    protected final VoltageSetterHelper mSetterHelper;
    protected double mVoltage;

    public static interface VoltageSetterHelper
    {
        public void setVoltage(double aVoltage);
    }

    public AnalogWrapper(int aIndex, VoltageSetterHelper aSetterHelper)
    {
        this("Analog " + aIndex, aSetterHelper);
    }

    public AnalogWrapper(String aName, VoltageSetterHelper aSetterHelper)
    {
        super(aName);

        mSetterHelper = aSetterHelper;
    }

    public void setVoltage(double aVoltage)
    {
        boolean isUpdate = mVoltage != aVoltage;
        mVoltage = aVoltage;
        if (isUpdate)
        {
            mSetterHelper.setVoltage(mVoltage);
        }
    }

    public double getVoltage()
    {
        return mVoltage;
    }
}
