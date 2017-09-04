package com.snobot.simulator.config;

import org.junit.Assert;
import org.junit.Test;

import com.snobot.test.utilities.BaseSimulatorTest;
import com.snobot.test.utilities.MockRobot;

public class TestWriteConfig extends BaseSimulatorTest
{

    @Test
    public void testWriteConfig()
    {
        String dump_file = "test_output/testWriteFile.yml";

        new MockRobot();

        SimulatorConfigWriter writer = new SimulatorConfigWriter();
        Assert.assertTrue(writer.writeConfig(dump_file));
    }

    @Test
    public void testWriteConfigToNonExistingDirectory()
    {
        String dump_file = "directory_does_not_exist/testWriteFile.yml";

        SimulatorConfigWriter writer = new SimulatorConfigWriter();
        Assert.assertFalse(writer.writeConfig(dump_file));
    }
}
