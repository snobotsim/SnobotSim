package com.snobot.simulator.simulator_components.factory;

import java.util.Collection;

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
}
