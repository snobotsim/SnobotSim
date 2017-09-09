package com.snobot.simulator.ctre_override;

import org.junit.Before;

import com.snobot.simulator.JniLibraryResourceLoader;
import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;

import edu.wpi.first.wpilibj.RobotBase;

public class BaseSimulatorTest
{
    protected static final double DOUBLE_EPSILON = .00001;

    @Before
    public void setup()
    {
        JniLibraryResourceLoader.loadLibrary("wpiutil");
        JniLibraryResourceLoader.loadLibrary("wpiHal");
        RobotBase.initializeHardwareConfiguration();
    }

    protected void simulateForTime(double aSeconds, Runnable task)
    {
        simulateForTime(aSeconds, .02, task);
    }

    protected void simulateForTime(double aSeconds, double aUpdatePeriod, Runnable aTask)
    {
        double update_frequency = 1 / aUpdatePeriod;

        for (int i = 0; i < update_frequency * aSeconds; ++i)
        {
            aTask.run();
            DataAccessorFactory.getInstance().getSimulatorDataAccessor().updateSimulatorComponents(aUpdatePeriod);
        }
    }
}
