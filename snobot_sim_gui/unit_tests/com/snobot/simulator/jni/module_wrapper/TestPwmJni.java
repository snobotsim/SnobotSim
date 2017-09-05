package com.snobot.simulator.jni.module_wrapper;

import org.junit.Assert;
import org.junit.Test;

import com.snobot.test.utilities.BaseSimulatorTest;

import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Talon;

public class TestPwmJni extends BaseSimulatorTest
{
    @Test
    public void testCreatePwm()
    {
        Assert.assertEquals(0, SpeedControllerWrapperJni.getPortList().length);

        new Jaguar(0);
        Assert.assertEquals(1, SpeedControllerWrapperJni.getPortList().length);
        Assert.assertEquals("SpeedController 0", SpeedControllerWrapperJni.getName(0));

        new Talon(3);
        Assert.assertEquals(2, SpeedControllerWrapperJni.getPortList().length);
        Assert.assertEquals("SpeedController 3", SpeedControllerWrapperJni.getName(3));
    }

    @Test(expected = RuntimeException.class)
    public void testReusePort()
    {
        Assert.assertEquals(0, SpeedControllerWrapperJni.getPortList().length);

        new Talon(0);
        Assert.assertEquals(1, SpeedControllerWrapperJni.getPortList().length);

        new Talon(0);
    }

    @Test
    public void testSet()
    {
        Assert.assertEquals(0, SpeedControllerWrapperJni.getPortList().length);

        Talon talon = new Talon(1);
        Assert.assertEquals(0, talon.get(), DOUBLE_EPSILON);
        Assert.assertEquals(0, SpeedControllerWrapperJni.getVoltagePercentage(1), DOUBLE_EPSILON);

        talon.set(.5);
        Assert.assertEquals(.5, talon.get(), DOUBLE_EPSILON);
        Assert.assertEquals(.5, SpeedControllerWrapperJni.getVoltagePercentage(1), DOUBLE_EPSILON);

        talon.set(-.5);
        Assert.assertEquals(-.5, talon.get(), DOUBLE_EPSILON);
        Assert.assertEquals(-.5, SpeedControllerWrapperJni.getVoltagePercentage(1), DOUBLE_EPSILON);

        talon.set(1.1);
        Assert.assertEquals(1.0, talon.get(), DOUBLE_EPSILON);
        Assert.assertEquals(1.0, SpeedControllerWrapperJni.getVoltagePercentage(1), DOUBLE_EPSILON);

        talon.set(-2.1);
        Assert.assertEquals(-1.0, talon.get(), DOUBLE_EPSILON);
        Assert.assertEquals(-1.0, SpeedControllerWrapperJni.getVoltagePercentage(1), DOUBLE_EPSILON);
    }
}
