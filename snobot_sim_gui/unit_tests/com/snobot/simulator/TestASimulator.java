package com.snobot.simulator;

import org.junit.Assert;
import org.junit.Test;

import com.snobot.simulator.motor_sim.SimpleMotorSimulationConfig;
import com.snobot.simulator.motor_sim.StaticLoadMotorSimulationConfig;
import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;
import com.snobot.test.utilities.BaseSimulatorTest;
import com.snobot.test.utilities.MockRobot;

public class TestASimulator extends BaseSimulatorTest
{

    @Test
    public void testASimulator()
    {
        String file = "test_files/ConfigTest/ReadConfig/testReadFile.yml";

        ASimulator baseSimulator = new ASimulator();
        Assert.assertTrue(baseSimulator.loadConfig(file));

        // Used to create some components
        new MockRobot();

        baseSimulator.createSimulatorComponents();

        Assert.assertEquals("I2C Accel X", DataAccessorFactory.getInstance().getAccelerometerAccessor().getName(0));
        Assert.assertEquals("I2C Accel Y", DataAccessorFactory.getInstance().getAccelerometerAccessor().getName(1));
        Assert.assertEquals("I2C Accel Z", DataAccessorFactory.getInstance().getAccelerometerAccessor().getName(2));
        Assert.assertEquals("SPI Accel X", DataAccessorFactory.getInstance().getAccelerometerAccessor().getName(150));
        Assert.assertEquals("SPI Accel Y", DataAccessorFactory.getInstance().getAccelerometerAccessor().getName(151));
        Assert.assertEquals("SPI Accel Z", DataAccessorFactory.getInstance().getAccelerometerAccessor().getName(152));
        Assert.assertEquals("SPI Accel X", DataAccessorFactory.getInstance().getAccelerometerAccessor().getName(75));
        Assert.assertEquals("SPI Accel Y", DataAccessorFactory.getInstance().getAccelerometerAccessor().getName(76));
        Assert.assertEquals("SPI Accel Z", DataAccessorFactory.getInstance().getAccelerometerAccessor().getName(77));

        Assert.assertEquals("Analog 0", DataAccessorFactory.getInstance().getAnalogAccessor().getName(0));
        Assert.assertEquals("Analog 1", DataAccessorFactory.getInstance().getAnalogAccessor().getName(1));
        Assert.assertEquals("Analog 2", DataAccessorFactory.getInstance().getAnalogAccessor().getName(2));

        Assert.assertEquals("Digital Source0", DataAccessorFactory.getInstance().getDigitalAccessor().getName(0));
        Assert.assertEquals("Digital Source1", DataAccessorFactory.getInstance().getDigitalAccessor().getName(1));
        Assert.assertEquals("Digital Source2", DataAccessorFactory.getInstance().getDigitalAccessor().getName(2));
        Assert.assertEquals("Digital Source3", DataAccessorFactory.getInstance().getDigitalAccessor().getName(3));
        Assert.assertEquals("Digital Source4", DataAccessorFactory.getInstance().getDigitalAccessor().getName(4));
        Assert.assertEquals("Digital Source5", DataAccessorFactory.getInstance().getDigitalAccessor().getName(5));
        Assert.assertEquals("Digital Source6", DataAccessorFactory.getInstance().getDigitalAccessor().getName(6));
        Assert.assertEquals("Digital Source7", DataAccessorFactory.getInstance().getDigitalAccessor().getName(7));
        Assert.assertEquals("Digital Source8", DataAccessorFactory.getInstance().getDigitalAccessor().getName(8));
        Assert.assertEquals("Digital Source9", DataAccessorFactory.getInstance().getDigitalAccessor().getName(9));

        Assert.assertEquals("Encoder 0", DataAccessorFactory.getInstance().getEncoderAccessor().getName(0));
        Assert.assertEquals("Encoder 1", DataAccessorFactory.getInstance().getEncoderAccessor().getName(1));
        Assert.assertEquals("Encoder 2", DataAccessorFactory.getInstance().getEncoderAccessor().getName(2));
        Assert.assertTrue(DataAccessorFactory.getInstance().getEncoderAccessor().isHookedUp(0));
        Assert.assertFalse(DataAccessorFactory.getInstance().getEncoderAccessor().isHookedUp(1));
        Assert.assertFalse(DataAccessorFactory.getInstance().getEncoderAccessor().isHookedUp(2));
        Assert.assertEquals(1, DataAccessorFactory.getInstance().getEncoderAccessor().getHookedUpId(0));

        Assert.assertEquals("Analog Gyro", DataAccessorFactory.getInstance().getGyroAccessor().getName(1));

        Assert.assertEquals("Speed Controller 0", DataAccessorFactory.getInstance().getSpeedControllerAccessor().getName(0));
        Assert.assertEquals("Speed Controller 1", DataAccessorFactory.getInstance().getSpeedControllerAccessor().getName(1));
        Assert.assertEquals("Speed Controller 2", DataAccessorFactory.getInstance().getSpeedControllerAccessor().getName(2));
        Assert.assertEquals("Speed Controller 3", DataAccessorFactory.getInstance().getSpeedControllerAccessor().getName(3));
        Assert.assertEquals("Speed Controller 4", DataAccessorFactory.getInstance().getSpeedControllerAccessor().getName(4));
        Assert.assertEquals("Speed Controller 5", DataAccessorFactory.getInstance().getSpeedControllerAccessor().getName(5));
        Assert.assertEquals("Speed Controller 6", DataAccessorFactory.getInstance().getSpeedControllerAccessor().getName(6));
        Assert.assertEquals("Speed Controller 7", DataAccessorFactory.getInstance().getSpeedControllerAccessor().getName(7));
        Assert.assertEquals("Speed Controller 8", DataAccessorFactory.getInstance().getSpeedControllerAccessor().getName(8));

        Assert.assertEquals("Relay 0", DataAccessorFactory.getInstance().getRelayAccessor().getName(0));

        Assert.assertEquals("Solenoid 0", DataAccessorFactory.getInstance().getSolenoidAccessor().getName(0));
        Assert.assertEquals("Solenoid 1", DataAccessorFactory.getInstance().getSolenoidAccessor().getName(1));
        Assert.assertEquals("Solenoid 3", DataAccessorFactory.getInstance().getSolenoidAccessor().getName(3));

        {
            SimpleMotorSimulationConfig config = DataAccessorFactory.getInstance().getSpeedControllerAccessor().getMotorSimSimpleModelConfig(0);
            Assert.assertEquals(12, config.mMaxSpeed, DOUBLE_EPSILON);
        }
        {
            StaticLoadMotorSimulationConfig config = DataAccessorFactory.getInstance().getSpeedControllerAccessor().getMotorSimStaticModelConfig(1);
            Assert.assertEquals(.5, config.mLoad, DOUBLE_EPSILON);
            Assert.assertEquals(6.28, config.mConversionFactor, DOUBLE_EPSILON);
        }

        Assert.assertEquals(3, DataAccessorFactory.getInstance().getSimulatorDataAccessor().getSimulatorComponentConfigs().size());

    }
}
