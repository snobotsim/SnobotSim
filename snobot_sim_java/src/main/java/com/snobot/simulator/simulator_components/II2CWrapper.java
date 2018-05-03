package com.snobot.simulator.simulator_components;

import java.nio.ByteBuffer;

public interface II2CWrapper
{

    void shutdown();

    void handleRead(ByteBuffer aBuffer);

    void handleWrite(ByteBuffer aBuffer);

}
