package com.snobot.simulator.jni;

import org.junit.Assert;
import org.junit.Test;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.test.utilities.BaseSimulatorTest;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;

public class TestRegisterJavaCallback extends BaseSimulatorTest
{
    private void checkSizes(int analogSize, int digitalSize, int encoderSize, int pcmSize, int pdpSize, int numPwm)
    {
        Assert.assertEquals(analogSize, SensorActuatorRegistry.get().getAnalog().size());
        Assert.assertEquals(digitalSize, SensorActuatorRegistry.get().getDigitalSources().size());
        Assert.assertEquals(encoderSize, SensorActuatorRegistry.get().getEncoders().size());
        Assert.assertEquals(pcmSize, SensorActuatorRegistry.get().getSolenoids().size());
        Assert.assertEquals(numPwm, SensorActuatorRegistry.get().getSpeedControllers().size());
    }

    @Test
    public void testCallback()
    {
        RobotBase.initializeHardwareConfiguration();
        RegisterCallbacksJni.registerAllCallbacks();

        checkSizes(0, 0, 0, 0, 0, 0);

        SpeedController sc0 = new Talon(0);
        SpeedController sc1 = new Talon(1);
        SpeedController sc2 = new Talon(2);
        SpeedController sc3 = new Talon(3);
        checkSizes(0, 0, 0, 0, 0, 4);

        new AnalogInput(0);
        checkSizes(1, 0, 0, 0, 0, 4);

        new DigitalInput(0);
        checkSizes(1, 1, 0, 0, 0, 4);

        new Encoder(1, 2);
        checkSizes(1, 3, 1, 0, 0, 4);

        new Solenoid(0);
        checkSizes(1, 3, 1, 1, 0, 4);

    }

    @Test
    public void testUnsupportedOptions()
    {
        RegisterCallbacksJni.analogCallback("DoesntExist", 0, null);
        RegisterCallbacksJni.analogGyroCallback("DoesntExist", 0, null);
        RegisterCallbacksJni.digitalCallback("DoesntExist", 0, null);
        RegisterCallbacksJni.encoderCallback("DoesntExist", 0, null);
        RegisterCallbacksJni.pcmCallback("DoesntExist", 0, null);
        RegisterCallbacksJni.pdpCallback("DoesntExist", 0, null);
        RegisterCallbacksJni.pwmCallback("DoesntExist", 0, null);
        RegisterCallbacksJni.relayCallback("DoesntExist", 0, null);
    }
}
