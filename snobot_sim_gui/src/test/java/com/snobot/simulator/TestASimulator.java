package com.snobot.simulator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
        Assertions.assertTrue(baseSimulator.loadConfig(file));

        // Used to create some components
        new MockRobot();


        Assertions.assertEquals("I2C ADXL345 X Accel", DataAccessorFactory.getInstance().getAccelerometerAccessor().getWrapper(50).getName());
        Assertions.assertEquals("I2C ADXL345 Y Accel", DataAccessorFactory.getInstance().getAccelerometerAccessor().getWrapper(51).getName());
        Assertions.assertEquals("I2C ADXL345 Z Accel", DataAccessorFactory.getInstance().getAccelerometerAccessor().getWrapper(52).getName());
        Assertions.assertEquals("SPI ADXL345 X Accel", DataAccessorFactory.getInstance().getAccelerometerAccessor().getWrapper(103).getName());
        Assertions.assertEquals("SPI ADXL345 Y Accel", DataAccessorFactory.getInstance().getAccelerometerAccessor().getWrapper(104).getName());
        Assertions.assertEquals("SPI ADXL345 Z Accel", DataAccessorFactory.getInstance().getAccelerometerAccessor().getWrapper(105).getName());
        Assertions.assertEquals("SPI ADXL362 X Accel", DataAccessorFactory.getInstance().getAccelerometerAccessor().getWrapper(156).getName());
        Assertions.assertEquals("SPI ADXL362 Y Accel", DataAccessorFactory.getInstance().getAccelerometerAccessor().getWrapper(157).getName());
        Assertions.assertEquals("SPI ADXL362 Z Accel", DataAccessorFactory.getInstance().getAccelerometerAccessor().getWrapper(158).getName());

        Assertions.assertEquals("Analog In 0", DataAccessorFactory.getInstance().getAnalogInAccessor().getWrapper(0).getName());
        Assertions.assertEquals("Analog In 1", DataAccessorFactory.getInstance().getAnalogInAccessor().getWrapper(1).getName());
        Assertions.assertEquals("Analog In 2", DataAccessorFactory.getInstance().getAnalogInAccessor().getWrapper(2).getName());

        Assertions.assertEquals("Digital IO 0", DataAccessorFactory.getInstance().getDigitalAccessor().getWrapper(0).getName());
        Assertions.assertEquals("Digital IO 1", DataAccessorFactory.getInstance().getDigitalAccessor().getWrapper(1).getName());
        Assertions.assertEquals("Digital IO 2", DataAccessorFactory.getInstance().getDigitalAccessor().getWrapper(2).getName());
        Assertions.assertEquals("Digital IO 3", DataAccessorFactory.getInstance().getDigitalAccessor().getWrapper(3).getName());
        Assertions.assertEquals("Digital IO 4", DataAccessorFactory.getInstance().getDigitalAccessor().getWrapper(4).getName());
        Assertions.assertEquals("Digital IO 5", DataAccessorFactory.getInstance().getDigitalAccessor().getWrapper(5).getName());
        Assertions.assertEquals("Digital IO 6", DataAccessorFactory.getInstance().getDigitalAccessor().getWrapper(6).getName());
        Assertions.assertEquals("Digital IO 7", DataAccessorFactory.getInstance().getDigitalAccessor().getWrapper(7).getName());
        Assertions.assertEquals("Digital IO 8", DataAccessorFactory.getInstance().getDigitalAccessor().getWrapper(8).getName());
        Assertions.assertEquals("Digital IO 9", DataAccessorFactory.getInstance().getDigitalAccessor().getWrapper(9).getName());

        Assertions.assertEquals("Encoder 0", DataAccessorFactory.getInstance().getEncoderAccessor().getWrapper(0).getName());
        Assertions.assertEquals("Encoder 1", DataAccessorFactory.getInstance().getEncoderAccessor().getWrapper(1).getName());
        Assertions.assertEquals("Encoder 2", DataAccessorFactory.getInstance().getEncoderAccessor().getWrapper(2).getName());
        Assertions.assertTrue(DataAccessorFactory.getInstance().getEncoderAccessor().getWrapper(0).isHookedUp());
        Assertions.assertFalse(DataAccessorFactory.getInstance().getEncoderAccessor().getWrapper(1).isHookedUp());
        Assertions.assertFalse(DataAccessorFactory.getInstance().getEncoderAccessor().getWrapper(2).isHookedUp());
        Assertions.assertEquals(1, DataAccessorFactory.getInstance().getEncoderAccessor().getWrapper(0).getHookedUpId());

        Assertions.assertEquals("Analog Gyro", DataAccessorFactory.getInstance().getGyroAccessor().getWrapper(1).getName());

        Assertions.assertEquals("Speed Controller 0", DataAccessorFactory.getInstance().getSpeedControllerAccessor().getWrapper(0).getName());
        Assertions.assertEquals("Speed Controller 1", DataAccessorFactory.getInstance().getSpeedControllerAccessor().getWrapper(1).getName());
        Assertions.assertEquals("Speed Controller 2", DataAccessorFactory.getInstance().getSpeedControllerAccessor().getWrapper(2).getName());
        Assertions.assertEquals("Speed Controller 3", DataAccessorFactory.getInstance().getSpeedControllerAccessor().getWrapper(3).getName());
        Assertions.assertEquals("Speed Controller 4", DataAccessorFactory.getInstance().getSpeedControllerAccessor().getWrapper(4).getName());
        Assertions.assertEquals("Speed Controller 5", DataAccessorFactory.getInstance().getSpeedControllerAccessor().getWrapper(5).getName());
        // Assertions.assertEquals("Speed Controller 6",
        // DataAccessorFactory.getInstance().getSpeedControllerAccessor().getWrapper(6).getName());
        Assertions.assertEquals("Speed Controller 7", DataAccessorFactory.getInstance().getSpeedControllerAccessor().getWrapper(7).getName());
        Assertions.assertEquals("Speed Controller 8", DataAccessorFactory.getInstance().getSpeedControllerAccessor().getWrapper(8).getName());

        Assertions.assertEquals("Relay 0", DataAccessorFactory.getInstance().getRelayAccessor().getWrapper(0).getName());

        Assertions.assertEquals("Solenoid 0", DataAccessorFactory.getInstance().getSolenoidAccessor().getWrapper(0).getName());
        Assertions.assertEquals("Solenoid 1", DataAccessorFactory.getInstance().getSolenoidAccessor().getWrapper(1).getName());
        Assertions.assertEquals("Solenoid 3", DataAccessorFactory.getInstance().getSolenoidAccessor().getWrapper(3).getName());

        {
            SimpleMotorSimulationConfig config = DataAccessorFactory.getInstance().getSpeedControllerAccessor().getMotorSimSimpleModelConfig(0);
            Assertions.assertEquals(12, config.mMaxSpeed, DOUBLE_EPSILON);
        }
        {
            StaticLoadMotorSimulationConfig config = DataAccessorFactory.getInstance().getSpeedControllerAccessor().getMotorSimStaticModelConfig(1);
            Assertions.assertEquals(.5, config.mLoad, DOUBLE_EPSILON);
            Assertions.assertEquals(6.28, config.mConversionFactor, DOUBLE_EPSILON);
        }

        Assertions.assertEquals(3, DataAccessorFactory.getInstance().getSimulatorDataAccessor().getSimulatorComponentConfigs().size());
    }
}
