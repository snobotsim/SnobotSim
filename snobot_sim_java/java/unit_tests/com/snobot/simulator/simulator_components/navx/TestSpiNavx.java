package com.snobot.simulator.simulator_components.navx;

import org.junit.Assert;
import org.junit.Test;

import com.kauailabs.navx.frc.AHRS;
import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.simulator_components.gyro.GyroWrapper;
import com.snobot.test.utilities.BaseSimulatorTest;

import edu.wpi.first.wpilibj.SPI;

public class TestSpiNavx extends BaseSimulatorTest
{

    @Test
    public void testSpiNavx() throws InterruptedException
    {
        final int sleepTime = 100;
        AHRS navx = new AHRS(SPI.Port.kOnboardCS1);
        navx.enableLogging(true);

        Assert.assertEquals(3, SensorActuatorRegistry.get().getGyros().size());
        GyroWrapper yawWrapper = SensorActuatorRegistry.get().getGyros().get(1);
        GyroWrapper pitchWrapper = SensorActuatorRegistry.get().getGyros().get(2);
        GyroWrapper rolWrapper = SensorActuatorRegistry.get().getGyros().get(3);

        Thread.sleep(500);
        Assert.assertEquals(0, yawWrapper.getAngle(), DOUBLE_EPSILON);
        Assert.assertEquals(0, pitchWrapper.getAngle(), DOUBLE_EPSILON);
        Assert.assertEquals(0, rolWrapper.getAngle(), DOUBLE_EPSILON);
        Assert.assertEquals(0, navx.getYaw(), DOUBLE_EPSILON);
        Assert.assertEquals(0, navx.getPitch(), DOUBLE_EPSILON);
        Assert.assertEquals(0, navx.getRoll(), DOUBLE_EPSILON);

        yawWrapper.setAngle(180);
        pitchWrapper.setAngle(-180);
        rolWrapper.setAngle(30);
        Thread.sleep(sleepTime);
        Assert.assertEquals(180, yawWrapper.getAngle(), DOUBLE_EPSILON);
        Assert.assertEquals(-180, pitchWrapper.getAngle(), DOUBLE_EPSILON);
        Assert.assertEquals(30, rolWrapper.getAngle(), DOUBLE_EPSILON);
        Assert.assertEquals(180, navx.getYaw(), DOUBLE_EPSILON);
        Assert.assertEquals(-180, navx.getPitch(), DOUBLE_EPSILON);
        Assert.assertEquals(30, navx.getRoll(), DOUBLE_EPSILON);

        // Test wrap around
        yawWrapper.setAngle(-181);
        pitchWrapper.setAngle(700);
        rolWrapper.setAngle(-470);
        Thread.sleep(sleepTime);
        Assert.assertEquals(-181, yawWrapper.getAngle(), DOUBLE_EPSILON);
        Assert.assertEquals(700, pitchWrapper.getAngle(), DOUBLE_EPSILON);
        Assert.assertEquals(-470, rolWrapper.getAngle(), DOUBLE_EPSILON);
        Assert.assertEquals(179, navx.getYaw(), DOUBLE_EPSILON);
        Assert.assertEquals(-20, navx.getPitch(), DOUBLE_EPSILON);
        Assert.assertEquals(-110, navx.getRoll(), DOUBLE_EPSILON);
    }
}
