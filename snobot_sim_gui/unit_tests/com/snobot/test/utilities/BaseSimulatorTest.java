package com.snobot.test.utilities;

import java.io.File;

import org.junit.Before;

import com.snobot.simulator.jni.SnobotSimulatorJni;

import edu.wpi.first.wpilibj.RobotBase;

public class BaseSimulatorTest
{
    private static boolean INITIALIZED = false;

    private void delete(File path)
    {
        File[] l = path.listFiles();
        for (File f : l)
        {
            if (f.isDirectory())
            {
                delete(f);
            }
            else
            {
                f.delete();
            }
        }
        path.delete();
    }

    @Before
    public void setup()
    {
        if (!INITIALIZED)
        {
            SnobotSimulatorJni.reset();
            RobotBase.initializeHardwareConfiguration();

            SnobotSimulatorJni.reset();
            RobotBase.initializeHardwareConfiguration();

            File directory = new File("test_output");
            if (directory.exists())
            {
                delete(directory);
            }
            directory.mkdirs();
        }
    }
}
