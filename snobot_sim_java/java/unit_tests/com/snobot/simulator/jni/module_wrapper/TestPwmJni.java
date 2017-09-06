package com.snobot.simulator.jni.module_wrapper;

import org.junit.Assert;
import org.junit.Test;

import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;
import com.snobot.test.utilities.BaseSimulatorTest;

import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Talon;

public class TestPwmJni extends BaseSimulatorTest
{
    @Test
    public void testCreatePwm()
    {
        Assert.assertEquals(0, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPortList().size());

        new Jaguar(0);
        Assert.assertEquals(1, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPortList().size());
        Assert.assertEquals("Speed Controller 0", DataAccessorFactory.getInstance().getSpeedControllerAccessor().getName(0));

        new Talon(3);
        Assert.assertEquals(2, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPortList().size());
        Assert.assertEquals("Speed Controller 3", DataAccessorFactory.getInstance().getSpeedControllerAccessor().getName(3));
    }

    @Test(expected = RuntimeException.class)
    public void testReusePort()
    {
        Assert.assertEquals(0, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPortList().size());

        new Talon(0);
        Assert.assertEquals(1, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPortList().size());

        new Talon(0);
    }

    @Test
    public void testSet()
    {
        Assert.assertEquals(0, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPortList().size());

        Talon talon = new Talon(1);
        Assert.assertEquals(0, talon.get(), DOUBLE_EPSILON);
        Assert.assertEquals(0, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVoltagePercentage(1), DOUBLE_EPSILON);

        talon.set(.5);
        Assert.assertEquals(.5, talon.get(), DOUBLE_EPSILON);
        Assert.assertEquals(.5, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVoltagePercentage(1), DOUBLE_EPSILON);

        talon.set(-.5);
        Assert.assertEquals(-.5, talon.get(), DOUBLE_EPSILON);
        Assert.assertEquals(-.5, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVoltagePercentage(1), DOUBLE_EPSILON);

        talon.set(1.1);
        Assert.assertEquals(1.0, talon.get(), DOUBLE_EPSILON);
        Assert.assertEquals(1.0, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVoltagePercentage(1), DOUBLE_EPSILON);

        talon.set(-2.1);
        Assert.assertEquals(-1.0, talon.get(), DOUBLE_EPSILON);
        Assert.assertEquals(-1.0, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVoltagePercentage(1), DOUBLE_EPSILON);
    }
}
