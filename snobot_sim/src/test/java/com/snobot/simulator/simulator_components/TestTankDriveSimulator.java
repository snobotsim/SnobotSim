package com.snobot.simulator.simulator_components;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import com.snobot.simulator.motor_sim.SimpleMotorSimulationConfig;
import com.snobot.simulator.simulator_components.config.TankDriveConfig;
import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;
import com.snobot.test.utilities.BaseSimulatorJniTest;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.interfaces.Gyro;

public class TestTankDriveSimulator extends BaseSimulatorJniTest
{
    @Test
    public void testTankDrive()
    {
        final SpeedController rightSC = new Talon(0);
        final SpeedController leftSC = new Talon(1);
        final Gyro gyro = new AnalogGyro(0);

        Assertions.assertTrue(DataAccessorFactory.getInstance().getSimulatorDataAccessor().setSpeedControllerModel_Simple(0, new SimpleMotorSimulationConfig(1)));
        Assertions.assertTrue(DataAccessorFactory.getInstance().getSimulatorDataAccessor().setSpeedControllerModel_Simple(1, new SimpleMotorSimulationConfig(1)));
        Assertions.assertTrue(DataAccessorFactory.getInstance().getSimulatorDataAccessor().connectTankDriveSimulator(1, 0, 0, 180 / Math.PI));

        // Turn Left
        simulateForTime(90, () ->
        {
            rightSC.set(1);
            leftSC.set(-1);
        });
        Assertions.assertEquals(-180, gyro.getAngle(), DOUBLE_EPSILON);
        Assertions.assertEquals(-180, DataAccessorFactory.getInstance().getGyroAccessor().getWrapper(0).getAngle(), DOUBLE_EPSILON);

        // Turn right
        simulateForTime(45, () ->
        {
            rightSC.set(-1);
            leftSC.set(1);
        });
        Assertions.assertEquals(-90, gyro.getAngle(), DOUBLE_EPSILON);
        Assertions.assertEquals(-90, DataAccessorFactory.getInstance().getGyroAccessor().getWrapper(0).getAngle(), DOUBLE_EPSILON);
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
        new Talon(6);
        DataAccessorFactory.getInstance().getSimulatorDataAccessor().connectTankDriveSimulator(0, 1, 0, 180 / Math.PI);

        // First two things not null
        DataAccessorFactory.getInstance().getSimulatorDataAccessor().reset();
        new Talon(3);
        new Talon(1);
        DataAccessorFactory.getInstance().getSimulatorDataAccessor().connectTankDriveSimulator(0, 1, 0, 180 / Math.PI);

        // Third thing not null
        DataAccessorFactory.getInstance().getSimulatorDataAccessor().reset();
        new AnalogGyro(0);
        DataAccessorFactory.getInstance().getSimulatorDataAccessor().connectTankDriveSimulator(0, 1, 0, 180 / Math.PI);
    }
}
