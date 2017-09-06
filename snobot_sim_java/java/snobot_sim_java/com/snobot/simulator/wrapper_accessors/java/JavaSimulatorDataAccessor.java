package com.snobot.simulator.wrapper_accessors.java;

import com.snobot.simulator.DcMotorModelConfig;
import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.jni.RegisterCallbacksJni;
import com.snobot.simulator.wrapper_accessors.SimulatorDataAccessor;

public class JavaSimulatorDataAccessor implements SimulatorDataAccessor
{

    @Override
    public void setLogLevel(SnobotLogLevel logLevel)
    {

    }

    @Override
    public String getNativeBuildVersion()
    {
        return "TODO";
    }

    @Override
    public void reset()
    {
        RegisterCallbacksJni.reset();
        SensorActuatorRegistry.get().reset();
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
        // throw new UnsupportedOperationException();
    }

    @Override
    public void setAutonomous(boolean b)
    {
        // throw new UnsupportedOperationException();
    }

    @Override
    public double getMatchTime()
    {
        return 1;
        // throw new UnsupportedOperationException();
    }

    @Override
    public void waitForProgramToStart()
    {
        // throw new UnsupportedOperationException();

        try
        {
            Thread.sleep(1500);
        }
        catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void updateLoop()
    {
        // throw new UnsupportedOperationException();
    }

    @Override
    public void waitForNextUpdateLoop()
    {
        try
        {
            Thread.sleep(20);
        }
        catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // throw new UnsupportedOperationException();
    }

    @Override
    public void setJoystickInformation(int i, float[] axisValues, short[] povValues, int buttonCount, int buttonMask)
    {
        // throw new UnsupportedOperationException();
    }

}
