package com.snobot.test.utilities;

import java.io.File;

import org.junit.Before;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.jni.RegisterCallbacksJni;
import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;
import com.snobot.simulator.wrapper_accessors.java.JavaDataAccessor;

import edu.wpi.first.wpilibj.RobotBase;

public class BaseSimulatorTest
{
    private static boolean INITIALIZED = false;
    protected static final double DOUBLE_EPSILON = .00001;

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
            DataAccessorFactory.setAccessor(new JavaDataAccessor());

            File directory = new File("test_output");
            if (directory.exists())
            {
                delete(directory);
            }
            directory.mkdirs();
        }

        RegisterCallbacksJni.reset();
        SensorActuatorRegistry.get().reset();
        RobotBase.initializeHardwareConfiguration();
    }
}
