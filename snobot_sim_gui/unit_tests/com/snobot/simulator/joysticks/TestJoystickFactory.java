package com.snobot.simulator.joysticks;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

import com.snobot.simulator.joysticks.joystick_specializations.KeyboardJoystick;
import com.snobot.simulator.joysticks.joystick_specializations.NullJoystick;
import com.snobot.simulator.joysticks.joystick_specializations.Ps4Joystick;
import com.snobot.simulator.joysticks.joystick_specializations.XboxJoystick;
import com.snobot.test.utilities.BaseSimulatorTest;
import com.snobot.test.utilities.MockController;

import edu.wpi.first.wpilibj.DriverStation;

public class TestJoystickFactory extends BaseSimulatorTest
{
    @Test
    public void testJoystickFactory() throws Exception
    {
        File configFile = new File(JoystickFactory.sJOYSTICK_CONFIG_FILE);
        if (configFile.exists())
        {
            Assert.assertTrue(configFile.delete());
        }

        if (!configFile.getParentFile().exists())
        {
            Assert.assertTrue(configFile.getParentFile().mkdirs());
        }

        JoystickFactory factory = JoystickFactory.get();

        Map<String, ControllerConfiguration> config = factory.getControllerConfiguration();
        Assert.assertEquals(0, config.size());

        for (int i = 0; i < DriverStation.kJoystickPorts; ++i)
        {
            Assert.assertTrue(factory.get(i) instanceof NullJoystick);
        }

        config.put("X", new ControllerConfiguration(new MockController(), XboxJoystick.class));
        config.put("Y", new ControllerConfiguration(new MockController(), Ps4Joystick.class));
        config.put("Z", new ControllerConfiguration(new MockController(), KeyboardJoystick.class));

        factory.setJoysticks(0, "X");
        factory.setJoysticks(3, "Y");
        factory.setJoysticks(5, "Z");

        Assert.assertTrue(factory.get(0) instanceof XboxJoystick);
        Assert.assertTrue(factory.get(1) instanceof NullJoystick);
        Assert.assertTrue(factory.get(2) instanceof NullJoystick);
        Assert.assertTrue(factory.get(3) instanceof Ps4Joystick);
        Assert.assertTrue(factory.get(4) instanceof NullJoystick);
        Assert.assertTrue(factory.get(5) instanceof KeyboardJoystick);

        InputStream input_stream = new FileInputStream(JoystickFactory.sJOYSTICK_CONFIG_FILE);
        Properties properties = new Properties();
        properties.load(input_stream);
        input_stream.close();

        Assert.assertEquals("X---com.snobot.simulator.joysticks.joystick_specializations.XboxJoystick", properties.getProperty("Joystick_0"));
        Assert.assertEquals("Null Joystick---null", properties.getProperty("Joystick_1"));
        Assert.assertEquals("Null Joystick---null", properties.getProperty("Joystick_2"));
        Assert.assertEquals("Y---com.snobot.simulator.joysticks.joystick_specializations.Ps4Joystick", properties.getProperty("Joystick_3"));
        Assert.assertEquals("Null Joystick---null", properties.getProperty("Joystick_4"));
        Assert.assertEquals("Z---com.snobot.simulator.joysticks.joystick_specializations.KeyboardJoystick", properties.getProperty("Joystick_5"));
    }

}
