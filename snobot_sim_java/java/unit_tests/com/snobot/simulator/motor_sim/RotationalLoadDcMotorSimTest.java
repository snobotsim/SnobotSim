package com.snobot.simulator.motor_sim;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.snobot.simulator.DcMotorModelConfig;
import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;
import com.snobot.test.utilities.BaseSimulatorTest;

import edu.wpi.first.wpilibj.Talon;

public class RotationalLoadDcMotorSimTest extends BaseSimulatorTest
{
    @Test
    public void testMotor() throws IOException
    {
        double dt = .0001;

        double armCenterOfMass = .82;  // m
        double armMass = .2;  // kg

        new Talon(0);
        DcMotorModelConfig motorConfig = DataAccessorFactory.getInstance().getSimulatorDataAccessor().createMotor("CIM");
        Assert.assertTrue(DataAccessorFactory.getInstance().getSimulatorDataAccessor().setSpeedControllerModel_Rotational(0, motorConfig, armCenterOfMass, armMass));

        // PwmWrapper wrapper = new PwmWrapper(0);
        //
        // DcMotorModel motor =
        // MakeTransmission.makeTransmission(PublishedMotorFactory.makeRS775(),
        // 2, 77.0, .8);
        // IMotorSimulator motorSim = new RotationalLoadDcMotorSim(motor,
        // wrapper, armCenterOfMass, armMass, constantAssistTorque,
        // overCenterAssistTorque);
        // wrapper.setMotorSimulator(motorSim);
        //
        // BufferedWriter bw = new BufferedWriter(new FileWriter("test.txt"));
        //
        // for (int i = 0; i < 1136; ++i)
        // {
        // wrapper.set(1);
        // bw.write(i * dt + ", " + motor.getPosition() + ", " +
        // motor.getVelocity() + ", " + motor.getCurrent() + ", " + "\n");
        // }
        //
        // bw.close();
    }

    @Test
    public void testInvalidMotor()
    {
        DcMotorModelConfig motorConfig = DataAccessorFactory.getInstance().getSimulatorDataAccessor().createMotor("CIM");
        Assert.assertFalse(DataAccessorFactory.getInstance().getSimulatorDataAccessor().setSpeedControllerModel_Gravitational(0, motorConfig, 0));
    }

}
