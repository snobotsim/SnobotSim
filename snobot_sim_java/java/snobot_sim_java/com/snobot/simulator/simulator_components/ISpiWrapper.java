package com.snobot.simulator.simulator_components;

import java.nio.ByteBuffer;

public interface ISpiWrapper
{
    void shutdown();

    void handleRead(ByteBuffer buffer);

    void handleWrite(ByteBuffer buffer);
}
