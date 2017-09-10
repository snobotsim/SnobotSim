package com.snobot.simulator.simulator_components;

import org.junit.Assert;
import org.junit.Test;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.simulator_components.gyro.GyroWrapper;
import com.snobot.test.utilities.BaseSimulatorTest;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;

public class TestSpiJni extends BaseSimulatorTest
{

    @Test
    public void testSpiGyro()
    {
        ADXRS450_Gyro gyro = new ADXRS450_Gyro();

        GyroWrapper wrapper = SensorActuatorRegistry.get().getGyros().get(100);
        Assert.assertEquals(0, wrapper.getAngle(), DOUBLE_EPSILON);
        Assert.assertEquals(0, gyro.getAngle(), DOUBLE_EPSILON);

        wrapper.setAngle(90);
        Assert.assertEquals(90, wrapper.getAngle(), DOUBLE_EPSILON);
        Assert.assertEquals(90, gyro.getAngle(), DOUBLE_EPSILON);

        wrapper.setAngle(192.1234);
        Assert.assertEquals(192.1234, wrapper.getAngle(), DOUBLE_EPSILON);
        Assert.assertEquals(192.1234, gyro.getAngle(), DOUBLE_EPSILON);

        wrapper.setAngle(359.9999);
        Assert.assertEquals(359.9999, wrapper.getAngle(), DOUBLE_EPSILON);
        Assert.assertEquals(359.9999, gyro.getAngle(), DOUBLE_EPSILON);

        wrapper.setAngle(-421.3358);
        Assert.assertEquals(-421.3358, wrapper.getAngle(), DOUBLE_EPSILON);
        Assert.assertEquals(-421.3358, gyro.getAngle(), DOUBLE_EPSILON);

        System.out.println(wrapper);
    }
}
