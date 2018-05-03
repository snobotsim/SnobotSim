package com.snobot.simulator.simulator_components.gyro;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;
import com.snobot.test.utilities.BaseSimulatorJniTest;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.SPI;

@RunWith(value = Parameterized.class)
public class TestSpiGyro extends BaseSimulatorJniTest
{
    @Parameters()
    public static Collection<SPI.Port> data()
    {
        Collection<SPI.Port> output = new ArrayList<>();

        output.addAll(Arrays.asList(SPI.Port.values()));

        return output;
    }

    private final SPI.Port mPort;

    public TestSpiGyro(SPI.Port aPort)
    {
        mPort = aPort;
    }

    @Test
    public void testSpiGyro()
    {
        DataAccessorFactory.getInstance().getSimulatorDataAccessor().setDefaultSpiSimulator(mPort.value, "ADXRS450");
        ADXRS450_Gyro gyro = new ADXRS450_Gyro(mPort);

        int gyroHandle = 100 + mPort.value;
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

        // Reset
        gyro.reset();
        Assert.assertEquals(0, gyro.getAngle(), DOUBLE_EPSILON);
    }
}
