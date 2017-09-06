package com.snobot.simulator.gui;

import org.junit.Test;

import com.snobot.test.utilities.BaseSimulatorTest;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;

public class TestGuiModulePanels extends BaseSimulatorTest
{

    @Test
    public void testGuiPanels()
    {
        new Talon(0);
        new Solenoid(0);
        new Relay(0);
        new Encoder(0, 1);
        new AnalogPotentiometer(0);

        GraphicalSensorDisplayPanel panel = new GraphicalSensorDisplayPanel();
        panel.create();
        panel.update();

        panel.repaint();
    }
}
