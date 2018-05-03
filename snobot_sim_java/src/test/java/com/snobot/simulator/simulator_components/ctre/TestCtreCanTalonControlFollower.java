package com.snobot.simulator.simulator_components.ctre;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;
import com.snobot.test.utilities.BaseSimulatorTest;

@RunWith(value = Parameterized.class)
public class TestCtreCanTalonControlFollower extends BaseSimulatorTest
{
    private static final double sDOUBLE_EPSILON = 1.0 / 1023;

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

    public TestCtreCanTalonControlFollower(int aCanHandle)
    {
        mCanHandle = aCanHandle;
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
        TalonSRX leadTalon = new TalonSRX(leadTalonId);
        TalonSRX talon = new TalonSRX(mCanHandle);
        Assert.assertEquals(2, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPortList().size());

        talon.set(ControlMode.Follower, leadTalonId);

        leadTalon.set(ControlMode.PercentOutput, .5);

        Assert.assertEquals(.5, talon.getMotorOutputPercent(), sDOUBLE_EPSILON);
        Assert.assertEquals(leadTalon.getMotorOutputPercent(), talon.getMotorOutputPercent(), sDOUBLE_EPSILON);
    }
}
