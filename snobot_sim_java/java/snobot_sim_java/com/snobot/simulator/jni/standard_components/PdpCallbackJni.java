package com.snobot.simulator.jni.standard_components;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.snobot.simulator.jni.HalCallbackValue;

public class PdpCallbackJni
{
    private static final Logger sLOGGER = Logger.getLogger(PdpCallbackJni.class);

    public static native void registerPdpCallback(String functionName);

    public static native void reset();

    public static void registerPdpCallback()
    {
        registerPdpCallback("pdpCallback");
    }

    public static void pdpCallback(String callbackType, int port, HalCallbackValue halValue)
    {
        if ("Initialized".equals(callbackType))
        {

        }
        else
        {
            sLOGGER.log(Level.ERROR, "Unknown PDP callback " + callbackType + " - " + halValue);
        }
    }
}
