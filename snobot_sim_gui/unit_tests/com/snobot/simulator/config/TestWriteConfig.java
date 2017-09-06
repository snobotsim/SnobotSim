package com.snobot.simulator.config;

import org.junit.Assert;
import org.junit.Test;

import com.snobot.test.utilities.BaseSimulatorTest;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;

public class TestWriteConfig extends BaseSimulatorTest
{

    @Test
    public void testWriteConfig()
    {
        String dump_file = "test_output/testWriteFile.yml";

        new Talon(0);
        new Solenoid(0);
        new Relay(0);
        new Encoder(0, 1);
        new AnalogPotentiometer(0);

        SimulatorConfigWriter writer = new SimulatorConfigWriter();
        Assert.assertTrue(writer.writeConfig(dump_file));
    }

    @Test
    public void testWriteConfigToNonExistingDirectory()
    {
        String dump_file = "directory_does_not_exist/testWriteFile.yml";

        SimulatorConfigWriter writer = new SimulatorConfigWriter();
        Assert.assertFalse(writer.writeConfig(dump_file));
    }
}
