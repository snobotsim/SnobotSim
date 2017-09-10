package com.snobot.simulator.simulator_components.gyro;

import org.junit.Assert;
import org.junit.Test;

import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;
import com.snobot.test.utilities.BaseSimulatorTest;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;

public class TestSpiGyro extends BaseSimulatorTest
{

    @Test
    public void testSpiGyro()
    {
        ADXRS450_Gyro gyro = new ADXRS450_Gyro();

        int gyroHandle = 0;
        Assert.assertTrue(DataAccessorFactory.getInstance().getGyroAccessor().getPortList().contains(gyroHandle));

        Assert.assertEquals(0, DataAccessorFactory.getInstance().getGyroAccessor().getAngle(gyroHandle), DOUBLE_EPSILON);
        Assert.assertEquals(0, gyro.getAngle(), DOUBLE_EPSILON);

        DataAccessorFactory.getInstance().getGyroAccessor().setAngle(gyroHandle, 90);
        Assert.assertEquals(90, DataAccessorFactory.getInstance().getGyroAccessor().getAngle(gyroHandle), DOUBLE_EPSILON);
        Assert.assertEquals(90, gyro.getAngle(), DOUBLE_EPSILON);

        DataAccessorFactory.getInstance().getGyroAccessor().setAngle(gyroHandle, 192.1234);
        Assert.assertEquals(192.1234, DataAccessorFactory.getInstance().getGyroAccessor().getAngle(gyroHandle), DOUBLE_EPSILON);
        Assert.assertEquals(192.1234, gyro.getAngle(), DOUBLE_EPSILON);

        DataAccessorFactory.getInstance().getGyroAccessor().setAngle(gyroHandle, 359.9999);
        Assert.assertEquals(359.9999, DataAccessorFactory.getInstance().getGyroAccessor().getAngle(gyroHandle), DOUBLE_EPSILON);
        Assert.assertEquals(359.9999, gyro.getAngle(), DOUBLE_EPSILON);

        DataAccessorFactory.getInstance().getGyroAccessor().setAngle(gyroHandle, -421.3358);
        Assert.assertEquals(-421.3358, DataAccessorFactory.getInstance().getGyroAccessor().getAngle(gyroHandle), DOUBLE_EPSILON);
        Assert.assertEquals(-421.3358, gyro.getAngle(), DOUBLE_EPSILON);
    }
}
