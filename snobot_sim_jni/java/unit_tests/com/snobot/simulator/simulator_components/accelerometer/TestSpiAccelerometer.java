package com.snobot.simulator.simulator_components.accelerometer;

import org.junit.Assert;
import org.junit.Test;

import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;
import com.snobot.test.utilities.BaseSimulatorTest;

import edu.wpi.first.wpilibj.ADXL345_SPI;
import edu.wpi.first.wpilibj.ADXL345_SPI.AllAxes;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.interfaces.Accelerometer.Range;

public class TestSpiAccelerometer extends BaseSimulatorTest
{
    @Test
    public void testADXL345_SPI()
    {
        ADXL345_SPI accel = new ADXL345_SPI(SPI.Port.kOnboardCS2, Range.k2G);
        AllAxes axes = null;

        int xHandle = 151;
        int yHandle = 152;
        int zHandle = 153;

        Assert.assertEquals(3, DataAccessorFactory.getInstance().getAccelerometerAccessor().getPortList().size());
        System.out.println(DataAccessorFactory.getInstance().getAccelerometerAccessor().getPortList());
        Assert.assertTrue(DataAccessorFactory.getInstance().getAccelerometerAccessor().getPortList().contains(xHandle));
        Assert.assertTrue(DataAccessorFactory.getInstance().getAccelerometerAccessor().getPortList().contains(yHandle));
        Assert.assertTrue(DataAccessorFactory.getInstance().getAccelerometerAccessor().getPortList().contains(zHandle));

        DataAccessorFactory.getInstance().getAccelerometerAccessor().setAcceleration(xHandle, 0);
        DataAccessorFactory.getInstance().getAccelerometerAccessor().setAcceleration(yHandle, 1);
        DataAccessorFactory.getInstance().getAccelerometerAccessor().setAcceleration(zHandle, 2);
        axes = accel.getAccelerations();
        Assert.assertEquals(0, DataAccessorFactory.getInstance().getAccelerometerAccessor().getAcceleration(xHandle), DOUBLE_EPSILON);
        Assert.assertEquals(1, DataAccessorFactory.getInstance().getAccelerometerAccessor().getAcceleration(yHandle), DOUBLE_EPSILON);
        Assert.assertEquals(2, DataAccessorFactory.getInstance().getAccelerometerAccessor().getAcceleration(zHandle), DOUBLE_EPSILON);
        Assert.assertEquals(0, accel.getX(), DOUBLE_EPSILON);
        Assert.assertEquals(1, accel.getY(), DOUBLE_EPSILON);
        Assert.assertEquals(2, accel.getZ(), DOUBLE_EPSILON);

        // TODO read all accel
    }
}
