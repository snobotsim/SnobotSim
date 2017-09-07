package com.snobot.simulator.module_wrapper;

import org.junit.Assert;
import org.junit.Test;

import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;
import com.snobot.test.utilities.BaseSimulatorTest;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;

public class TestDigitalIOJni extends BaseSimulatorTest
{
    @Test
    public void testCreateDigitalIn()
    {
        Assert.assertEquals(0, DataAccessorFactory.getInstance().getDigitalAccessor().getPortList().size());

        new DigitalInput(0);
        Assert.assertEquals(1, DataAccessorFactory.getInstance().getDigitalAccessor().getPortList().size());
        Assert.assertEquals("Digital Source0", DataAccessorFactory.getInstance().getDigitalAccessor().getName(0));

        new DigitalInput(3);
        Assert.assertEquals(2, DataAccessorFactory.getInstance().getDigitalAccessor().getPortList().size());
        Assert.assertEquals("Digital Source3", DataAccessorFactory.getInstance().getDigitalAccessor().getName(3));
    }

    @Test
    public void testCreateDigitalOut()
    {
        Assert.assertEquals(0, DataAccessorFactory.getInstance().getDigitalAccessor().getPortList().size());

        new DigitalOutput(0);
        Assert.assertEquals(1, DataAccessorFactory.getInstance().getDigitalAccessor().getPortList().size());

        new DigitalOutput(3);
        Assert.assertEquals(2, DataAccessorFactory.getInstance().getDigitalAccessor().getPortList().size());
    }

    @Test
    public void testDigitalIn()
    {
        DigitalInput input = new DigitalInput(0);

        Assert.assertTrue(input.get());
        Assert.assertTrue(DataAccessorFactory.getInstance().getDigitalAccessor().getState(0));

        DataAccessorFactory.getInstance().getDigitalAccessor().setState(0, false);
        Assert.assertFalse(input.get());
        Assert.assertFalse(DataAccessorFactory.getInstance().getDigitalAccessor().getState(0));
    }

}
