package com.snobot.simulator;

import org.junit.Assert;
import org.junit.Test;

import com.snobot.simulator.jni.module_wrapper.SpeedControllerWrapperJni;

import edu.wpi.first.wpilibj.hal.HAL;
import edu.wpi.first.wpilibj.hal.PWMJNI;

public class TestJniLinking
{

    @Test
    public void testLoad()
    {
        try
        {
             HAL.initialize(0, 0);
             Assert.assertEquals(0,
             SpeedControllerWrapperJni.getPortList().length);
            
            // Initialize one, check again
             PWMJNI.initializePWMPort(0);
             Assert.assertEquals(1,
             SpeedControllerWrapperJni.getPortList().length);
        }
        catch (Exception | UnsatisfiedLinkError e)
        {
            e.printStackTrace();
        }
    }
}