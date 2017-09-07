package com.snobot.simulator.module_wrapper;

public class DigitalSourceWrapper extends ASensorWrapper
{
    public static interface StateSetterHelper
    {
        public void setState(boolean aState);
    }

    private final StateSetterHelper mSetterHelper;
    private boolean mState;

    public DigitalSourceWrapper(int index, StateSetterHelper aSetterHelper)
    {
        super("Digital Source" + index);

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
