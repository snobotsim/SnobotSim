package com.snobot.simulator.simulator_components;

import org.junit.Assert;
import org.junit.Test;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.module_wrapper.AnalogWrapper;
import com.snobot.simulator.module_wrapper.PwmWrapper;
import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;
import com.snobot.test.utilities.BaseSimulatorTest;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.interfaces.Potentiometer;

public class TestPotentiometerSimulator extends BaseSimulatorTest
{
    @Test
    public void testPotentiometer()
    {
        Potentiometer potentiometer = new AnalogPotentiometer(0);
        SpeedController sc = new Talon(0);

        AnalogWrapper analogWrapper = SensorActuatorRegistry.get().getAnalog().get(0);
        PwmWrapper pwmWrapper = SensorActuatorRegistry.get().getSpeedControllers().get(0);
        DataAccessorFactory.getInstance().getSimulatorDataAccessor().setSpeedControllerModel_Simple(0, 1);

        PotentiometerSimulator potSim = new PotentiometerSimulator(analogWrapper, pwmWrapper);
        Assert.assertTrue(potSim.isSetup());
        potSim.setParameters(5, 0, 5);

        double update_rate = 50;

        // Turn Left
        for (int i = 0; i < update_rate * 5; ++i)
        {
            sc.set(1);

            DataAccessorFactory.getInstance().getSimulatorDataAccessor().updateLoop();
        }

        Assert.assertEquals(1, potentiometer.get(), DOUBLE_EPSILON);
        Assert.assertEquals(5, pwmWrapper.getPosition(), DOUBLE_EPSILON);
    }

    @Test
    public void testIncompletePotSim()
    {
        PotentiometerSimulator pot = new PotentiometerSimulator(null, null);
        Assert.assertFalse(pot.isSetup());
    }
}
