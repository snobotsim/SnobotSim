package com.snobot.simulator.simulator_components.accelerometer;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.snobot.simulator.simulator_components.ISpiWrapper;
import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;
import com.snobot.test.utilities.BaseSimulatorTest;

import edu.wpi.first.wpilibj.ADXL362;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.interfaces.Accelerometer.Range;

@RunWith(value = Parameterized.class)
public class TestADXL362_SPIAccelerometer extends BaseSimulatorTest
{
    @Parameters()
    public static Collection<Object[]> data()
    {
        Collection<Object[]> output = new ArrayList<>();

        for(SPI.Port port : SPI.Port.values())
        {
            output.add(new Object[]{port, Range.k2G});
        }

        return output;
    }

    private final SPI.Port mPort;
    private final Range mRange;

    public TestADXL362_SPIAccelerometer(SPI.Port aPort, Range aRange)
    {
        mPort = aPort;
        mRange = aRange;
    }
    
    @Test
    public void testADXL362_SPI()
    {
        DataAccessorFactory.getInstance().getSimulatorDataAccessor().setDefaultSpiSimulator(mPort.value, "ADXL362");

        ADXL362 accel = new ADXL362(mPort, mRange);
        ADXL362.AllAxes axes = null;

        int xHandle = 75 + mPort.value * 3;
        int yHandle = 76 + mPort.value * 3;
        int zHandle = 77 + mPort.value * 3;

        System.out.println(DataAccessorFactory.getInstance().getAccelerometerAccessor().getPortList());
        Assert.assertEquals(3, DataAccessorFactory.getInstance().getAccelerometerAccessor().getPortList().size());
        Assert.assertTrue(DataAccessorFactory.getInstance().getAccelerometerAccessor().getPortList().contains(xHandle));
        Assert.assertTrue(DataAccessorFactory.getInstance().getAccelerometerAccessor().getPortList().contains(yHandle));
        Assert.assertTrue(DataAccessorFactory.getInstance().getAccelerometerAccessor().getPortList().contains(zHandle));

        // Initial State
        Assert.assertEquals(0, DataAccessorFactory.getInstance().getAccelerometerAccessor().getAcceleration(xHandle), DOUBLE_EPSILON);
        Assert.assertEquals(0, DataAccessorFactory.getInstance().getAccelerometerAccessor().getAcceleration(yHandle), DOUBLE_EPSILON);
        Assert.assertEquals(0, DataAccessorFactory.getInstance().getAccelerometerAccessor().getAcceleration(zHandle), DOUBLE_EPSILON);
        Assert.assertEquals(0, accel.getX(), DOUBLE_EPSILON);
        Assert.assertEquals(0, accel.getY(), DOUBLE_EPSILON);
        Assert.assertEquals(0, accel.getZ(), DOUBLE_EPSILON);

        // Set positive accelerations
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
        Assert.assertEquals(0, axes.XAxis, DOUBLE_EPSILON);
        Assert.assertEquals(1, axes.YAxis, DOUBLE_EPSILON);
        Assert.assertEquals(2, axes.ZAxis, DOUBLE_EPSILON);

        // Set Negative accelerations
        DataAccessorFactory.getInstance().getAccelerometerAccessor().setAcceleration(xHandle, -0.3);
        DataAccessorFactory.getInstance().getAccelerometerAccessor().setAcceleration(yHandle, -1.3);
        DataAccessorFactory.getInstance().getAccelerometerAccessor().setAcceleration(zHandle, -2.0);
        axes = accel.getAccelerations();
        Assert.assertEquals(-0.3, DataAccessorFactory.getInstance().getAccelerometerAccessor().getAcceleration(xHandle), DOUBLE_EPSILON);
        Assert.assertEquals(-1.3, DataAccessorFactory.getInstance().getAccelerometerAccessor().getAcceleration(yHandle), DOUBLE_EPSILON);
        Assert.assertEquals(-2.0, DataAccessorFactory.getInstance().getAccelerometerAccessor().getAcceleration(zHandle), DOUBLE_EPSILON);
        Assert.assertEquals(-0.3, accel.getX(), DOUBLE_EPSILON);
        Assert.assertEquals(-1.3, accel.getY(), DOUBLE_EPSILON);
        Assert.assertEquals(-2.0, accel.getZ(), DOUBLE_EPSILON);
        Assert.assertEquals(-0.3, axes.XAxis, DOUBLE_EPSILON);
        Assert.assertEquals(-1.3, axes.YAxis, DOUBLE_EPSILON);
        Assert.assertEquals(-2.0, axes.ZAxis, DOUBLE_EPSILON);
    }


    @Test
    public void testInvalidRead()
    {
        ISpiWrapper accel = new ADXL362_SpiAccelerometer(0);
        accel.handleRead();
    }
}
