package com.snobot.simulator.wrapper_accessors.java;

import java.util.Map;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.module_wrapper.interfaces.IAccelerometerWrapper;
import com.snobot.simulator.wrapper_accessors.AccelerometerWrapperAccessor;

public class JavaAccelerometerWrapperAccessor extends BaseWrapperAccessor<IAccelerometerWrapper> implements AccelerometerWrapperAccessor
{

    @Override
    public IAccelerometerWrapper createSimulator(int aPort, String aType) {
        return null;
    }

    @Override
    protected Map<Integer, IAccelerometerWrapper> getMap() {
        return SensorActuatorRegistry.get().getAccelerometers();
    }

    @Override
    public IAccelerometerWrapper getWrapper(int aHandle) {
        return getValue(aHandle);
    }

}
