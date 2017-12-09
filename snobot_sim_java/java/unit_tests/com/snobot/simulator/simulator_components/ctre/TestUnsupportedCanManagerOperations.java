package com.snobot.simulator.simulator_components.ctre;

import java.nio.ByteBuffer;

import org.junit.Test;

import com.snobot.test.utilities.BaseSimulatorTest;

public class TestUnsupportedCanManagerOperations extends BaseSimulatorTest
{

    @Test
    public void testUnsupportedSendMessage()
    {
        CtreManager deviceManager = new CtreManager();

        ByteBuffer buffer = ByteBuffer.allocate(8);
        deviceManager.handleSendMessage("", 0x12340000, 8, buffer, 8);
    }

    @Test
    public void testUnsupportedReceiveMessage()
    {
        CtreManager deviceManager = new CtreManager();

        ByteBuffer buffer = ByteBuffer.allocate(8);
        deviceManager.handleReceiveMessage("", 0x12340000, 8, buffer);
    }

    @Test
    public void testUnsupportedOpenStreamMessage()
    {
        CtreManager deviceManager = new CtreManager();

        deviceManager.handleOpenStream("", 0x12340000, 0, 0);
    }

    @Test
    public void testUnsupportedCloseStreamMessage()
    {
        CtreManager deviceManager = new CtreManager();

        deviceManager.handleCloseStream("", 0);
    }
}
