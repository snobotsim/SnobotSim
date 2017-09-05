package com.snobot.simulator.jni.module_wrapper;

import org.junit.Assert;
import org.junit.Test;

import com.snobot.test.utilities.BaseSimulatorTest;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.AnalogOutput;

public class TestAnalogIOJni extends BaseSimulatorTest
{
    @Test
    public void testCreateAnalogIn()
    {
        Assert.assertEquals(0, AnalogSourceWrapperJni.getPortList().length);

        new AnalogInput(0);
        Assert.assertEquals("Analog Source0", AnalogSourceWrapperJni.getName(0));
        Assert.assertEquals(1, AnalogSourceWrapperJni.getPortList().length);

        new AnalogInput(3);
        Assert.assertEquals("Analog Source3", AnalogSourceWrapperJni.getName(3));
        Assert.assertEquals(2, AnalogSourceWrapperJni.getPortList().length);
    }

    @Test
    public void testCreateAnalogOut()
    {
        Assert.assertEquals(0, AnalogSourceWrapperJni.getPortList().length);

        new AnalogOutput(0);
        Assert.assertEquals(1, AnalogSourceWrapperJni.getPortList().length);

        new AnalogOutput(1);
        Assert.assertEquals(2, AnalogSourceWrapperJni.getPortList().length);
    }

    @Test(expected = RuntimeException.class)
    public void testReuseInPort()
    {
        Assert.assertEquals(0, AnalogSourceWrapperJni.getPortList().length);

        new AnalogInput(0);
        Assert.assertEquals(1, AnalogSourceWrapperJni.getPortList().length);

        new AnalogInput(0);
    }

    @Test(expected = RuntimeException.class)
    public void testReuseOutPort()
    {
        Assert.assertEquals(0, AnalogSourceWrapperJni.getPortList().length);

        new AnalogOutput(0);
        Assert.assertEquals(1, AnalogSourceWrapperJni.getPortList().length);

        new AnalogOutput(0);
    }

    @Test
    public void testAnalogIn()
    {
        Assert.assertEquals(0, AnalogSourceWrapperJni.getPortList().length);
        AnalogInput input = new AnalogInput(0);
        Assert.assertEquals(1, AnalogSourceWrapperJni.getPortList().length);

        Assert.assertEquals(0, input.getVoltage(), DOUBLE_EPSILON);
        Assert.assertEquals(0, AnalogSourceWrapperJni.getVoltage(0), DOUBLE_EPSILON);
    }
}
