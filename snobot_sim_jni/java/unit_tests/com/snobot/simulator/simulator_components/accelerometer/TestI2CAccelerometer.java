package com.snobot.simulator.simulator_components.accelerometer;

import org.junit.Assert;
import org.junit.Test;

import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;
import com.snobot.test.utilities.BaseSimulatorTest;

import edu.wpi.first.wpilibj.ADXL345_I2C;
import edu.wpi.first.wpilibj.ADXL345_I2C.AllAxes;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.interfaces.Accelerometer.Range;

public class TestI2CAccelerometer extends BaseSimulatorTest
{
    @Test
    public void testADXL345_I2C()
    {
        ADXL345_I2C accel = new ADXL345_I2C(I2C.Port.kOnboard, Range.k2G);
        AllAxes axes = null;

        int yawHandle = 0;
        int pitchHandle = 1;
        int rollHandle = 2;

        Assert.assertTrue(DataAccessorFactory.getInstance().getAccelerometerAccessor().getPortList().contains(yawHandle));
        Assert.assertTrue(DataAccessorFactory.getInstance().getAccelerometerAccessor().getPortList().contains(pitchHandle));
        Assert.assertTrue(DataAccessorFactory.getInstance().getAccelerometerAccessor().getPortList().contains(rollHandle));

        DataAccessorFactory.getInstance().getAccelerometerAccessor().setAcceleration(yawHandle, 0);
        DataAccessorFactory.getInstance().getAccelerometerAccessor().setAcceleration(pitchHandle, 1);
        DataAccessorFactory.getInstance().getAccelerometerAccessor().setAcceleration(rollHandle, 2);
        System.out.println("XXXXXXXXX");
        axes = accel.getAccelerations();
        // System.out.println(axes.XAxis);
        // System.out.println(axes.YAxis);
        // System.out.println(axes.ZAxis);
        Assert.assertEquals(0, DataAccessorFactory.getInstance().getAccelerometerAccessor().getAcceleration(yawHandle), DOUBLE_EPSILON);
        Assert.assertEquals(1, DataAccessorFactory.getInstance().getAccelerometerAccessor().getAcceleration(pitchHandle), DOUBLE_EPSILON);
        Assert.assertEquals(2, DataAccessorFactory.getInstance().getAccelerometerAccessor().getAcceleration(rollHandle), DOUBLE_EPSILON);
        Assert.assertEquals(0, accel.getX(), DOUBLE_EPSILON);
        Assert.assertEquals(1, accel.getY(), DOUBLE_EPSILON);
        Assert.assertEquals(2, accel.getZ(), DOUBLE_EPSILON);

        // TODO read all accel
    }
}
