package com.snobot.simulator.wrapper_accessors.java;

import java.util.Map;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.simulator_components.gyro.IGyroWrapper;
import com.snobot.simulator.wrapper_accessors.GyroWrapperAccessor;

public class JavaGyroWrapperAccessor extends BaseWrapperAccessor<IGyroWrapper> implements GyroWrapperAccessor
{

    @Override
    public double getAngle(int aPort)
    {
        return getValue(aPort).getAngle();
    }

    @Override
    public void setAngle(int aPort, double aValue)
    {
        getValue(aPort).setAngle(aValue);
    }

    @Override
    public void reset(int aPort)
    {
        // Nothing to do
    }

    @Override
    protected Map<Integer, IGyroWrapper> getMap()
    {
        return SensorActuatorRegistry.get().getGyros();
    }

    @Override
    public void register(int aPort, String aName)
    {
        // TODO Auto-generated method stub

    }

}
