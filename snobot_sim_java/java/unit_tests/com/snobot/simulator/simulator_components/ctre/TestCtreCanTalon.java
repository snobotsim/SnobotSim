package com.snobot.simulator.simulator_components.ctre;

import org.junit.Assert;
import org.junit.Test;

import com.ctre.CANTalon;
import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;
import com.snobot.test.utilities.BaseSimulatorTest;


public class TestCtreCanTalon extends BaseSimulatorTest
{
    @Test
    public void testSimpleSetup()
    {
        double DOUBLE_EPSILON = 1.0 / 1023;

        for (int i = 0; i < 64; ++i)
        {
            CANTalon canTalon1 = new CANTalon(i);

            Assert.assertEquals(0, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVoltagePercentage(i + 100), DOUBLE_EPSILON);

            canTalon1.set(-1.0);
            Assert.assertEquals(-1.0, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVoltagePercentage(i + 100), DOUBLE_EPSILON);
            Assert.assertEquals(-1.0, canTalon1.get(), DOUBLE_EPSILON);

            canTalon1.set(-0.5);
            Assert.assertEquals(-0.5, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVoltagePercentage(i + 100), DOUBLE_EPSILON);
            Assert.assertEquals(-0.5, canTalon1.get(), DOUBLE_EPSILON);

            canTalon1.set(-0.1);
            Assert.assertEquals(-0.1, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVoltagePercentage(i + 100), DOUBLE_EPSILON);
            Assert.assertEquals(-0.1, canTalon1.get(), DOUBLE_EPSILON);

            canTalon1.set(0);
            Assert.assertEquals(0.0, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVoltagePercentage(i + 100), DOUBLE_EPSILON);
            Assert.assertEquals(0.0, canTalon1.get(), DOUBLE_EPSILON);

            canTalon1.set(.1);
            Assert.assertEquals(0.1, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVoltagePercentage(i + 100), DOUBLE_EPSILON);
            Assert.assertEquals(0.1, canTalon1.get(), DOUBLE_EPSILON);

            canTalon1.set(.5);
            Assert.assertEquals(0.5, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVoltagePercentage(i + 100), DOUBLE_EPSILON);
            Assert.assertEquals(0.5, canTalon1.get(), DOUBLE_EPSILON);

            canTalon1.set(1);
            Assert.assertEquals(1.0, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVoltagePercentage(i + 100), DOUBLE_EPSILON);
            Assert.assertEquals(1.0, canTalon1.get(), DOUBLE_EPSILON);
        }
    }

}
