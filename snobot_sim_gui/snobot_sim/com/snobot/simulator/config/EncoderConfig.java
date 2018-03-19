package com.snobot.simulator.config;

public class EncoderConfig extends BasicModuleConfig
{
    private int mConnectedSpeedControllerHandle;

    public EncoderConfig()
    {
        this(-1, "Unset", -1);
    }

    public EncoderConfig(int aHandle, String aName, int aConnectedSpeedControllerHandle)
    {
        super(aHandle, aName);
        mConnectedSpeedControllerHandle = aConnectedSpeedControllerHandle;
    }

    public int getmConnectedSpeedControllerHandle()
    {
        return mConnectedSpeedControllerHandle;
    }

    public void setmConnectedSpeedControllerHandle(int aConnectedSpeedControllerHandle)
    {
        this.mConnectedSpeedControllerHandle = aConnectedSpeedControllerHandle;
    }

}
