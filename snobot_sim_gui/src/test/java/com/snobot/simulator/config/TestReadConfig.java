package com.snobot.simulator.config;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.snobot.simulator.DefaultDataAccessorFactory;
import com.snobot.test.utilities.BaseSimulatorTest;

import edu.wpi.first.networktables.NetworkTableInstance;


public class TestReadConfig extends BaseSimulatorTest
{
    public static final int sTEST_PARAMETER = 5;

    @BeforeEach
    public void setup() // NOPMD
    {
        DefaultDataAccessorFactory.initalize();
    }

    @Test
    public void testReadConfig()
    {
        String file = "test_files/ConfigTest/ReadConfig/testReadFile.yml";
        SimulatorConfigReader reader = new SimulatorConfigReader();
        Assertions.assertTrue(reader.loadConfig(file));
        Assertions.assertNotNull(reader.getConfig());
    }

    @Test
    public void testReadEmptyFile()
    {
        String file = "test_files/ConfigTest/ReadConfig/emptyFile.yml";
        SimulatorConfigReader reader = new SimulatorConfigReader();
        Assertions.assertTrue(reader.loadConfig(file));
        Assertions.assertNotNull(reader.getConfig());
    }

    @Test
    public void testReadNullFile()
    {
        String file = null;
        SimulatorConfigReader reader = new SimulatorConfigReader();
        Assertions.assertTrue(reader.loadConfig(file));
        Assertions.assertNull(reader.getConfig());
    }

    @Test
    public void testReadNonExistingFile()
    {
        String file = "does_not_exist.yml";
        SimulatorConfigReader reader = new SimulatorConfigReader();
        Assertions.assertFalse(reader.loadConfig(file));
        Assertions.assertNull(reader.getConfig());
    }

    @AfterEach
    public void cleanup()
    {
        NetworkTableInstance.getDefault().stopServer();
    }
}
