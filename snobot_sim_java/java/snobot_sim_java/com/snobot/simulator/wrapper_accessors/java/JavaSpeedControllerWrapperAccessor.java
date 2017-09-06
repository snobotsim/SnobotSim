package com.snobot.simulator.wrapper_accessors.java;

import java.util.Map;

import com.snobot.simulator.DcMotorModelConfig;
import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.module_wrapper.SpeedControllerWrapper;
import com.snobot.simulator.wrapper_accessors.SpeedControllerWrapperAccessor;

public class JavaSpeedControllerWrapperAccessor extends BaseWrapperAccessor<SpeedControllerWrapper> implements SpeedControllerWrapperAccessor
{
    @Override
    protected Map<Integer, SpeedControllerWrapper> getMap()
    {
        return SensorActuatorRegistry.get().getSpeedControllers();
    }

    @Override
    public double getVoltagePercentage(int aPort)
    {
        return getValue(aPort).get();
    }

    @Override
    public void updateAllSpeedControllers(double aUpdatePeriod)
    {
        for (SpeedControllerWrapper wrapper : getMap().values())
        {
            wrapper.update(aUpdatePeriod);
        }

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
    public DcMotorModelConfig getMotorConfig(int aPort)
    {
        // TODO Auto-generated method stub
        return null;
    }

}
