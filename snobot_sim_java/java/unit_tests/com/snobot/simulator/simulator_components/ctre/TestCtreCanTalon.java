package com.snobot.simulator.simulator_components.ctre;

import org.junit.Assert;
import org.junit.Test;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;
import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;
import com.snobot.test.utilities.BaseSimulatorTest;


public class TestCtreCanTalon extends BaseSimulatorTest
{

    @Test
    public void testSimpleSetters()
    {
        CANTalon talon = new CANTalon(0);
        talon.changeControlMode(TalonControlMode.Position);

        // PID stuff
        {
            talon.setP(.5);
            talon.setI(1.5);
            talon.setD(100.7890);
            talon.setF(-9.485);

            Assert.assertEquals(.5, talon.getP(), DOUBLE_EPSILON);
            Assert.assertEquals(1.5, talon.getI(), DOUBLE_EPSILON);
            Assert.assertEquals(100.7890, talon.getD(), DOUBLE_EPSILON);
            Assert.assertEquals(-9.485, talon.getF(), DOUBLE_EPSILON);
        }

        Assert.assertEquals(30, talon.getTemperature(), DOUBLE_EPSILON);
        Assert.assertEquals(12, talon.getBusVoltage(), DOUBLE_EPSILON);

        // System.out.println("\n\n");
        // System.out.println("\n\n");
        // double kp = talon.getP();
        // double ki = talon.getI();
        // double kd = talon.getD();
        // double kf = talon.getF();
    }

    @Test
    public void testSetWithAppliedThrottle()
    {
        double DOUBLE_EPSILON = 1.0 / 1023;

        for (int i = 0; i < 64; ++i)
        // int i = 1;
        {
            CANTalon canTalon1 = new CANTalon(i);

            Assert.assertEquals(0, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVoltagePercentage(i + 100), DOUBLE_EPSILON);

            canTalon1.set(-1.0);
            Assert.assertEquals(-1.0, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVoltagePercentage(i + 100), DOUBLE_EPSILON);
            Assert.assertEquals(-1.0, canTalon1.get(), DOUBLE_EPSILON);

            canTalon1.set(-0.5);
            Assert.assertEquals(-0.5, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVoltagePercentage(i + 100), DOUBLE_EPSILON);
            Assert.assertEquals(-0.5, canTalon1.get(), DOUBLE_EPSILON);

            canTalon1.set(-0.1);
            Assert.assertEquals(-0.1, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVoltagePercentage(i + 100), DOUBLE_EPSILON);
            Assert.assertEquals(-0.1, canTalon1.get(), DOUBLE_EPSILON);

            canTalon1.set(0);
            Assert.assertEquals(0.0, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVoltagePercentage(i + 100), DOUBLE_EPSILON);
            Assert.assertEquals(0.0, canTalon1.get(), DOUBLE_EPSILON);

            canTalon1.set(.1);
            Assert.assertEquals(0.1, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVoltagePercentage(i + 100), DOUBLE_EPSILON);
            Assert.assertEquals(0.1, canTalon1.get(), DOUBLE_EPSILON);

            canTalon1.set(.5);
            Assert.assertEquals(0.5, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVoltagePercentage(i + 100), DOUBLE_EPSILON);
            Assert.assertEquals(0.5, canTalon1.get(), DOUBLE_EPSILON);

            canTalon1.set(1);
            Assert.assertEquals(1.0, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVoltagePercentage(i + 100), DOUBLE_EPSILON);
            Assert.assertEquals(1.0, canTalon1.get(), DOUBLE_EPSILON);
        }
    }

    @Test
    public void testSetWithPosition()
    {
        int i = 0;
        CANTalon talon = new CANTalon(i);
        talon.changeControlMode(TalonControlMode.Position);
        // talon.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
        // talon.setP(.5);
        // talon.setI(1.5);
        // talon.setD(100.7890);
        // talon.setF(-9.485);
    }

    @Test
    public void testSetWithVoltage()
    {
        double DOUBLE_EPSILON = .01;

        int i = 0;
        CANTalon talon = new CANTalon(i);
        talon.changeControlMode(TalonControlMode.Voltage);

        talon.set(10.8);
        Assert.assertEquals(0.9, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVoltagePercentage(i + 100), DOUBLE_EPSILON);
        Assert.assertEquals(10.8, talon.get(), DOUBLE_EPSILON);

        talon.set(-6.0);
        Assert.assertEquals(-0.5, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVoltagePercentage(i + 100), DOUBLE_EPSILON);
        Assert.assertEquals(-6.0, talon.get(), DOUBLE_EPSILON);

        talon.set(3.0);
        Assert.assertEquals(0.25, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVoltagePercentage(i + 100), DOUBLE_EPSILON);
        Assert.assertEquals(3.0, talon.get(), DOUBLE_EPSILON);
    }
}
