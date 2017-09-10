package com.snobot.simulator;

import java.io.File;

import org.junit.After;
import org.junit.Test;

import com.snobot.simulator.gui.SimulatorFrame;
import com.snobot.simulator.wrapper_accessors.SimulatorDataAccessor.SnobotLogLevel;
import com.snobot.test.utilities.BaseSimulatorTest;

import edu.wpi.first.networktables.NetworkTableInstance;

public class TestSimulator extends BaseSimulatorTest
{
    public class MockSimulator extends Simulator
    {
        public MockSimulator() throws Exception
        {
            super(SnobotLogLevel.DEBUG, new File("test_files/plugins"), "test_output/");
        }

        @Override
        protected void setFrameVisible(SimulatorFrame frame)
        {

        }

    }

    @Test
    public void testStartSimulator() throws Exception
    {
        Simulator simulator = new MockSimulator();
        simulator.startSimulation();
    }

    @After
    public void cleanup()
    {
        NetworkTableInstance.getDefault().stopServer();
    }
}
