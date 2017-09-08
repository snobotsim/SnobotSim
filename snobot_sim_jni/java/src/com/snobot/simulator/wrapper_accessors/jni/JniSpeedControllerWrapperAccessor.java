
package com.snobot.simulator.wrapper_accessors.jni;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.snobot.simulator.DcMotorModelConfig;
import com.snobot.simulator.jni.module_wrapper.SpeedControllerWrapperJni;
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
    public void updateAllSpeedControllers(double aUpdatePeriod)
    {
        SpeedControllerWrapperJni.updateAllSpeedControllers(aUpdatePeriod);
    }
    
    @Override
    public List<Integer> getPortList()
    {
        return IntStream.of(SpeedControllerWrapperJni.getPortList()).boxed().collect(Collectors.toList());
    }

    @Override
    public DcMotorModelConfig getMotorConfig(int aPort)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public double getMotorSimSimpleModelConfig(int aPort)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getMotorSimStaticModelConfig(int aPort)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getMotorSimGravitationalModelConfig(int aPort)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public MotorSimType getMotorSimType(int aHandle)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public double getPosition(int i)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getVelocity(int i)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getCurrent(int i)
    {
        // TODO Auto-generated method stub
        return 0;
    }
}
