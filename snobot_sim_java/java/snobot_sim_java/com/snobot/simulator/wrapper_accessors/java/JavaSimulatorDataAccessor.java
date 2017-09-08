package com.snobot.simulator.wrapper_accessors.java;

import com.snobot.simulator.DcMotorModelConfig;
import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.jni.RegisterCallbacksJni;
import com.snobot.simulator.jni.SensorFeedbackJni;
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
    public boolean connectTankDriveSimulator(int leftEncHandle, int rightEncHandle, int gyroHandle, double turnKp)
    {
        EncoderWrapper rightEncWrapper = SensorActuatorRegistry.get().getEncoders().get(rightEncHandle);
        EncoderWrapper leftEncWrapper = SensorActuatorRegistry.get().getEncoders().get(leftEncHandle);
        GyroWrapper gyroWrapper = SensorActuatorRegistry.get().getGyros().get(gyroHandle);

        TankDriveGyroSimulator simulator = new TankDriveGyroSimulator(leftEncWrapper, rightEncWrapper, gyroWrapper);
        simulator.setTurnKp(turnKp);

        return simulator.isSetup();
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
            modelConfig = VexMotorFactory.createMotor(motorType);
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
    public boolean setSpeedControllerModel_Simple(int aScHandle, double maxSpeed)
    {
        boolean success = false;

        PwmWrapper speedController = SensorActuatorRegistry.get().getSpeedControllers().get(aScHandle);
        if (speedController != null)
        {
            speedController.setMotorSimulator(new SimpleMotorSimulator(maxSpeed));
            success = true;
        }
        else
        {
            System.err.println("Unknown speed controller " + aScHandle);
        }

        return success;
    }

    private DcMotorModel convert(DcMotorModelConfig aIn)
    {
        return new DcMotorModel(aIn.NOMINAL_VOLTAGE, aIn.FREE_SPEED_RPM, aIn.FREE_CURRENT, aIn.STALL_TORQUE, aIn.STALL_CURRENT, aIn.mMotorInertia);
    }

    @Override
    public boolean setSpeedControllerModel_Static(int aScHandle, DcMotorModelConfig motorConfig, double load)
    {
        return setSpeedControllerModel_Static(aScHandle, motorConfig, load, 1.0);
    }

    @Override
    public boolean setSpeedControllerModel_Static(int aScHandle, DcMotorModelConfig motorConfig, double load, double conversionFactor)
    {
        boolean success = false;
        PwmWrapper wrapper = SensorActuatorRegistry.get().getSpeedControllers().get(aScHandle);
        IMotorSimulator motorModel = new StaticLoadDcMotorSim(convert(motorConfig), load);
        if (wrapper != null)
        {
            wrapper.setMotorSimulator(motorModel);
            success = true;
        }
        else
        {
            System.err.println("Unknown speed controller " + aScHandle);
        }

        return success;
    }

    @Override
    public boolean setSpeedControllerModel_Gravitational(int aScHandle, DcMotorModelConfig motorConfig, double load)
    {
        boolean success = false;
        PwmWrapper wrapper = SensorActuatorRegistry.get().getSpeedControllers().get(aScHandle);
        IMotorSimulator motorModel = new GravityLoadDcMotorSim(convert(motorConfig), load);
        if (wrapper != null)
        {
            wrapper.setMotorSimulator(motorModel);
            success = true;
        }
        else
        {
            System.err.println("Unknown speed controller " + aScHandle);
        }

        return success;
    }

    @Override
    public boolean setSpeedControllerModel_Rotational(int aScHandle, DcMotorModelConfig motorConfig, double armCenterOfMass, double armMass)
    {
        boolean success = false;
        PwmWrapper wrapper = SensorActuatorRegistry.get().getSpeedControllers().get(aScHandle);
        IMotorSimulator motorModel = new RotationalLoadDcMotorSim(convert(motorConfig), wrapper, armCenterOfMass, armMass);
        if (wrapper != null)
        {
            wrapper.setMotorSimulator(motorModel);
            success = true;
        }
        else
        {
            System.err.println("Unknown speed controller " + aScHandle);
        }

        return success;
    }

    @Override
    public void setDisabled(boolean aDisabled)
    {
        SensorFeedbackJni.setEnabled(!aDisabled);
    }

    @Override
    public void setAutonomous(boolean aAuton)
    {
        SensorFeedbackJni.setAutonomous(aAuton);
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
            SensorFeedbackJni.notifyDsOfData();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void setJoystickInformation(int aJoystickHandle, float[] aAxesArray, short[] aPovsArray, int aButtonCount, int aButtonMask)
    {
        SensorFeedbackJni.setJoystickInformation(aJoystickHandle, aAxesArray, aPovsArray, aButtonCount, aButtonMask);
    }

    @Override
    public void setUpdateRate(double aUpdatePeriod)
    {
        mUpdatePeriod = aUpdatePeriod;
    }

}
