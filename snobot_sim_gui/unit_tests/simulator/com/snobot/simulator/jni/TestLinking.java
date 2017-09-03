package simulator.com.snobot.simulator.jni;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.snobot.simulator.jni.SnobotSimulatorJni;
import com.snobot.simulator.jni.module_wrapper.SpeedControllerWrapperJni;

import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.hal.HAL;

public class TestLinking 
{
    @Before
    public void setup()
    {
        SnobotSimulatorJni.initializeLogging(0);
        SnobotSimulatorJni.reset();
        RobotBase.initializeHardwareConfiguration();
    }

    @Test
	public void testHalLinking()
	{
		Assert.assertTrue(HAL.initialize(0, 0));
	}

    @Test
    public void testSnobotSimLinking()
    {
        Assert.assertEquals(0, SpeedControllerWrapperJni.getPortList().length);
    }
}
