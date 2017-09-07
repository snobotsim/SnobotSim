package com.snobot.simulator.wrapper_accessors.java;

import com.snobot.simulator.DcMotorModelConfig;
import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.jni.RegisterCallbacksJni;
import com.snobot.simulator.module_wrapper.PwmWrapper;
import com.snobot.simulator.motor_sim.SimpleMotorSimulator;
import com.snobot.simulator.simulator_components.ISimulatorUpdater;
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

    }

    @Override
    public DcMotorModelConfig createMotor(String selectedMotor, int numMotors, double gearReduction, double efficiency)
    {
        return new DcMotorModelConfig("", 0, 0, 0, 0, 0, 0, 0, 0, 0);
    }

    @Override
    public DcMotorModelConfig createMotor(String motorType)
    {
        return new DcMotorModelConfig("", 0, 0, 0, 0, 0, 0, 0, 0, 0);
    }

    @Override
    public void setSpeedControllerModel_Simple(int aScHandle, double maxSpeed)
    {
        PwmWrapper speedController = SensorActuatorRegistry.get().getSpeedControllers().get(aScHandle);
        if (speedController != null)
        {
            speedController.setMotorSimulator(new SimpleMotorSimulator(maxSpeed));
        }
        else
        {
            System.err.println("Unknown speed controller " + aScHandle);
        }
    }

    @Override
    public void setSpeedControllerModel_Static(int mHandle, DcMotorModelConfig motorConfig, double load)
    {

    }

    @Override
    public void setSpeedControllerModel_Static(int aScHandle, DcMotorModelConfig motorConfig, double load, double conversionFactor)
    {

    }

    @Override
    public void setSpeedControllerModel_Gravitational(int aScHandle, DcMotorModelConfig motorConfig, double load)
    {

    }

    @Override
    public void setSpeedControllerModel_Rotational(int aScHandle, DcMotorModelConfig motorConfig, double armCenterOfMass, double armMass)
    {

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
            e.printStackTrace();
        }
    }

    @Override
    public void updateLoop()
    {
        for (PwmWrapper wrapper : SensorActuatorRegistry.get().getSpeedControllers().values())
        {
            wrapper.update(.02);
            // tankDriveSimulator.update();
        }

        for (ISimulatorUpdater updater : SensorActuatorRegistry.get().getSimulatorComponents())
        {
            updater.update();
        }
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
