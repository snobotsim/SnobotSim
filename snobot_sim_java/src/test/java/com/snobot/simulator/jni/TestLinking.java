package com.snobot.simulator.jni;

import org.junit.Assert;
import org.junit.Test;

import com.snobot.test.utilities.BaseSimulatorJavaTest;

import edu.wpi.first.wpilibj.hal.HAL;

public class TestLinking extends BaseSimulatorJavaTest
{
    @Test
    public void testHalLinking()
    {
        Assert.assertTrue(HAL.initialize(0, 0));
    }

    @Test
    public void testSnobotSimLinking()
    {
        RegisterCallbacksJni.reset();
    }
}
