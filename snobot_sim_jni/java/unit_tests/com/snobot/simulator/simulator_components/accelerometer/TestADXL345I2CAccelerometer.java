package com.snobot.simulator.simulator_components.accelerometer;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;
import com.snobot.test.utilities.BaseSimulatorTest;

import edu.wpi.first.wpilibj.ADXL345_I2C;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.interfaces.Accelerometer.Range;

@RunWith(value = Parameterized.class)
public class TestADXL345I2CAccelerometer extends BaseSimulatorTest
{
    private static final double sDOUBLE_EPSILON = 1 / 256.0; // Resoultion isn't as good as normal sensors

    @Parameters()
    public static Collection<Object[]> data()
    {
        Collection<Object[]> output = new ArrayList<>();

        for (I2C.Port port : I2C.Port.values())
        {
            for (Range range : Range.values())
            {
                output.add(new Object[]{ port, range });
            }
        }

        return output;
    }

    private final I2C.Port mPort;
    private final Range mRange;

    public TestADXL345I2CAccelerometer(I2C.Port aPort, Range aRange)
    {
        mPort = aPort;
        mRange = aRange;
    }

    @Test
    public void testADXL345_I2C()
    {
        DataAccessorFactory.getInstance().getSimulatorDataAccessor().setDefaultI2CSimulator(mPort.value, "ADXL345");
        ADXL345_I2C accel = new ADXL345_I2C(mPort, mRange);

        int xHandle = 50 + mPort.value * 3;
        int yHandle = 51 + mPort.value * 3;
        int zHandle = 52 + mPort.value * 3;

        Assert.assertEquals(3, DataAccessorFactory.getInstance().getAccelerometerAccessor().getPortList().size());
        Assert.assertTrue(DataAccessorFactory.getInstance().getAccelerometerAccessor().getPortList().contains(xHandle));
        Assert.assertTrue(DataAccessorFactory.getInstance().getAccelerometerAccessor().getPortList().contains(yHandle));
        Assert.assertTrue(DataAccessorFactory.getInstance().getAccelerometerAccessor().getPortList().contains(zHandle));

        // Initial State
        Assert.assertEquals(0, DataAccessorFactory.getInstance().getAccelerometerAccessor().getAcceleration(xHandle), sDOUBLE_EPSILON);
        Assert.assertEquals(0, DataAccessorFactory.getInstance().getAccelerometerAccessor().getAcceleration(yHandle), sDOUBLE_EPSILON);
        Assert.assertEquals(0, DataAccessorFactory.getInstance().getAccelerometerAccessor().getAcceleration(zHandle), sDOUBLE_EPSILON);
        Assert.assertEquals(0, accel.getX(), sDOUBLE_EPSILON);
        Assert.assertEquals(0, accel.getY(), sDOUBLE_EPSILON);
        Assert.assertEquals(0, accel.getZ(), sDOUBLE_EPSILON);

        // Set positive accelerations
        DataAccessorFactory.getInstance().getAccelerometerAccessor().setAcceleration(xHandle, 0);
        DataAccessorFactory.getInstance().getAccelerometerAccessor().setAcceleration(yHandle, 1);
        DataAccessorFactory.getInstance().getAccelerometerAccessor().setAcceleration(zHandle, 2);
        ADXL345_I2C.AllAxes axes = accel.getAccelerations();
        Assert.assertEquals(0, DataAccessorFactory.getInstance().getAccelerometerAccessor().getAcceleration(xHandle), sDOUBLE_EPSILON);
        Assert.assertEquals(1, DataAccessorFactory.getInstance().getAccelerometerAccessor().getAcceleration(yHandle), sDOUBLE_EPSILON);
        Assert.assertEquals(2, DataAccessorFactory.getInstance().getAccelerometerAccessor().getAcceleration(zHandle), sDOUBLE_EPSILON);
        Assert.assertEquals(0, accel.getX(), sDOUBLE_EPSILON);
        Assert.assertEquals(1, accel.getY(), sDOUBLE_EPSILON);
        Assert.assertEquals(2, accel.getZ(), sDOUBLE_EPSILON);
        Assert.assertEquals(0, axes.XAxis, sDOUBLE_EPSILON);
        Assert.assertEquals(1, axes.YAxis, sDOUBLE_EPSILON);
        Assert.assertEquals(2, axes.ZAxis, sDOUBLE_EPSILON);

        // Set Negative accelerations
        DataAccessorFactory.getInstance().getAccelerometerAccessor().setAcceleration(xHandle, -0.3);
        DataAccessorFactory.getInstance().getAccelerometerAccessor().setAcceleration(yHandle, -1.3);
        DataAccessorFactory.getInstance().getAccelerometerAccessor().setAcceleration(zHandle, -2.0);
        axes = accel.getAccelerations();
        Assert.assertEquals(-0.3, DataAccessorFactory.getInstance().getAccelerometerAccessor().getAcceleration(xHandle), sDOUBLE_EPSILON);
        Assert.assertEquals(-1.3, DataAccessorFactory.getInstance().getAccelerometerAccessor().getAcceleration(yHandle), sDOUBLE_EPSILON);
        Assert.assertEquals(-2.0, DataAccessorFactory.getInstance().getAccelerometerAccessor().getAcceleration(zHandle), sDOUBLE_EPSILON);
        Assert.assertEquals(-0.3, accel.getX(), sDOUBLE_EPSILON);
        Assert.assertEquals(-1.3, accel.getY(), sDOUBLE_EPSILON);
        Assert.assertEquals(-2.0, accel.getZ(), sDOUBLE_EPSILON);
        Assert.assertEquals(-0.3, axes.XAxis, sDOUBLE_EPSILON);
        Assert.assertEquals(-1.3, axes.YAxis, sDOUBLE_EPSILON);
        Assert.assertEquals(-2.0, axes.ZAxis, sDOUBLE_EPSILON);
    }
}
