package com.snobot.simulator.simulator_components.ctre;

import java.nio.ByteBuffer;

import org.junit.Test;

import com.snobot.test.utilities.BaseSimulatorTest;

public class TestUnsupportedCanManagerOperations extends BaseSimulatorTest
{

    @Test
    public void testUnsupportedSendMessage()
    {
        CanManager deviceManager = new CanManager();

        ByteBuffer buffer = ByteBuffer.allocate(8);
        deviceManager.handleSendMessage("", 0x12340000, 8, buffer, 8);
    }

    @Test
    public void testUnsupportedReceiveMessage()
    {
        CanManager deviceManager = new CanManager();

        ByteBuffer buffer = ByteBuffer.allocate(8);
        deviceManager.handleReceiveMessage("", 0x12340000, 8, buffer);
    }

    @Test
    public void testUnsupportedOpenStreamMessage()
    {
        CanManager deviceManager = new CanManager();

        deviceManager.handleOpenStream("", 0x12340000, 0, 0);
    }

    @Test
    public void testUnsupportedCloseStreamMessage()
    {
        CanManager deviceManager = new CanManager();

        deviceManager.handleCloseStream("", 0);
    }
}
