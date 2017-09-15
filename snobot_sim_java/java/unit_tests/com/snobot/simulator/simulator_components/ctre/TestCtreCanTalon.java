package com.snobot.simulator.simulator_components.ctre;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;
import com.snobot.simulator.motor_sim.DcMotorModelConfig;
import com.snobot.simulator.motor_sim.StaticLoadMotorSimulationConfig;
import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;
import com.snobot.test.utilities.BaseSimulatorTest;


@RunWith(value = Parameterized.class)
public class TestCtreCanTalon extends BaseSimulatorTest
{
    private final int mCanHandle;
    private final int mRawHandle;

    @Parameters()
    public static Collection<Integer> data()
    {
        Collection<Integer> output = new ArrayList<>();

        for (int i = 0; i < 64; ++i)
        {
            output.add(i);
        }

        return output;
    }

    public TestCtreCanTalon(int aCanHandle)
    {
        mCanHandle = aCanHandle;
        mRawHandle = mCanHandle + 100;
    }

    @Test
    public void testSimpleSetters()
    {
        Assert.assertEquals(0, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPortList().size());

        CANTalon talon = new CANTalon(mCanHandle);
        Assert.assertEquals(1, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPortList().size());

        // PID stuff
        {
            talon.setP(.5);
            talon.setI(1.5);
            talon.setD(100.7890);
            talon.setF(-9.485);
            talon.setIZone(40);

            Assert.assertEquals(.5, talon.getP(), DOUBLE_EPSILON);
            Assert.assertEquals(1.5, talon.getI(), DOUBLE_EPSILON);
            Assert.assertEquals(100.7890, talon.getD(), DOUBLE_EPSILON);
            Assert.assertEquals(-9.485, talon.getF(), DOUBLE_EPSILON);
            Assert.assertEquals(40, talon.getIZone(), DOUBLE_EPSILON);
        }

        Assert.assertEquals(30, talon.getTemperature(), DOUBLE_EPSILON);
        Assert.assertEquals(12, talon.getBusVoltage(), DOUBLE_EPSILON);
    }

    @Test
    public void testSetWithAppliedThrottle()
    {
        double DOUBLE_EPSILON = 1.0 / 1023;

        Assert.assertEquals(0, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPortList().size());
        CANTalon canTalon1 = new CANTalon(mCanHandle);
        Assert.assertEquals(1, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPortList().size());

        Assert.assertEquals(0, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVoltagePercentage(mRawHandle), DOUBLE_EPSILON);

        canTalon1.set(-1.0);
        Assert.assertEquals(-1.0, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVoltagePercentage(mRawHandle), DOUBLE_EPSILON);
        Assert.assertEquals(-1.0, canTalon1.get(), DOUBLE_EPSILON);

        canTalon1.set(-0.5);
        Assert.assertEquals(-0.5, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVoltagePercentage(mRawHandle), DOUBLE_EPSILON);
        Assert.assertEquals(-0.5, canTalon1.get(), DOUBLE_EPSILON);

        canTalon1.set(-0.1);
        Assert.assertEquals(-0.1, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVoltagePercentage(mRawHandle), DOUBLE_EPSILON);
        Assert.assertEquals(-0.1, canTalon1.get(), DOUBLE_EPSILON);

        canTalon1.set(0);
        Assert.assertEquals(0.0, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVoltagePercentage(mRawHandle), DOUBLE_EPSILON);
        Assert.assertEquals(0.0, canTalon1.get(), DOUBLE_EPSILON);

        canTalon1.set(.1);
        Assert.assertEquals(0.1, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVoltagePercentage(mRawHandle), DOUBLE_EPSILON);
        Assert.assertEquals(0.1, canTalon1.get(), DOUBLE_EPSILON);

        canTalon1.set(.5);
        Assert.assertEquals(0.5, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVoltagePercentage(mRawHandle), DOUBLE_EPSILON);
        Assert.assertEquals(0.5, canTalon1.get(), DOUBLE_EPSILON);

        canTalon1.set(1);
        Assert.assertEquals(1.0, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVoltagePercentage(mRawHandle), DOUBLE_EPSILON);
        Assert.assertEquals(1.0, canTalon1.get(), DOUBLE_EPSILON);
    }

    @Test
    public void testSetWithPositionEncoder()
    {
        Assert.assertEquals(0, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPortList().size());
        CANTalon talon = new CANTalon(mCanHandle);
        Assert.assertEquals(1, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPortList().size());

        talon.changeControlMode(TalonControlMode.Position);
        talon.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);

        // Simulate CIM drivetrain
        DcMotorModelConfig motorConfig = DataAccessorFactory.getInstance().getSimulatorDataAccessor().createMotor("CIM", 1, 10, 1);
        Assert.assertTrue(DataAccessorFactory.getInstance().getSimulatorDataAccessor().setSpeedControllerModel_Static(mRawHandle, motorConfig,
                new StaticLoadMotorSimulationConfig(0.01)));

