package com.snobot.simulator.simulator_components.ctre;

import org.junit.Assert;
import org.junit.Test;

import com.ctre.PigeonImu;
import com.ctre.PigeonImu.FusionStatus;
import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;
import com.snobot.test.utilities.BaseSimulatorTest;

public class TestPigeonImu extends BaseSimulatorTest
{
    private final int mDeviceId;

    public TestPigeonImu()
    {
        mDeviceId = 3;
    }

    @Test
    public void testPigeon()
    {
        final double ANGLE_EPSILON = 1 / 16.0;

        PigeonImu imu = new PigeonImu(mDeviceId);

        int basePort = 400 + mDeviceId * 3;

        int yawPort = basePort + 0;
        int pitchPort = basePort + 1;
        int rollPort = basePort + 2;
        int xPort = basePort + 0;
        int yPort = basePort + 1;
        int zPort = basePort + 2;

        double[] rawAngles = new double[3];
        double fusedHeading = 0;
        FusionStatus fusionStatus = new FusionStatus();

        Assert.assertEquals(3, DataAccessorFactory.getInstance().getGyroAccessor().getPortList().size());
        Assert.assertEquals(3, DataAccessorFactory.getInstance().getAccelerometerAccessor().getPortList().size());
        Assert.assertTrue(DataAccessorFactory.getInstance().getGyroAccessor().getPortList().contains(yawPort));
        Assert.assertTrue(DataAccessorFactory.getInstance().getGyroAccessor().getPortList().contains(pitchPort));
        Assert.assertTrue(DataAccessorFactory.getInstance().getGyroAccessor().getPortList().contains(rollPort));
        Assert.assertTrue(DataAccessorFactory.getInstance().getAccelerometerAccessor().getPortList().contains(xPort));
        Assert.assertTrue(DataAccessorFactory.getInstance().getAccelerometerAccessor().getPortList().contains(yPort));
        Assert.assertTrue(DataAccessorFactory.getInstance().getAccelerometerAccessor().getPortList().contains(zPort));

        imu.GetRawGyro(rawAngles);
        fusedHeading = imu.GetFusedHeading(fusionStatus);
        Assert.assertEquals(0, fusedHeading, ANGLE_EPSILON);
        Assert.assertEquals(0, rawAngles[0], ANGLE_EPSILON);
        Assert.assertEquals(0, rawAngles[1], ANGLE_EPSILON);
        Assert.assertEquals(0, rawAngles[2], ANGLE_EPSILON);
        Assert.assertEquals(0, DataAccessorFactory.getInstance().getGyroAccessor().getAngle(yawPort), ANGLE_EPSILON);
        Assert.assertEquals(0, DataAccessorFactory.getInstance().getGyroAccessor().getAngle(pitchPort), ANGLE_EPSILON);
        Assert.assertEquals(0, DataAccessorFactory.getInstance().getGyroAccessor().getAngle(rollPort), ANGLE_EPSILON);

        DataAccessorFactory.getInstance().getGyroAccessor().setAngle(yawPort, 47);
        DataAccessorFactory.getInstance().getGyroAccessor().setAngle(pitchPort, -98);
        DataAccessorFactory.getInstance().getGyroAccessor().setAngle(rollPort, 24);

        imu.GetRawGyro(rawAngles);
        fusedHeading = imu.GetFusedHeading(fusionStatus);
        // Assert.assertEquals(47, fusedHeading, ANGLE_EPSILON);
        Assert.assertEquals(47, rawAngles[0], ANGLE_EPSILON);
        Assert.assertEquals(-98, rawAngles[1], ANGLE_EPSILON);
        Assert.assertEquals(24, rawAngles[2], ANGLE_EPSILON);
        Assert.assertEquals(47, DataAccessorFactory.getInstance().getGyroAccessor().getAngle(yawPort), ANGLE_EPSILON);
        Assert.assertEquals(-98, DataAccessorFactory.getInstance().getGyroAccessor().getAngle(pitchPort), ANGLE_EPSILON);
        Assert.assertEquals(24, DataAccessorFactory.getInstance().getGyroAccessor().getAngle(rollPort), ANGLE_EPSILON);
    }
}
