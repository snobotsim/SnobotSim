package com.snobot.simulator.module_wrapper;

import org.junit.Assert;
import org.junit.Test;

import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;
import com.snobot.test.utilities.BaseSimulatorTest;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.AnalogOutput;

public class TestAnalogIOJni extends BaseSimulatorTest
{
    @Test
    public void testCreateAnalogIn()
    {
        Assert.assertEquals(0, DataAccessorFactory.getInstance().getAnalogAccessor().getPortList().size());

        new AnalogInput(0);
        Assert.assertEquals(1, DataAccessorFactory.getInstance().getAnalogAccessor().getPortList().size());
        Assert.assertEquals("Analog 0", DataAccessorFactory.getInstance().getAnalogAccessor().getName(0));
        Assert.assertFalse(DataAccessorFactory.getInstance().getAnalogAccessor().getWantsHidden(0));

        new AnalogInput(3);
        Assert.assertEquals(2, DataAccessorFactory.getInstance().getAnalogAccessor().getPortList().size());
        Assert.assertEquals("Analog 3", DataAccessorFactory.getInstance().getAnalogAccessor().getName(3));
        Assert.assertFalse(DataAccessorFactory.getInstance().getAnalogAccessor().getWantsHidden(3));
        
        DataAccessorFactory.getInstance().getAnalogAccessor().setName(3, "NewNameFor3");
        Assert.assertEquals("NewNameFor3", DataAccessorFactory.getInstance().getAnalogAccessor().getName(3));

        // Set name for non-existing sensor
        DataAccessorFactory.getInstance().getAnalogAccessor().setName(4, "NewNameFor4");
    }

    @Test
    public void testCreateAnalogOut()
    {
        Assert.assertEquals(0, DataAccessorFactory.getInstance().getAnalogAccessor().getPortList().size());

        new AnalogOutput(0);
        Assert.assertEquals(1, DataAccessorFactory.getInstance().getAnalogAccessor().getPortList().size());

        new AnalogOutput(1);
        Assert.assertEquals(2, DataAccessorFactory.getInstance().getAnalogAccessor().getPortList().size());
    }

    @Test(expected = RuntimeException.class)
    public void testReuseInPort()
    {
        Assert.assertEquals(0, DataAccessorFactory.getInstance().getAnalogAccessor().getPortList().size());

        new AnalogInput(0);
        Assert.assertEquals(1, DataAccessorFactory.getInstance().getAnalogAccessor().getPortList().size());

        new AnalogInput(0);
    }

    @Test(expected = RuntimeException.class)
    public void testReuseOutPort()
    {
        Assert.assertEquals(0, DataAccessorFactory.getInstance().getAnalogAccessor().getPortList().size());

        new AnalogOutput(0);
        Assert.assertEquals(1, DataAccessorFactory.getInstance().getAnalogAccessor().getPortList().size());

        new AnalogOutput(0);
    }

    @Test
    public void testAnalogIn()
    {
        Assert.assertEquals(0, DataAccessorFactory.getInstance().getAnalogAccessor().getPortList().size());
        AnalogInput input = new AnalogInput(0);
        Assert.assertEquals(1, DataAccessorFactory.getInstance().getAnalogAccessor().getPortList().size());

        Assert.assertEquals(0, input.getVoltage(), DOUBLE_EPSILON);
        Assert.assertEquals(0, DataAccessorFactory.getInstance().getAnalogAccessor().getVoltage(0), DOUBLE_EPSILON);
    }
}
