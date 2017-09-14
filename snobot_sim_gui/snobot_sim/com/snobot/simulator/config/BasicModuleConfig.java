package com.snobot.simulator.config;

public class BasicModuleConfig
{
    public int mHandle;
    public String mName;

    public BasicModuleConfig()
    {
        this(-1, "Unset");
    }

    public BasicModuleConfig(int aHandle, String aName)
    {
        mHandle = aHandle;
        mName = aName;
    }

    public int getmHandle()
    {
        return mHandle;
    }

    public void setmHandle(int mHandle)
    {
        this.mHandle = mHandle;
    }

    public String getmName()
    {
        return mName;
    }

    public void setmName(String mName)
    {
        this.mName = mName;
    }
}
