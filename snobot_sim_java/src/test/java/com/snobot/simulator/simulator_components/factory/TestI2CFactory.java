package com.snobot.simulator.simulator_components.factory;

import java.util.Collection;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;
import com.snobot.test.utilities.BaseSimulatorTest;

import edu.wpi.first.wpilibj.ADXL345_I2C;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.interfaces.Accelerometer.Range;

public class TestI2CFactory extends BaseSimulatorTest
{
    @Test
    public void testAvailableDataTypes()
    {
        DataAccessorFactory.getInstance().getSimulatorDataAccessor().reset();
        Collection<String> available = DataAccessorFactory.getInstance().getSimulatorDataAccessor().getAvailableI2CSimulators();

        Assert.assertEquals(2, available.size());
        Assert.assertTrue(available.contains("NavX"));
        Assert.assertTrue(available.contains("ADXL345"));
    }

    @Test
    public void testInvalidType()
    {
        DataAccessorFactory.getInstance().getSimulatorDataAccessor().reset();
        DataAccessorFactory.getInstance().getSimulatorDataAccessor().setDefaultSpiSimulator(0, "DoesntExist");

        new ADXL345_I2C(I2C.Port.kOnboard, Range.k2G);
    }

    @Test
    public void testDefaultI2CWrappers()
    {
        DataAccessorFactory.getInstance().getSimulatorDataAccessor().setDefaultI2CSimulator(0, "TestA");
        DataAccessorFactory.getInstance().getSimulatorDataAccessor().setDefaultI2CSimulator(1, "TestB");
        Map<Integer, String> defaults = DataAccessorFactory.getInstance().getSimulatorDataAccessor().getDefaultI2CWrappers();

        Assert.assertEquals(2, defaults.size());
        Assert.assertEquals("TestA", defaults.get(0));
        Assert.assertEquals("TestB", defaults.get(1));
    }
}
