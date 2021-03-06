package com.snobot.simulator.simulator_components.factory;

import java.util.Collection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;
import com.snobot.test.utilities.BaseSimulatorJavaTest;

public class TestI2CFactory extends BaseSimulatorJavaTest
{
    @Test
    public void testAvailableDataTypes()
    {
        DataAccessorFactory.getInstance().getSimulatorDataAccessor().reset();
        Collection<String> available = DataAccessorFactory.getInstance().getI2CAccessor().getAvailableI2CSimulators();

        Assertions.assertEquals(2, available.size());
        Assertions.assertTrue(available.contains("NavX"));
        Assertions.assertTrue(available.contains("ADXL345"));
    }

    @Test
    public void testInvalidType()
    {
        DataAccessorFactory.getInstance().getSimulatorDataAccessor().reset();
        DataAccessorFactory.getInstance().getI2CAccessor().createI2CSimulator(0, "DoesntExist");
    }
}
