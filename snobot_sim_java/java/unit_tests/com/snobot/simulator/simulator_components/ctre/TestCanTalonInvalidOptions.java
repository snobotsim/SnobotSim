package com.snobot.simulator.simulator_components.ctre;

import java.nio.ByteBuffer;

import org.junit.Test;

import com.snobot.simulator.jni.SensorFeedbackJniTestHelper;
import com.snobot.test.utilities.BaseSimulatorTest;

public class TestCanTalonInvalidOptions extends BaseSimulatorTest
{

    @Test
    public void testInvalidCallback()
    {
        CanManager manager = new CanManager();
        manager.handleIncomingMessage("DoesntExist", 0);
        manager.handleIncomingMessage("ReceiveMessage", 0);
    }

    @Test
    public void testInvalidOptions()
    {
        ByteBuffer buffer;

        buffer = ByteBuffer.allocateDirect(8);

        // Invalid Message ID
        SensorFeedbackJniTestHelper.commandCan(0, buffer, 8);

        // Invalid set command
        buffer.put(5, (byte) 0x70);
        SensorFeedbackJniTestHelper.commandCan(0x02040000, buffer, 8);

        // Invalid set - command
        buffer.put(5, (byte) 0x20);
        buffer.put(6, (byte) 0xEE);
        SensorFeedbackJniTestHelper.commandCan(0x02040000, buffer, 8);

        // Invalid GetParam
        SensorFeedbackJniTestHelper.commandCan(0x02041800, buffer, 8);

        // Invalid SetParam
        buffer.put(5, (byte) 0x20);
        buffer.put(6, (byte) 0xEE);
        SensorFeedbackJniTestHelper.commandCan(0x02041880, buffer, 8);

    }

}
