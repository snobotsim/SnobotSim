package simulator.com.snobot.simulator.jni.module_wrapper;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.snobot.simulator.jni.SnobotSimulatorJni;
import com.snobot.simulator.jni.module_wrapper.RelayWrapperJni;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Value;
import edu.wpi.first.wpilibj.RobotBase;

public class TestRelayJni
{
    @Before
    public void setup()
    {
        SnobotSimulatorJni.reset();
        RobotBase.initializeHardwareConfiguration();
    }

    @Test
    public void testCreateRelays()
    {
        Assert.assertEquals(0, RelayWrapperJni.getPortList().length);

        new Relay(0);
        Assert.assertEquals(1, RelayWrapperJni.getPortList().length);

        new Relay(1);
        Assert.assertEquals(2, RelayWrapperJni.getPortList().length);
    }

    @Test(expected = RuntimeException.class)
    public void testReusePort()
    {
        Assert.assertEquals(0, RelayWrapperJni.getPortList().length);

        new Relay(2);
        Assert.assertEquals(1, RelayWrapperJni.getPortList().length);

        new Relay(2);
    }

    @Test
    public void testSetRelay()
    {
        Assert.assertEquals(0, RelayWrapperJni.getPortList().length);

        Relay relay = new Relay(1);
        Assert.assertFalse(RelayWrapperJni.getFowardValue(1));
        Assert.assertFalse(RelayWrapperJni.getReverseValue(1));

        relay.set(Value.kOn);
        Assert.assertTrue(RelayWrapperJni.getFowardValue(1));
        Assert.assertTrue(RelayWrapperJni.getReverseValue(1));

        relay.set(Value.kForward);
        Assert.assertTrue(RelayWrapperJni.getFowardValue(1));
        Assert.assertFalse(RelayWrapperJni.getReverseValue(1));

        relay.set(Value.kReverse);
        Assert.assertFalse(RelayWrapperJni.getFowardValue(1));
        Assert.assertTrue(RelayWrapperJni.getReverseValue(1));

        relay.set(Value.kOff);
        Assert.assertFalse(RelayWrapperJni.getFowardValue(1));
        Assert.assertFalse(RelayWrapperJni.getReverseValue(1));

        SnobotSimulatorJni.reset();

        relay = new Relay(1);
    }
}
