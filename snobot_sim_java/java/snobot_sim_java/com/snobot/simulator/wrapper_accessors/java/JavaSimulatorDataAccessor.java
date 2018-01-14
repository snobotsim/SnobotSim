package com.snobot.simulator.wrapper_accessors.java;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.jni.DriverStationSimulatorJni;
import com.snobot.simulator.jni.RegisterCallbacksJni;
import com.snobot.simulator.jni.standard_components.I2CCallbackJni;
import com.snobot.simulator.jni.standard_components.SpiCallbackJni;
import com.snobot.simulator.module_wrapper.PwmWrapper;
import com.snobot.simulator.motor_sim.DcMotorModel;
import com.snobot.simulator.motor_sim.DcMotorModelConfig;
import com.snobot.simulator.motor_sim.GravityLoadDcMotorSim;
import com.snobot.simulator.motor_sim.GravityLoadMotorSimulationConfig;
import com.snobot.simulator.motor_sim.IMotorSimulator;
import com.snobot.simulator.motor_sim.RotationalLoadDcMotorSim;
import com.snobot.simulator.motor_sim.RotationalLoadMotorSimulationConfig;
import com.snobot.simulator.motor_sim.SimpleMotorSimulationConfig;
import com.snobot.simulator.motor_sim.SimpleMotorSimulator;
import com.snobot.simulator.motor_sim.StaticLoadDcMotorSim;
import com.snobot.simulator.motor_sim.StaticLoadMotorSimulationConfig;
import com.snobot.simulator.motor_sim.motor_factory.MakeTransmission;
import com.snobot.simulator.motor_sim.motor_factory.PublishedMotorFactory;
import com.snobot.simulator.motor_sim.motor_factory.VexMotorFactory;
import com.snobot.simulator.simulator_components.ISimulatorUpdater;
import com.snobot.simulator.simulator_components.TankDriveGyroSimulator;
import com.snobot.simulator.wrapper_accessors.SimulatorDataAccessor;

public class JavaSimulatorDataAccessor implements SimulatorDataAccessor
{
    private static final Logger sLOGGER = Logger.getLogger(JavaSimulatorDataAccessor.class);

    @Override
    public void setLogLevel(SnobotLogLevel logLevel)
    {
    }

    @Override
    public String getNativeBuildVersion()
    {
        return JaveSimulatorVersion.Version;
    }

    @Override
    public void reset()
    {
        getDefaultI2CWrappers().clear();
        getDefaultSpiWrappers().clear();
        SensorActuatorRegistry.get().reset();
        RegisterCallbacksJni.reset();
    }

    @Override
    public boolean connectTankDriveSimulator(int leftEncHandle, int rightEncHandle, int gyroHandle, double turnKp)
    {
        TankDriveGyroSimulator simulator = new TankDriveGyroSimulator(
                new TankDriveGyroSimulator.TankDriveConfig(leftEncHandle, rightEncHandle, gyroHandle, turnKp));

        return simulator.isSetup();
    }

    @Override
    public Collection<Object> getSimulatorComponentConfigs()
    {
        Collection<Object> output = new ArrayList<>();

        for (ISimulatorUpdater sim : SensorActuatorRegistry.get().getSimulatorComponents())
        {
            output.add(sim.getConfig());
        }

        return output;
    }

    @Override
    public DcMotorModelConfig createMotor(String motorType)
    {
        if ("rs775".equals(motorType))
        {
            return PublishedMotorFactory.makeRS775();
        }
        else
        {
            return VexMotorFactory.createMotor(motorType);
        }
    }

    @Override
    public DcMotorModelConfig createMotor(String motorType, int numMotors, double gearReduction, double efficiency, boolean aInverted, boolean aBrake)
    {
        DcMotorModelConfig config=  createMotor(motorType);
        config.mFactoryParams.mHasBrake = aBrake;
        config.mFactoryParams.mInverted = aInverted;
        return MakeTransmission.makeTransmission(config, numMotors, gearReduction, efficiency);
    }

    @Override
    public boolean setSpeedControllerModel_Simple(int aScHandle, SimpleMotorSimulationConfig aConfig)
    {
        boolean success = false;

        PwmWrapper speedController = SensorActuatorRegistry.get().getSpeedControllers().get(aScHandle);
        if (speedController != null)
        {
            speedController.setMotorSimulator(new SimpleMotorSimulator(aConfig));
            success = true;
        }
        else
        {
            sLOGGER.log(Level.ERROR, "Unknown speed controller " + aScHandle);
        }

        return success;
    }

