package com.snobot.simulator.wrapper_accessors.java;

import java.util.Map;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.module_wrapper.PwmWrapper;
import com.snobot.simulator.motor_sim.BaseDcMotorSimulator;
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
import com.snobot.simulator.wrapper_accessors.SpeedControllerWrapperAccessor;

public class JavaSpeedControllerWrapperAccessor extends BaseWrapperAccessor<PwmWrapper> implements SpeedControllerWrapperAccessor
{
    @Override
    protected Map<Integer, PwmWrapper> getMap()
    {
        return SensorActuatorRegistry.get().getSpeedControllers();
    }

    @Override
    public double getVoltagePercentage(int aPort)
    {
        return getValue(aPort).get();
    }

    @Override
    public SimpleMotorSimulationConfig getMotorSimSimpleModelConfig(int aPort)
    {
        SimpleMotorSimulator simulator = (SimpleMotorSimulator) getValue(aPort).getMotorSimulator();
        return simulator.getConfig();
    }

    @Override
    public StaticLoadMotorSimulationConfig getMotorSimStaticModelConfig(int aPort)
    {
        StaticLoadDcMotorSim simulator = (StaticLoadDcMotorSim) getValue(aPort).getMotorSimulator();
        return simulator.getConfig();
    }

    @Override
    public GravityLoadMotorSimulationConfig getMotorSimGravitationalModelConfig(int aPort)
    {
        GravityLoadDcMotorSim simulator = (GravityLoadDcMotorSim) getValue(aPort).getMotorSimulator();
        return simulator.getConfig();
    }

    @Override
    public RotationalLoadMotorSimulationConfig getMotorSimRotationalModelConfig(int aPort)
    {
        RotationalLoadDcMotorSim simulator = (RotationalLoadDcMotorSim) getValue(aPort).getMotorSimulator();
        return simulator.getConfig();
    }

    @Override
    public MotorSimType getMotorSimType(int aHandle)
    {
        IMotorSimulator simulator = getValue(aHandle).getMotorSimulator();

        if (simulator instanceof SimpleMotorSimulator)
        {
            return MotorSimType.Simple;
        }
        if (simulator instanceof StaticLoadDcMotorSim)
        {
            return MotorSimType.StaticLoad;
        }
        if (simulator instanceof GravityLoadDcMotorSim)
        {
            return MotorSimType.GravitationalLoad;
        }
        if (simulator instanceof RotationalLoadDcMotorSim)
        {
            return MotorSimType.RotationalLoad;
        }
        return MotorSimType.None;
    }

    @Override
    public DcMotorModelConfig getMotorConfig(int aPort)
    {
        IMotorSimulator simulator = getValue(aPort).getMotorSimulator();
        if (simulator instanceof BaseDcMotorSimulator)
        {
            BaseDcMotorSimulator castSim = (BaseDcMotorSimulator) simulator;
            return castSim.getMotorConfig();
        }
        return null;
    }

    @Override
    public double getPosition(int aPort)
    {
        return SensorActuatorRegistry.get().getSpeedControllers().get(aPort).getPosition();
    }

    @Override
    public double getVelocity(int aPort)
    {
        return SensorActuatorRegistry.get().getSpeedControllers().get(aPort).getVelocity();
    }

    @Override
    public double getCurrent(int aPort)
    {
        return SensorActuatorRegistry.get().getSpeedControllers().get(aPort).getCurrent();
    }

    @Override
    public double getAcceleration(int aPort)
    {
        return SensorActuatorRegistry.get().getSpeedControllers().get(aPort).getAcceleration();
    }

}
