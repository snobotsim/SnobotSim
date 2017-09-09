package com.snobot.simulator.ctre_override;

import org.junit.Test;

import com.ctre.CANTalon;

public class TestCanTalon extends BaseSimulatorTest
{

    @Test
    public void testSetup()
    {

        CANTalon talon = new CANTalon(0);
        talon.set(5);

        System.out.println(talon.get());
    }
}
