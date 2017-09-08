package com.snobot.simulator.motor_sim;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;

import com.snobot.simulator.DcMotorModelConfig;
import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;
import com.snobot.test.utilities.BaseSimulatorTest;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;

public class StaticLoadDcMotorSimTest extends BaseSimulatorTest
{
    @Test
    public void testRS775_6VSmallLoad()
    {
        SpeedController rs775 = new Talon(0);
        DcMotorModelConfig motorConfig = DataAccessorFactory.getInstance().getSimulatorDataAccessor().createMotor("rs775", 1, 10, 1);
        DataAccessorFactory.getInstance().getSimulatorDataAccessor().setSpeedControllerModel_Static(0, motorConfig, .01);

        simulateForTime(10, 0.01, () ->
        {
            rs775.set(.5);
        });

        // We expect negligible final current, and a final velocity of ~68.04
        // rad/sec.
        assertEquals(DataAccessorFactory.getInstance().getSpeedControllerAccessor().getCurrent(0), 0.0, 1E-3);
        assertEquals(DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVelocity(0), 68.06, 1E-2);

    }

    @Test
    public void testRS775_12VSmallLoad()
    {
        SpeedController rs775 = new Talon(0);
        DcMotorModelConfig motorConfig = DataAccessorFactory.getInstance().getSimulatorDataAccessor().createMotor("rs775", 1, 10, 1);
        DataAccessorFactory.getInstance().getSimulatorDataAccessor().setSpeedControllerModel_Static(0, motorConfig, .01);

        // Apply a larger voltage.
        simulateForTime(10, 0.01, () ->
        {
            rs775.set(1);
        });

        // We expect negligible final current, and a final velocity of ~2 *
        // 68.04 rad/sec.
        assertEquals(DataAccessorFactory.getInstance().getSpeedControllerAccessor().getCurrent(0), 0.0, 1E-3);
        assertEquals(DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVelocity(0), 68.04 * 2, 1E-1);

    }

    @Test
    public void testRS775_12VLargeLoad()
    {
        SpeedController rs775 = new Talon(0);
        DcMotorModelConfig motorConfig = DataAccessorFactory.getInstance().getSimulatorDataAccessor().createMotor("rs775", 1, 10, 1);
        DataAccessorFactory.getInstance().getSimulatorDataAccessor().setSpeedControllerModel_Static(0, motorConfig, 1);

        simulateForTime(10, 0.01, () ->
        {
            rs775.set(1);
        });

        // This is slower, so 1000 iterations isn't enough to get to steady
        // state
        assertEquals(DataAccessorFactory.getInstance().getSpeedControllerAccessor().getCurrent(0), 48.758, 1E-3);
        assertEquals(DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVelocity(0), 59.59, 1E-1);
    }

    @Test
    public void testDoubleRS775_100Efficiency_12VLargeLoad()
    {
        SpeedController rs775 = new Talon(0);
        DcMotorModelConfig motorConfig = DataAccessorFactory.getInstance().getSimulatorDataAccessor().createMotor("rs775", 2, 10, 1);
        DataAccessorFactory.getInstance().getSimulatorDataAccessor().setSpeedControllerModel_Static(0, motorConfig, 1);

        simulateForTime(10, 0.01, () ->
        {
            rs775.set(1);
        });

        // We expect the two motor version to move faster than the single motor
        // version.
        assertEquals(DataAccessorFactory.getInstance().getSpeedControllerAccessor().getCurrent(0), 17.378, 1E-3);
        assertEquals(DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVelocity(0), 122.517, 1E-1);
    }

