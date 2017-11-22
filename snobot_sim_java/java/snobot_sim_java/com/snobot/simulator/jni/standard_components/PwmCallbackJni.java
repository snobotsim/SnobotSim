package com.snobot.simulator.jni.standard_components;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.jni.HalCallbackValue;
import com.snobot.simulator.module_wrapper.PwmWrapper;

public class PwmCallbackJni
{
    private static final Logger sLOGGER = Logger.getLogger(PwmCallbackJni.class);

    public static native void registerPwmCallback(String functionName);

    public static native void reset();

    public static void registerPwmCallback()
    {
        registerPwmCallback("pwmCallback");
    }

    public static void pwmCallback(String callbackType, int port, HalCallbackValue halValue)
    {
        if ("Initialized".equals(callbackType))
        {
            SensorActuatorRegistry.get().register(new PwmWrapper(port), port);
        }
        else if ("Speed".equals(callbackType))
        {
            SensorActuatorRegistry.get().getSpeedControllers().get(port).set(halValue.mDouble);
        }
        else
        {
            sLOGGER.log(Level.ERROR, "Unknown PWM callback " + callbackType + " - " + halValue);
        }
    }

}
