package com.snobot.simulator.wrapper_accessors.java;

import com.snobot.simulator.DcMotorModelConfig;
import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.jni.RegisterCallbacksJni;
import com.snobot.simulator.module_wrapper.EncoderWrapper;
import com.snobot.simulator.module_wrapper.PwmWrapper;
import com.snobot.simulator.motor_sim.DcMotorModel;
import com.snobot.simulator.motor_sim.GravityLoadDcMotorSim;
import com.snobot.simulator.motor_sim.IMotorSimulator;
import com.snobot.simulator.motor_sim.RotationalLoadDcMotorSim;
import com.snobot.simulator.motor_sim.SimpleMotorSimulator;
import com.snobot.simulator.motor_sim.StaticLoadDcMotorSim;
import com.snobot.simulator.motor_sim.motor_factory.MakeTransmission;
import com.snobot.simulator.motor_sim.motor_factory.PublishedMotorFactory;
import com.snobot.simulator.motor_sim.motor_factory.VexMotorFactory;
import com.snobot.simulator.simulator_components.ISimulatorUpdater;
import com.snobot.simulator.simulator_components.TankDriveGyroSimulator;
import com.snobot.simulator.simulator_components.gyro.GyroWrapper;
import com.snobot.simulator.wrapper_accessors.SimulatorDataAccessor;

public class JavaSimulatorDataAccessor implements SimulatorDataAccessor
{
    private double mUpdatePeriod = .02;

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
    public void connectTankDriveSimulator(int leftEncHandle, int rightEncHandle, int gyroHandle, double turnKp)
    {
        EncoderWrapper rightEncWrapper = SensorActuatorRegistry.get().getEncoders().get(rightEncHandle);
        EncoderWrapper leftEncWrapper = SensorActuatorRegistry.get().getEncoders().get(leftEncHandle);
        GyroWrapper gyroWrapper = SensorActuatorRegistry.get().getGyros().get(gyroHandle);

        TankDriveGyroSimulator simulator = new TankDriveGyroSimulator(leftEncWrapper, rightEncWrapper, gyroWrapper);
        simulator.setTurnKp(turnKp);
    }

    private DcMotorModel getModelConfig(String motorType)
    {
        DcMotorModel modelConfig;
        if ("rs775".equals(motorType))
        {
            modelConfig = PublishedMotorFactory.makeRS775();
        }
        else
        {
            modelConfig = VexMotorFactory.make775Pro();
        }

        return modelConfig;
    }

    @Override
    public DcMotorModelConfig createMotor(String motorType)
    {
        return DcMotorModel.convert(motorType, getModelConfig(motorType));
    }

    @Override
    public DcMotorModelConfig createMotor(String motorType, int numMotors, double gearReduction, double efficiency)
    {
        DcMotorModel throughTranny = MakeTransmission.makeTransmission(getModelConfig(motorType), numMotors, gearReduction, efficiency);

        return DcMotorModel.convert(motorType, throughTranny);
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

    private DcMotorModel convert(DcMotorModelConfig aIn)
    {
        return new DcMotorModel(
                aIn.NOMINAL_VOLTAGE, 
                aIn.FREE_SPEED_RPM, 
                aIn.FREE_CURRENT, 
                aIn.STALL_TORQUE, 
                aIn.STALL_CURRENT, 
                aIn.mMotorInertia);
    }

    @Override
    public void setSpeedControllerModel_Static(int aScHandle, DcMotorModelConfig motorConfig, double load)
    {
        setSpeedControllerModel_Static(aScHandle, motorConfig, load, 1.0);
    }

    @Override
    public void setSpeedControllerModel_Static(int aScHandle, DcMotorModelConfig motorConfig, double load, double conversionFactor)
    {
        PwmWrapper wrapper = SensorActuatorRegistry.get().getSpeedControllers().get(aScHandle);
        IMotorSimulator motorModel = new StaticLoadDcMotorSim(convert(motorConfig), load);
        if (wrapper != null)
        {
            wrapper.setMotorSimulator(motorModel);
        }
        else
        {
            System.err.println("Unknown speed controller " + aScHandle);
        }
    }

    @Override
    public void setSpeedControllerModel_Gravitational(int aScHandle, DcMotorModelConfig motorConfig, double load)
    {
        PwmWrapper wrapper = SensorActuatorRegistry.get().getSpeedControllers().get(aScHandle);
        IMotorSimulator motorModel = new GravityLoadDcMotorSim(convert(motorConfig), load);
        if (wrapper != null)
        {
            wrapper.setMotorSimulator(motorModel);
        }
        else
        {
            System.err.println("Unknown speed controller " + aScHandle);
        }
    }

    @Override
    public void setSpeedControllerModel_Rotational(int aScHandle, DcMotorModelConfig motorConfig, double armCenterOfMass, double armMass)
    {
        PwmWrapper wrapper = SensorActuatorRegistry.get().getSpeedControllers().get(aScHandle);
        IMotorSimulator motorModel = new RotationalLoadDcMotorSim(convert(motorConfig), wrapper, armCenterOfMass, armMass);
        if (wrapper != null)
        {
            wrapper.setMotorSimulator(motorModel);
        }
        else
        {
            System.err.println("Unknown speed controller " + aScHandle);
        }
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
            wrapper.update(mUpdatePeriod);
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

    @Override
    public void setUpdateRate(double aUpdatePeriod)
    {
        mUpdatePeriod = aUpdatePeriod;
    }

}
