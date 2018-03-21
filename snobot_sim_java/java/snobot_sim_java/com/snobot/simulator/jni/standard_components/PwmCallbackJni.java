package com.snobot.simulator.jni.standard_components;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.jni.HalCallbackValue;
import com.snobot.simulator.module_wrapper.PwmWrapper;

public final class PwmCallbackJni
{
    private static final Logger sLOGGER = LogManager.getLogger(PwmCallbackJni.class);

    private PwmCallbackJni()
    {

    }

    public static native void reset();

    public static native void registerPwmCallback(String aFunctionName);

    public static void registerPwmCallback()
    {
        registerPwmCallback("pwmCallback");
    }

    public static void pwmCallback(String aCallbackType, int aPort, HalCallbackValue aHalValue)
    {
        if ("Initialized".equals(aCallbackType))
        {
            SensorActuatorRegistry.get().register(new PwmWrapper(aPort), aPort);
        }
        else if ("Speed".equals(aCallbackType))
        {
            SensorActuatorRegistry.get().getSpeedControllers().get(aPort).set(aHalValue.mDouble);
        }
        else if ("ZeroLatch".equals(aCallbackType))
        {
            sLOGGER.log(Level.DEBUG, "ZeroLatch ignored");
        }
        else
        {
            sLOGGER.log(Level.ERROR, "Unknown PWM callback " + aCallbackType + " - " + aHalValue);
        }
    }

}
