
package com.snobot.simulator.wrapper_accessors.jni;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.snobot.simulator.jni.module_wrapper.SpeedControllerWrapperJni;
import com.snobot.simulator.motor_sim.DcMotorModelConfig;
import com.snobot.simulator.motor_sim.GravityLoadMotorSimulationConfig;
import com.snobot.simulator.motor_sim.RotationalLoadMotorSimulationConfig;
import com.snobot.simulator.motor_sim.SimpleMotorSimulationConfig;
import com.snobot.simulator.motor_sim.StaticLoadMotorSimulationConfig;
import com.snobot.simulator.wrapper_accessors.SpeedControllerWrapperAccessor;

public class JniSpeedControllerWrapperAccessor implements SpeedControllerWrapperAccessor
{
    @Override
    public void setName(int aPort, String aName)
    {
        SpeedControllerWrapperJni.setName(aPort, aName);
    }
    
    @Override
    public String getName(int aPort)
    {
        return SpeedControllerWrapperJni.getName(aPort);
    }

    @Override
    public boolean getWantsHidden(int aPort)
    {
        return SpeedControllerWrapperJni.getWantsHidden(aPort);
    }
    
    @Override
    public double getVoltagePercentage(int aPort)
    {
        return SpeedControllerWrapperJni.getVoltagePercentage(aPort);
    }
    
    @Override
    public List<Integer> getPortList()
    {
        return IntStream.of(SpeedControllerWrapperJni.getPortList()).boxed().collect(Collectors.toList());
    }

    @Override
    public DcMotorModelConfig getMotorConfig(int aPort)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public SimpleMotorSimulationConfig getMotorSimSimpleModelConfig(int aPort)
    {
        return new SimpleMotorSimulationConfig(0);
    }

    @Override
    public StaticLoadMotorSimulationConfig getMotorSimStaticModelConfig(int aPort)
    {
        return new StaticLoadMotorSimulationConfig(0);
    }

    @Override
    public GravityLoadMotorSimulationConfig getMotorSimGravitationalModelConfig(int aPort)
    {
        return new GravityLoadMotorSimulationConfig(0);
    }

    @Override
    public RotationalLoadMotorSimulationConfig getMotorSimRotationalModelConfig(int aPort)
    {
        return new RotationalLoadMotorSimulationConfig(0, 0);
    }

    @Override
    public MotorSimType getMotorSimType(int aHandle)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getPosition(int i)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getVelocity(int i)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getCurrent(int i)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getAcceleration(int i)
    {
        throw new UnsupportedOperationException();
    }
}
