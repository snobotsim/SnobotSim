package com.snobot.simulator.module_wrapper;

import org.junit.Assert;
import org.junit.Test;

import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;
import com.snobot.test.utilities.BaseSimulatorTest;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Solenoid;

public class TestSolenoidJni extends BaseSimulatorTest
{
    @Test
    public void testCreateSolenoid()
    {
        Assert.assertEquals(0, DataAccessorFactory.getInstance().getSolenoidAccessor().getPortList().size());

        new Solenoid(0);
        Assert.assertEquals(1, DataAccessorFactory.getInstance().getSolenoidAccessor().getPortList().size());
        Assert.assertEquals("Solenoid 0", DataAccessorFactory.getInstance().getSolenoidAccessor().getName(0));
        Assert.assertFalse(DataAccessorFactory.getInstance().getSolenoidAccessor().getWantsHidden(0));

        new Solenoid(3);
        Assert.assertEquals(2, DataAccessorFactory.getInstance().getSolenoidAccessor().getPortList().size());
        Assert.assertEquals("Solenoid 3", DataAccessorFactory.getInstance().getSolenoidAccessor().getName(3));
        Assert.assertFalse(DataAccessorFactory.getInstance().getSolenoidAccessor().getWantsHidden(3));

        DataAccessorFactory.getInstance().getSolenoidAccessor().setName(0, "NewNameFor0");
        Assert.assertEquals("NewNameFor0", DataAccessorFactory.getInstance().getSolenoidAccessor().getName(0));
    }

    @Test(expected = RuntimeException.class)
    public void testReusePort()
    {
        Assert.assertEquals(0, DataAccessorFactory.getInstance().getSolenoidAccessor().getPortList().size());

        new Solenoid(1);
        Assert.assertEquals(1, DataAccessorFactory.getInstance().getSolenoidAccessor().getPortList().size());

        new Solenoid(1);
    }

    @Test
    public void testSet()
    {
        Assert.assertEquals(0, DataAccessorFactory.getInstance().getSolenoidAccessor().getPortList().size());

        Solenoid solenoid = new Solenoid(0);

        solenoid.set(true);
        Assert.assertTrue(solenoid.get());
        Assert.assertTrue(DataAccessorFactory.getInstance().getSolenoidAccessor().get(0));

        solenoid.set(false);
        Assert.assertFalse(solenoid.get());
        Assert.assertFalse(DataAccessorFactory.getInstance().getSolenoidAccessor().get(0));

        DoubleSolenoid doubleSolenoid = new DoubleSolenoid(1, 2);

        doubleSolenoid.set(Value.kForward);
        Assert.assertTrue(DataAccessorFactory.getInstance().getSolenoidAccessor().get(1));
        Assert.assertFalse(DataAccessorFactory.getInstance().getSolenoidAccessor().get(2));

        doubleSolenoid.set(Value.kReverse);
        Assert.assertFalse(DataAccessorFactory.getInstance().getSolenoidAccessor().get(1));
        Assert.assertTrue(DataAccessorFactory.getInstance().getSolenoidAccessor().get(2));

        doubleSolenoid.set(Value.kOff);
        Assert.assertFalse(DataAccessorFactory.getInstance().getSolenoidAccessor().get(1));
        Assert.assertFalse(DataAccessorFactory.getInstance().getSolenoidAccessor().get(2));
    }
}
