package simulator.com.snobot.simulator.jni.module_wrapper;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.snobot.simulator.jni.SnobotSimulatorJni;
import com.snobot.simulator.jni.module_wrapper.SpeedControllerWrapperJni;

import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.Talon;

public class TestSpeedControllerJni
{
    @Before
    public void setup()
    {
        SnobotSimulatorJni.initializeLogging(0);
        SnobotSimulatorJni.reset();
        RobotBase.initializeHardwareConfiguration();
    }

    @Test
    public void testCreatePwm()
    {
        Assert.assertEquals(0, SpeedControllerWrapperJni.getPortList().length);

        new Jaguar(0);
        Assert.assertEquals(1, SpeedControllerWrapperJni.getPortList().length);

        new Talon(3);
        Assert.assertEquals(2, SpeedControllerWrapperJni.getPortList().length);
    }

    @Test(expected = RuntimeException.class)
    public void testReusePort()
    {
        Assert.assertEquals(0, SpeedControllerWrapperJni.getPortList().length);

        new Talon(1);
        Assert.assertEquals(1, SpeedControllerWrapperJni.getPortList().length);

        new Talon(1);
    }
}
