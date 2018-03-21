package com.snobot.simulator.jni.standard_components;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.jni.HalCallbackValue;
import com.snobot.simulator.module_wrapper.DigitalSourceWrapper;
import com.snobot.simulator.module_wrapper.DigitalSourceWrapper.StateSetterHelper;

public final class DigitalCallbackJni
{
    private static final Logger sLOGGER = LogManager.getLogger(DigitalCallbackJni.class);

    private DigitalCallbackJni()
    {

    }

    public static native void setDigitalInput(int aHandle, boolean aState);

    public static native void registerDigitalCallback(String aFunctionName);

    public static void registerDigitalCallback()
    {
        registerDigitalCallback("digitalCallback");
    }

    public static native void reset();

    public static void digitalCallback(String aCallbackType, int aPort, HalCallbackValue aHalValue)
    {
        if ("Initialized".equals(aCallbackType))
        {
            SensorActuatorRegistry.get().register(new DigitalSourceWrapper(aPort, new StateSetterHelper()
            {

                @Override
                public void setState(boolean aState)
                {
                    setDigitalInput(aPort, aState);
                }
            }), aPort);
        }
        else if ("Value".equals(aCallbackType))
        {
            SensorActuatorRegistry.get().getDigitalSources().get(aPort).set(aHalValue.mBoolean);
        }
        else
        {
            sLOGGER.log(Level.ERROR, "Unknown Digital callback " + aCallbackType + " - " + aHalValue);
        }
    }
}
