package com.snobot.simulator.module_wrapper;

public class RelayWrapper extends ASensorWrapper
{

    private boolean mForwards;
    private boolean mReverse;

    public RelayWrapper(int aPort)
    {
        super("Relay " + aPort);
    }

    public void setRelayForwards(boolean aForwards)
    {
        mForwards = aForwards;
    }

    public void setRelayReverse(boolean aReverse)
    {
        mReverse = aReverse;
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
