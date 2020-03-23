package com.snobot.simulator.wrapper_accessors;

import java.util.List;

import com.snobot.simulator.module_wrapper.interfaces.ISensorWrapper;

public interface IBasicSensorActuatorWrapperAccessor<WrapperType extends ISensorWrapper>
{
    WrapperType createSimulator(int aPort, String aType);

    List<Integer> getPortList();

    WrapperType getWrapper(int aHandle);
    
	String getType(int portHandle);
}
