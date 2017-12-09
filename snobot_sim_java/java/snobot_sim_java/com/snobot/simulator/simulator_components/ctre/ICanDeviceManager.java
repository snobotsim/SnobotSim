package com.snobot.simulator.simulator_components.ctre;

import java.nio.ByteBuffer;

public interface ICanDeviceManager
{

    void handleSend(int aCanMessageId, int aCanPort, ByteBuffer aData, int aDataSize);

    int handleReceive(int aCanMessageId, int aCanPort, ByteBuffer aData);

    int readStreamSession(ByteBuffer[] messages, int messagesToRead);

}
