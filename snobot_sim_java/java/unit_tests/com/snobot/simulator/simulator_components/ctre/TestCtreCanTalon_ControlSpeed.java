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
public class TestCtreCanTalon_ControlSpeed extends BaseSimulatorTest
{
    @Parameters(name = "{index}: Port={0}, Device={1}")
    public static Collection<Object[]> ddata()
    {
        Collection<Object[]> output = new ArrayList<>();

        for (int i = 0; i < 64; ++i)
        {

            output.add(new Object[]{ i, FeedbackDevice.CtreMagEncoder_Relative });
            output.add(new Object[]{ i, FeedbackDevice.CtreMagEncoder_Absolute });
            // output.add(new Object[]{ i, FeedbackDevice.AnalogPot });
        }

        return output;
    }

    private final int mCanHandle;
    private final int mRawHandle;
    private final FeedbackDevice mFeedbackDevice;

    public TestCtreCanTalon_ControlSpeed(int aCanHandle, FeedbackDevice aFeedbackDevice)
    {
        mCanHandle = aCanHandle;
        mRawHandle = mCanHandle + 100;
        mFeedbackDevice = aFeedbackDevice;
    }

    @Test
    public void testSetWithSpeedEncoder()
    {
        Assert.assertEquals(0, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPortList().size());
        CANTalon talon = new CANTalon(mCanHandle);
        Assert.assertEquals(1, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPortList().size());

        talon.changeControlMode(TalonControlMode.Speed);
        talon.setFeedbackDevice(mFeedbackDevice);
        checkForFeedbackDevice();

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
        Assert.assertEquals(40, talon.get(), .05);
    }

    private void checkForFeedbackDevice()
    {
        switch (mFeedbackDevice)
        {
        case CtreMagEncoder_Absolute:
        case CtreMagEncoder_Relative:
            Assert.assertTrue(DataAccessorFactory.getInstance().getEncoderAccessor().getPortList().contains(mRawHandle));
            break;
        case AnalogEncoder:
        case AnalogPot:
            Assert.assertTrue(DataAccessorFactory.getInstance().getAnalogAccessor().getPortList().contains(mRawHandle));
            break;
        default:
            Assert.assertTrue(false);
        }
    }
}
