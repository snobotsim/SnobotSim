package com.snobot.simulator.config;

import org.junit.Assert;
import org.junit.Test;

import com.snobot.simulator.config.SimulatorConfigReader;


public class TestReadConfig
{

    @Test
    public void testReadConfig()
    {
        String file = "test_files/testReadFile.yml";
        SimulatorConfigReader reader = new SimulatorConfigReader();
        Assert.assertTrue(reader.loadConfig(file));
    }

    @Test
    public void testReadNullFile()
    {
        String file = null;
        SimulatorConfigReader reader = new SimulatorConfigReader();
        Assert.assertTrue(reader.loadConfig(file));
    }

    @Test
    public void testReadNonExistingFile()
    {
        String file = "does_not_exist.yml";
        SimulatorConfigReader reader = new SimulatorConfigReader();
        Assert.assertFalse(reader.loadConfig(file));
    }
}