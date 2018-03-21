package com.snobot.simulator;

import java.io.File;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.snobot.simulator.gui.SimulatorFrame;
import com.snobot.simulator.wrapper_accessors.SimulatorDataAccessor.SnobotLogLevel;
import com.snobot.test.utilities.BaseSimulatorTest;

public class TestSimulator extends BaseSimulatorTest
{
    private static final long TIME_TO_RUN_MS = 10;
    private static final String sTEST_PLUGIN_DIR = "test_files/plugins";

    @Rule
    public TestName mTestName = new TestName();

    @Before
    public void setup() // NOPMD
    {
        System.out.println("\n******************************\n" + mTestName.getMethodName() + "\n******************************\n"); // NOPMD
        super.setup();
    }

    public class MockSimulator extends Simulator
    {
        boolean mError = false;

        public MockSimulator(SnobotLogLevel aLogLevel, File aPluginDirectory, String aUserConfigDir) throws Exception
        {
            super(aLogLevel, aPluginDirectory, aUserConfigDir);
        }

        @Override
        protected void setFrameVisible(SimulatorFrame aFrame)
        {
            // Nothing to do
        }

        @Override
        protected void exitWithError()
        {
            mError = true;
            stop();
            System.out.println("Exiting with error"); // NOPMD
        }

        protected void runTestForTime(long aMilliseconds) throws Exception
        {
            startSimulation();
            Thread.sleep(aMilliseconds);
            stop();
        }

    }

    @Test
    public void testStartSimulator() throws Exception
    {
        MockSimulator simulator = new MockSimulator(SnobotLogLevel.DEBUG, new File(sTEST_PLUGIN_DIR), "test_output/test_start_simulator/");
        simulator.runTestForTime(TIME_TO_RUN_MS);
        Assert.assertFalse(simulator.mError);
    }

    @Test
    public void testValidUserConfig() throws Exception
    {
        MockSimulator simulator = new MockSimulator(SnobotLogLevel.DEBUG, new File(sTEST_PLUGIN_DIR),
                "test_files/SimulatorTest/TestValidUserConfig/");
        simulator.runTestForTime(TIME_TO_RUN_MS);
        Assert.assertFalse(simulator.mError);
    }

    @Test
    public void testInvalidSimulatorName() throws Exception
    {
        MockSimulator simulator = new MockSimulator(SnobotLogLevel.DEBUG, new File(sTEST_PLUGIN_DIR),
                "test_files/SimulatorTest/InvalidSimulatorName/");
        simulator.runTestForTime(TIME_TO_RUN_MS);
        Assert.assertTrue(simulator.mError);
    }

    @Ignore
    @Test
    public void testCustomSimulatorName() throws Exception
    {
        MockSimulator simulator = new MockSimulator(SnobotLogLevel.DEBUG, new File(sTEST_PLUGIN_DIR),
                "test_files/SimulatorTest/CustomSimulatorName/");
        simulator.runTestForTime(TIME_TO_RUN_MS);
        Assert.assertFalse(simulator.mError);
    }
}
