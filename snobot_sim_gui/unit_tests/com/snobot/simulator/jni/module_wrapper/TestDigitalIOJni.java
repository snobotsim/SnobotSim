package com.snobot.simulator.jni.module_wrapper;

import org.junit.Assert;
import org.junit.Test;

import com.snobot.test.utilities.BaseSimulatorTest;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;

public class TestDigitalIOJni extends BaseSimulatorTest
{
    @Test
    public void testCreateDigitalIn()
    {
        Assert.assertEquals(0, DigitalSourceWrapperJni.getPortList().length);

        new DigitalInput(0);
        Assert.assertEquals("Digital Source0", DigitalSourceWrapperJni.getName(0));
        Assert.assertEquals(1, DigitalSourceWrapperJni.getPortList().length);

        new DigitalInput(3);
        Assert.assertEquals("Digital Source3", DigitalSourceWrapperJni.getName(3));
        Assert.assertEquals(2, DigitalSourceWrapperJni.getPortList().length);
    }

    @Test
    public void testCreateDigitalOut()
    {
        Assert.assertEquals(0, DigitalSourceWrapperJni.getPortList().length);

        new DigitalOutput(0);
        Assert.assertEquals(1, DigitalSourceWrapperJni.getPortList().length);

        new DigitalOutput(3);
        Assert.assertEquals(2, DigitalSourceWrapperJni.getPortList().length);
    }

    @Test
    public void testDigitalIn()
    {
        DigitalInput input = new DigitalInput(0);

        Assert.assertTrue(input.get());
        Assert.assertTrue(DigitalSourceWrapperJni.getState(0));

        DigitalSourceWrapperJni.setState(0, false);
        Assert.assertFalse(input.get());
        Assert.assertFalse(DigitalSourceWrapperJni.getState(0));
    }

}
