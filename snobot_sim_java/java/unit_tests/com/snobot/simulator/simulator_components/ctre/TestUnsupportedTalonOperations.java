package com.snobot.simulator.simulator_components.ctre;

import java.nio.ByteBuffer;

import org.junit.Assert;
import org.junit.Test;

import com.snobot.test.utilities.BaseSimulatorTest;

public class TestUnsupportedTalonOperations extends BaseSimulatorTest
{

    @Test
    public void testUnsupportedSend()
    {
        TalonSrxDeviceManager deviceManager = new TalonSrxDeviceManager();

        deviceManager.handleSend(0xFFFFFFFF, 6, ByteBuffer.allocate(8), 8);
    }

    @Test
    public void testUnsupportedReceive()
    {
        TalonSrxDeviceManager deviceManager = new TalonSrxDeviceManager();

        Assert.assertEquals(0, deviceManager.handleReceive(0xFFFFFFFF, 6, ByteBuffer.allocate(8)));
    }

    @Test
    public void testUnsupportedTxRequest1()
    {
        TalonSrxDeviceManager deviceManager = new TalonSrxDeviceManager();

        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(0xFFFFFFFFFFFFFFFFL);
        deviceManager.handleSend(0x0204000F, 6, buffer, 8);
    }

    @Test
    public void testUnsupportedDemandCommand()
    {
        TalonSrxDeviceManager deviceManager = new TalonSrxDeviceManager();

        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(0x000000000022EE00L);
        deviceManager.handleSend(0x0204000F, 6, buffer, 8);
    }

    @Test
    public void testUnsupportedSetParam()
    {
        TalonSrxDeviceManager deviceManager = new TalonSrxDeviceManager();

        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(0xFFFFFFFFFFFFFFFFL);
        deviceManager.handleSend(0x02041880, 6, buffer, 8);
    }

    @Test
    public void testUnsupportedParamRequest()
    {
        TalonSrxDeviceManager deviceManager = new TalonSrxDeviceManager();

        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(0xFFFFFFFFFFFFFFFFL);
        deviceManager.handleSend(0x02041800, 6, buffer, 8);
    }

}