    @Override
    public boolean setSpeedControllerModel_Static(int aScHandle, DcMotorModelConfig motorConfig, StaticLoadMotorSimulationConfig aConfig)
    {
        boolean success = false;
        PwmWrapper wrapper = SensorActuatorRegistry.get().getSpeedControllers().get(aScHandle);
        IMotorSimulator motorModel = new StaticLoadDcMotorSim(new DcMotorModel(motorConfig), aConfig);
        if (wrapper != null)
        {
            wrapper.setMotorSimulator(motorModel);
            success = true;
        }
        else
        {
            sLOGGER.log(Level.ERROR, "Unknown speed controller " + aScHandle);
        }

        return success;
    }

    @Override
    public boolean setSpeedControllerModel_Gravitational(int aScHandle, DcMotorModelConfig motorConfig, GravityLoadMotorSimulationConfig aConfig)
    {
        boolean success = false;
        PwmWrapper wrapper = SensorActuatorRegistry.get().getSpeedControllers().get(aScHandle);
        IMotorSimulator motorModel = new GravityLoadDcMotorSim(new DcMotorModel(motorConfig), aConfig);
        if (wrapper != null)
        {
            wrapper.setMotorSimulator(motorModel);
            success = true;
        }
        else
        {
            sLOGGER.log(Level.ERROR, "Unknown speed controller " + aScHandle);
        }

        return success;
    }

    @Override
    public boolean setSpeedControllerModel_Rotational(int aScHandle, DcMotorModelConfig motorConfig, RotationalLoadMotorSimulationConfig aConfig)
    {
        boolean success = false;
        PwmWrapper wrapper = SensorActuatorRegistry.get().getSpeedControllers().get(aScHandle);
        IMotorSimulator motorModel = new RotationalLoadDcMotorSim(new DcMotorModel(motorConfig), wrapper, aConfig);
        if (wrapper != null)
        {
            wrapper.setMotorSimulator(motorModel);
            success = true;
        }
        else
        {
            sLOGGER.log(Level.ERROR, "Unknown speed controller " + aScHandle);
        }

        return success;
    }

    @Override
    public void setDisabled(boolean aDisabled)
    {
        DriverStationSimulatorJni.setEnabled(!aDisabled);
    }

    @Override
    public void setAutonomous(boolean aAuton)
    {
        DriverStationSimulatorJni.setAutonomous(aAuton);
    }

    @Override
    public double getMatchTime()
    {
        return DriverStationSimulatorJni.getMatchTime();
    }

    @Override
    public void waitForProgramToStart()
    {
        DriverStationSimulatorJni.waitForProgramToStart();
    }

    @Override
    public void updateSimulatorComponents(double aUpdatePeriod)
    {
        for (PwmWrapper wrapper : SensorActuatorRegistry.get().getSpeedControllers().values())
        {
            wrapper.update(aUpdatePeriod);
            // tankDriveSimulator.update();
        }

        for (ISimulatorUpdater updater : SensorActuatorRegistry.get().getSimulatorComponents())
        {
            updater.update();
        }
    }

    @Override
    public void waitForNextUpdateLoop(double aUpdatePeriod)
    {
        DriverStationSimulatorJni.delayForNextUpdateLoop(aUpdatePeriod);
    }

    @Override
    public void setJoystickInformation(int aJoystickHandle, float[] aAxesArray, short[] aPovsArray, int aButtonCount, int aButtonMask)
    {
        DriverStationSimulatorJni.setJoystickInformation(aJoystickHandle, aAxesArray, aPovsArray, aButtonCount, aButtonMask);
    }

    @Override
    public void setMatchInfo(String eventName, MatchType matchType, int matchNumber, int replayNumber, String gameSpecificMessage)
    {
        DriverStationSimulatorJni.setMatchInfo(eventName, matchType.ordinal(), matchNumber, replayNumber, gameSpecificMessage);
    }

    @Override
    public void setDefaultSpiSimulator(int aPort, String aType)
    {
        SpiCallbackJni.setDefaultWrapper(aPort, aType);
    }

    @Override
    public void setDefaultI2CSimulator(int aPort, String aType)
    {
        I2CCallbackJni.setDefaultWrapper(aPort, aType);
    }

    @Override
    public Collection<String> getAvailableSpiSimulators()
    {
        return SpiCallbackJni.getAvailableTypes();
    }

    @Override
    public Collection<String> getAvailableI2CSimulators()
    {
        return I2CCallbackJni.getAvailableTypes();
    }

    @Override
    public Map<Integer, String> getDefaultI2CWrappers()
    {
        return I2CCallbackJni.getDefaults();
    }

    @Override
    public Map<Integer, String> getDefaultSpiWrappers()
    {
        return SpiCallbackJni.getDefaults();
    }

    @Override
    public void removeSimulatorComponent(Object comp)
    {
        for (ISimulatorUpdater sim : SensorActuatorRegistry.get().getSimulatorComponents())
        {
            if (sim.getConfig().equals(comp))
            {
                SensorActuatorRegistry.get().getSimulatorComponents().remove(sim);
                break;
            }
        }
    }

}
