package com.snobot.simulator.module_wrapper;

import org.junit.Test;

import com.snobot.test.utilities.BaseSimulatorTest;

import edu.wpi.first.wpilibj.PowerDistributionPanel;

public class TestPdpCallback extends BaseSimulatorTest
{

    @Test
    public void testPdp()
    {
        new PowerDistributionPanel();
    }
}
