package com.snobot.simulator.simulator_components.accelerometer;

import org.junit.Assert;
import org.junit.Test;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.jni.RegisterCallbacksJni;
import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;
import com.snobot.test.utilities.BaseSimulatorTest;

import edu.wpi.first.wpilibj.ADXL345_SPI;
import edu.wpi.first.wpilibj.ADXL345_SPI.AllAxes;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.interfaces.Accelerometer.Range;

public class TestSpiAccelerometer extends BaseSimulatorTest
{
    @Test
    public void testADXL345_I2C()
    {
        RegisterCallbacksJni.sSPI_FACTORY.setDefaultWrapper(0, SpiAccelerometer.class);
        ADXL345_SPI accel = new ADXL345_SPI(SPI.Port.kOnboardCS0, Range.k2G);
        AllAxes axes = null;

        int xHandle = 0;
        int yHandle = 1;
        int zHandle = 2;

        Assert.assertEquals(3, SensorActuatorRegistry.get().getAccelerometers().size());
        System.out.println(SensorActuatorRegistry.get().getAccelerometers());
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
