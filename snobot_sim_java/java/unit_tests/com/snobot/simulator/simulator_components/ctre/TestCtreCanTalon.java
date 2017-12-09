package com.snobot.simulator.simulator_components.ctre;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.MotionProfileStatus;
import com.ctre.CANTalon.TalonControlMode;
import com.ctre.CANTalon.TrajectoryPoint;
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
    }

    @Test
    public void testSetWithPositionAnalog()
    {
        Assert.assertEquals(0, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPortList().size());
        CANTalon talon = new CANTalon(mCanHandle);
        Assert.assertEquals(1, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPortList().size());

        talon.changeControlMode(TalonControlMode.Position);
        talon.setFeedbackDevice(FeedbackDevice.AnalogPot);
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
    }

    @Test
    public void testSetWithSpeedAnalog()
    {
        Assert.assertEquals(0, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPortList().size());
        CANTalon talon = new CANTalon(mCanHandle);
        Assert.assertEquals(1, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPortList().size());

        talon.changeControlMode(TalonControlMode.Speed);
        talon.setFeedbackDevice(FeedbackDevice.AnalogPot);
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
        double DOUBLE_EPSILON = 1.0 / 1023;

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

        Assert.assertEquals(.5, talon.get(), DOUBLE_EPSILON);
        Assert.assertEquals(leadTalon.get(), talon.get(), DOUBLE_EPSILON);
    }

    @Test
    public void testSetWithMotionProfile()
    {

        Assert.assertEquals(0, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPortList().size());
        CANTalon talon = new CANTalon(mCanHandle);
        Assert.assertEquals(1, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPortList().size());


        talon.setP(.045);
        talon.setF(0.018);
        talon.setIZone(1);
        talon.changeControlMode(TalonControlMode.MotionProfile);

        MotionProfileStatus status = new MotionProfileStatus();
        talon.getMotionProfileStatus(status);

        List<TrajectoryPoint> points = generatePoints(1, 3, 4, 12, 12, 35, .02);
        for (int i = 0; i < points.size(); ++i)
        {
            Assert.assertTrue(talon.pushMotionProfileTrajectory(points.get(i)));
            talon.getMotionProfileStatus(status);
            Assert.assertEquals(0, status.btmBufferCnt, 0);
            Assert.assertEquals(i + 1, status.topBufferCnt);
            Assert.assertEquals(2048 - i - 1, status.topBufferRem);
        }
        for (int i = 0; i < points.size(); ++i)
        {
            talon.processMotionProfileBuffer();
        }

        // Hookup motor config
        DcMotorModelConfig motorConfig = DataAccessorFactory.getInstance().getSimulatorDataAccessor().createMotor("CIM", 1, 10, 1);
        Assert.assertTrue(DataAccessorFactory.getInstance().getSimulatorDataAccessor().setSpeedControllerModel_Static(mRawHandle, motorConfig,
                new StaticLoadMotorSimulationConfig(.2)));

        talon.set(CANTalon.SetValueMotionProfile.Disable.value);
        simulateForTime(.2, () ->
        {
        });

        talon.set(CANTalon.SetValueMotionProfile.Hold.value);
        simulateForTime(.2, () ->
        {
        });

        talon.set(CANTalon.SetValueMotionProfile.Enable.value);
        simulateForTime(10, () ->
        {
        });
    }

    @SuppressWarnings("unused")
    private void printMotionProfileStatus(CANTalon talon, MotionProfileStatus status)
    {
        System.out.println("Getting status...");
        TrajectoryPoint point = status.activePoint;

        System.out.println("  btmBufferCnt     : " + status.btmBufferCnt);
        System.out.println("  topBufferCnt     :" + status.topBufferCnt);
        System.out.println("  topBufferRem     :" + status.topBufferRem);
        System.out.println("  activePointValid :" + status.activePointValid);
        System.out.println("  hasUnderrun      :" + status.hasUnderrun);
        System.out.println("  isUnderrun       :" + status.isUnderrun);
        System.out.println("  outputEnable     :" + status.outputEnable);
        System.out.println("  point            :");
        System.out.println("      position          : " + point.position);
        System.out.println("      velocity          : " + point.velocity);
        System.out.println("      profileSlotSelect : " + point.profileSlotSelect);
        System.out.println("      timeDurMs         : " + point.timeDurMs);
        System.out.println("      isLastPoint       : " + point.isLastPoint);
        System.out.println("      velocityOnly      : " + point.velocityOnly);
        System.out.println("      zeroPos           : " + point.zeroPos);
    }

    @Test
    public void testSetWithMotionMagic()
    {

        Assert.assertEquals(0, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPortList().size());
        CANTalon talon = new CANTalon(mCanHandle);
        Assert.assertEquals(1, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPortList().size());

        // Hookup motor config
        DcMotorModelConfig motorConfig = DataAccessorFactory.getInstance().getSimulatorDataAccessor().createMotor("CIM", 1, 10, 1);
        Assert.assertTrue(DataAccessorFactory.getInstance().getSimulatorDataAccessor().setSpeedControllerModel_Static(mRawHandle, motorConfig,
                new StaticLoadMotorSimulationConfig(.2)));

        talon.setP(.11);
        talon.setI(.005);
        talon.setF(0.018);
        talon.setIZone(2);

        talon.changeControlMode(TalonControlMode.MotionMagic);
        talon.setMotionMagicCruiseVelocity(12);
        talon.setMotionMagicAcceleration(24);
        talon.set(30 * 12);

        simulateForTime(8, () ->
        {
        });

        Assert.assertEquals(0, talon.getClosedLoopError(), 2);
    }
    
    private List<TrajectoryPoint> generatePoints(
            double t1, double t2, double t3,
            double aMaxVelocity, double aMaxAccel, double aPosition,
            double aDt)
    {
        List<TrajectoryPoint> output = new ArrayList<>();

        double pos = 0.0;
        double vel = 0.0;

        for (double t = 0; t < t1; t += aDt)
        {
            pos = .5 * aMaxAccel * (t * t);
            vel = aMaxAccel * t;

            TrajectoryPoint point = new TrajectoryPoint();
            point.isLastPoint = false;
            point.position = pos;
            point.velocity = vel;
            point.profileSlotSelect = 0;
            point.timeDurMs = (int) (aDt * 1000);
            point.velocityOnly = false;
            point.zeroPos = t == 0;
            output.add(point);
        }

        for (double t = t1; t < t2; t += aDt)
        {
            pos = .5 * ((aMaxVelocity * aMaxVelocity) / aMaxAccel) + aMaxVelocity * (t - (aMaxVelocity / aMaxAccel));
            vel = aMaxVelocity;

            TrajectoryPoint point = new TrajectoryPoint();
            point.isLastPoint = false;
            point.position = pos;
            point.velocity = vel;
            point.profileSlotSelect = 0;
            point.timeDurMs = (int) (aDt * 1000);
            point.velocityOnly = false;
            point.zeroPos = t == 0;
            output.add(point);
        }

        for (double t = t2; t < t3; t += aDt)
        {
            pos = aPosition - .5 * aMaxAccel * Math.pow((t - (t1 + t2)), 2);
            vel = aMaxAccel * ((aMaxVelocity / aMaxAccel) + (aPosition / aMaxVelocity) - t);

            TrajectoryPoint point = new TrajectoryPoint();
            point.isLastPoint = false;
            point.position = pos;
            point.velocity = vel;
            point.profileSlotSelect = 0;
            point.timeDurMs = (int) (aDt * 1000);
            point.velocityOnly = false;
            point.zeroPos = t == 0;
            output.add(point);
        }
        
        return output;
    }
}
