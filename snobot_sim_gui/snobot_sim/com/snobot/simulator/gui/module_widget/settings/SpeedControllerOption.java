package com.snobot.simulator.gui.module_widget.settings;

public class SpeedControllerOption
{
    int mHandle;
    String mName;

    public SpeedControllerOption(int aHandle, String aName)
    {
        mHandle = aHandle;
        mName = aName;
    }

    @Override
    public String toString()
    {
        return mName + "(" + mHandle + ")";
    }
}