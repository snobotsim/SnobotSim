package com.snobot.simulator.wrapper_accessors.jni;

import com.snobot.simulator.jni.SimulationConnectorJni;
import com.snobot.simulator.motor_sim.DcMotorModelConfig;
import com.snobot.simulator.motor_sim.GravityLoadMotorSimulationConfig;
import com.snobot.simulator.motor_sim.RotationalLoadMotorSimulationConfig;
import com.snobot.simulator.motor_sim.SimpleMotorSimulationConfig;
import com.snobot.simulator.motor_sim.StaticLoadMotorSimulationConfig;
import com.snobot.simulator.wrapper_accessors.SimulatorDataAccessor;

public class JniSimulatorDataAccessor implements SimulatorDataAccessor
{

    @Override
    public void setLogLevel(SnobotLogLevel logLevel)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getNativeBuildVersion()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void reset()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean connectTankDriveSimulator(int leftEncHandle, int rightEncHandle, int aGyroHandle, double turnKp)
    {
        SimulationConnectorJni.connectTankDriveSimulator(leftEncHandle, rightEncHandle, aGyroHandle, turnKp);
        return true;
    }

    @Override
    public DcMotorModelConfig createMotor(String selectedMotor, int numMotors, double gearReduction, double efficiency)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public DcMotorModelConfig createMotor(String motorType)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean setSpeedControllerModel_Simple(int aScHandle, SimpleMotorSimulationConfig aConfig)
    {
        SimulationConnectorJni.setSpeedControllerModel_Simple(aScHandle, aConfig.mMaxSpeed);
        return true;
    }

    @Override
    public boolean setSpeedControllerModel_Static(int mHandle, DcMotorModelConfig motorConfig, StaticLoadMotorSimulationConfig aConfig)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean setSpeedControllerModel_Gravitational(int aScHandle, DcMotorModelConfig aMotorConfig, GravityLoadMotorSimulationConfig aConfig)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean setSpeedControllerModel_Rotational(int aScHandle, DcMotorModelConfig motorConfig, RotationalLoadMotorSimulationConfig aConfig)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDisabled(boolean b)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setAutonomous(boolean b)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getMatchTime()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void waitForProgramToStart()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void updateSimulatorComponents(double aUpdatePeriod)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void waitForNextUpdateLoop(double aUpdatePeriod)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setJoystickInformation(int i, float[] axisValues, short[] povValues, int buttonCount, int buttonMask)
    {
        throw new UnsupportedOperationException();
    }

}
