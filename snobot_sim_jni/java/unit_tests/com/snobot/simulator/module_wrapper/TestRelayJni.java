package com.snobot.simulator.module_wrapper;

import org.junit.Assert;
import org.junit.Test;

import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;
import com.snobot.test.utilities.BaseSimulatorTest;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Value;

public class TestRelayJni extends BaseSimulatorTest
{
    @Test
    public void testCreateRelays()
    {
        Assert.assertEquals(0, DataAccessorFactory.getInstance().getRelayAccessor().getPortList().size());

        new Relay(0);
        Assert.assertEquals(1, DataAccessorFactory.getInstance().getRelayAccessor().getPortList().size());
        Assert.assertEquals("Relay 0", DataAccessorFactory.getInstance().getRelayAccessor().getName(0));
        Assert.assertFalse(DataAccessorFactory.getInstance().getRelayAccessor().getWantsHidden(0));

        new Relay(1);
        Assert.assertEquals(2, DataAccessorFactory.getInstance().getRelayAccessor().getPortList().size());
        Assert.assertEquals("Relay 1", DataAccessorFactory.getInstance().getRelayAccessor().getName(1));
        Assert.assertFalse(DataAccessorFactory.getInstance().getRelayAccessor().getWantsHidden(1));

        DataAccessorFactory.getInstance().getRelayAccessor().setName(0, "NewNameFor0");
        Assert.assertEquals("NewNameFor0", DataAccessorFactory.getInstance().getRelayAccessor().getName(0));
    }

    @Test(expected = RuntimeException.class)
    public void testReusePort()
    {
        Assert.assertEquals(0, DataAccessorFactory.getInstance().getRelayAccessor().getPortList().size());

        new Relay(2);
        Assert.assertEquals(1, DataAccessorFactory.getInstance().getRelayAccessor().getPortList().size());

        new Relay(2);
    }

    @Test
    public void testSetRelay()
    {
        Assert.assertEquals(0, DataAccessorFactory.getInstance().getRelayAccessor().getPortList().size());

        Relay relay = new Relay(1);
        Assert.assertFalse(DataAccessorFactory.getInstance().getRelayAccessor().getFowardValue(1));
        Assert.assertFalse(DataAccessorFactory.getInstance().getRelayAccessor().getReverseValue(1));

        relay.set(Value.kOn);
        Assert.assertTrue(DataAccessorFactory.getInstance().getRelayAccessor().getFowardValue(1));
        Assert.assertTrue(DataAccessorFactory.getInstance().getRelayAccessor().getReverseValue(1));

        relay.set(Value.kForward);
        Assert.assertTrue(DataAccessorFactory.getInstance().getRelayAccessor().getFowardValue(1));
        Assert.assertFalse(DataAccessorFactory.getInstance().getRelayAccessor().getReverseValue(1));

        relay.set(Value.kReverse);
        Assert.assertFalse(DataAccessorFactory.getInstance().getRelayAccessor().getFowardValue(1));
        Assert.assertTrue(DataAccessorFactory.getInstance().getRelayAccessor().getReverseValue(1));

        relay.set(Value.kOff);
        Assert.assertFalse(DataAccessorFactory.getInstance().getRelayAccessor().getFowardValue(1));
        Assert.assertFalse(DataAccessorFactory.getInstance().getRelayAccessor().getReverseValue(1));
    }
}
