package com.snobot.simulator.config;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.snobot.simulator.DefaultDataAccessorFactory;
import com.snobot.test.utilities.BaseSimulatorTest;

import edu.wpi.first.networktables.NetworkTableInstance;


public class TestReadConfig extends BaseSimulatorTest
{
    public static final int sTEST_PARAMETER = 5;

    @Before
    public void setup() // NOPMD
    {
        DefaultDataAccessorFactory.initalize();
    }

    @Test
    public void testReadConfig()
    {
        String file = "test_files/ConfigTest/ReadConfig/testReadFile.yml";
        SimulatorConfigReader reader = new SimulatorConfigReader();
        Assert.assertTrue(reader.loadConfig(file));
        Assert.assertNotNull(reader.getConfig());
    }

    @Test
    public void testReadEmptyFile()
    {
        String file = "test_files/ConfigTest/ReadConfig/emptyFile.yml";
        SimulatorConfigReader reader = new SimulatorConfigReader();
        Assert.assertTrue(reader.loadConfig(file));
        Assert.assertNotNull(reader.getConfig());
    }

    @Test
    public void testReadNullFile()
    {
        String file = null;
        SimulatorConfigReader reader = new SimulatorConfigReader();
        Assert.assertTrue(reader.loadConfig(file));
        Assert.assertNull(reader.getConfig());
    }

    @Test
    public void testReadNonExistingFile()
    {
        String file = "does_not_exist.yml";
        SimulatorConfigReader reader = new SimulatorConfigReader();
        Assert.assertFalse(reader.loadConfig(file));
        Assert.assertNull(reader.getConfig());
    }

    @After
    public void cleanup()
    {
        NetworkTableInstance.getDefault().stopServer();
    }
}
