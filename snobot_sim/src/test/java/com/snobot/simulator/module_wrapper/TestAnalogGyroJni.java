package com.snobot.simulator.module_wrapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.snobot.simulator.module_wrapper.interfaces.IGyroWrapper;
import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;
import com.snobot.test.utilities.BaseSimulatorJniTest;

import edu.wpi.first.wpilibj.AnalogGyro;

public class TestAnalogGyroJni extends BaseSimulatorJniTest
{
    @Test
    public void testAnalogGyro()
    {
        AnalogGyro gyro = new AnalogGyro(0);

        int gyroHandle = 0;
        Assertions.assertTrue(DataAccessorFactory.getInstance().getGyroAccessor().getPortList().contains(gyroHandle));
        IGyroWrapper simWrapper = DataAccessorFactory.getInstance().getGyroAccessor().getWrapper(gyroHandle);

        Assertions.assertEquals(0, simWrapper.getAngle(), DOUBLE_EPSILON);
        Assertions.assertEquals(0, gyro.getAngle(), DOUBLE_EPSILON);

        simWrapper.setAngle(90);
        Assertions.assertEquals(90, simWrapper.getAngle(), DOUBLE_EPSILON);
        Assertions.assertEquals(90, gyro.getAngle(), DOUBLE_EPSILON);
    }

    @Test
    public void testAnalogGyroPreSetup()
    {
        int gyroHandle = 1;

        DataAccessorFactory.getInstance().getGyroAccessor().createSimulator(gyroHandle, "com.snobot.simulator.module_wrapper.wpi.WpiAnalogGyroWrapper");
        IGyroWrapper simWrapper = DataAccessorFactory.getInstance().getGyroAccessor().getWrapper(gyroHandle);

        Assertions.assertFalse(simWrapper.isInitialized());

        new AnalogGyro(gyroHandle);
        Assertions.assertTrue(simWrapper.isInitialized());

    }

}
