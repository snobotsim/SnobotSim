package com.snobot.simulator.wrapper_accessors.jni;

import com.snobot.simulator.DcMotorModelConfig;
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
    public void connectTankDriveSimulator(int leftEncHandle, int rightEncHandle, int scHandle, double turnKp)
    {
        throw new UnsupportedOperationException();
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
    public void setSpeedControllerModel_Simple(int aScHandle, double maxSpeed)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSpeedControllerModel_Static(int mHandle, DcMotorModelConfig motorConfig, double load)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSpeedControllerModel_Static(int aScHandle, DcMotorModelConfig motorConfig, double load, double conversionFactor)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSpeedControllerModel_Gravitational(int aScHandle, DcMotorModelConfig motorConfig, double load)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSpeedControllerModel_Rotational(int aScHandle, DcMotorModelConfig motorConfig, double armCenterOfMass, double armMass)
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
    public void updateLoop()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void waitForNextUpdateLoop()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setJoystickInformation(int i, float[] axisValues, short[] povValues, int buttonCount, int buttonMask)
    {
        throw new UnsupportedOperationException();
    }

}
