package com.snobot.simulator.module_wrapper;

public class RelayWrapper extends ASensorWrapper
{

    private boolean mForwards;
    private boolean mReverse;

    public RelayWrapper(int aPort)
    {
        super("Relay " + aPort);
    }

    public void setRelayForwards(boolean b)
    {
        mForwards = b;
    }

    public void setRelayReverse(boolean b)
    {
        mReverse = b;
    }

    public boolean getRelayForwards()
    {
        return mForwards;
    }

    public boolean getRelayReverse()
    {
        return mReverse;
    }

}
