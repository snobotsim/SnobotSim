package com.snobot.simulator.simulator_components;

import java.nio.ByteBuffer;

public interface ISpiWrapper
{
    void handleRead(ByteBuffer buffer);

    void handleWrite(ByteBuffer buffer);

    void handleTransaction();

    void resetAccumulator();
}
