package com.snobot.simulator.simulator_components.ctre;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.ctre.phoenix.MotorControl.CAN.TalonSRX;
import com.ctre.PigeonImu;
import com.ctre.PigeonImu.FusionStatus;
import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;
import com.snobot.test.utilities.BaseSimulatorTest;

@RunWith(value = Parameterized.class)
public class TestPigeonImu extends BaseSimulatorTest
{
    private final int mDeviceId;

    @Parameters()
    public static Collection<Integer> data()
    {
        Collection<Integer> output = new ArrayList<>();

        for (int i = 0; i < 48; ++i)
        {
            output.add(i);
        }

        return output;
    }

    public TestPigeonImu(int aDeviceId)
    {
        mDeviceId = aDeviceId;
    }

    @Test
    public void testPigeonInSeries()
    {
        PigeonImu imu = new PigeonImu(mDeviceId);
        testImu(imu);
    }

    @Test
    public void testPigeonInTalon()
    {
        CANTalon talon = new CANTalon(mDeviceId);
        PigeonImu imu = new PigeonImu(talon);
        testImu(imu);
    }

    private void testImu(PigeonImu imu)
    {
        final double ANGLE_EPSILON = 1 / 16.0;

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
