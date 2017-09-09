package com.snobot.simulator.ctre_override;

import org.junit.Test;

import com.ctre.CANTalon;

public class TestCanTalon extends BaseSimulatorTest
{

    @Test
    public void testSetup()
    {

        new CANTalon(0);
    }
}