        talon.setP(.11);
        talon.setI(.005);
        talon.setIZone(2);

        talon.set(36);

        simulateForTime(1, () ->
        {
        });

        Assert.assertEquals(36, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPosition(mRawHandle), .05);
        System.out.println(talon.getEncPosition());
        System.out.println(talon.getEncVelocity());
    }

    @Test
    public void testSetWithPositionAnalog()
    {
        Assert.assertEquals(0, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPortList().size());
        CANTalon talon = new CANTalon(mCanHandle);
        Assert.assertEquals(1, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPortList().size());

        talon.changeControlMode(TalonControlMode.Position);
        talon.setFeedbackDevice(FeedbackDevice.AnalogPot);

        System.out.println(talon.getAnalogInPosition());
    }

    @Test
    public void testSetWithSpeedEncoder()
    {
        Assert.assertEquals(0, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPortList().size());
        CANTalon talon = new CANTalon(mCanHandle);
        Assert.assertEquals(1, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPortList().size());

        talon.changeControlMode(TalonControlMode.Speed);
        talon.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);

        // Simulate CIM drivetrain
        DcMotorModelConfig motorConfig = DataAccessorFactory.getInstance().getSimulatorDataAccessor().createMotor("CIM", 1, 10, 1);
        Assert.assertTrue(DataAccessorFactory.getInstance().getSimulatorDataAccessor().setSpeedControllerModel_Static(mRawHandle, motorConfig,
                new StaticLoadMotorSimulationConfig(.2)));

        talon.setP(.045);
        talon.setF(0.018);
        talon.setIZone(1);

        // 55.8 max velocity
        talon.set(40);

        simulateForTime(1, () ->
        {
        });

        Assert.assertEquals(40, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVelocity(mRawHandle), .05);


        System.out.println(talon.getEncPosition());
        System.out.println(talon.getEncVelocity());
    }

    @Test
    public void testSetWithSpeedAnalog()
    {
        Assert.assertEquals(0, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPortList().size());
        CANTalon talon = new CANTalon(mCanHandle);
        Assert.assertEquals(1, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPortList().size());

        talon.changeControlMode(TalonControlMode.Speed);
        talon.setFeedbackDevice(FeedbackDevice.AnalogPot);

        System.out.println(talon.getAnalogInVelocity());

    }

    @Test
    public void testSetWithCurrent()
    {

        Assert.assertEquals(0, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPortList().size());
        CANTalon talon = new CANTalon(mCanHandle);
        Assert.assertEquals(1, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPortList().size());

        talon.changeControlMode(TalonControlMode.Current);
        talon.set(5);
    }

    @Test
    public void testSetWithVoltage()
    {
        double DOUBLE_EPSILON = .01;

        Assert.assertEquals(0, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPortList().size());
        CANTalon talon = new CANTalon(mCanHandle);
        Assert.assertEquals(1, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPortList().size());

        talon.changeControlMode(TalonControlMode.Voltage);

        talon.set(10.8);
        Assert.assertEquals(0.9, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVoltagePercentage(mRawHandle), DOUBLE_EPSILON);
        Assert.assertEquals(10.8, talon.get(), DOUBLE_EPSILON);

        talon.set(-6.0);
        Assert.assertEquals(-0.5, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVoltagePercentage(mRawHandle), DOUBLE_EPSILON);
        Assert.assertEquals(-6.0, talon.get(), DOUBLE_EPSILON);

        talon.set(3.0);
        Assert.assertEquals(0.25, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVoltagePercentage(mRawHandle), DOUBLE_EPSILON);
        Assert.assertEquals(3.0, talon.get(), DOUBLE_EPSILON);
    }

    @Test
    public void testSetWithFollower()
    {
        int leadTalonId = 5;
        if (mCanHandle == leadTalonId)
        {
            return;
        }

        Assert.assertEquals(0, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPortList().size());
        CANTalon leadTalon = new CANTalon(leadTalonId);
        CANTalon talon = new CANTalon(mCanHandle);
        Assert.assertEquals(2, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPortList().size());

        talon.changeControlMode(TalonControlMode.Follower);
        talon.set(leadTalonId);

        leadTalon.set(.5);
    }

    @Test
    public void testSetWithMotionProfile()
    {

        Assert.assertEquals(0, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPortList().size());
        CANTalon talon = new CANTalon(mCanHandle);
        Assert.assertEquals(1, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPortList().size());

        talon.changeControlMode(TalonControlMode.MotionProfile);
        talon.set(5);
    }

    @Test
    public void testSetWithMotionMagic()
    {

        Assert.assertEquals(0, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPortList().size());
        CANTalon talon = new CANTalon(mCanHandle);
        Assert.assertEquals(1, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPortList().size());

        talon.changeControlMode(TalonControlMode.MotionMagic);
        talon.set(5);
    }
}
