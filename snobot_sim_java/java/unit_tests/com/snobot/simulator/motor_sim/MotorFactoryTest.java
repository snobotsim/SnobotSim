package com.snobot.simulator.motor_sim;

import org.junit.Assert;
import org.junit.Test;

import com.snobot.simulator.DcMotorModelConfig;
import com.snobot.simulator.motor_sim.motor_factory.VexMotorFactory;
import com.snobot.test.utilities.BaseSimulatorTest;

public class MotorFactoryTest extends BaseSimulatorTest
{

    @Test
    public void testInvalidMotorName()
    {
        DcMotorModelConfig config = VexMotorFactory.createMotor("DoesNotExist");

        Assert.assertEquals(0, config.mMotorParams.FREE_CURRENT, DOUBLE_EPSILON);
        Assert.assertEquals(0, config.mMotorParams.FREE_SPEED_RPM, DOUBLE_EPSILON);
        Assert.assertEquals(0, config.mMotorParams.FREE_CURRENT, DOUBLE_EPSILON);
        Assert.assertEquals(0, config.mMotorParams.STALL_TORQUE, DOUBLE_EPSILON);
        Assert.assertEquals(0, config.mMotorParams.STALL_CURRENT, DOUBLE_EPSILON);
        Assert.assertEquals(0, config.mMotorParams.FREE_CURRENT, DOUBLE_EPSILON);
        Assert.assertEquals(0, config.mMotorParams.MOTOR_INERTIA, DOUBLE_EPSILON);
    }
}
