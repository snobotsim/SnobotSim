package com.snobot.simulator.module_wrapper;

public class DigitalSourceWrapper extends ASensorWrapper
{
    private final StateSetterHelper mSetterHelper;
    private boolean mState;

    public static interface StateSetterHelper
    {
        public void setState(boolean aState);
    }

    public DigitalSourceWrapper(int aIndex, StateSetterHelper aSetterHelper)
    {
        super("Digital Source" + aIndex);

        mSetterHelper = aSetterHelper;
        mState = true;
    }

    public boolean get()
    {
        return mState;
    }

    public void set(boolean aState)
    {
        boolean isUpdate = mState != aState;
        mState = aState;
        if (isUpdate)
        {
            mSetterHelper.setState(mState);
        }

    }
}
