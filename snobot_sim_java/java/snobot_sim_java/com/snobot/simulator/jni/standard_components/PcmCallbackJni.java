package com.snobot.simulator.jni.standard_components;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.jni.HalCallbackValue;
import com.snobot.simulator.module_wrapper.SolenoidWrapper;

public class PcmCallbackJni
{
    private static final Logger sLOGGER = Logger.getLogger(PcmCallbackJni.class);

    public static native void registerPcmCallback(String functionName);

    public static native void reset();

    public static void registerPcmCallback()
    {
        registerPcmCallback("pcmCallback");
    }

    public static void pcmCallback(String callbackType, int port, HalCallbackValue halValue)
    {
        if ("SolenoidInitialized".equals(callbackType))
        {
            SensorActuatorRegistry.get().register(new SolenoidWrapper(port), port);
        }
        else if ("SolenoidOutput".equals(callbackType))
        {
            SensorActuatorRegistry.get().getSolenoids().get(port).set(halValue.mBoolean);
        }
        else
        {
            sLOGGER.log(Level.ERROR, "Unknown PCM callback " + callbackType + " - " + halValue);
        }
    }

}
