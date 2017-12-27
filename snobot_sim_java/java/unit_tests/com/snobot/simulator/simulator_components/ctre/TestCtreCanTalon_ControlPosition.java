package com.snobot.simulator.simulator_components.ctre;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.ctre.phoenix.MotorControl.SmartMotorController.FeedbackDevice;
import com.ctre.phoenix.MotorControl.SmartMotorController.TalonControlMode;
import com.ctre.phoenix.MotorControl.CAN.TalonSRX;
import com.snobot.simulator.motor_sim.DcMotorModelConfig;
import com.snobot.simulator.motor_sim.StaticLoadMotorSimulationConfig;
import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;
import com.snobot.test.utilities.BaseSimulatorTest;

@RunWith(value = Parameterized.class)
public class TestCtreCanTalon_ControlPosition extends BaseSimulatorTest
{
    @Parameters(name = "{index}: Port={0}, Device={1}")
    public static Collection<Object[]> ddata()
    {
        Collection<Object[]> output = new ArrayList<>();

        for (int i = 0; i < 64; ++i)
        {

            output.add(new Object[]{ i, FeedbackDevice.CtreMagEncoder_Relative });
            output.add(new Object[]{ i, FeedbackDevice.CtreMagEncoder_Absolute });
            output.add(new Object[]{ i, FeedbackDevice.AnalogPot });
        }

        return output;
    }

    private final int mCanHandle;
    private final int mRawHandle;
    private final FeedbackDevice mFeedbackDevice;

    public TestCtreCanTalon_ControlPosition(int aCanHandle, FeedbackDevice aFeedbackDevice)
    {
        mCanHandle = aCanHandle;
        mRawHandle = mCanHandle + 100;
        mFeedbackDevice = aFeedbackDevice;
    }

    @Test
    public void testSetWithPosition()
    {
        Assert.assertEquals(0, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPortList().size());
        TalonSRX talon = new TalonSRX(mCanHandle);
        Assert.assertEquals(1, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPortList().size());

        talon.changeControlMode(TalonControlMode.Position);
        talon.setFeedbackDevice(mFeedbackDevice);
        checkForFeedbackDevice();

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
        Assert.assertEquals(36, talon.getPosition(), .05);
        Assert.assertEquals(36, talon.get(), .05);

        switch (mFeedbackDevice)
        {
        case CtreMagEncoder_Absolute:
        case CtreMagEncoder_Relative:
            Assert.assertEquals(36, talon.getEncPosition());
            Assert.assertEquals(36, DataAccessorFactory.getInstance().getEncoderAccessor().getDistance(mRawHandle), .05);
            break;
        default:
            // ok
        }
        
    }

    private void checkForFeedbackDevice()
    {
        switch (mFeedbackDevice)
        {
        case CtreMagEncoder_Absolute:
        case CtreMagEncoder_Relative:
            Assert.assertTrue(DataAccessorFactory.getInstance().getEncoderAccessor().getPortList().contains(mRawHandle));
            Assert.assertEquals("CAN Encoder (" + mCanHandle + ")", DataAccessorFactory.getInstance().getEncoderAccessor().getName(mRawHandle));
            break;
        case AnalogEncoder:
        case AnalogPot:
            Assert.assertTrue(DataAccessorFactory.getInstance().getAnalogAccessor().getPortList().contains(mRawHandle));
            Assert.assertEquals("CAN Analog (" + mCanHandle + ")", DataAccessorFactory.getInstance().getAnalogAccessor().getName(mRawHandle));
            break;
        default:
            Assert.assertTrue(false);
        }
    }
}
