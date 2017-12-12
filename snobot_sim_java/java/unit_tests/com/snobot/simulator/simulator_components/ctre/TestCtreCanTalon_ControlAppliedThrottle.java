package com.snobot.simulator.simulator_components.ctre;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.ctre.CANTalon;
import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;
import com.snobot.test.utilities.BaseSimulatorTest;

@RunWith(value = Parameterized.class)
public class TestCtreCanTalon_ControlAppliedThrottle extends BaseSimulatorTest
{
    private final int mCanHandle;
    private final int mRawHandle;

    @Parameters(name = "Test: {index} CanPort={0}")
    public static Collection<Integer> data()
    {
        Collection<Integer> output = new ArrayList<>();

        for (int i = 0; i < 64; ++i)
        {
            output.add(i);
        }

        return output;
    }

    public TestCtreCanTalon_ControlAppliedThrottle(int aCanHandle)
    {
        mCanHandle = aCanHandle;
        mRawHandle = mCanHandle + 100;
    }

    @Test
    public void testSetWithAppliedThrottle()
    {
        double DOUBLE_EPSILON = 1.0 / 1023;

        Assert.assertEquals(0, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPortList().size());
        CANTalon canTalon1 = new CANTalon(mCanHandle);
        Assert.assertEquals(1, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPortList().size());

        Assert.assertEquals(0, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVoltagePercentage(mRawHandle), DOUBLE_EPSILON);

        canTalon1.set(-1.0);
        Assert.assertEquals(-1.0, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVoltagePercentage(mRawHandle), DOUBLE_EPSILON);
        Assert.assertEquals(-1.0, canTalon1.get(), DOUBLE_EPSILON);

        canTalon1.set(-0.5);
        Assert.assertEquals(-0.5, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVoltagePercentage(mRawHandle), DOUBLE_EPSILON);
        Assert.assertEquals(-0.5, canTalon1.get(), DOUBLE_EPSILON);

        canTalon1.set(-0.1);
        Assert.assertEquals(-0.1, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVoltagePercentage(mRawHandle), DOUBLE_EPSILON);
        Assert.assertEquals(-0.1, canTalon1.get(), DOUBLE_EPSILON);

        canTalon1.set(0);
        Assert.assertEquals(0.0, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVoltagePercentage(mRawHandle), DOUBLE_EPSILON);
        Assert.assertEquals(0.0, canTalon1.get(), DOUBLE_EPSILON);

        canTalon1.set(.1);
        Assert.assertEquals(0.1, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVoltagePercentage(mRawHandle), DOUBLE_EPSILON);
        Assert.assertEquals(0.1, canTalon1.get(), DOUBLE_EPSILON);

        canTalon1.set(.5);
        Assert.assertEquals(0.5, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVoltagePercentage(mRawHandle), DOUBLE_EPSILON);
        Assert.assertEquals(0.5, canTalon1.get(), DOUBLE_EPSILON);

        canTalon1.set(1);
        Assert.assertEquals(1.0, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVoltagePercentage(mRawHandle), DOUBLE_EPSILON);
        Assert.assertEquals(1.0, canTalon1.get(), DOUBLE_EPSILON);
    }
}
