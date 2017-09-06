package com.snobot.simulator.config;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.snobot.simulator.DefaultDataAccessorFactory;


public class TestReadConfig
{
    public static final int sTEST_PARAMETER = 5;

    @Before
    public void setup()
    {
        DefaultDataAccessorFactory.initalize();
    }

    @Test
    public void testReadConfig()
    {
        String file = "test_files/testReadFile.yml";
        SimulatorConfigReader reader = new SimulatorConfigReader();
        Assert.assertTrue(reader.loadConfig(file));
    }

    @Test
    public void testReadEmptyFile()
    {
        String file = "test_files/emptyFile.yml";
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
