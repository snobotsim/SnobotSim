package simulator.com.snobot.simulator.jni.module_wrapper;



import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.snobot.simulator.jni.SnobotSimulatorJni;
import com.snobot.simulator.jni.module_wrapper.EncoderWrapperJni;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotBase;

public class TestEncoderJni
{
    @Before
    public void setup()
    {
        SnobotSimulatorJni.initializeLogging(0);
        SnobotSimulatorJni.reset();
        RobotBase.initializeHardwareConfiguration();
    }

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

        new Encoder(4, 5);
        // Assert.assertEquals(1, EncoderWrapperJni.getPortList().length);

        new Encoder(6, 5);
    }
}
