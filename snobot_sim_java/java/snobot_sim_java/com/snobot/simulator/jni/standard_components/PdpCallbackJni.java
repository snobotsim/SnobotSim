package com.snobot.simulator.jni.standard_components;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.snobot.simulator.jni.HalCallbackValue;

public final class PdpCallbackJni
{
    private static final Logger sLOGGER = LogManager.getLogger(PdpCallbackJni.class);

    private PdpCallbackJni()
    {

    }

    public static native void reset();

    public static native void registerPdpCallback(String aFunctionName);

    public static void registerPdpCallback()
    {
        registerPdpCallback("pdpCallback");
    }

    public static void pdpCallback(String aCallbackType, int aPort, HalCallbackValue aHalValue)
    {
        if ("Initialized".equals(aCallbackType))
        { // NOPMD
            // Nothing to do
        }
        else
        {
            sLOGGER.log(Level.ERROR, "Unknown PDP callback " + aCallbackType + " - " + aHalValue);
        }
    }
}
