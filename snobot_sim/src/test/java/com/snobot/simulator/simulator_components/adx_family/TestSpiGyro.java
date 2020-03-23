package com.snobot.simulator.simulator_components.adx_family;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.snobot.simulator.module_wrapper.interfaces.IGyroWrapper;
import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;
import com.snobot.test.utilities.BaseSimulatorJniTest;
import com.snobot.test.utilities.SimDeviceDumpHelper;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.SPI;

public class TestSpiGyro extends BaseSimulatorJniTest
{
    public static Collection<SPI.Port> getData()
    {
        Collection<SPI.Port> output = new ArrayList<>();

        output.addAll(Arrays.asList(SPI.Port.values()));

        return output;
    }

    @ParameterizedTest
    @MethodSource("getData")
    public void testSpiGyro(SPI.Port aPort)
    {
        DataAccessorFactory.getInstance().getSpiAccessor().createSpiSimulator(aPort.value, "ADXRS450");
        ADXRS450_Gyro gyro = new ADXRS450_Gyro(aPort);
        SimDeviceDumpHelper.dumpSimDevices();

        int gyroHandle = 100 + aPort.value;
        Assertions.assertTrue(DataAccessorFactory.getInstance().getGyroAccessor().getPortList().contains(gyroHandle));
        IGyroWrapper wrapper = DataAccessorFactory.getInstance().getGyroAccessor().getWrapper(gyroHandle);

        Assertions.assertEquals(0, wrapper.getAngle(), DOUBLE_EPSILON);
        Assertions.assertEquals(0, gyro.getAngle(), DOUBLE_EPSILON);

        wrapper.setAngle(90);
        Assertions.assertEquals(90, wrapper.getAngle(), DOUBLE_EPSILON);
        Assertions.assertEquals(90, gyro.getAngle(), DOUBLE_EPSILON);

        wrapper.setAngle(192.1234);
        Assertions.assertEquals(192.1234, wrapper.getAngle(), DOUBLE_EPSILON);
        Assertions.assertEquals(192.1234, gyro.getAngle(), DOUBLE_EPSILON);

        wrapper.setAngle(359.9999);
        Assertions.assertEquals(359.9999, wrapper.getAngle(), DOUBLE_EPSILON);
        Assertions.assertEquals(359.9999, gyro.getAngle(), DOUBLE_EPSILON);

        wrapper.setAngle(-421.3358);
        Assertions.assertEquals(-421.3358, wrapper.getAngle(), DOUBLE_EPSILON);
        Assertions.assertEquals(-421.3358, gyro.getAngle(), DOUBLE_EPSILON);

        // Reset
        gyro.reset();
        Assertions.assertEquals(0, gyro.getAngle(), DOUBLE_EPSILON);
    }

    @Disabled
    @ParameterizedTest
    @MethodSource("getData")
    public void longTest(SPI.Port aPort)
    {
        int gyroHandle = 100 + aPort.value;
        DataAccessorFactory.getInstance().getSpiAccessor().createSpiSimulator(aPort.value, "ADXRS450");
        ADXRS450_Gyro gyro = new ADXRS450_Gyro(aPort);

        for (int i = 0; i < 1e8; ++i)
        {
            DataAccessorFactory.getInstance().getGyroAccessor().getWrapper(gyroHandle).setAngle(i);
            gyro.getAngle();
        }
    }
}
