package com.snobot.simulator.simulator_components.gyro;

import org.junit.Assert;
import org.junit.Test;

import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;
import com.snobot.test.utilities.BaseSimulatorJavaTest;

import edu.wpi.first.wpilibj.AnalogGyro;

public class TestAnalogGyro extends BaseSimulatorJavaTest
{
    @Test
    public void testAnalogGyro()
    {
        AnalogGyro gyro = new AnalogGyro(0);

        int gyroHandle = 0;
        Assert.assertTrue(DataAccessorFactory.getInstance().getGyroAccessor().getPortList().contains(gyroHandle));

        Assert.assertEquals(0, DataAccessorFactory.getInstance().getGyroAccessor().getAngle(gyroHandle), DOUBLE_EPSILON);
        Assert.assertEquals(0, gyro.getAngle(), DOUBLE_EPSILON);

        DataAccessorFactory.getInstance().getGyroAccessor().setAngle(gyroHandle, 90);
        Assert.assertEquals(90, DataAccessorFactory.getInstance().getGyroAccessor().getAngle(gyroHandle), DOUBLE_EPSILON);
        Assert.assertEquals(90, gyro.getAngle(), DOUBLE_EPSILON);
    }

}
