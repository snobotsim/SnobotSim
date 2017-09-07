package com.snobot.simulator.motor_sim;

import org.junit.Test;

import com.snobot.simulator.module_wrapper.PwmWrapper;
import com.snobot.simulator.motor_sim.motor_factory.MakeTransmission;
import com.snobot.simulator.motor_sim.motor_factory.PublishedMotorFactory;

public class GravityLoadDcMotorSimTest
{

    private DcMotorModel getSingle775WithTransmission(int numMotors, double effiecency)
    {
        return MakeTransmission.makeTransmission(PublishedMotorFactory.makeRS775(), numMotors, 10.0, effiecency);
    }

    @Test
    public void testMotor()
    {
        int motors = 1;
        double efficiency = 1;
        double load = .01;

        PwmWrapper wrapper = new PwmWrapper(0);
        IMotorSimulator motorSim = new GravityLoadDcMotorSim(getSingle775WithTransmission(motors, efficiency), load);
        wrapper.setMotorSimulator(motorSim);

        System.out.println(motorSim);
    }

}
