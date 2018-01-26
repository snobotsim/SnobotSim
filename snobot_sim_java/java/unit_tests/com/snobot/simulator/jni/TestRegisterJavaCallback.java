package com.snobot.simulator.jni;

import java.nio.ByteBuffer;

import org.junit.Assert;
import org.junit.Test;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.jni.standard_components.AnalogCallbackJni;
import com.snobot.simulator.jni.standard_components.AnalogGyroCallbackJni;
import com.snobot.simulator.jni.standard_components.DigitalCallbackJni;
import com.snobot.simulator.jni.standard_components.EncoderCallbackJni;
import com.snobot.simulator.jni.standard_components.I2CCallbackJni;
import com.snobot.simulator.jni.standard_components.PcmCallbackJni;
import com.snobot.simulator.jni.standard_components.PdpCallbackJni;
import com.snobot.simulator.jni.standard_components.PwmCallbackJni;
import com.snobot.simulator.jni.standard_components.RelayCallbackJni;
import com.snobot.simulator.jni.standard_components.SpiCallbackJni;
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
    private void checkSizes(int aAnalogSize, int aDigitalSize, int aEncoderSize, int aPcmSize, int aPdpSize, int aNumPwm)
    {
        Assert.assertEquals(aAnalogSize, SensorActuatorRegistry.get().getAnalog().size());
        Assert.assertEquals(aDigitalSize, SensorActuatorRegistry.get().getDigitalSources().size());
        Assert.assertEquals(aEncoderSize, SensorActuatorRegistry.get().getEncoders().size());
        Assert.assertEquals(aPcmSize, SensorActuatorRegistry.get().getSolenoids().size());
        Assert.assertEquals(aNumPwm, SensorActuatorRegistry.get().getSpeedControllers().size());
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
        AnalogCallbackJni.analogCallback("DoesntExist", 0, null);
        AnalogGyroCallbackJni.analogGyroCallback("DoesntExist", 0, null);
        DigitalCallbackJni.digitalCallback("DoesntExist", 0, null);
        EncoderCallbackJni.encoderCallback("DoesntExist", 0, null);
        I2CCallbackJni.i2cCallback("DoesntExist", 0, (HalCallbackValue) null);
        I2CCallbackJni.i2cCallback("DoesntExist", 0, ByteBuffer.allocate(5));
        PcmCallbackJni.pcmCallback("DoesntExist", 0, null);
        PdpCallbackJni.pdpCallback("DoesntExist", 0, null);
        PwmCallbackJni.pwmCallback("DoesntExist", 0, null);
        RelayCallbackJni.relayCallback("DoesntExist", 0, null);
        SpiCallbackJni.spiCallback("DoesntExist", 0, (HalCallbackValue) null);
        SpiCallbackJni.spiCallback("DoesntExist", 0, ByteBuffer.allocate(5));
    }
}
