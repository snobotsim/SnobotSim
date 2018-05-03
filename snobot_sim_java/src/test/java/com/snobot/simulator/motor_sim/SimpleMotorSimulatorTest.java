package com.snobot.simulator.motor_sim;

import org.junit.Assert;
import org.junit.Test;

import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;
import com.snobot.simulator.wrapper_accessors.SpeedControllerWrapperAccessor.MotorSimType;
import com.snobot.test.utilities.BaseSimulatorJavaTest;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;

public class SimpleMotorSimulatorTest extends BaseSimulatorJavaTest
{
    @Test
    public void testSimpleSimulator()
    {
        SpeedController sc = new Talon(0);
        Assert.assertTrue(
                DataAccessorFactory.getInstance().getSimulatorDataAccessor().setSpeedControllerModel_Simple(0, new SimpleMotorSimulationConfig(7.8)));

        simulateForTime(5, () ->
        {
            sc.set(.5);
        });
        Assert.assertEquals(.5, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVoltagePercentage(0), DOUBLE_EPSILON);
        Assert.assertEquals(19.5, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPosition(0), DOUBLE_EPSILON);

        simulateForTime(3, () ->
        {
            sc.set(-1);
        });
        Assert.assertEquals(-1, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVoltagePercentage(0), DOUBLE_EPSILON);
        Assert.assertEquals(-3.9, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPosition(0), DOUBLE_EPSILON);

        Assert.assertEquals(-3.9, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPosition(0), DOUBLE_EPSILON);
        Assert.assertEquals(-7.8, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVelocity(0), DOUBLE_EPSILON);
        Assert.assertEquals(0, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getCurrent(0), DOUBLE_EPSILON);
        Assert.assertEquals(0, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getAcceleration(0), DOUBLE_EPSILON);

        DataAccessorFactory.getInstance().getSpeedControllerAccessor().reset(0);
        Assert.assertEquals(0, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPosition(0), DOUBLE_EPSILON);
        Assert.assertEquals(0, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVelocity(0), DOUBLE_EPSILON);
        Assert.assertEquals(0, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getCurrent(0), DOUBLE_EPSILON);
        Assert.assertEquals(0, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getAcceleration(0), DOUBLE_EPSILON);

        DataAccessorFactory.getInstance().getSpeedControllerAccessor().reset(0, 4, 6, 10);
        Assert.assertEquals(4, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPosition(0), DOUBLE_EPSILON);
        Assert.assertEquals(6, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVelocity(0), DOUBLE_EPSILON);
        Assert.assertEquals(0, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getCurrent(0), DOUBLE_EPSILON);
        Assert.assertEquals(0, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getAcceleration(0), DOUBLE_EPSILON);

        Assert.assertEquals(MotorSimType.Simple, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getMotorSimType(0));
        SimpleMotorSimulationConfig simConfig = DataAccessorFactory.getInstance().getSpeedControllerAccessor().getMotorSimSimpleModelConfig(0);
        Assert.assertEquals(7.8, simConfig.mMaxSpeed, DOUBLE_EPSILON);
    }

    @Test
    public void testBadSpeedController()
    {
        Assert.assertFalse(
                DataAccessorFactory.getInstance().getSimulatorDataAccessor().setSpeedControllerModel_Simple(0, new SimpleMotorSimulationConfig(7.8)));

        new Talon(0);
        Assert.assertTrue(
                DataAccessorFactory.getInstance().getSimulatorDataAccessor().setSpeedControllerModel_Simple(0, new SimpleMotorSimulationConfig(10)));

        DcMotorModelConfig createdConfig = DataAccessorFactory.getInstance().getSpeedControllerAccessor().getMotorConfig(0);
        Assert.assertNull(createdConfig);
    }
}
