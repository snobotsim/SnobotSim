package com.snobot.simulator.simulator_components;

public interface ISpiWrapper
{
    void handleRead();

    void handleWrite();

    void handleTransaction();

    void resetAccumulator();
}
