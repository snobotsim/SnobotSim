package com.snobot.simulator.simulator_components;

import java.nio.ByteBuffer;

public interface ISpiWrapper
{
    void shutdown();

    void handleRead(ByteBuffer aBuffer);

    void handleWrite(ByteBuffer aBuffer);
}
