package simulator.com.snobot.simulator.jni.module_wrapper;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.snobot.simulator.jni.SnobotSimulatorJni;
import com.snobot.simulator.jni.module_wrapper.SolenoidWrapperJni;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.Solenoid;

public class TestSolenoidJni
{
    @Before
    public void setup()
    {
        SnobotSimulatorJni.reset();
        RobotBase.initializeHardwareConfiguration();
    }

    @Test
    public void testCreateSolenoid()
    {
        Assert.assertEquals(0, SolenoidWrapperJni.getPortList().length);

        new Solenoid(0);
        Assert.assertEquals(1, SolenoidWrapperJni.getPortList().length);

        new Solenoid(3);
        Assert.assertEquals(2, SolenoidWrapperJni.getPortList().length);
    }

    @Test(expected = RuntimeException.class)
    public void testReusePort()
    {
        Assert.assertEquals(0, SolenoidWrapperJni.getPortList().length);

        new Solenoid(1);
        Assert.assertEquals(1, SolenoidWrapperJni.getPortList().length);

        new Solenoid(1);
    }

    @Test
    public void testSet()
    {
        Assert.assertEquals(0, SolenoidWrapperJni.getPortList().length);

        Solenoid solenoid = new Solenoid(0);

        solenoid.set(true);
        Assert.assertTrue(solenoid.get());
        Assert.assertTrue(SolenoidWrapperJni.get(0));

        solenoid.set(false);
        Assert.assertFalse(solenoid.get());
        Assert.assertFalse(SolenoidWrapperJni.get(0));

        DoubleSolenoid doubleSolenoid = new DoubleSolenoid(1, 2);

        doubleSolenoid.set(Value.kForward);
        Assert.assertTrue(SolenoidWrapperJni.get(1));
        Assert.assertFalse(SolenoidWrapperJni.get(2));

        doubleSolenoid.set(Value.kReverse);
        Assert.assertFalse(SolenoidWrapperJni.get(1));
        Assert.assertTrue(SolenoidWrapperJni.get(2));

        doubleSolenoid.set(Value.kOff);
        Assert.assertFalse(SolenoidWrapperJni.get(1));
        Assert.assertFalse(SolenoidWrapperJni.get(2));
    }
}
