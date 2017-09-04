package com.snobot.simulator.jni.module_wrapper;



import org.junit.Assert;
import org.junit.Test;

import com.snobot.simulator.jni.SimulationConnectorJni;
import com.snobot.test.utilities.BaseSimulatorTest;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;

public class TestEncoderJni extends BaseSimulatorTest
{
    @Test
    public void testCreateEncoder()
    {
        Assert.assertEquals(0, EncoderWrapperJni.getPortList().length);

        new Encoder(0, 1);
        Assert.assertEquals(1, EncoderWrapperJni.getPortList().length);

        new Encoder(2, 3);
        Assert.assertEquals(2, EncoderWrapperJni.getPortList().length);
    }

    @Test(expected = RuntimeException.class)
    public void testReusePort()
    {
        Assert.assertEquals(0, EncoderWrapperJni.getPortList().length);

        new Encoder(0, 1);
        Assert.assertEquals(1, EncoderWrapperJni.getPortList().length);

        new Encoder(1, 2);
    }

    @Test
    public void testSpeedControllerFeedback()
    {
        SpeedController sc = new Talon(0);
        Encoder encoder = new Encoder(1, 2);

        EncoderWrapperJni.connectSpeedController(0, 0);
        SimulationConnectorJni.setSpeedControllerModel_Simple(0, 12);

        for (int i = 0; i < 50; ++i)
        {
            sc.set(1);
            SpeedControllerWrapperJni.updateAllSpeedControllers(.02);
        }

        // Assert.assertEquals(12.0, encoder.getDistance(), .00001);
        Assert.assertEquals(12.0, EncoderWrapperJni.getDistance(0), .00001);
    }
}
