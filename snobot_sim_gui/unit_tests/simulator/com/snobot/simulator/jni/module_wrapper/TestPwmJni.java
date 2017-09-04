package simulator.com.snobot.simulator.jni.module_wrapper;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.snobot.simulator.jni.SnobotSimulatorJni;
import com.snobot.simulator.jni.module_wrapper.SpeedControllerWrapperJni;

import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.Talon;

public class TestPwmJni
{
    @Before
    public void setup()
    {
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

        new Talon(0);
        Assert.assertEquals(1, SpeedControllerWrapperJni.getPortList().length);

        new Talon(0);
    }

    private static final double EPSILON = .00000001;

    @Test
    public void testSet()
    {
        Assert.assertEquals(0, SpeedControllerWrapperJni.getPortList().length);

        Talon talon = new Talon(1);
        Assert.assertEquals(0, talon.get(), EPSILON);
        Assert.assertEquals(0, SpeedControllerWrapperJni.getVoltagePercentage(1), EPSILON);

        talon.set(.5);
        Assert.assertEquals(.5, talon.get(), EPSILON);
        Assert.assertEquals(.5, SpeedControllerWrapperJni.getVoltagePercentage(1), EPSILON);

        talon.set(-.5);
        Assert.assertEquals(-.5, talon.get(), EPSILON);
        Assert.assertEquals(-.5, SpeedControllerWrapperJni.getVoltagePercentage(1), EPSILON);

        talon.set(1.1);
        Assert.assertEquals(1.0, talon.get(), EPSILON);
        Assert.assertEquals(1.0, SpeedControllerWrapperJni.getVoltagePercentage(1), EPSILON);

        talon.set(-2.1);
        Assert.assertEquals(-1.0, talon.get(), EPSILON);
        Assert.assertEquals(-1.0, SpeedControllerWrapperJni.getVoltagePercentage(1), EPSILON);
    }
}
