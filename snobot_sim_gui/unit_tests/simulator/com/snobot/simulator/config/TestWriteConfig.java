package simulator.com.snobot.simulator.config;

import java.io.File;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.snobot.simulator.config.SimulatorConfigWriter;
import com.snobot.simulator.jni.SnobotSimulatorJni;

import edu.wpi.first.wpilibj.RobotBase;

public class TestWriteConfig
{
    @Before
    public void setup()
    {
        SnobotSimulatorJni.initializeLogging(0);
        SnobotSimulatorJni.reset();
        RobotBase.initializeHardwareConfiguration();

        File directory = new File("test_output");
        if (!directory.exists())
        {
            directory.mkdirs();
        }
    }

    @Test
    public void testWriteConfig()
    {
        String dump_file = "test_output/testWriteFile.yml";

        // new MockRobot();

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
