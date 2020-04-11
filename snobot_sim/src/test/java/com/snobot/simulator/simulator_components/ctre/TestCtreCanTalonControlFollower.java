package com.snobot.simulator.simulator_components.ctre;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.MethodSource;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;
import com.snobot.test.utilities.BaseSimulatorJniTest;

@Tag("CTRE")
public class TestCtreCanTalonControlFollower extends BaseSimulatorJniTest
{
    private static final double sDOUBLE_EPSILON = 1.0 / 1023;

    @ParameterizedTest
	@ArgumentsSource(GetCtreTestIds.class)
    public void testSetWithFollower(int aCanHandle)
    {
        int leadTalonId = 5;
        if (aCanHandle == leadTalonId)
        {
            return;
        }

        Assertions.assertEquals(0, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPortList().size());
        TalonSRX leadTalon = new TalonSRX(leadTalonId);
        TalonSRX talon = new TalonSRX(aCanHandle);
        talon.follow(leadTalon);
        Assertions.assertEquals(2, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPortList().size());

        leadTalon.set(ControlMode.PercentOutput, .5);

        Assertions.assertEquals(.5, talon.getMotorOutputPercent(), sDOUBLE_EPSILON);
        Assertions.assertEquals(leadTalon.getMotorOutputPercent(), talon.getMotorOutputPercent(), sDOUBLE_EPSILON);
    }
}