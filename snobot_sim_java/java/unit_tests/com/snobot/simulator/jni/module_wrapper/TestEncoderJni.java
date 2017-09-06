package com.snobot.simulator.jni.module_wrapper;



import org.junit.Assert;
import org.junit.Test;

import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;
import com.snobot.test.utilities.BaseSimulatorTest;

import edu.wpi.first.wpilibj.Encoder;

public class TestEncoderJni extends BaseSimulatorTest
{
    @Test
    public void testCreateEncoder()
    {
        Assert.assertEquals(0, DataAccessorFactory.getInstance().getEncoderAccessor().getPortList().size());

        new Encoder(0, 1);
        Assert.assertEquals(1, DataAccessorFactory.getInstance().getEncoderAccessor().getPortList().size());
        Assert.assertEquals("Encoder 0", DataAccessorFactory.getInstance().getEncoderAccessor().getName(0));

        new Encoder(2, 3);
        Assert.assertEquals(2, DataAccessorFactory.getInstance().getEncoderAccessor().getPortList().size());
        // Assert.assertEquals("Encoder (0, 0)", EncoderWrapperJni.getName(1));
    }

    @Test(expected = RuntimeException.class)
    public void testReusePort()
    {
        Assert.assertEquals(0, DataAccessorFactory.getInstance().getEncoderAccessor().getPortList().size());

        new Encoder(0, 1);
        Assert.assertEquals(1, DataAccessorFactory.getInstance().getEncoderAccessor().getPortList().size());

        new Encoder(1, 2);
    }
}
