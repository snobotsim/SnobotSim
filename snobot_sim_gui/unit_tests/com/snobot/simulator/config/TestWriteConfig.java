package com.snobot.simulator.config;

import org.junit.Assert;
import org.junit.Test;

import com.snobot.simulator.motor_sim.DcMotorModelConfig;
import com.snobot.simulator.motor_sim.SimpleMotorSimulationConfig;
import com.snobot.simulator.motor_sim.StaticLoadMotorSimulationConfig;
import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;
import com.snobot.test.utilities.BaseSimulatorTest;
import com.snobot.test.utilities.MockRobot;

public class TestWriteConfig extends BaseSimulatorTest
{

    @Test
    public void testWriteConfig()
    {
        String dump_file = "test_output/testWriteFile.yml";

        // mAdxl345I2cAccel = new ADXL345_I2C(I2C.Port.kOnboard, Range.k2G);
        // mAdxr450SpiGyro0 = new ADXRS450_Gyro();
        // mAdxl345SpiAccel = new ADXL345_SPI(SPI.Port.kOnboardCS1, Range.k2G);
        // mAdxl362SpiAccel = new ADXL362(SPI.Port.kOnboardCS2, Range.k2G);
        // mAdxr450SpiGyro1 = new ADXRS450_Gyro(SPI.Port.kOnboardCS3);
        DataAccessorFactory.getInstance().getSimulatorDataAccessor().setDefaultSpiSimulator(0, "ADXRS450");
        DataAccessorFactory.getInstance().getSimulatorDataAccessor().setDefaultSpiSimulator(1, "ADXL345");
        DataAccessorFactory.getInstance().getSimulatorDataAccessor().setDefaultSpiSimulator(2, "ADXL362");
        DataAccessorFactory.getInstance().getSimulatorDataAccessor().setDefaultSpiSimulator(3, "ADXRS450");
        DataAccessorFactory.getInstance().getSimulatorDataAccessor().setDefaultI2CSimulator(0, "ADXL345");

        // Used to create some components
        new MockRobot();

        DataAccessorFactory.getInstance().getEncoderAccessor().connectSpeedController(0, 1);
        DataAccessorFactory.getInstance().getSimulatorDataAccessor().setSpeedControllerModel_Simple(0, new SimpleMotorSimulationConfig(12));

        DataAccessorFactory.getInstance().getSimulatorDataAccessor().connectTankDriveSimulator(0, 1, 1, .5);
        DataAccessorFactory.getInstance().getSimulatorDataAccessor().connectTankDriveSimulator(0, 1, 100, 1);
        DataAccessorFactory.getInstance().getSimulatorDataAccessor().connectTankDriveSimulator(0, 1, 103, 2);

        DcMotorModelConfig motorConfig = DataAccessorFactory.getInstance().getSimulatorDataAccessor().createMotor("rs775", 1, 10, 1);
        DataAccessorFactory.getInstance().getSimulatorDataAccessor().setSpeedControllerModel_Static(1, motorConfig,
                new StaticLoadMotorSimulationConfig(.5, 6.28));

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
