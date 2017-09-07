package com.snobot.simulator.motor_sim;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Test;

import com.snobot.simulator.module_wrapper.PwmWrapper;
import com.snobot.simulator.motor_sim.motor_factory.MakeTransmission;
import com.snobot.simulator.motor_sim.motor_factory.PublishedMotorFactory;

public class RotationalLoadDcMotorSimTest
{
    @Test
    public void testMotor() throws IOException
    {
        double dt = .0001;

        double armCenterOfMass = .82;  // m
        double armMass = .2;  // kg
        double constantAssistTorque = 0.0;  // N*m
        double overCenterAssistTorque = 0.0;  // N*m

        PwmWrapper wrapper = new PwmWrapper(0);

        DcMotorModel motor = MakeTransmission.makeTransmission(PublishedMotorFactory.makeRS775(), 2, 77.0, .8);
        IMotorSimulator motorSim = new RotationalLoadDcMotorSim(motor, wrapper, armCenterOfMass, armMass, constantAssistTorque, overCenterAssistTorque);
        wrapper.setMotorSimulator(motorSim);

        BufferedWriter bw = new BufferedWriter(new FileWriter("test.txt"));

        for (int i = 0; i < 1136; ++i)
        {
            wrapper.set(1);
            bw.write(i * dt + ", " + motor.getPosition() + ", " + motor.getVelocity() + ", " + motor.getCurrent() + ", " + "\n");
        }

        bw.close();
    }

}
