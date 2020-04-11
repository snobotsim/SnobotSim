package com.snobot.simulator.simulator_components.rev;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.snobot.simulator.wrapper_accessors.jni.JniSpeedControllerWrapperAccessor;
import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;
import com.snobot.test.utilities.BaseSimulatorJniTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Collection;

public class TestRevControlFollower extends BaseSimulatorJniTest
{

    private static final double sDOUBLE_EPSILON = 1.0 / 1023;
    private static final int sFOLLOWER_ID = 11;

    @ParameterizedTest
	@ArgumentsSource(GetRevTestIds.class)
    public void testFollower(int aCanHandle)
    {
        if (aCanHandle == sFOLLOWER_ID)
        {
            return;
        }

        int rawHandle = aCanHandle + JniSpeedControllerWrapperAccessor.sCAN_SC_OFFSET;
        int followerRawHandle = sFOLLOWER_ID + JniSpeedControllerWrapperAccessor.sCAN_SC_OFFSET;

        Assertions.assertEquals(0, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPortList().size());

        CANSparkMax sparksMax = new CANSparkMax(aCanHandle, CANSparkMaxLowLevel.MotorType.kBrushless);
        CANSparkMax follower = new CANSparkMax(sFOLLOWER_ID, CANSparkMaxLowLevel.MotorType.kBrushless);
        Assertions.assertEquals(2, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPortList().size());

        follower.follow(sparksMax);

        sparksMax.set(-0.5);
        Assertions.assertEquals(-0.5, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVoltagePercentage(rawHandle), sDOUBLE_EPSILON);
        Assertions.assertEquals(-0.5, sparksMax.get(), sDOUBLE_EPSILON);
        Assertions.assertEquals(-0.5, sparksMax.getAppliedOutput(), sDOUBLE_EPSILON);

        Assertions.assertEquals(-0.5, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVoltagePercentage(followerRawHandle), sDOUBLE_EPSILON);
//        Assertions.assertEquals(-0.5, follower.get(), sDOUBLE_EPSILON); // Doesn't work, vendor issue
        Assertions.assertEquals(-0.5, sparksMax.getAppliedOutput(), sDOUBLE_EPSILON);

        sparksMax.close();
        follower.close();

    }
}