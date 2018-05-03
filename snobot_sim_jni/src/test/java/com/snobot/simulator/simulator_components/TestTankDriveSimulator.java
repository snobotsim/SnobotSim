package com.snobot.simulator.simulator_components;

import org.junit.Assert;
import org.junit.Test;

import com.snobot.simulator.motor_sim.SimpleMotorSimulationConfig;
import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;
import com.snobot.test.utilities.BaseSimulatorTest;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.interfaces.Gyro;

public class TestTankDriveSimulator extends BaseSimulatorTest
{
    @Test
    public void testTankDrive()
    {
        final SpeedController rightSC = new Talon(0);
        final SpeedController leftSC = new Talon(1);
        final Encoder rightEnc = new Encoder(0, 1);
        final Encoder leftEnc = new Encoder(2, 3);
        final Gyro gyro = new AnalogGyro(0);

        Assert.assertTrue(DataAccessorFactory.getInstance().getEncoderAccessor().connectSpeedController(0, 0));
        Assert.assertTrue(DataAccessorFactory.getInstance().getEncoderAccessor().connectSpeedController(1, 1));
        Assert.assertTrue(DataAccessorFactory.getInstance().getSimulatorDataAccessor().setSpeedControllerModel_Simple(0, new SimpleMotorSimulationConfig(1)));
        Assert.assertTrue(DataAccessorFactory.getInstance().getSimulatorDataAccessor().setSpeedControllerModel_Simple(1, new SimpleMotorSimulationConfig(1)));
        Assert.assertTrue(DataAccessorFactory.getInstance().getSimulatorDataAccessor().connectTankDriveSimulator(1, 0, 0, 180 / Math.PI));

        // Turn Left
        simulateForTime(90, () ->
        {
            rightSC.set(1);
            leftSC.set(-1);
        });
        Assert.assertEquals(-180, gyro.getAngle(), DOUBLE_EPSILON);
        Assert.assertEquals(89, rightEnc.getDistance(), DOUBLE_EPSILON);
        Assert.assertEquals(-89, leftEnc.getDistance(), DOUBLE_EPSILON);
        Assert.assertEquals(-180, DataAccessorFactory.getInstance().getGyroAccessor().getAngle(0), DOUBLE_EPSILON);
        Assert.assertEquals(90, DataAccessorFactory.getInstance().getEncoderAccessor().getDistance(0), DOUBLE_EPSILON);
        Assert.assertEquals(-90, DataAccessorFactory.getInstance().getEncoderAccessor().getDistance(1), DOUBLE_EPSILON);

        // Turn right
        simulateForTime(45, () ->
        {
            rightSC.set(-1);
            leftSC.set(1);
        });
        Assert.assertEquals(-90, gyro.getAngle(), DOUBLE_EPSILON);
        Assert.assertEquals(45, rightEnc.getDistance(), DOUBLE_EPSILON);
        Assert.assertEquals(-45, leftEnc.getDistance(), DOUBLE_EPSILON);
        Assert.assertEquals(-90, DataAccessorFactory.getInstance().getGyroAccessor().getAngle(0), DOUBLE_EPSILON);
        Assert.assertEquals(45, DataAccessorFactory.getInstance().getEncoderAccessor().getDistance(0), DOUBLE_EPSILON);
        Assert.assertEquals(-45, DataAccessorFactory.getInstance().getEncoderAccessor().getDistance(1), DOUBLE_EPSILON);
    }

    @Test
    public void testInvalidSetup()
    {
        // Everything null
        DataAccessorFactory.getInstance().getSimulatorDataAccessor().reset();
        DataAccessorFactory.getInstance().getSimulatorDataAccessor().connectTankDriveSimulator(0, 1, 0, 180 / Math.PI);
        DataAccessorFactory.getInstance().getSimulatorDataAccessor().updateSimulatorComponents(0); // shouldn't do anything

        // First thing not null
        DataAccessorFactory.getInstance().getSimulatorDataAccessor().reset();
        new Encoder(0, 1);
        DataAccessorFactory.getInstance().getSimulatorDataAccessor().connectTankDriveSimulator(0, 1, 0, 180 / Math.PI);

        // First two things not null
        DataAccessorFactory.getInstance().getSimulatorDataAccessor().reset();
        new Encoder(0, 1);
        new Encoder(2, 3);
        DataAccessorFactory.getInstance().getSimulatorDataAccessor().connectTankDriveSimulator(0, 1, 0, 180 / Math.PI);

        // Third thing not null
        DataAccessorFactory.getInstance().getSimulatorDataAccessor().reset();
        new AnalogGyro(0);
        DataAccessorFactory.getInstance().getSimulatorDataAccessor().connectTankDriveSimulator(0, 1, 0, 180 / Math.PI);
    }
}
