package com.snobot.simulator.jni;

import org.junit.Assert;
import org.junit.Test;

import com.snobot.simulator.jni.module_wrapper.SpeedControllerWrapperJni;
import com.snobot.test.utilities.BaseSimulatorJniTest;

import edu.wpi.first.wpilibj.hal.HAL;

public class TestLinking extends BaseSimulatorJniTest
{
    @Test
    public void testHalLinking()
    {
        Assert.assertTrue(HAL.initialize(0, 0));
    }

    @Test
    public void testSnobotSimLinking()
    {
        Assert.assertEquals(0, SpeedControllerWrapperJni.getPortList().length);
    }
}
