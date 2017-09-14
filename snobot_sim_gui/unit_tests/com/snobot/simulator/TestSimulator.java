package com.snobot.simulator;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import com.snobot.simulator.gui.SimulatorFrame;
import com.snobot.simulator.wrapper_accessors.SimulatorDataAccessor.SnobotLogLevel;
import com.snobot.test.utilities.BaseSimulatorTest;

public class TestSimulator extends BaseSimulatorTest
{
    public class MockSimulator extends Simulator
    {
        boolean error = false;
        public MockSimulator(SnobotLogLevel aLogLevel, File aPluginDirectory, String aUserConfigDir) throws Exception
        {
            super(aLogLevel, aPluginDirectory, aUserConfigDir);
        }

        @Override
        protected void setFrameVisible(SimulatorFrame frame)
        {

        }

        protected void stop()
        {
            System.out.println("Stopping mock simulator...");
            try
            {
                Thread.sleep(500);
            }
            catch (InterruptedException e)
            {
                //
            }
            super.stop();
        }

        protected void exitWithError()
        {
            error = true;
            stop();
            System.out.println("Exiting with error");
        }

    }

    @Test
    public void testStartSimulator() throws Exception
    {
        MockSimulator simulator = new MockSimulator(SnobotLogLevel.DEBUG, new File("test_files/plugins"), "test_output/test_start_simulator/");
        simulator.startSimulation();

        simulator.stop();
        Assert.assertFalse(simulator.error);
    }

    @Test
    public void testValidUserConfig() throws Exception
    {
        MockSimulator simulator = new MockSimulator(SnobotLogLevel.DEBUG, new File("test_files/plugins"),
                "test_files/SimulatorTest/TestValidUserConfig/");
        simulator.startSimulation();
        simulator.stop();
        Assert.assertFalse(simulator.error);
    }

    @Test
    public void testInvalidSimulatorName() throws Exception
    {
        MockSimulator simulator = new MockSimulator(SnobotLogLevel.DEBUG, new File("test_files/plugins"),
                "test_files/SimulatorTest/InvalidSimulatorName/");

        simulator.startSimulation();
        simulator.stop();
        Assert.assertTrue(simulator.error);
    }

    @Test
    public void testCustomSimulatorName() throws Exception
    {
        MockSimulator simulator = new MockSimulator(SnobotLogLevel.DEBUG, new File("test_files/plugins"),
                "test_files/SimulatorTest/CustomSimulatorName/");
        simulator.startSimulation();
        simulator.stop();
        Assert.assertFalse(simulator.error);
    }

    @Test
    public void testSimulatorUpdates() throws Exception
    {
        MockSimulator simulator = new MockSimulator(SnobotLogLevel.DEBUG, new File("test_files/plugins"),
                "test_files/SimulatorTest/CustomSimulatorName/");
        simulator.startSimulation();

        Thread.sleep(10000);
    }
}
