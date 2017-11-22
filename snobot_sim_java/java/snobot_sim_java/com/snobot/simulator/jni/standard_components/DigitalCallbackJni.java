package com.snobot.simulator.jni.standard_components;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.jni.HalCallbackValue;
import com.snobot.simulator.module_wrapper.DigitalSourceWrapper;
import com.snobot.simulator.module_wrapper.DigitalSourceWrapper.StateSetterHelper;

public class DigitalCallbackJni
{
    private static final Logger sLOGGER = Logger.getLogger(DigitalCallbackJni.class);

    public static native void setDigitalInput(int aHandle, boolean aState);

    public static native void registerDigitalCallback(String functionName);

    public static native void reset();

    public static void registerDigitalCallback()
    {
        registerDigitalCallback("digitalCallback");
    }

    public static void digitalCallback(String callbackType, int port, HalCallbackValue halValue)
    {
        if ("Initialized".equals(callbackType))
        {
            SensorActuatorRegistry.get().register(new DigitalSourceWrapper(port, new StateSetterHelper()
            {

                @Override
                public void setState(boolean aState)
                {
                    setDigitalInput(port, aState);
                }
            }), port);
        }
        else if ("Value".equals(callbackType))
        {
            SensorActuatorRegistry.get().getDigitalSources().get(port).set(halValue.mBoolean);
        }
        else
        {
            sLOGGER.log(Level.ERROR, "Unknown Digital callback " + callbackType + " - " + halValue);
        }
    }
}
