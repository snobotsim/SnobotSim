package com.snobot.simulator.simulator_components.ctre;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.TrajectoryPoint;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.snobot.simulator.motor_sim.DcMotorModelConfig;
import com.snobot.simulator.motor_sim.StaticLoadMotorSimulationConfig;
import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;
import com.snobot.test.utilities.BaseSimulatorTest;

@RunWith(value = Parameterized.class)
public class TestCtreCanTalon_ControlMotionProfile extends BaseSimulatorTest
{
    @Parameters(name = "Test: {index} CanPort={0}")
    public static Collection<Integer> data()
    {
        Collection<Integer> output = new ArrayList<>();

        for (int i = 0; i < 64; ++i)
        {
            output.add(i);
        }

        return output;
    }

    private final int mCanHandle;
    private final int mRawHandle;

    public TestCtreCanTalon_ControlMotionProfile(int aCanHandle)
    {
        mCanHandle = aCanHandle;
        mRawHandle = mCanHandle + 100;
    }

    @Test
    public void testSetWithMotionProfile()
    {

        Assert.assertEquals(0, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPortList().size());
        TalonSRX talon = new TalonSRX(mCanHandle);
        Assert.assertEquals(1, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPortList().size());

        talon.config_kP(0, .045, 5);
        talon.config_kF(0, 0.018, 5);
        talon.config_IntegralZone(0, 1, 5);


        MotionProfileStatus status = new MotionProfileStatus();
        talon.getMotionProfileStatus(status);
        printMotionProfileStatus(talon, status);

        List<TrajectoryPoint> points = generatePoints(1, 3, 4, 12, 12, 35, .02);
        for (int i = 0; i < points.size(); ++i)
        {
            Assert.assertEquals(ErrorCode.OK, talon.pushMotionProfileTrajectory(points.get(i)));
            talon.getMotionProfileStatus(status);
            // printMotionProfileStatus(talon, status);
            // Assert.assertEquals(0, status.btmBufferCnt, 0);
            // Assert.assertEquals(i + 1, status.topBufferCnt);
            // Assert.assertEquals(2048 - i - 1, status.topBufferRem);
        }
        for (int i = 0; i < points.size(); ++i)
        {
            talon.processMotionProfileBuffer();
        }

        // Hookup motor config
        DcMotorModelConfig motorConfig = DataAccessorFactory.getInstance().getSimulatorDataAccessor().createMotor("CIM", 1, 10, 1);
        Assert.assertTrue(DataAccessorFactory.getInstance().getSimulatorDataAccessor().setSpeedControllerModel_Static(mRawHandle, motorConfig,
                new StaticLoadMotorSimulationConfig(.2)));

        // talon.set(TalonSRX.SetValueMotionProfile.Disable.value);
        simulateForTime(.2, () ->
        {
            talon.set(ControlMode.MotionProfile, -1);
        });

        // talon.set(TalonSRX.SetValueMotionProfile.Hold.value);
        simulateForTime(.2, () ->
        {
            talon.set(ControlMode.MotionProfile, 0);
        });

        // talon.set(TalonSRX.SetValueMotionProfile.Enable.value);
        simulateForTime(10, () ->
        {
            talon.set(ControlMode.MotionProfile, 1);
        });
    }

    private List<TrajectoryPoint> generatePoints(double t1, double t2, double t3, double aMaxVelocity, double aMaxAccel, double aPosition, double aDt)
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
            point.zeroPos = t == 0;
            output.add(point);
        }

        return output;
    }

    @SuppressWarnings("unused")
    private void printMotionProfileStatus(TalonSRX talon, MotionProfileStatus status)
    {
        System.out.println("Getting status...");

        System.out.println("  btmBufferCnt     : " + status.btmBufferCnt);
        System.out.println("  topBufferCnt     : " + status.topBufferCnt);
        System.out.println("  topBufferRem     : " + status.topBufferRem);
        System.out.println("  activePointValid : " + status.activePointValid);
        System.out.println("  hasUnderrun      : " + status.hasUnderrun);
        System.out.println("  isUnderrun       : " + status.isUnderrun);
        System.out.println("  outputEnable     : " + status.outputEnable);
        System.out.println("  point            :");
        System.out.println("      position          : " + talon.getActiveTrajectoryPosition());
        System.out.println("      velocity          : " + talon.getActiveTrajectoryVelocity());
    }
}