    @Test
    public void testDoubleRS775_80Efficiency_12VLargeLoad()
    {
        SpeedController rs775 = new Talon(0);
        DcMotorModelConfig motorConfig = DataAccessorFactory.getInstance().getSimulatorDataAccessor().createMotor("rs775", 2, 10, .8);
        DataAccessorFactory.getInstance().getSimulatorDataAccessor().setSpeedControllerModel_Static(0, motorConfig, 1);

        // Make it less efficient.
        simulateForTime(10, 0.01, () ->
        {
            rs775.set(1);
        });

        assertEquals(DataAccessorFactory.getInstance().getSpeedControllerAccessor().getCurrent(0), 27.540, 1E-3);
        assertEquals(DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVelocity(0), 114.545, 1E-1);
    }

    @Test
    public void testRS775_Neg12VSmallLoad()
    {
        SpeedController rs775 = new Talon(0);
        DcMotorModelConfig motorConfig = DataAccessorFactory.getInstance().getSimulatorDataAccessor().createMotor("rs775", 1, 10, 1);
        DataAccessorFactory.getInstance().getSimulatorDataAccessor().setSpeedControllerModel_Static(0, motorConfig, 1);

        // Go in reverse.
        simulateForTime(10, 0.01, () ->
        {
            rs775.set(-1);
        });
        assertEquals(DataAccessorFactory.getInstance().getSpeedControllerAccessor().getCurrent(0), 48.758, 1E-3);
        assertEquals(DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVelocity(0), -59.590, 1E-1);
        Assert.assertTrue(DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPosition(0) < 0);

    }

    @Test
    public void testInverted()
    {
        SpeedController rs775 = new Talon(0);
        DcMotorModelConfig motorConfig = DataAccessorFactory.getInstance().getSimulatorDataAccessor().createMotor("rs775", 1, 10, 1);
        motorConfig.setInverted(true);
        DataAccessorFactory.getInstance().getSimulatorDataAccessor().setSpeedControllerModel_Static(0, motorConfig, 1);

        // Go in reverse.
        simulateForTime(10, 0.01, () ->
        {
            rs775.set(-1);
        });
        assertEquals(DataAccessorFactory.getInstance().getSpeedControllerAccessor().getCurrent(0), 48.758, 1E-3);
        assertEquals(DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVelocity(0), 59.590, 1E-1);
        Assert.assertTrue(DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPosition(0) > 0);
    }

    @Test
    public void testBrake()
    {
        SpeedController rs775 = new Talon(0);
        DcMotorModelConfig motorConfig = DataAccessorFactory.getInstance().getSimulatorDataAccessor().createMotor("rs775", 1, 10, 1);
        motorConfig.setHasBrake(true);
        DataAccessorFactory.getInstance().getSimulatorDataAccessor().setSpeedControllerModel_Static(0, motorConfig, 1);

        // Go in reverse.
        simulateForTime(2, 0.01, () ->
        {
            rs775.set(-1);
        });
        assertEquals(DataAccessorFactory.getInstance().getSpeedControllerAccessor().getCurrent(0), 77.284, 1E-3);
        assertEquals(DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPosition(0), -15.23, 1E-1);
        assertEquals(DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVelocity(0), -14.807, 1E-1);
        assertEquals(DataAccessorFactory.getInstance().getSpeedControllerAccessor().getAcceleration(0), -6.987, 1E-1);

        // Stop sending data, motor should immediatly stop
        simulateForTime(.01, 0.01, () ->
        {
            rs775.set(0);
        });
        assertEquals(DataAccessorFactory.getInstance().getSpeedControllerAccessor().getCurrent(0), 0, 1E-3);
        assertEquals(DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPosition(0), -15.23, 1E-1);
        assertEquals(DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVelocity(0), 0, 1E-1);
        assertEquals(DataAccessorFactory.getInstance().getSpeedControllerAccessor().getAcceleration(0), 0, 1E-1);
    }

    @Test
    public void testInvalidMotor()
    {
        DcMotorModelConfig motorConfig = DataAccessorFactory.getInstance().getSimulatorDataAccessor().createMotor("CIM");
        Assert.assertFalse(DataAccessorFactory.getInstance().getSimulatorDataAccessor().setSpeedControllerModel_Static(0, motorConfig, 0));
    }
}
